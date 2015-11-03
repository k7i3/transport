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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import k7i3.code.tnc.transport.Constants;
import k7i3.code.tnc.transport.helper.SecurityHelper;
import k7i3.code.tnc.transport.helper.Utils;
import k7i3.code.tnc.transport.model.InvocationContext;
import k7i3.code.tnc.transport.model.Route;
import k7i3.code.tnc.transport.model.Track;
import k7i3.code.tnc.transport.model.Transport;

/**
 * Created by k7i3 on 03.11.15.
 */
public class TracksLoader extends AsyncTaskLoader<Map<Route, Track>> {
    private static final String TAG = "====> TracksLoader";
    private static final String URL = "http://62.133.191.98:47201/vms-ws/rest/RouteWS/getCurrentObject";

    private List<Route> routes;

    private InvocationContext invocationContext;
    private GsonBuilder gson;

    private Map<Route, Track> trackByRoute;

    public TracksLoader (Context context, Bundle args) {
        super(context);
        Log.d(TAG, "CONSTRUCTOR!!!");
        routes = args.getParcelableArrayList(Constants.ROUTES);
        Log.d(TAG, "CONSTRUCTOR!!! routes.size(): " + routes.size());
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading()");
        super.onStartLoading();
        trackByRoute = new HashMap<>();
        invocationContext = new InvocationContext(Utils.getIPAddress(true), "Android", SecurityHelper.encrypt("Klim55CVfg"), "Klim");
        gson = new GsonBuilder();
        forceLoad();
    }

    @Override
    public Map<Route, Track> loadInBackground() {
        Log.d(TAG, "loadInBackground()");

        try {
            String request;
            OutputStream out;
            InputStream in;
            BufferedReader bufferedReader;
            Track track;
            for (Route route : routes) {
                request = gson.create().toJson(new Object[]{invocationContext, route.getId()});
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

                track = gson.create().fromJson(bufferedReader, Track.class);

                //TODO ?
                bufferedReader.close();
                in.close();
                c.disconnect();

                trackByRoute.put(route, track);
            }
        } catch (Exception e) {
            Log.d(TAG, "error: " + e.getMessage() + " " + e);
        }
        return trackByRoute;
    }
}
