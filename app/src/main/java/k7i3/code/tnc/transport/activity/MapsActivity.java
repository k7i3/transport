package k7i3.code.tnc.transport.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import k7i3.code.tnc.transport.R;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback {

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
        initInstances();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.setMyLocationEnabled(true);
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);

        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}
