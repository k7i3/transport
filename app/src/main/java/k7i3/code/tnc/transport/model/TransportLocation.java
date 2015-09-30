package k7i3.code.tnc.transport.model;

import java.util.Date;

/**
 * Created by k7i3 on 25.09.15.
 */
//1
//{"msgId":0,"className":"ru.infor.ws.objects.vms.entities.NDData",
// "dataJson":[
// {"type":{"id":6290,"code":"01","description":"по времени","isDeleted":0},"tripIndex":0,"powerValue":2764.0,"ae1":0.0,"ae2":0.0,"de1":1,"de2":1,"de3":1,"de4":1,"de5":0,"de6":1,"de7":0,"alarmDevice":0,"deviceId":9399,"createdDateTime":"Sep 28, 2015 3:40:51 PM","lat":54.720715,"lon":55.950318333333335,"speed":14.0752,"alarm":0},
// {"type":{"id":6290,"code":"01","description":"по времени","isDeleted":0},"tripIndex":0,"powerValue":2763.0,"ae1":110.0,"ae2":0.0,"de1":1,"de2":1,"de3":1,"de4":0,"de5":1,"de6":1,"de7":0,"alarmDevice":0,"deviceId":9457,"createdDateTime":"Sep 28, 2015 3:40:50 PM","lat":54.743568333333336,"lon":55.989871666666666,"speed":50.744800000000005,"alarm":0}
// ],
// "sid":"0015002ecc7c8","serviceName":"NDDataWS","methodName":"sendList","messageType":"ru.infor.websocket.transport.DataPack"}

//2
//{"msgId":0,"className":"ru.infor.ws.objects.vms.entities.NDData",
// "dataJson":[
// {"type":{"id":6290,"code":"01","description":"по времени","isDeleted":0},"tripIndex":0,"powerValue":27.05,"ae1":0.0,"ae2":0.0,"direction":86.0,"alarmDevice":0,"deviceId":87494846933,"createdDateTime":"Sep 28, 2015 3:41:10 PM","lat":54.57453666666667,"lon":55.913535,"speed":34.817600000000006,"gpsSatCount":19,"alarm":0},
// {"type":{"id":6290,"code":"01","description":"по времени","isDeleted":0},"tripIndex":0,"powerValue":27.07,"ae1":0.0,"ae2":0.0,"direction":78.0,"alarmDevice":0,"deviceId":87494846933,"createdDateTime":"Sep 28, 2015 3:41:09 PM","lat":54.57452833333333,"lon":55.91338833333333,"speed":36.299200000000006,"gpsSatCount":19,"alarm":0},
// {"type":{"id":6290,"code":"01","description":"по времени","isDeleted":0},"tripIndex":0,"alarmDevice":0,"deviceId":9633,"createdDateTime":"Sep 28, 2015 3:41:09 PM","lat":54.69870666666668,"lon":55.98778833333334,"speed":26.854000000000003,"alarm":0},
// {"type":{"id":6290,"code":"01","description":"по времени","isDeleted":0},"tripIndex":0,"powerValue":2757.0,"ae1":0.0,"ae2":0.0,"de1":0,"de2":1,"de3":1,"de4":1,"de5":0,"de6":1,"de7":0,"alarmDevice":0,"deviceId":9723,"createdDateTime":"Sep 28, 2015 3:41:09 PM","lat":54.767536666666665,"lon":56.04633,"speed":56.486,"alarm":0},
// {"type":{"id":6290,"code":"01","description":"по времени","isDeleted":0},"tripIndex":0,"powerValue":26.93,"ae1":0.0,"ae2":0.0,"direction":71.0,"alarmDevice":0,"deviceId":87494846933,"createdDateTime":"Sep 28, 2015 3:41:08 PM","lat":54.574508333333334,"lon":55.91323666666667,"speed":37.2252,"gpsSatCount":19,"alarm":0},
// {"type":{"id":6290,"code":"01","description":"по времени","isDeleted":0},"tripIndex":0,"powerValue":26.990000000000002,"ae1":0.0,"ae2":0.0,"direction":61.0,"alarmDevice":0,"deviceId":87494846933,"createdDateTime":"Sep 28, 2015 3:41:04 PM","lat":54.57433666666667,"lon":55.912625,"speed":44.077600000000004,"gpsSatCount":19,"alarm":0}
// ],
// "sid":"0015002ecc7c8","serviceName":"NDDataWS","methodName":"sendList","messageType":"ru.infor.websocket.transport.DataPack"}

public class TransportLocation {
    private long id;
    private long deviceId;
    private Date createdDataTime; //"createdDateTime":"Sep 30, 2015 12:10:57 PM" TODO why null (at Transport the same "lastTransfer": "Sep 30, 2015 4:57:59 AM", but working)?
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransportLocation that = (TransportLocation) o;

        return deviceId == that.deviceId;
    }

    @Override
    public int hashCode() {
        return (int) (deviceId ^ (deviceId >>> 32));
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
