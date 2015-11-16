package k7i3.code.tnc.transport.loader;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
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

//        TODO one of this
        loadFromServer();
//        loadFromFile();

        return trackByRoute;
    }

    private void loadFromFile() {
        try {
            InputStream in;
            for (Route route : routes) {
                in = getContext().getAssets().open("tracks/incorrect/" + route.getId() + ".json");

//                TODO doesn't work
//                bufferedReader = new BufferedReader(new InputStreamReader(in));
//                track = gson.create().fromJson(bufferedReader, Track.class);

                trackByRoute.put(route, parseJsonFromStream(in));
            }
        } catch (Exception e) {
            Log.d(TAG, "error: " + e.getMessage() + " " + e);
        }
    }

    private void loadFromServer() {
        try {
            String request;
            OutputStream out;
            InputStream in;
            Track track;
            for (Route route : routes) {
                // OUT
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

//                TODO doesn't work
//                bufferedReader = new BufferedReader(new InputStreamReader(in));
//                track = gson.create().fromJson(bufferedReader, Track.class);

                track = parseJsonFromStream(in);
                if (track != null) {
                    trackByRoute.put(route, track);
                }

                // CLOSE TODO finally?
                c.disconnect();
            }
        } catch (Exception e) {
            Log.d(TAG, "error: " + e.getMessage() + " " + e);
            e.printStackTrace();
        }
    }

//    TODO solve try/catch/finally/return
    private Track parseJsonFromStream(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        Track track = null;
        try {
            JSONObject jsonObject = new JSONObject(result.toString());
//            TODO if the JSON would be correct
//            JSONObject routeGeomGJ = jsonObject.getJSONObject("routeGeomGJ");
//            JSONObject areaRouteGeomGJ = jsonObject.getJSONObject("areaRouteGeomGJ");
            JSONObject routeGeomGJ = new JSONObject(jsonObject.getString("routeGeomGJ"));
            JSONObject areaRouteGeomGJ = new JSONObject(jsonObject.getString("areaRouteGeomGJ"));
            int color;
            if (jsonObject.has("color")) {
                color = jsonObject.getInt("color");
            } else {
                Log.d(TAG, "no color");
                color = Color.MAGENTA;
            }
            Log.d(TAG, "routeGeomGJ: " + routeGeomGJ.toString());
            Log.d(TAG, "areaRouteGeomGJ: " + areaRouteGeomGJ.toString());
            Log.d(TAG, "color: " + color);
            track = new Track(routeGeomGJ, areaRouteGeomGJ, color);
        } catch (JSONException e) {
            Log.d(TAG, "error: " + e.getMessage() + " " + e);
            e.printStackTrace();
        } finally {
                bufferedReader.close();
                in.close();
        }
        Log.d(TAG, "try/catch/finally/return");
        return track;
    }
}
