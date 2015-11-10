package k7i3.code.tnc.transport.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.model.Marker;

import java.util.List;
import java.util.Map;

import k7i3.code.tnc.transport.model.Route;
import k7i3.code.tnc.transport.model.Track;
import k7i3.code.tnc.transport.model.Transport;

/**
 * Created by k7i3 on 06.10.15.
 */
public class RetainedTransportFragment extends Fragment {

    private List<Route> routes;
    private Map<Route, List<Transport>> transportByRoute;
    private Map<Route, Track> trackByRoute;
    private Map<Long, Marker> markerByDeviceId;

    //LIFECYCLE

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    //GETTERS & SETTERS

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public Map<Route, List<Transport>> getTransportByRoute() {
        return transportByRoute;
    }

    public void setTransportByRoute(Map<Route, List<Transport>> transportByRoute) {
        this.transportByRoute = transportByRoute;
    }

    public Map<Route, Track> getTrackByRoute() {
        return trackByRoute;
    }

    public void setTrackByRoute(Map<Route, Track> trackByRoute) {
        this.trackByRoute = trackByRoute;
    }

    public Map<Long, Marker> getMarkerByDeviceId() {
        return markerByDeviceId;
    }

    public void setMarkerByDeviceId(Map<Long, Marker> markerByDeviceId) {
        this.markerByDeviceId = markerByDeviceId;
    }
}
