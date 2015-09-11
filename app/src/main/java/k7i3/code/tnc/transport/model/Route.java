package k7i3.code.tnc.transport.model;

/**
 * Created by k7i3 on 13.08.15.
 */
public class Route {
    private long id;
    private String num;
    private double distance;
    private int routeDurInMin;
    private String name;

    public Route(long id, String num, int distance, int routeDurInMin, String name) {
        this.id = id;
        this.num = num;
        this.distance = distance;
        this.routeDurInMin = routeDurInMin;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (id != route.id) return false;
        if (Double.compare(route.distance, distance) != 0) return false;
        if (routeDurInMin != route.routeDurInMin) return false;
        if (!num.equals(route.num)) return false;
        return name.equals(route.name);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + num.hashCode();
        temp = Double.doubleToLongBits(distance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + routeDurInMin;
        result = 31 * result + name.hashCode();
        return result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
