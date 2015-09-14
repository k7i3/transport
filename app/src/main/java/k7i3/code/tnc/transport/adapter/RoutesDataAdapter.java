package k7i3.code.tnc.transport.adapter;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import k7i3.code.tnc.transport.R;
import k7i3.code.tnc.transport.model.Route;

/**
 * Created by k7i3 on 13.08.15.
 */
public class RoutesDataAdapter extends RecyclerView.Adapter<RoutesDataAdapter.RouteViewHolder> {
    private SortedList<Route> routes = new SortedList<Route>(Route.class, new SortedListAdapterCallback<Route>(this) {
    @Override
    public int compare(Route o1, Route o2) {
        return o1.getNum().compareTo(o2.getNum());
    }

    @Override
    public boolean areContentsTheSame(Route oldItem, Route newItem) {
//        TODO make more intelligently
//        Called by the SortedList when it wants to check whether two items have the same data or not. SortedList uses this information to decide whether it should call onChanged(int, int) or not.
//        SortedList uses this method to check equality instead of equals(Object) so that you can change its behavior depending on your UI.
//        For example, if you are using SortedList with a RecyclerView.Adapter, you should return whether the items' visual representations are the same or not.
        return oldItem.equals(newItem);
    }

    @Override
    public boolean areItemsTheSame(Route item1, Route item2) {
//        TODO may be equals() instead of ==?
//        Called by the SortedList to decide whether two object represent the same Item or not.
//        For example, if your items have unique ids, this method should check their equality.
        return item1.getId() == item2.getId();
    }
});

    public RoutesDataAdapter(List<Route> routes) {
        this.routes.addAll(routes);
    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_routes, parent, false);
        return new RouteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RouteViewHolder holder, int position) {
        holder.routeNumber.setText(routes.get(position).getNum());
        holder.routeName.setText(routes.get(position).getName());
        holder.routeDistance.setText(routes.get(position).getDistance() + " км");
        holder.routeDuration.setText(routes.get(position).getRouteDurInMin() + " мин");
        holder.checkBox.setChecked(routes.get(position).isChecked());
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView routeNumber;
        TextView routeName;
        TextView routeDistance;
        TextView routeDuration;
        CheckBox checkBox;

        public RouteViewHolder(View itemView) {
            super(itemView);

            routeNumber = (TextView)itemView.findViewById(R.id.routeNumber);
            routeName = (TextView)itemView.findViewById(R.id.routeName);
            routeDistance = (TextView)itemView.findViewById(R.id.routeDistance);
            routeDuration = (TextView)itemView.findViewById(R.id.routeDuration);
            checkBox = (CheckBox)itemView.findViewById(R.id.checkBox);
        }
    }

    public void setRoutes(List<Route> routes) {
//        this.routes = routes;
        if(routes != null) {
            this.routes.clear();
            this.routes.addAll(routes);
        }
    }

    public SortedList<Route> getRoutes() {
        return routes;
    }

}
