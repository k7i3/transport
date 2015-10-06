package k7i3.code.tnc.transport.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;
import k7i3.code.tnc.transport.Constants;
import k7i3.code.tnc.transport.R;
import k7i3.code.tnc.transport.fragment.RetainedTransportFragment;
import k7i3.code.tnc.transport.helper.gmaps.MarkerAnimation;
import k7i3.code.tnc.transport.loader.TransportLoader;
import k7i3.code.tnc.transport.model.LocationMessage;
import k7i3.code.tnc.transport.model.Route;
import k7i3.code.tnc.transport.model.Transport;
import k7i3.code.tnc.transport.model.TransportLocation;
import k7i3.code.tnc.transport.service.LocationService;

import static k7i3.code.tnc.transport.helper.gmaps.LatLngInterpolator.LinearFixed;

public class TransportActivity extends BaseGoogleMapsActivity
        implements android.support.v4.app.LoaderManager.LoaderCallbacks<Map<Route, List<Transport>>> {

    private static final int REQUEST_CODE_ROUTES = 1;
    private static final int LOADER_TRANSPORT = 70;
    private static final int REQUEST_CODE_LOCATION = 50;

    private FloatingActionButton routesFAB;

    private List<Route> routes;
    private Map<Route, List<Transport>> transportByRoute;
    private Map<Long, Marker> markersByDeviceId;

    private RetainedTransportFragment retainedTransportFragment;

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
//        LocationIntentService.stopService(this); doesn't work well
        LocationService.stop(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {
            refreshIfPossible();
        }
        return super.onOptionsItemSelected(item);
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
                    Toast.makeText(this, "выбрано маршрутов: " + routes.size(), Toast.LENGTH_SHORT).show();
                    startTransportLoader();
                    break;
            }
        } else {
            if (routes == null) {
                Toast.makeText(this, "маршруты не выбраны", Toast.LENGTH_SHORT).show();
            }
            clearMap();
        }
        //PENDING INTENT
