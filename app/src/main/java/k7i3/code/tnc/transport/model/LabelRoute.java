package k7i3.code.tnc.transport.model;

import android.provider.BaseColumns;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by k7i3 on 26.11.15.
 */
@Table(name = "LabelRoute", id = BaseColumns._ID)
public class LabelRoute extends Model {
    private static final String TAG = "====> LabelRoute";

    @Column(name = "label", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.NO_ACTION)
    Label label;

    @Column(name = "route", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.NO_ACTION)
    Route route;

    public LabelRoute() {
        super();
    }

    public LabelRoute(Label label, Route route) {
        super();
        this.label = label;
        this.route = route;
    }

    //HELPERS

    public static void deleteLabelRouteByLable(Label label) {
        ActiveAndroid.beginTransaction();
        try {
            for (LabelRoute labelRoute : getLabelRouteByLabel(label)){
                labelRoute.delete();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }

    public static List<LabelRoute> getLabelRouteByLabel(Label label) {
        return new Select()
                .from(LabelRoute.class)
                .where("label = ?", label.getId())
                .execute();
    }

    //GETTERS & SETTERS

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }


//        TODO {FROM LABEL.CLASS} ===> doesn't work: http://stackoverflow.com/questions/26207948/activeandroid-many-to-many-relationship / http://stackoverflow.com/questions/17645900/active-android-many-to-many-relationship / https://github.com/pardom/ActiveAndroid/issues/46 / http://stackoverflow.com/questions/31060893/follow-up-on-many-to-many-relationships-with-active-android
    public List<Route> routes() {
        Log.d(TAG, "routes()");
        return getMany(Route.class, "LabelRoute");
    }

    public List<Label> labels() {
        return getMany(Label.class, "LabelRoute");
    }
}
