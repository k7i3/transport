package k7i3.code.tnc.transport.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import k7i3.code.tnc.transport.AnalyticsApplication;
import k7i3.code.tnc.transport.R;

/**
 * Created by k7i3 on 05.08.15.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected DrawerLayout drawerLayout;
    private NavigationView drawerNavigationView;
    protected ActionBarDrawerToggle drawerToggle;
    protected CoordinatorLayout rootLayout;
    protected Toolbar toolbar;
    protected String TAG;
    private Menu menu;

    public Tracker tracker;

    protected abstract int getLayoutResource();

    //LIFECYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        initBaseInstances();

        //Analytics
        tracker = ((AnalyticsApplication) getApplication()).getTracker(AnalyticsApplication.TrackerName.PROGRAMMATICALLY_APP_TRACKER);

        //PREFERENCES
        // Setting Default Values (call one/first time if false) http://developer.android.com/guide/topics/ui/settings.html#Defaults
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals(SettingsActivity.KEY_PREF_SHOW_TRACKS)) {
                    if (prefs.getBoolean(SettingsActivity.KEY_PREF_SHOW_TRACKS, false)) {
                        //Analytics
                        tracker.send(new HitBuilders.EventBuilder()
                                .setCategory("PREFERENCES")
                                .setAction("pref_show_tracks_ON")
                                .setLabel("boolean")
                                .setValue(1) //ценность события
                                .build());
                    } else {
                        //Analytics
                        tracker.send(new HitBuilders.EventBuilder()
                                .setCategory("PREFERENCES")
                                .setAction("pref_show_tracks_OFF")
                                .setLabel("boolean")
                                .setValue(0) //ценность события
                                .build());
                    }
                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
//        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();
        switch (id) {
            case R.id.refresh:
                //Analytics
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UI")
                        .setAction("tnc_logo_was_clicked")
                        .setLabel(getClass().getSimpleName())
                        .build());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        switch (item.getItemId()) {
//            case android.R.id.home:
//                Toast.makeText(this, "android.R.id.home", Toast.LENGTH_SHORT).show();
//                onBackPressed();
////              NavUtils.navigateUpFromSameTask(this); // up to parent activity https://developer.android.com/training/implementing-navigation/ancestral.html
//                return true;
//        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

//        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //HELPERS

    private void initBaseInstances() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerNavigationView = (NavigationView) findViewById(R.id.navigation);
        drawerNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Log.d(TAG, "onNavigationItemSelected()");
                //TODO checking if the item is in checked state or not
//                if(menuItem.isChecked()) menuItem.setChecked(false);
//                else menuItem.setChecked(true);
//                if(!menuItem.isChecked()) menuItem.setChecked(true);

                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()){
                    case R.id.transport:
                        Log.d(TAG, "transport");
                        startActivity(new Intent(getBaseContext(), TransportActivity.class));
                        //Analytics
                        tracker.send(new HitBuilders.EventBuilder()
                                .setCategory("UI")
                                .setAction("drawer_item_transport(map)_was_clicked")
                                .setLabel("base")
                                .build());
                        return true;
//                    case R.id.routes:
//                        Log.d(TAG, "routes");
//                        startActivityForResult(new Intent(getBaseContext(), RoutesActivity.class), TransportActivity.REQUEST_CODE_ROUTES);
//                        return true;
                    case R.id.app_settings:
                        Log.d(TAG, "settings");
                        startActivity(new Intent(getBaseContext(), SettingsActivity.class));
                        //Analytics
                        tracker.send(new HitBuilders.EventBuilder()
                                .setCategory("UI")
                                .setAction("drawer_item_settings_was_clicked")
                                .setLabel("base")
                                .build());
                        return true;
                    case R.id.app_about:
                        Log.d(TAG, "about");
                        startActivity(new Intent(getBaseContext(), AboutActivity.class));
                        //Analytics
                        tracker.send(new HitBuilders.EventBuilder()
                                .setCategory("UI")
                                .setAction("drawer_item_about_was_clicked")
                                .setLabel("base")
                                .build());
                        return true;
                    default:
                        Log.d(TAG, "default");
                        return true;
                }
            }
        });
        drawerToggle = new ActionBarDrawerToggle(BaseActivity.this, drawerLayout, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
//                TODO make appropriate menu_item at nav_drawer_menu checked
                //Analytics
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UI")
                        .setAction("drawer_was_opened")
                        .setLabel("base")
                        .build());
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);

//        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setElevation(0); // it sets at layout by app:elevation="0dp"
    }

    protected void setRefreshActionButtonState(final boolean refreshing) {
        if (menu != null) {
            final MenuItem refreshItem = menu.findItem(R.id.refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.actionbar_refresh);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    //GETTERS & SETTERS

    protected void setTAG(String TAG) {
        this.TAG = TAG;
    }

//    private void syncActionBarArrowState() {
//        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
//        drawerToggle.setDrawerIndicatorEnabled(backStackEntryCount == 0);
//        drawerToggle.setDrawerIndicatorEnabled(false);
//    }
}