//        } else if (resultCode == LocationIntentService.STATUS_START) {
//            switch (requestCode) {
//                case REQUEST_CODE_LOCATION:
//                    Toast.makeText(this, "STATUS_START", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        } else if (resultCode == LocationIntentService.STATUS_UPDATE) {
//            switch (requestCode) {
//                case REQUEST_CODE_LOCATION:
//                    Toast.makeText(this, "STATUS_UPDATE", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        } else if (resultCode == LocationIntentService.STATUS_FINISH) {
//            switch (requestCode) {
//                case REQUEST_CODE_LOCATION:
//                    Toast.makeText(this, "STATUS_FINISH", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        } else if (resultCode == LocationIntentService.STATUS_ERROR) {
//            switch (requestCode) {
//                case REQUEST_CODE_LOCATION:
//                    Toast.makeText(this, "STATUS_ERROR", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
    }

    //LOADERS

    @Override
    public Loader<Map<Route, List<Transport>>> onCreateLoader(int id, Bundle args) {
        Loader<Map<Route, List<Transport>>> loader = null;
        //TODO switch if needed
        if (id == LOADER_TRANSPORT) {
            Log.d(TAG, "onCreateLoader() / id == LOADER_TRANSPORT");
            loader = new TransportLoader(this, args);
            setRefreshActionButtonState(true);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Map<Route, List<Transport>>> loader, Map<Route, List<Transport>> data) {
        Log.d(TAG, "onLoadFinished()");
        //TODO switch if needed
        if (loader.getId() == LOADER_TRANSPORT) {
            Log.d(TAG, "if (loader.getId() == LOADER_TRANSPORT)");
            transportByRoute = data;
            retainedTransportFragment.setTransportByRoute(transportByRoute);
            Log.d(TAG, "transportByRoute.size(): " + transportByRoute.size());

            drawTransport();

            //TODO start next loader/service for retrieve coordinates
            //1.PENDING INTENT via createPendingResult() with callback at onActivityResult() (doesn't work correctly, activity call onPause() when intent (which used to start service) contains PendingIntent. Without PI onPause() doesn't called) http://startandroid.ru/ru/uroki/vse-uroki-spiskom/160-urok-95-service-obratnaja-svjaz-s-pomoschju-pendingintent.html
//            Log.d(TAG, "1. PENDING INTENT START");
//            PendingIntent pendingIntent = createPendingResult(REQUEST_CODE_LOCATION, new Intent(), 0); // intent may not be null, but in example is null!
//            Set<Long> set = markersByDeviceId.keySet();
//            LocationIntentService.startActionPendingIntent(this, set.toArray(new Long[set.size()]), pendingIntent);
//            Log.d(TAG, "1. PENDING INTENT FINISH");
            //2. PENDING INTENT for a broadcast (with getBroadcast()) http://stackoverflow.com/questions/6099364/how-to-use-pendingintent-to-communicate-from-a-service-to-a-client-activity
            //3. EVENTBUS
            startLocationServiceIfNeeded();

            Toast.makeText(this, "найдено автобусов: " + markersByDeviceId.size(), Toast.LENGTH_SHORT).show();
            setRefreshActionButtonState(false);
        }
    }

    @Override
    public void onLoaderReset(Loader<Map<Route, List<Transport>>> loader) {

    }

    //EVENTBUS

    public void onEventMainThread(LocationMessage locationMessage) {
        Log.d(TAG, "EVENTBUS onEventMainThread(LocationMessage locationMessage)");

//        TODO asyncTask?
//        Marker marker;
        for (TransportLocation transportLocation : locationMessage.getDataJson()) {
            Log.d(TAG, "!!! transportLocation: " + transportLocation);
            final Marker marker = markersByDeviceId.get(transportLocation.getDeviceId()); //TODO final?
            float direction = (float) transportLocation.getDirection();
            if (direction != 0) marker.setRotation(direction);
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
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        routesFAB = (FloatingActionButton) findViewById(R.id.routesFAB);
        routesFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), RoutesActivity.class), REQUEST_CODE_ROUTES);
            }
        });
    }

    private void initFields() {
        FragmentManager fm = getSupportFragmentManager();
        retainedTransportFragment = (RetainedTransportFragment) fm.findFragmentByTag(TAG);
        if (retainedTransportFragment != null) {
            routes = retainedTransportFragment.getRoutes();
            transportByRoute = retainedTransportFragment.getTransportByRoute();
            markersByDeviceId = retainedTransportFragment.getMarkersByDeviceId();
        } else {
            retainedTransportFragment = new RetainedTransportFragment();
            fm.beginTransaction().add(retainedTransportFragment, TAG).commit();

            markersByDeviceId = new HashMap<>();
            retainedTransportFragment.setMarkersByDeviceId(markersByDeviceId);
        }
    }

    private void startTransportLoader() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.ROUTES, (ArrayList<Route>) routes);
        getSupportLoaderManager().restartLoader(LOADER_TRANSPORT, bundle, this); // if Loader already exist, when initLoader() called, constructor doesn't called, and routes remain old
    }

    private void startLocationServiceIfNeeded() {
        if (markersByDeviceId.size() != 0) {
            retainedTransportFragment.setMarkersByDeviceId(markersByDeviceId);
            Set<Long> set = markersByDeviceId.keySet();
            LocationService.start(this, set.toArray(new Long[set.size()]));
        } else {
            Log.d(TAG, "markersByDeviceId.size() == 0");
        }
    }

    private void drawTransport() {
        Log.d(TAG, "drawTransport()");
        clearMap();

//        googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(location.getTime() + " " + location.getProvider()));

        //DRAWABLE
//        TODO make good nine-patch drawable (.9.png) or custom drawable (.xml) https://romannurik.github.io/AndroidAssetStudio/index.html
        Drawable drawable = getResources().getDrawable(R.drawable.arrow);
//        drawable.setTint(Color.CYAN); // API 21
        ColorFilter filter = new PorterDuffColorFilter(Color.parseColor("#2196F3"), PorterDuff.Mode.MULTIPLY);
        drawable.setColorFilter(filter);
//        drawable.setAlpha(255);

        //ICONGENERATOR
        IconGenerator iconFactory = new IconGenerator(this);
        iconFactory.setBackground(drawable);
        iconFactory.setContentPadding(0, 0, 0, 0);
        iconFactory.setTextAppearance(R.style.Marker);
        iconFactory.setContentRotation(-90);
//        iconFactory.setColor(Color.CYAN); // ONLY FOR DEFAULT MARKER?
//        iconFactory.setStyle(IconGenerator.STYLE_PURPLE); // ONLY FOR DEFAULT MARKER?

        Route route;
        List<Transport> transportList;
        for(Map.Entry<Route, List<Transport>> entry : transportByRoute.entrySet()) {
            location.setLatitude(location.getLatitude() + 0.001);
            route = entry.getKey();
            transportList = entry.getValue();

            for (Transport transport : transportList) {
                location.setLongitude(location.getLongitude() + 0.001);
                addMarker(iconFactory, route.getNum(), location, transport.getDeviceId());
            }
        }


        // TEST
//        location.setBearing(0);  // mock direction
//        addMarker(iconFactory, "!!!1", location, 1);

//        final Handler handler = new Handler();
//        final Runnable r = new Runnable() {
//            public void run() {
////                location.setLongitude(location.getLongitude() + 0.0001);
//                location.setLatitude(location.getLatitude() + 0.001);
//                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                MarkerAnimation.animateMarkerToICS(markersByDeviceId.get(1L), latLng, new Spherical());
//                handler.postDelayed(this, 3000);
//            }
//        };
//        if (true) handler.postDelayed(r, 3000);

//        location.setLongitude(location.getLongitude() + 0.001);
//        addMarker(iconFactory, "!!!2", location, 2);
//        addMarker(iconFactory, "!!!3", location, 3);
//        location.setLongitude(location.getLongitude() + 0.001);
//        addMarker(iconFactory, "290", location, 2);
//        location.setLongitude(location.getLongitude() + 0.001);
//        addMarker(iconFactory, "69", location, 3);
//        location.setLongitude(location.getLongitude() + 0.001);
//        addMarker(iconFactory, "6", location, 4);
    }

    private void clearMap() {
        if (googleMap != null) googleMap.clear();
        markersByDeviceId.clear();
        retainedTransportFragment.setMarkersByDeviceId(markersByDeviceId);
    }

    private void addMarker(IconGenerator iconFactory, String title, Location location, long deviceId) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(title))).
                position(new LatLng(location.getLatitude(), location.getLongitude())).
                flat(true).
                rotation(location.getBearing()).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV()).
                visible(false);

        markersByDeviceId.put(deviceId, googleMap.addMarker(markerOptions));
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
            startTransportLoader();
        } else {
            Toast.makeText(this, "маршруты не выбраны", Toast.LENGTH_SHORT).show();
        }
    }
}
