package k7i3.code.tnc.transport.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import k7i3.code.tnc.transport.AnalyticsApplication;
import k7i3.code.tnc.transport.Constants;
import k7i3.code.tnc.transport.helper.SecurityHelper;
import k7i3.code.tnc.transport.helper.Utils;
import k7i3.code.tnc.transport.model.InvocationContext;
import k7i3.code.tnc.transport.model.Route;
import k7i3.code.tnc.transport.model.Transport;

/**
 * Created by k7i3 on 16.09.15.
 */
public class TransportLoader extends AsyncTaskLoader<Map<Route, List<Transport>>> {
    private static final String TAG = "====> TransportLoader";
    private static final String URL = "http://62.133.191.98:47201/vms-ws/rest/WayBillSimpleWS/getListTransport";

    private List<Route> routes;
    private GsonBuilder gsonCustomDateFormat;
    private GsonBuilder gson;
    private Type type;
    private InvocationContext invocationContext;
    private Map<Route, List<Transport>> transportByRoute;


    public TransportLoader(Context context, Bundle args) {
        super(context);
        Log.d(TAG, "CONSTRUCTOR!!!");
        routes = args.getParcelableArrayList(Constants.ROUTES);
        Log.d(TAG, "CONSTRUCTOR!!! routes.size(): " + routes.size());
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading()");
        super.onStartLoading();
        transportByRoute = new HashMap<>();
        invocationContext = new InvocationContext(Utils.getIPAddress(true), "Android", SecurityHelper.encrypt("Klim55CVfg"), "Klim");
        gsonCustomDateFormat = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // out
        gson = new GsonBuilder(); // in (default DateFormat)
        type = new TypeToken<ArrayList<Transport>>(){}.getType();
        forceLoad();
    }

    @Override
    public Map<Route, List<Transport>> loadInBackground() {
        Log.d(TAG, "loadInBackground()");

        //Analytics
        Tracker tracker = ((AnalyticsApplication) getContext()).getTracker(AnalyticsApplication.TrackerName.PROGRAMMATICALLY_APP_TRACKER);

        try {
            String request;
            OutputStream out;
            InputStream in;
            BufferedReader bufferedReader;
            List<Transport> transport;
            Date date = new Date();
            date.setHours(0);
            for (Route route : routes) {
                request = gsonCustomDateFormat.create().toJson(new Object[]{invocationContext, route.getRemoteId(), date});
                Log.d(TAG, "request: " + request);

                HttpURLConnection c = (HttpURLConnection) new URL(URL).openConnection();
                c.setRequestMethod("POST");
                c.setDoInput(true);
                c.setDoOutput(true);

                c.connect();

                out = c.getOutputStream();
                out.write(request.getBytes("UTF-8"));
                out.flush();
                out.close();

                // IN
                Log.d(TAG, "c.getResponseCode(): " + c.getResponseCode());
                in = (c.getResponseCode() == 200) ? c.getInputStream() : c.getErrorStream();
                bufferedReader = new BufferedReader(new InputStreamReader(in));

                transport = gson.create().fromJson(bufferedReader, type);

                //TODO ?
                bufferedReader.close();
                in.close();
                c.disconnect();

                addTransportToRoute(transport, route);

                //Analytics
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("DATA")
                        .setAction("transport_loader_success")
                        .setLabel("loaders")
                        .build());
            }
        } catch (Exception e) {
            Log.d(TAG, "error: " + e.getMessage() + " " + e);

            //Analytics TODO exception_tracker instead of it?
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("DATA")
                    .setAction("transport_loader_error")
                    .setLabel("loaders")
                    .build());
        }
        return transportByRoute;
    }

    private void addTransportToRoute(List<Transport> transport, Route route) {
        Log.d(TAG, "addTransportToRoute");
        Log.d(TAG, "route: " + route.getNum() + " transport.size(): " + transport.size() + " first TRANSPORT:" + transport.get(0).toString() );
        transportByRoute.put(route, transport);
    }
}
