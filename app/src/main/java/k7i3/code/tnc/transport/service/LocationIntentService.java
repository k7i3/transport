package k7i3.code.tnc.transport.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import k7i3.code.tnc.transport.helper.Utils;
import k7i3.code.tnc.transport.model.TransportLocation;
import k7i3.code.tnc.transport.model.LocationMessage;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static helper methods.
 */
public class LocationIntentService extends IntentService {
    private static final String TAG = "====> LocationIntentService";

    public static final String ACTION_PENDING_INTENT = "k7i3.code.tnc.transport.service.action.PENDING_INTENT";
    public static final String ACTION_EVENT_BUS = "k7i3.code.tnc.transport.service.action.EVENT_BUS";

    public static final String EXTRA_DEVICE_IDS = "k7i3.code.tnc.transport.service.extra.DEVICE_IDS";
    public static final String EXTRA_PENDING_INTENT = "k7i3.code.tnc.transport.service.extra.EXTRA_PENDING_INTENT";

    //TEST PENDING INTENT
    public static final int STATUS_START = 100;
    public static final int STATUS_UPDATE = 101;
    public static final int STATUS_FINISH = 102;
    public static final int STATUS_ERROR = 103;

    public LocationIntentService() {
        super("LocationIntentService");
    }

    //STATIC HELPERS

    public static void startActionPendingIntent(Context context, Long[] deviceIds, PendingIntent pendingIntent) {
        Intent intent = new Intent(context, LocationIntentService.class);
        intent.setAction(ACTION_PENDING_INTENT);
        intent.putExtra(EXTRA_DEVICE_IDS, deviceIds); // error with cast Long[] to long[]
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);
        context.startService(intent);
    }

    public static void startActionEventBus(Context context, Long[] deviceIds) {
        Log.d(TAG, "startActionEventBus()... deviceIds.length: " + deviceIds.length);
        Intent intent = new Intent(context, LocationIntentService.class);
        intent.setAction(ACTION_EVENT_BUS);
        intent.putExtra(EXTRA_DEVICE_IDS, Utils.castLongArrayToPrimitive(deviceIds));
        context.startService(intent);
    }

    //LIFECYCLE

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent()");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PENDING_INTENT.equals(action)) {
                final long[] deviceIds = intent.getLongArrayExtra(EXTRA_DEVICE_IDS);
                PendingIntent pendingIntent = intent.getParcelableExtra(EXTRA_PENDING_INTENT);
                handleActionPendingIntent(deviceIds, pendingIntent);
            } else if (ACTION_EVENT_BUS.equals(action)) {
                final long[] deviceIds = intent.getLongArrayExtra(EXTRA_DEVICE_IDS);
                Log.d(TAG, "onHandleIntent()... deviceIds.length: " + deviceIds.length);
                handleActionEventBus(deviceIds);
            }
        }
    }

    //HELPERS

    private void handleActionPendingIntent(long[] deviceIds, PendingIntent pendingIntent) {
        Log.d(TAG, "handleActionPendingIntent() start");
        try {
            try {
                //TEST
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
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "handleActionPendingIntent() finish");
    }

    private void handleActionEventBus(long[] deviceIds) {
        Log.d(TAG, "handleActionEventBus() start");
        Log.d(TAG, "handleActionEventBus()... deviceIds.length: " + deviceIds.length);

        TransportLocation[] transportLocations = {new TransportLocation(0, 1, new Date(), 0, 0, 90, 10, 5, 210)};
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        EventBus.getDefault().post(new LocationMessage(transportLocations));



        Log.d(TAG, "handleActionEventBus() finish");
    }
}
