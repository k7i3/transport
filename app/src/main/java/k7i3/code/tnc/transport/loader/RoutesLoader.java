package k7i3.code.tnc.transport.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import k7i3.code.tnc.transport.Constants;
import k7i3.code.tnc.transport.helper.DateTimeHelper;
import k7i3.code.tnc.transport.helper.SecurityHelper;
import k7i3.code.tnc.transport.model.InvocationContext;
import k7i3.code.tnc.transport.model.Route;

/**
 * Created by k7i3 on 07.09.15.
 */
public class RoutesLoader extends AsyncTaskLoader<List<Route>> {
    private static final String TAG = "=====> RoutesLoader";
    private static final String URL = "http://62.133.191.98:47201/vms-ws/rest/WayBillSimpleWS/getListRoute";

    InvocationContext invocationContext;
    private List<Route> routes;
    private int position;

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
        invocationContext = new InvocationContext("127.0.0.1", "Android", SecurityHelper.encrypt("!QAZxsw2"), "Администратор БАТ");
        forceLoad();
    }

    @Override
    public List<Route> loadInBackground() {
        Log.d(TAG, "loadInBackground() before sleep");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            return null;
        }
        Log.d(TAG, "loadInBackground() after sleep");

        routes = new ArrayList<>();
        initMockRoutes();

        return routes;



//        try {
//            // OUT
//            String request = new GsonBuilder().create().toJson(new Object[]{invocationContext, DateTimeHelper.now()});
//            Log.d(TAG, "request: " + request);
//            HttpURLConnection c = (HttpURLConnection) new URL(URL).openConnection();
//            c.setRequestMethod("POST");
//            c.setDoInput(true);
//            c.setDoOutput(true);
//            c.connect();
//
//            OutputStream out = c.getOutputStream();
//            out.write(request.getBytes("UTF-8"));
//            out.flush();
//            out.close();
//
//            // IN
//            InputStream in = (c.getResponseCode() == 200) ? c.getInputStream() : c.getErrorStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
//
////            Student obj = gson.fromJson(buffered, Student.class);
//
//
//            String line;
//            StringBuilder stringBuilder = new StringBuilder();
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//            String response = stringBuilder.toString();
//            Log.d(TAG, "response: " + response);
//        } catch (Exception e) {
//            Log.d(TAG, "error: " + e.getMessage() + " " + e);
//        }

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
    }

    private void initMockRoutes() {
        Log.d(TAG, "initMockRoutes()");
        routes.add(new Route(position + "", "Точка А", "Точка Б"));
        routes.add(new Route("110", "ВАЗ", "Аэропорт"));
        routes.add(new Route("110с", "ДОК", "Аэропорт"));
        routes.add(new Route("51", "Точка А", "Точка Б"));
        routes.add(new Route("51а", "Точка А", "Точка Б"));
        routes.add(new Route("69", "Точка А", "Точка Б"));
        routes.add(new Route("74", "Точка А", "Точка Б"));
        routes.add(new Route("57", "Точка А", "Точка Б"));
        routes.add(new Route("290", "Точка А", "Точка Б"));
        routes.add(new Route("226", "Точка А", "Точка Б"));
    }
}
