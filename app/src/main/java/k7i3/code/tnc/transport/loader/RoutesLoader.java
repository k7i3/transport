package k7i3.code.tnc.transport.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import k7i3.code.tnc.transport.model.InvocationContext;
import k7i3.code.tnc.transport.model.Route;

/**
 * Created by k7i3 on 07.09.15.
 */
public class RoutesLoader extends AsyncTaskLoader<List<Route>> {
    private static final String TAG = "=====> RoutesLoader";
    private static final String URL = "http://62.133.191.98:47201/vms-ws/rest/WayBillSimpleWS/getListRoute";
    InvocationContext invocationContext;

    public RoutesLoader(Context context, Bundle args) {
        super(context);
        invocationContext = new InvocationContext("127.0.0.1", "Android", SecurityUtils.encrypt("!QAZxsw2".getBytes("UTF-8")), "Администратор БАТ");

//        TODO check args for understanding which routes is needed (all, favorites or nearest). Or may be need separate Loaders.
    }

    @Override
    public List<Route> loadInBackground() {

        try {
            // OUT
            String request = new GsonBuilder().create().toJson(new Object[]{invocationContext, new Date()});
            Log.d(TAG, "request: " + request);
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
            InputStream in = (c.getResponseCode() == 200) ? c.getInputStream() : c.getErrorStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String response = stringBuilder.toString();
            Log.d(TAG, "response: " + response);
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

        return null;
    }
}
