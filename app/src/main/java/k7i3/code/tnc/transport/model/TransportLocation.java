package k7i3.code.tnc.transport.model;

import java.util.Date;

/**
 * Created by k7i3 on 25.09.15.
 */
public class TransportLocation {
    private long id;
    private long deviceId;
    private Date createdDataTime;
    private double lat;
    private double lon;
    private double speed;
    private int gpsSatCount;
    private int gsmSignalLevel;
    private double direction;

    public TransportLocation(long id, long deviceId, Date createdDataTime, double lat, double lon, double speed, int gpsSatCount, int gsmSignalLevel, double direction) {
        this.id = id;
        this.deviceId = deviceId;
        this.createdDataTime = createdDataTime;
        this.lat = lat;
        this.lon = lon;
        this.speed = speed;
        this.gpsSatCount = gpsSatCount;
        this.gsmSignalLevel = gsmSignalLevel;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "TransportLocation{" +
                "id=" + id +
                ", deviceId=" + deviceId +
                ", createdDataTime=" + createdDataTime +
                ", lat=" + lat +
                ", lon=" + lon +
                ", speed=" + speed +
                ", gpsSatCount=" + gpsSatCount +
                ", gsmSignalLevel=" + gsmSignalLevel +
                ", direction=" + direction +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public Date getCreatedDataTime() {
        return createdDataTime;
    }

    public void setCreatedDataTime(Date createdDataTime) {
        this.createdDataTime = createdDataTime;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getGpsSatCount() {
        return gpsSatCount;
    }

    public void setGpsSatCount(int gpsSatCount) {
        this.gpsSatCount = gpsSatCount;
    }

    public int getGsmSignalLevel() {
        return gsmSignalLevel;
    }

    public void setGsmSignalLevel(int gsmSignalLevel) {
        this.gsmSignalLevel = gsmSignalLevel;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }
}
