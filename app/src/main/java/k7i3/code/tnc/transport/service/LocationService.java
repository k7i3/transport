package k7i3.code.tnc.transport.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import k7i3.code.tnc.transport.helper.Utils;
import k7i3.code.tnc.transport.model.LocationMessage;

public class LocationService extends Service {
    private static final String TAG = "====> LocationService";
    private static final String WS_URI = "ws://62.133.191.98:47201/vms-ws/socket";
    private static final String EXTRA_DEVICE_IDS = "k7i3.code.tnc.transport.service.extra.DEVICE_IDS";
    private ExecutorService executorService;
    private WebSocketConnection wsConnection;


    public LocationService() {
    }

    public static void start(Context context, Long[] deviceIds) {
        Log.d(TAG, "start()" + deviceIds.length);
        Intent intent = new Intent(context, LocationService.class);
        intent.putExtra(EXTRA_DEVICE_IDS, Utils.castLongArrayToPrimitive(deviceIds));
        context.startService(intent);
    }

    public static void stop(Context context) {
        Log.d(TAG, "stop()");
        Intent intent = new Intent(context, LocationService.class);
        context.stopService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
        executorService = Executors.newFixedThreadPool(1);
        wsConnection = new WebSocketConnection();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
        wsConnection.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");

        long[] deviceIds = intent.getLongArrayExtra(EXTRA_DEVICE_IDS);
        ConnectionToWS connectionToWS = new ConnectionToWS(startId, deviceIds);
        executorService.execute(connectionToWS);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class ConnectionToWS implements Runnable {
        int startId;
        long[] deviceIds;

        public ConnectionToWS(int startId, long[] deviceIds) {
            this.startId = startId;
            this.deviceIds = deviceIds;
        }

        @Override
        public void run() {
            connectToWS(deviceIds);
        }

        private void connectToWS(final long[] deviceIds) {
            final GsonBuilder gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // "createdDateTime":"Sep 30, 2015 12:10:57 PM" - dateFormat doesn't work
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
                            locationMessage.filterDuplicates();
                            EventBus.getDefault().post(locationMessage);
                            Log.d(TAG, "EVENTBUS post");
                        }
                    }

                    @Override
                    public void onClose(int code, String reason) {
                        Log.d(TAG, "Connection lost: " + code + " " + reason);
                        reconnectToWS(deviceIds);
                    }
                });
            } catch (WebSocketException e) {
                Log.d(TAG, e.toString());
                reconnectToWS(deviceIds);
            }
        }

        private void reconnectToWS(long[] deviceIds) {
            Log.d(TAG, "reconnectToWS()");
            wsConnection.disconnect();
            connectToWS(deviceIds);
        }
    }

}
