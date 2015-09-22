package k7i3.code.tnc.transport.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static helper methods.
 */
public class LocationIntentService extends IntentService {
    private static final String TAG = "LocationIntentService";

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_PENDING_INTENT = "k7i3.code.tnc.transport.service.action.PENDING_INTENT";

    private static final String ACTION_BAZ = "k7i3.code.tnc.transport.service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "k7i3.code.tnc.transport.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "k7i3.code.tnc.transport.service.extra.PARAM2";

    public static final String EXTRA_DEVICE_IDS = "k7i3.code.tnc.transport.service.extra.DEVICE_IDS";
    public static final String EXTRA_PENDING_INTENT = "k7i3.code.tnc.transport.service.extra.EXTRA_PENDING_INTENT";

    //TEST
    public static final int STATUS_START = 100;
    public static final int STATUS_UPDATE = 101;
    public static final int STATUS_FINISH = 102;
    public static final int STATUS_ERROR = 103;




    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionPendingIntent(Context context, Long[] deviceIds, PendingIntent pendingIntent) {
        Intent intent = new Intent(context, LocationIntentService.class);
        intent.setAction(ACTION_PENDING_INTENT);
//        intent.putExtra(EXTRA_DEVICE_IDS, deviceIds);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, LocationIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public LocationIntentService() {
        super("LocationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PENDING_INTENT.equals(action)) {
//                final long[] deviceIds = intent.getLongArrayExtra(EXTRA_DEVICE_IDS);
                final long[] deviceIds = null;
                PendingIntent pendingIntent = intent.getParcelableExtra(EXTRA_PENDING_INTENT);
                handleActionPendingIntent(deviceIds, pendingIntent);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionPendingIntent(long[] deviceIds, PendingIntent pendingIntent) {
        // TODO: Handle action Pending Intent
        try {
            Log.d(TAG, "handleActionPendingIntent() before sleep loop");
            try {
                TimeUnit.SECONDS.sleep(5);
                pendingIntent.send(STATUS_START);
                TimeUnit.SECONDS.sleep(5);
                pendingIntent.send(STATUS_UPDATE);
                TimeUnit.SECONDS.sleep(5);
                pendingIntent.send(STATUS_FINISH);
                TimeUnit.SECONDS.sleep(5);
                pendingIntent.send(STATUS_ERROR);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "handleActionPendingIntent() after sleep loop");
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
