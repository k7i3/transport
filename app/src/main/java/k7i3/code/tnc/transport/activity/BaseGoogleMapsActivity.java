package k7i3.code.tnc.transport.activity;

import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import k7i3.code.tnc.transport.R;

/**
 * Created by k7i3 on 05.10.15.
 */
public abstract class BaseGoogleMapsActivity extends BaseActivity
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
    private SupportMapFragment supportMapFragment;
    protected GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    private FloatingActionButton myLocationFAB;
    protected Location location;
    private boolean isFirstConnect = true;

    //LIFECYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            isFirstConnect = savedInstanceState.getBoolean("isFirstConnect");
        }

        FragmentManager fm = getSupportFragmentManager();
        supportMapFragment = ((SupportMapFragment) fm.findFragmentById(R.id.map));
        supportMapFragment.setRetainInstance(true);

        supportMapFragment.getMapAsync(this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
        if (googleMap != null) googleMap.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (googleMap != null) googleMap.clear();
        googleApiClient.disconnect();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isFirstConnect", isFirstConnect);
        super.onSaveInstanceState(outState);
    }

    //MAPS

    /**
     * Implementation of {@link OnMapReadyCallback}.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d(TAG, "onMapReady()");
        this.googleMap = googleMap;

//        TODO change position of maps api's buttons or make their custom http://stackoverflow.com/questions/14489880/how-to-change-the-position-of-maps-apis-get-my-location-button /// http://stackoverflow.com/questions/1768097/how-to-layout-zoom-control-with-setbuiltinzoomcontrolstrue
        googleMap.setMyLocationEnabled(true);
        googleMap.setPadding(36, 240, 36, 36);
        googleMap.setOnMyLocationButtonClickListener(this);
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setMapToolbarEnabled(false);

        myLocationFAB = (FloatingActionButton) findViewById(R.id.myLocationFAB);
        myLocationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if (location == null) {
                    location = googleMap.getMyLocation();
                    if (location == null) {
                        Toast.makeText(BaseGoogleMapsActivity.this, "не удалось определить местоположение", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BaseGoogleMapsActivity.this, "местоположение может быть устаревшим", Toast.LENGTH_SHORT).show();
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        moveCamera(latLng, 15);
                    }
                } else {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    moveCamera(latLng, 15);
                }

                //Analytics TODO send location_info here end where was firsZoom? (latLong + speed)
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UI")
                        .setAction("my_location_FAB_was_clicked")
                        .setLabel("FAB")
                        .build());
            }
        });
    }

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
        if (location == null) {
            location = new Location("");
            location.setLatitude(54.789);
            location.setLongitude(56.034);
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (isFirstConnect) {
            moveCamera(latLng, 10);
            isFirstConnect = false;
        }
    }

    /**
     * Callback called when disconnected from GCore. Implementation of {@link GoogleApiClient.ConnectionCallbacks}.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended()");
    }

    /**
     * Implementation of {@link GoogleApiClient.OnConnectionFailedListener}.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed()");
    }

    /**
     * Implementation of {@link com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener}.
     */
    @Override
    public boolean onMyLocationButtonClick() {
        Log.d(TAG, "onMyLocationButtonClick()");
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
//        Log.d(TAG, "onLocationChanged");
//        Toast.makeText(this, "TransportLocation = " + location, Toast.LENGTH_SHORT).show();
    }

    //HELPERS

    private void moveCamera(LatLng latLng, int zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)             // Sets the center of the map to location user
                .zoom(zoom)                   // Sets the zoom 17
                .bearing(0)
                .build();                   // Creates a CameraPosition from the builder
//                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    /**
     * Button to get current TransportLocation. This demonstrates how to get the current TransportLocation as required
     * without needing to register a LocationListener.
     */
//    public void showMyLocation(View view) {
//        if (googleApiClient.isConnected()) {
//            String msg = "TransportLocation = "
//                    + LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//        }
//    }
}
