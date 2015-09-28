package k7i3.code.tnc.transport.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import k7i3.code.tnc.transport.helper.Utils;
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

    private final WebSocketConnection wsConnection = new WebSocketConnection();
    private static final String WS_URI = "ws://62.133.191.98:47201/vms-ws/socket";

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

    private void handleActionEventBus(final long[] deviceIds) {
        Log.d(TAG, "handleActionEventBus() start");
        Log.d(TAG, "handleActionEventBus()... deviceIds.length: " + deviceIds.length);

        final GsonBuilder gson = new GsonBuilder();

        //TODO Connect to WS and retrieve locations
        try {
            wsConnection.connect(WS_URI, new WebSocketHandler() {
                boolean isFirstMessage = true;

                @Override
                public void onOpen() {
                    Log.d(TAG, "Status: Connected to " + WS_URI);

                    //TODO Make it by GsonBuilder
//                    InvocationContext invocationContext = new InvocationContext(Utils.getIPAddress(true), "Android", SecurityHelper.encrypt("Klim55CVfg"), "Klim");
//                    GsonBuilder gson = new GsonBuilder();
//                    String request = gson.create().toJson(new Object[]{invocationContext, new Date()});
//                    Log.d(TAG, "request: " + request); //[{"clientIPAddress":"192.168.137.201","initiator":"Android","password":"45BlPwIWKaZrIXlYNeCHQw\u003d\u003d","userName":"Klim"},"2015-09-10T12:31:11Z"]

                    // 51 маршрут = 39419173957
                    // автобусы 51 маршрута ("deviceId") = 9781,8741,8751,8759,8761,8773,9069,8777,8781,8783,8785,8795,8801,8813,8815,8823,9075,8861,87360616019,87360630497,87360641757,87360652815

                    Log.d(TAG, "!!!deviceIds1: " + Arrays.toString(deviceIds));
                    String request = "{\"serviceName\":\"NDDataWS\"," +
                            "\"methodName\":\"sendList\"," +
                            "\"messageType\":\"ru.infor.ws.business.vms.websocket.objects.SubscribingOptions_SendListNDData\"," +
                            "\"context\":{\"userName\":\"Klim\",\"password\":\"45BlPwIWKaZrIXlYNeCHQw\\u003d\\u003d\"}," +
                            "\"deviceIdList\":" + Arrays.toString(deviceIds) + "}";
//                            "\"deviceIdList\":[9781,8741,8751,8759,8761,8773,9069,8777,8781,8783,8785,8795,8801,8813,8815,8823,9075,8861,87360616019,87360630497,87360641757,87360652815]}";
                    Log.d(TAG, "request: " + request);
                    wsConnection.sendTextMessage(request);
                }

                @Override
                public void onTextMessage(String payload) {
                    Log.d(TAG, "Got echo: " + payload);
                    if (isFirstMessage) {
//                        TODO handle status message
//                        {"status":0,"sid":"0015002ecc7ce","serviceName":"NDDataWS","methodName":"sendList","messageType":"ru.infor.websocket.transport.SubscribingResult"}
                        isFirstMessage = false;
                        Log.d(TAG, "isFirstMessage change to false");
                    } else {
                        Log.d(TAG, "!isFirstMessage");
                        LocationMessage locationMessage = gson.create().fromJson(payload, LocationMessage.class);
                        Log.d(TAG, "locationMessage.getDataJson().size(): " + locationMessage.getDataJson().size());
                        EventBus.getDefault().post(locationMessage);
                        Log.d(TAG, "EVENTBUS post");
                    }
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.d(TAG, "Connection lost: " + code + " " + reason);
//                    TODO try to connect else? change isFirstMessage to true?
                }
            });
        } catch (WebSocketException e) {
            Log.d(TAG, e.toString());
        }

        //TODO make boolean field
        while (true) {
//            Log.d(TAG, "loop");
        }












        //TEST
//        TransportLocation[] transportLocations = {new TransportLocation(0, 1, new Date(), 0, 0, 90, 10, 5, 210)};
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        EventBus.getDefault().post(new LocationMessage(transportLocations));

//        Log.d(TAG, "handleActionEventBus() finish");
    }
}
