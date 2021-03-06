package k7i3.code.tnc.transport.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;
import k7i3.code.tnc.transport.AnalyticsApplication;
import k7i3.code.tnc.transport.Constants;
import k7i3.code.tnc.transport.R;
import k7i3.code.tnc.transport.fragment.RetainedTransportFragment;
import k7i3.code.tnc.transport.helper.gmaps.MarkerAnimation;
import k7i3.code.tnc.transport.loader.TracksLoader;
import k7i3.code.tnc.transport.loader.TransportLoader;
import k7i3.code.tnc.transport.model.LocationMessage;
import k7i3.code.tnc.transport.model.Route;
import k7i3.code.tnc.transport.model.Track;
import k7i3.code.tnc.transport.model.Transport;
import k7i3.code.tnc.transport.model.TransportLocation;
import k7i3.code.tnc.transport.service.LocationService;

import static k7i3.code.tnc.transport.helper.gmaps.LatLngInterpolator.LinearFixed;

public class TransportActivity extends BaseGoogleMapsActivity {

    protected static final int REQUEST_CODE_ROUTES = 1;
    private static final int LOADER_TRANSPORT = 70;
    private static final int LOADER_TRACKS = 80;

    private FloatingActionButton routesFAB;

    private List<Route> routes;
    private Map<Route, List<Transport>> transportByRoute;
    private Map<Route, Track> trackByRoute;
    private Map<Long, Marker> markerByDeviceId;

    private RetainedTransportFragment retainedTransportFragment;

    private IconGenerator iconGenerator;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_transport;
    }

    //LIFECYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTAG("====> TransportActivity");
        Log.d(TAG, "onCreate()");
        initInstances();
        initFields();
        startLocationServiceIfNeeded();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        checkRefreshActionButtonState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        LocationService.stop(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected()");
        int id = item.getItemId();
        switch (id) {
            case R.id.refresh:
                refreshIfPossible();
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //ACTIVITY RESULTS

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");
        Log.d(TAG, "requestCode = " + requestCode + " resultCode = " + resultCode);
        //ACTIVITY FOR RESULT
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ROUTES:
                    clearMap();
                    routes = data.getParcelableArrayListExtra(Constants.ROUTES);
                    retainedTransportFragment.setRoutes(routes);
//                    TODO may be check routes for == null or .size = 0??? => doesn't work setRefreshActionButtonState(false);
//                    if (routes == null || routes.size() == 0) {
//                        Toast.makeText(this, "маршруты не выбраны", Toast.LENGTH_SHORT).show();
//                        setRefreshActionButtonState(false);
//                        break;
//                    } else {
                        Toast.makeText(this, "выбрано маршрутов: " + routes.size(), Toast.LENGTH_SHORT).show();
                        startTransportLoader();
                        startTracksLoader();
                        break;
//                    }
            }
        } else {
            if (routes == null) {
                Toast.makeText(this, "маршруты не выбраны", Toast.LENGTH_SHORT).show();
            }
            clearMap();
        }
    }

    //LOADERS

    private void startTransportLoader() {
        Log.d(TAG, "startTransportLoader()");
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.ROUTES, (ArrayList<Route>) routes);
        // if Loader already exist, when initLoader() called, constructor doesn't called, and routes remain old
        getSupportLoaderManager().restartLoader(LOADER_TRANSPORT, bundle, new LoaderManager.LoaderCallbacks<Map<Route, List<Transport>>>() {
            @Override
            public Loader<Map<Route, List<Transport>>> onCreateLoader(int id, Bundle args) {
                Log.d(TAG, "Transport - onCreateLoader()");
                setRefreshActionButtonState(true);
                return new TransportLoader(TransportActivity.this, args);
            }

            @Override
            public void onLoadFinished(Loader<Map<Route, List<Transport>>> loader, Map<Route, List<Transport>> data) {
                Log.d(TAG, "Transport - onLoadFinished()");
                transportByRoute = data;
                retainedTransportFragment.setTransportByRoute(transportByRoute);
                Log.d(TAG, "transportByRoute.size(): " + transportByRoute.size());
                drawTransport();
                startLocationServiceIfNeeded();
                Toast.makeText(TransportActivity.this, "найдено автобусов: " + markerByDeviceId.size(), Toast.LENGTH_SHORT).show();
                setRefreshActionButtonState(false);
            }

            @Override
            public void onLoaderReset(Loader<Map<Route, List<Transport>>> loader) {

            }
        });
    }

    private void startTracksLoader() {
        // TODO make property showTrack and check it here /// or make startTracksLoaderIfNeeded
        Log.d(TAG, "startTracksLoader()");
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.ROUTES, (ArrayList<Route>) routes);
        // if Loader already exist, when initLoader() called, constructor doesn't called, and routes remain old
        getSupportLoaderManager().restartLoader(LOADER_TRACKS, bundle, new LoaderManager.LoaderCallbacks<Map<Route, Track>>() {
            @Override
            public Loader<Map<Route, Track>> onCreateLoader(int id, Bundle args) {
                Log.d(TAG, "Tracks - onCreateLoader()");
                return new TracksLoader(TransportActivity.this, args);
            }

            @Override
            public void onLoadFinished(Loader<Map<Route, Track>> loader, Map<Route, Track> data) {
                Log.d(TAG, "Tracks - onLoadFinished()");
                trackByRoute = data;
                retainedTransportFragment.setTrackByRoute(trackByRoute);
                Log.d(TAG, "trackByRoute.size(): " + trackByRoute.size());
                // check it, because may be case when it needed (if preferences change and loader start after onResume() in TransportActivity with loader that has been triggered previously)
                // to avoid найдено треков: 0, after change preferences
                if (PreferenceManager.getDefaultSharedPreferences(TransportActivity.this).getBoolean(SettingsActivity.KEY_PREF_SHOW_TRACKS, false)) {
                    Toast.makeText(TransportActivity.this, "найдено треков: " + trackByRoute.size(), Toast.LENGTH_SHORT).show();
                    drawTracks();
                }
                setRefreshActionButtonState(false);
            }

            @Override
            public void onLoaderReset(Loader<Map<Route, Track>> loader) {

            }
        });
    }

    //SERVICES

    private void startLocationServiceIfNeeded() {
        if (markerByDeviceId.size() != 0) {
            retainedTransportFragment.setMarkerByDeviceId(markerByDeviceId);
            Set<Long> set = markerByDeviceId.keySet();
            LocationService.start(this, set.toArray(new Long[set.size()]));
        } else {
            Log.d(TAG, "markerByDeviceId.size() == 0");
        }
    }

    //EVENTBUS

    public void onEventMainThread(LocationMessage locationMessage) {
        Log.d(TAG, "EVENTBUS onEventMainThread(LocationMessage locationMessage)");

//        TODO asyncTask?
//        Marker marker;
        for (TransportLocation transportLocation : locationMessage.getDataJson()) {
            Log.d(TAG, "!!! transportLocation: " + transportLocation);
            final Marker marker = markerByDeviceId.get(transportLocation.getDeviceId()); //TODO final?
            float direction = (float) transportLocation.getDirection();

            if (direction != 0) {
                marker.setRotation(direction);
            } else {
                marker.setRotation(calculateDirection(marker.getPosition(), new LatLng(transportLocation.getLat(), transportLocation.getLon())));
            }

            MarkerAnimation.animateMarkerToICS(
                    marker,
                    new LatLng(transportLocation.getLat(), transportLocation.getLon()),
                    new LinearFixed());


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!marker.isVisible()) marker.setVisible(true);
                }
            }, 3000);
