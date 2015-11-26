package k7i3.code.tnc.transport.model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by k7i3 on 26.11.15.
 */
@Table(name = "FavoriteRoutes", id = BaseColumns._ID)
public class FavoriteRoute extends Model {
    @Column(name = "label")
    String label;

    @Column(name = "route", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    Route route;

    public FavoriteRoute() {
        super();
    }

    public FavoriteRoute(String label, Route route) {
        super();
        this.label = label;
        this.route = route;
    }
}
