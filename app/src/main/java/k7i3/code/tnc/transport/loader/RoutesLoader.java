package k7i3.code.tnc.transport.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import k7i3.code.tnc.transport.Constants;
import k7i3.code.tnc.transport.helper.DateTimeHelper;
import k7i3.code.tnc.transport.helper.SecurityHelper;
import k7i3.code.tnc.transport.helper.Utils;
import k7i3.code.tnc.transport.model.InvocationContext;
import k7i3.code.tnc.transport.model.Route;

/**
 * Created by k7i3 on 07.09.15.
 */
public class RoutesLoader extends AsyncTaskLoader<List<Route>> {
    private static final String TAG = "====> RoutesLoader";
    private static final String URL = "http://62.133.191.98:47201/vms-ws/rest/WayBillSimpleWS/getListRoute";

    private InvocationContext invocationContext;
    private List<Route> routes;
    private int position;
    private GsonBuilder gson;
    private Type type;

    public RoutesLoader(Context context, Bundle args) {
        super(context);
//        TODO check args for understanding which routes is needed (all, favorites or nearest). Or may be need separate Loaders.
        position = args.getInt(Constants.KEY_POSITION, -1);
        Log.d(TAG, "position: " + position);
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading()");
        super.onStartLoading();
        invocationContext = new InvocationContext(Utils.getIPAddress(true), "Android", SecurityHelper.encrypt("Klim55CVfg"), "Klim");
        gson = new GsonBuilder();
        type = new TypeToken<ArrayList<Route>>(){}.getType();
        forceLoad();
    }

    @Override
    public List<Route> loadInBackground() {
        Log.d(TAG, "loadInBackground()");
        try {
            // OUT
            String request = gson.create().toJson(new Object[]{invocationContext, DateTimeHelper.now()});
            Log.d(TAG, "request: " + request); //[{"clientIPAddress":"192.168.137.201","initiator":"Android","password":"45BlPwIWKaZrIXlYNeCHQw\u003d\u003d","userName":"Klim"},"2015-09-10T12:31:11Z"]
            HttpURLConnection c = (HttpURLConnection) new URL(URL).openConnection();
            c.setRequestMethod("POST");
            c.setDoInput(true);
            c.setDoOutput(true);
            c.connect();

            OutputStream out = c.getOutputStream();
            out.write(request.getBytes("UTF-8"));
            out.flush();
            out.close();

            // IN
            Log.d(TAG, "c.getResponseCode(): " + c.getResponseCode());
            InputStream in = (c.getResponseCode() == 200) ? c.getInputStream() : c.getErrorStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            routes = gson.create().fromJson(bufferedReader, type);
//            String line;
//            StringBuilder stringBuilder = new StringBuilder();
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//            String response = stringBuilder.toString();
//            Log.d(TAG, "response: " + response);
        } catch (Exception e) {
            Log.d(TAG, "error: " + e.getMessage() + " " + e);
        }

//        WayBillSimpleWS.getListRoute
//        JSON
//        [
//        {"clientIPAddress":String, "initiator":String, "password":String, "userName":String},
//        Date
//        ]
//        параметры InvocationContext и дата на какое число нужен список маршрутов
//        вернется список маршрутов, геометрия там тоже будет, если она конечно задана у маршрута
//


//        WayBillSimpleWS.getListTransport(ic, routeId, date)  это список транспорта
//        JSON
//        [
//        {"clientIPAddress":String, "initiator":String, "password":String, "userName":String},
//        Long,
//        Date
//        ]


        //TEST
//        Log.d(TAG, "loadInBackground() before sleep");
//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException e) {
//            return null;
//        }
//        Log.d(TAG, "loadInBackground() after sleep");
//
//        routes = new ArrayList<>();
//        initMockRoutes();

        return routes;
    }
}