//
        }
    }

    //HELPERS

    private void initInstances() {
//        toolbar.setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        routesFAB = (FloatingActionButton) findViewById(R.id.routesFAB);
        routesFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), RoutesActivity.class), REQUEST_CODE_ROUTES);

                //Analytics
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UI")
                        .setAction("routes_FAB_was_clicked")
                        .setLabel("FAB")
                        .build());
            }
        });
    }

    private void initFields() {
        FragmentManager fm = getSupportFragmentManager();
        retainedTransportFragment = (RetainedTransportFragment) fm.findFragmentByTag(TAG);
        if (retainedTransportFragment != null) {
            routes = retainedTransportFragment.getRoutes();
            transportByRoute = retainedTransportFragment.getTransportByRoute();
            trackByRoute = retainedTransportFragment.getTrackByRoute();
            markerByDeviceId = retainedTransportFragment.getMarkerByDeviceId();
        } else {
            retainedTransportFragment = new RetainedTransportFragment();
            fm.beginTransaction().add(retainedTransportFragment, TAG).commit();

            transportByRoute = new HashMap<>();
            retainedTransportFragment.setTransportByRoute(transportByRoute);

            trackByRoute = new HashMap<>();
            retainedTransportFragment.setTrackByRoute(trackByRoute);

            markerByDeviceId = new HashMap<>();
            retainedTransportFragment.setMarkerByDeviceId(markerByDeviceId);
        }

        initIconGenerator();
    }

    private void initIconGenerator() {
        iconGenerator = new IconGenerator(this);
        iconGenerator.setBackground(makeIconDrawable());
        iconGenerator.setContentPadding(0, 0, 0, 0);
        iconGenerator.setTextAppearance(R.style.Marker);
//        iconGenerator.setContentRotation(-90);
//        iconGenerator.setColor(Color.CYAN); // ONLY FOR DEFAULT MARKER?
//        iconGenerator.setStyle(IconGenerator.STYLE_PURPLE); // ONLY FOR DEFAULT MARKER?
    }

    private Drawable makeIconDrawable() {
//        TODO make good nine-patch drawable (.9.png) or custom drawable (.xml) https://romannurik.github.io/AndroidAssetStudio/index.html dynamically: http://stackoverflow.com/questions/14442011/create-a-nine-patch-image-with-code /// http://stackoverflow.com/questions/5079868/create-a-ninepatch-ninepatchdrawable-in-runtime/14061128#14061128 /// https://gist.github.com/briangriffey/4391807
        Drawable drawable = getResources().getDrawable(R.drawable.a6); //arrow
//        drawable.setTint(Color.CYAN); // API 21
//        ColorFilter filter = new PorterDuffColorFilter(Color.parseColor("#2196F3"), PorterDuff.Mode.MULTIPLY);
//        drawable.setColorFilter(filter);
//        drawable.setAlpha(255);
        return drawable;
    }

    private void drawTransport() {
        Log.d(TAG, "drawTransport()");
//        clearMap(); off, because when track draw before transport it clear track

//        googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(location.getTime() + " " + location.getProvider()));

        Route route;
        List<Transport> transportList;
//        int transportCount = 0; //markerByDeviceId.size() instead
        for(Map.Entry<Route, List<Transport>> entry : transportByRoute.entrySet()) {
            //TODO location may be null if service unavailable!!! change logic or service..
            location.setLatitude(location.getLatitude() + 0.001);
            route = entry.getKey();
            transportList = entry.getValue();
//            transportCount += transportList.size(); //markerByDeviceId.size() instead

            for (Transport transport : transportList) {
                location.setLongitude(location.getLongitude() + 0.001);
                addMarker(iconGenerator, route.getNum(), location, transport.getDeviceId());
            }

            //Analytics 4-5
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("MAP")
                    .setAction("route_was_added")
                    .setLabel("drawTransport")
                    .setValue(transportList.size()) //ценность события
                    .setCustomDimension(4, route.getNum())
                    .setCustomDimension(5, transportList.size() + "") // TODO del? if no - filter or del! 5 by action at GA (route_transport_size)
                    .build());

            //Analytics 5 (count of transport in route)
//            tracker.send(new HitBuilders.EventBuilder()
//                    .setCategory("MAP")
//                    .setAction("route_transport_size")
//                    .setLabel("drawTransport")
//                    .setValue(transportList.size()) //ценность события
//                    .setCustomDimension(5, transportList.size() + "")
//                    .build());
        }

        //Analytics 6-7
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("MAP")
                .setAction("routes_were_added")
                .setLabel("drawTransport")
                .setValue(markerByDeviceId.size() / (transportByRoute.size() == 0 ? 1 : transportByRoute.size())) //ценность события //avoid java.lang.ArithmeticException: divide by zero
                .setCustomDimension(6, transportByRoute.size() + "")
                .setCustomDimension(7, markerByDeviceId.size() + "") // TODO del? if no - filter or del! 7 by action at GA (transport_size)
                .build());

        //Analytics 7
