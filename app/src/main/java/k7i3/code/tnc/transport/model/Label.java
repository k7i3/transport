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
 * Created by k7i3 on 27.11.15.
 */
@Table(name = "Labels", id = BaseColumns._ID)
public class Label extends Model {
    @Column(name = "text", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    String text;

    public Label() {
        super();
    }

    public Label(String text) {
        super();
        this.text = text;
    }

    //HELPERS

    public List<Route> getRoutes() {
//        TODO doesn't work: http://stackoverflow.com/questions/26207948/activeandroid-many-to-many-relationship / http://stackoverflow.com/questions/17645900/active-android-many-to-many-relationship / https://github.com/pardom/ActiveAndroid/issues/46 / http://stackoverflow.com/questions/31060893/follow-up-on-many-to-many-relationships-with-active-android
//        routes = ((LabelRoute) new Select().from(LabelRoute.class)
//                .where("label = ?", this.getId())
//                .executeSingle()).routes();
        return new Select()
                .from(Route.class)
                .innerJoin(LabelRoute.class).on("Routes._id = LabelRoute.route")
                .where("LabelRoute.label = ?", this.getId())
                .execute();
    }

    public void deleteRoutes() {
//        don't do it, because route may be needed for another label
        ActiveAndroid.beginTransaction();
        try {
            for (Route route : getRoutes()){
                route.delete();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }

    public static List<Route> getRoutesByLabelText(String lableText) {
        Label label = getLabelByText(lableText);
        if (label != null) {
            return label.getRoutes();
        } else {
            return null;
        }
    }

    public static Label getLabelByText(String text) {
        return new Select()
                .from(Label.class)
                .where("text = ?", text)
                .executeSingle();
    }

    public static void deleteLabelByText(String text) {
        Label label = getLabelByText(text);
        LabelRoute.deleteLabelRouteByLable(label);
        label.delete();
    }

    //GETTERS & SETTERS

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
