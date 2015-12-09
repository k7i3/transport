package k7i3.code.tnc.transport.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;

import k7i3.code.tnc.transport.AnalyticsApplication;
import k7i3.code.tnc.transport.R;

/**
 * Created by k7i3 on 17.11.15.
 */
public class SettingsActivity extends BaseActivity {

    public static final String KEY_PREF_SHOW_TRACKS = "pref_show_tracks";

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTAG("====> RoutesActivity");
        Log.d(TAG, "onCreate()");

        drawerToggle.setDrawerIndicatorEnabled(false);

//        TODO to initInstances()
        // disable refreshing
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setEnabled(false);

        // Display the fragment as the main content.
//        getFragmentManager().beginTransaction()
//                .replace(android.R.id.content, new SettingsFragment())
//                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}