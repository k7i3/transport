package k7i3.code.tnc.transport.model;

/**
 * Created by k7i3 on 25.09.15.
 */
public class LocationMessage {
    private TransportLocation[] transportLocations;

    public LocationMessage(TransportLocation[] transportLocations) {
        this.transportLocations = transportLocations;
    }

    public TransportLocation[] getTransportLocations() {
        return transportLocations;
    }

    public void setTransportLocations(TransportLocation[] transportLocations) {
        this.transportLocations = transportLocations;
    }
}
