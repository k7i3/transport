package k7i3.code.tnc.transport.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by k7i3 on 13.08.15.
 */
@Table(name = "Routes", id = BaseColumns._ID)
public class Route extends Model implements Parcelable{
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    @SerializedName("id")
    private long remoteId;

    @Column(name = "num")
    private String num;

    @Column(name = "distance")
    private double distance;

    @Column(name = "routeDurInMin")
    private int routeDurInMin;

    @Column(name = "name")
    private String name;

    private boolean checked;

    public Route() {
        super();
    }

    public Route(long remoteId, String num, int distance, int routeDurInMin, String name) {
        super();
        this.remoteId = remoteId;
        this.num = num;
        this.distance = distance;
        this.routeDurInMin = routeDurInMin;
        this.name = name;
    }

    private Route(Parcel in) {
        super();
        remoteId = in.readLong();
        num = in.readString();
        distance = in.readDouble();
        routeDurInMin = in.readInt();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(remoteId);
        dest.writeString(num);
        dest.writeDouble(distance);
        dest.writeInt(routeDurInMin);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<Route> CREATOR
            = new Parcelable.Creator<Route>() {
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (remoteId != route.remoteId) return false;
        if (Double.compare(route.distance, distance) != 0) return false;
        if (routeDurInMin != route.routeDurInMin) return false;
        if (!num.equals(route.num)) return false;
        return name.equals(route.name);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (remoteId ^ (remoteId >>> 32));
        result = 31 * result + num.hashCode();
        temp = Double.doubleToLongBits(distance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + routeDurInMin;
        result = 31 * result + name.hashCode();
        return result;
    }

    public void toggle() {
        setChecked(!checked);
    }

    public Boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(long remoteId) {
        this.remoteId = remoteId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getRouteDurInMin() {
        return routeDurInMin;
    }

    public void setRouteDurInMin(int routeDurInMin) {
        this.routeDurInMin = routeDurInMin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