//        tracker.send(new HitBuilders.EventBuilder()
//                .setCategory("MAP")
//                .setAction("transport_size")
//                .setLabel("drawTransport")
//                .setValue(transportCount) //ценность события
//                .setCustomDimension(7, transportCount + "")
//                .build());

    }

    private void drawTracks() {
        for (Track track : trackByRoute.values()) {
//            TODO it checked at TracksLoader too
            if (track != null) {
                GeoJsonLayer layer = new GeoJsonLayer(googleMap, track.getRouteGeomGJ());
                layer.getDefaultLineStringStyle().setColor(track.getColor());
                layer.getDefaultLineStringStyle().setWidth(4);
//                layer.getDefaultLineStringStyle().setGeodesic(false);
                layer.addLayerToMap();
            }
        }
    }

    private void addMarker(IconGenerator iconFactory, String title, Location location, long deviceId) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(title))).
                position(new LatLng(location.getLatitude(), location.getLongitude())).
                flat(true).
                rotation(location.getBearing()).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV()).
                visible(false);

        markerByDeviceId.put(deviceId, googleMap.addMarker(markerOptions));
    }

    private void clearMap() {
        if (googleMap != null) googleMap.clear();
        markerByDeviceId.clear();
        retainedTransportFragment.setMarkerByDeviceId(markerByDeviceId);
    }

    private void checkRefreshActionButtonState() {
        if (routes == null) {
            setRefreshActionButtonState(false);
        } else {
            setRefreshActionButtonState(true);
        }
    }

    private void refreshIfPossible() {
        if (routes != null) {
            Toast.makeText(this, "обновление...", Toast.LENGTH_SHORT).show();
            clearMap();
            startTransportLoader();
            if (trackByRoute.size() == routes.size()){ // TODO may be: trackByRoute.size() != 0 && trackByRoute.size() == routes.size();
                drawTracks();
            } else {
                startTracksLoader();
            }
        } else {
            Toast.makeText(this, "маршруты не выбраны", Toast.LENGTH_SHORT).show();
        }
    }

    private float calculateDirection(LatLng from, LatLng to) {
        return (float) SphericalUtil.computeHeading(from, to);
    }
}
