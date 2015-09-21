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
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import k7i3.code.tnc.transport.Constants;
import k7i3.code.tnc.transport.R;
import k7i3.code.tnc.transport.helper.gmaps.LatLngInterpolator;
import k7i3.code.tnc.transport.helper.gmaps.MarkerAnimation;
import k7i3.code.tnc.transport.loader.TransportLoader;
import k7i3.code.tnc.transport.model.Route;
import k7i3.code.tnc.transport.model.Transport;

import static k7i3.code.tnc.transport.helper.gmaps.LatLngInterpolator.*;

public class TransportActivity extends BaseActivity
        implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMyLocationButtonClickListener,
        LocationListener,
        android.support.v4.app.LoaderManager.LoaderCallbacks<Map<Route, List<Transport>>> {

    // These settings are the same as the settings for the map. They will in fact give you updates
    // at the maximal rates currently possible.
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    private static final String TAG = "====> TransportActivity";
    private static final int REQUEST_CODE_ROUTES = 1;
    private static final int LOADER_TRANSPORT = 1;

    private GoogleApiClient googleApiClient;

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    private FloatingActionButton routesFAB;

    private List<Route> routes;
    private Map<Route, List<Transport>> transportByRoute;
    private Map<Long, Marker> markersByDeviceId;
    private Location location;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_transport;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        initInstances();

        markersByDeviceId = new HashMap<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleMap != null) googleMap.clear();
        googleApiClient.disconnect();
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "requestCode = " + requestCode + " resultCode = " + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ROUTES:
                    routes = data.getParcelableArrayListExtra(Constants.ROUTES);
                    Toast.makeText(this, "!!! routes.size(): " + routes.size(), Toast.LENGTH_SHORT).show();
//                    drawTransport();
                    break;
            }
        } else {
            Toast.makeText(this, "!!!", Toast.LENGTH_SHORT).show();
        }

        startTransportLoader();
    }

    private void startTransportLoader() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.ROUTES, (ArrayList<Route>) routes);
        getSupportLoaderManager().restartLoader(LOADER_TRANSPORT, bundle, this); // if Loader already exist, when initLoader() called, constructor doesn't called, and routes remain old
    }

    /**
     * Implementation of {@link OnMapReadyCallback}.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

//        TODO change position of maps api's buttons or make their custom http://stackoverflow.com/questions/14489880/how-to-change-the-position-of-maps-apis-get-my-location-button /// http://stackoverflow.com/questions/1768097/how-to-layout-zoom-control-with-setbuiltinzoomcontrolstrue
        googleMap.setMyLocationEnabled(true);
        googleMap.setPadding(36, 240, 36, 240);
        googleMap.setOnMyLocationButtonClickListener(this);
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
    }

    /**
     * Button to get current Location. This demonstrates how to get the current Location as required
     * without needing to register a LocationListener.
     */
//    public void showMyLocation(View view) {
//        if (googleApiClient.isConnected()) {
//            String msg = "Location = "
//                    + LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//        }
//    }

    /**
     * Callback called when connected to GCore. Implementation of {@link GoogleApiClient.ConnectionCallbacks}.
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected()");
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient,
                REQUEST,
                this);  // LocationListener

        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        moveCamera(latLng);
    }

    private void moveCamera(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)             // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(0)
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    private void drawTransport() {
//        googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(location.getTime() + " " + location.getProvider()));

        //DRAWABLE
//        TODO make good nine-patch drawable (.9.png) or custom drawable (.xml) https://romannurik.github.io/AndroidAssetStudio/index.html
        Drawable drawable = getResources().getDrawable(R.drawable.arrow);
//        drawable.setTint(Color.CYAN); // API 21
        ColorFilter filter = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
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
//        location.setBearing(45);  // mock direction
        addMarker(iconFactory, "!!!1", location, 1);

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
//                location.setLongitude(location.getLongitude() + 0.0001);
                location.setLatitude(location.getLatitude() + 0.001);
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerAnimation.animateMarkerToICS(markersByDeviceId.get(1L), latLng, new Spherical());
                handler.postDelayed(this, 3000);
            }
        };
        if (true) handler.postDelayed(r, 3000);

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

    private void addMarker(IconGenerator iconFactory, String title, Location location, long deviceId) {
//        TODO make marker invisible
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(title))).
                position(new LatLng(location.getLatitude(), location.getLongitude())).
                flat(true).
                rotation(location.getBearing()).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        markersByDeviceId.put(deviceId, googleMap.addMarker(markerOptions));
    }

    /**
     * Callback called when disconnected from GCore. Implementation of {@link GoogleApiClient.ConnectionCallbacks}.
     */
    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * Implementation of {@link GoogleApiClient.OnConnectionFailedListener}.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * Implementation of {@link com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener}.
     */
    @Override
    public boolean onMyLocationButtonClick() {
//        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    /**
     * Implementation of {@link LocationListener}.
     */
    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(this, "Location = " + location, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader<Map<Route, List<Transport>>> onCreateLoader(int id, Bundle args) {
        Loader<Map<Route, List<Transport>>> loader = null;
        //TODO switch if needed
        if (id == LOADER_TRANSPORT) {
            Log.d(TAG, "onCreateLoader() / id == LOADER_TRANSPORT");
            loader = new TransportLoader(this, args);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Map<Route, List<Transport>>> loader, Map<Route, List<Transport>> data) {
        Log.d(TAG, "onLoadFinished()");
        //TODO switch if needed
        if (loader.getId() == LOADER_TRANSPORT) {
            Log.d(TAG, "if (loader.getId() == LOADER_TRANSPORT) {");
            transportByRoute = data;
            Log.d(TAG, "transportByRoute.size(): " + transportByRoute.size());
        }
        drawTransport();
        //TODO start next loader/service for retrieve coordinates
    }

    @Override
    public void onLoaderReset(Loader<Map<Route, List<Transport>>> loader) {

    }
}
