package k7i3.code.tnc.transport;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

/**
 * Created by k7i3 on 07.12.15.
 */
//TODO example https://github.com/googleanalytics/cute-pets-example-android-app/tree/master/app/src/main/java/com/example/cutepets
public class AnalyticsApplication extends com.activeandroid.app.Application {
    private static final String PROPERTY_ID = "UA-71106277-1";
    /**
     * The Analytics singleton. The field is set in onCreate method override when the application
     * class is initially created.
     */
    private static GoogleAnalytics analytics;

    /**
     * The tracker. The field is from onCreate callback when the application is
     * initially created.
     */
    private static Tracker tracker;

    private static Tracker defaultTracker;

    private static HashMap<TrackerName, Tracker> trackers = new HashMap<>();

    public AnalyticsApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        analytics = GoogleAnalytics.getInstance(this);
        analytics.enableAutoActivityReports(this);

        tracker = analytics.newTracker("UA-71106277-1");

        // Provide unhandled exceptions reports. Do that first after creating the tracker
        tracker.enableExceptionReporting(true);

        // Enable Remarketing, Demographics & Interests reports
        // https://developers.google.com/analytics/devguides/collection/android/display-features
        tracker.enableAdvertisingIdCollection(true);

        // Enable automatic activity tracking for your app
        tracker.enableAutoActivityTracking(true);

        tracker.send(new HitBuilders.ScreenViewBuilder().setCustomDimension(1, null).build());
    }

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     *
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        XML_APP_TRACKER // Configuration based on xml
    }

    synchronized public Tracker getTracker(TrackerName trackerId) {
        if (!trackers.containsKey(trackerId)) {
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics().newTracker(PROPERTY_ID) : analytics().newTracker(R.xml.app_tracker);
            trackers.put(trackerId, t);
        }
        return trackers.get(trackerId);
    }

    synchronized public Tracker getDefaultTracker() {
        if (defaultTracker == null) {
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            defaultTracker = analytics().newTracker(R.xml.global_tracker);
        }
        return defaultTracker;
    }

    /**
     * Access to the global Analytics singleton. If this method returns null you forgot to either
     * set android:name="&lt;this.class.name&gt;" attribute on your application element in
     * AndroidManifest.xml or you are not setting this.analytics field in onCreate method override.
     */
    public static GoogleAnalytics analytics() {
        return analytics;
    }

    /**
     * The tracker. If this method returns null you forgot to either set
     * android:name="&lt;this.class.name&gt;" attribute on your application element in
     * AndroidManifest.xml or you are not setting this.tracker field in onCreate method override.
     */
    public static Tracker tracker() {
        return tracker;
    }
}
