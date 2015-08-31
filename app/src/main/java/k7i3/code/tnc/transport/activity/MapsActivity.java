package k7i3.code.tnc.transport.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.WindowManager;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import k7i3.code.tnc.transport.R;

public class MapsActivity extends BaseActivity
        implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMyLocationButtonClickListener,
        LocationListener {

    // These settings are the same as the settings for the map. They will in fact give you updates
    // at the maximal rates currently possible.
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    private GoogleApiClient googleApiClient;

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    private FloatingActionButton routesFAB;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_maps;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        googleMap.clear();
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
                startActivity(new Intent(getBaseContext(), RoutesActivity.class));
            }
        });
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
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient,
                REQUEST,
                this);  // LocationListener

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        moveCamera(latLng);
        drawTransport(location);
    }

    private void moveCamera(LatLng latLng) {
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)             // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(0)
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void drawTransport(Location location) {
//        googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(location.getTime() + " " + location.getProvider()));

        location.setBearing(45);

        Drawable drawable = getResources().getDrawable(R.drawable.ic_navigation_white_24dp);
//        drawable.setTint(Color.CYAN); // API 21
        ColorFilter filter = new PorterDuffColorFilter(Color.CYAN, PorterDuff.Mode.MULTIPLY);
        drawable.setColorFilter(filter);
        drawable.setAlpha(255);

        IconGenerator iconFactory = new IconGenerator(this);
        iconFactory.setBackground(drawable);
        iconFactory.setContentPadding(0, 30, 0, 30);
        iconFactory.setTextAppearance(R.style.Marker);
//        iconFactory.setContentRotation(-90);
//        iconFactory.setColor(Color.CYAN);
//        iconFactory.setStyle(IconGenerator.STYLE_PURPLE);

        addIcon(iconFactory, "110c", location);

        //TEST
        location.setLongitude(location.getLongitude() + 0.001);
        addIcon(iconFactory, "290", location);
        location.setLongitude(location.getLongitude() + 0.001);
        addIcon(iconFactory, "69", location);
        location.setLongitude(location.getLongitude() + 0.001);
        addIcon(iconFactory, "6", location);

        iconFactory.setContentRotation(-90);
        iconFactory.setContentPadding(0, 0, 0, 0);
        iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
        location.setLongitude(location.getLongitude() + 0.001);
        addIcon(iconFactory, "110c", location);
    }

    //    TODO make custom marker icon http://stackoverflow.com/questions/14811579/android-map-api-v2-custom-marker-with-imageview /// http://stackoverflow.com/questions/13763545/android-maps-api-v2-with-custom-markers /// http://stackoverflow.com/questions/11100428/add-text-to-image-in-android-programmatically /// http://googlemaps.github.io/android-maps-utils/
    //    TODO use android-maps-utils https://github.com/googlemaps/android-maps-utils
    private void addIcon(IconGenerator iconFactory, String text, Location location) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(new LatLng(location.getLatitude(), location.getLongitude())).
                flat(true).
                rotation(location.getBearing()).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        googleMap.addMarker(markerOptions);
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
}
