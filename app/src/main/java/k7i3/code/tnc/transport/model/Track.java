package k7i3.code.tnc.transport.model;

import android.graphics.Color;

import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonMultiLineString;

import org.json.JSONObject;

/**
 * Created by k7i3 on 03.11.15.
 */
//{
//        "id": 39409966762,
//        "num": "110",
//        "distance": 40.85,
//        "routeDurInMin": 85,
//        "distanceSub": 40.85,
//        "routeDurInMinSub": 0,
//        "name": "110 ВАЗ-Аэропорт",
//        "routeType": {
//        "id": 38842135405,
//        "code": "1",
//        "description": "город",
//        "isDeleted": 0
//        },
//        "oldID": 1,
//        "routeGeom": "MULTILINESTRING((55.885345 54.5679233333333,55.8853083333333 54.5679133333333,55.8846733333333 54.5677116666667,55.8844783333333 54.567395,55.8844783333333 54.567395))",
//        "fleet": {
//        "id": 39487670382,
//        "fullName": "АТП №1 Уфимское",
//        "name": "АТП №1 Уфимское",
//        "possibility": 0,
//        "createdBy": {
//        "firstName": "",
//        "lastName": "",
//        "email": "",
//        "activityStatus": 1,
//        "typeMenu": 0,
//        "primaryGroupId": 6666,
//        "failedLoginAttempts": 0,
//        "phone": "",
//        "id": 6651,
//        "name": "Администратор БАТ",
//        "type": 2,
//        "createdById": 1,
//        "changedById": 1,
//        "changedDateTime": "Sep 10, 2015 10:39:42 AM",
//        "isDeleted": 0
//        },
//        "createdDateTime": "Jul 24, 2014 12:00:00 AM",
//        "changedBy": {
//        "firstName": "",
//        "lastName": "",
//        "email": "",
//        "activityStatus": 1,
//        "typeMenu": 0,
//        "primaryGroupId": 6666,
//        "failedLoginAttempts": 0,
//        "phone": "",
//        "id": 6651,
//        "name": "Администратор БАТ",
//        "type": 2,
//        "createdById": 1,
//        "changedById": 1,
//        "changedDateTime": "Sep 10, 2015 10:39:42 AM",
//        "isDeleted": 0
//        },
//        "changedDateTime": "Dec 22, 2014 2:26:24 PM",
//        "isDeleted": 0
//        },
//        "minOffsetInSecDef": 180,
//        "maxOffsetInSecDef": 180,
//        "minFactInSecDef": 1200,
//        "maxFactInSecDef": 1200,
//        "areaRouteGeom": "MULTIPOLYGON(((55.885384670972 54.5678188318241,55.8853514749101 54.5678119941909,55.8846545442149 54.567415366383,55.8846579963901 54.5673949999953)))",
//        "parkBus": {
//        "id": 38842153700,
//        "name": "АТП №1 Уфимское",
//        "contactName": "АТП №1 Уфимское",
//        "createdBy": {
//        "firstName": "",
//        "lastName": "",
//        "email": "",
//        "activityStatus": 1,
//        "typeMenu": 0,
//        "primaryGroupId": 6666,
//        "failedLoginAttempts": 0,
//        "phone": "",
//        "id": 6651,
//        "name": "Администратор БАТ",
//        "type": 2,
//        "createdById": 1,
//        "changedById": 1,
//        "changedDateTime": "Sep 10, 2015 10:39:42 AM",
//        "isDeleted": 0
//        },
//        "createdDateTime": "Jul 21, 2014 5:13:53 PM",
//        "isDeleted": 0
//        },
//        "color": -16711732,
//        "active": 1,
//        "routeGeomGJ": "{\"type\":\"Feature\",\"geometry\":{\"type\":\"MultiLineString\",\"coordinates\":[[[55.88535,54.56792],[55.88531,54.56791],[55.88448,54.5674],[55.88448,54.5674]]]},\"properties\":{},\"id\":\"geom\"}",
//        "areaRouteGeomGJ": "{\"type\":\"Feature\",\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[55.88538,54.56782],[55.88535,54.56781],[55.88465,54.56742],[55.88466,54.56739]]]]},\"properties\":{},\"id\":\"geom\"}",
//        "createdBy": {
//        "firstName": "",
//        "lastName": "",
//        "email": "",
//        "activityStatus": 1,
//        "typeMenu": 0,
//        "primaryGroupId": 11337787,
//        "failedLoginAttempts": 0,
//        "phone": "",
//        "id": 11345671,
//        "name": "bat1_2",
//        "type": 2,
//        "createdById": 6651,
//        "createdDateTime": "Feb 19, 2014 3:46:46 PM",
//        "changedById": 6651,
//        "changedDateTime": "Feb 19, 2014 3:51:37 PM",
//        "isDeleted": 0
//        },
//        "createdDateTime": "Jul 24, 2014 12:07:35 PM",
//        "changedBy": {
//        "firstName": "",
//        "lastName": "",
//        "email": "",
//        "activityStatus": 1,
//        "typeMenu": 0,
//        "primaryGroupId": 11337787,
//        "failedLoginAttempts": 0,
//        "phone": "",
//        "id": 11345310,
//        "name": "bat1_1",
//        "type": 2,
//        "createdById": 6651,
//        "createdDateTime": "Feb 19, 2014 3:46:24 PM",
//        "changedById": 6651,
//        "changedDateTime": "Feb 19, 2014 3:51:01 PM",
//        "isDeleted": 0
//        },
//        "changedDateTime": "Oct 12, 2015 8:26:51 AM",
//        "isDeleted": 0
//        }
public class Track {
    private JSONObject routeGeomGJ;
    private JSONObject areaRouteGeomGJ;
//    Color color; // TODO int?

    public Track(JSONObject routeGeomGJ, JSONObject areaRouteGeomGJ) {
        this.routeGeomGJ = routeGeomGJ;
        this.areaRouteGeomGJ = areaRouteGeomGJ;
    }

    @Override
    public String toString() {
        return "Track{" +
                "routeGeomGJ=" + routeGeomGJ +
                ", areaRouteGeomGJ=" + areaRouteGeomGJ +
                '}';
    }

    public JSONObject getRouteGeomGJ() {
        return routeGeomGJ;
    }

    public void setRouteGeomGJ(JSONObject routeGeomGJ) {
        this.routeGeomGJ = routeGeomGJ;
    }

    public JSONObject getAreaRouteGeomGJ() {
        return areaRouteGeomGJ;
    }

    public void setAreaRouteGeomGJ(JSONObject areaRouteGeomGJ) {
        this.areaRouteGeomGJ = areaRouteGeomGJ;
    }
}
