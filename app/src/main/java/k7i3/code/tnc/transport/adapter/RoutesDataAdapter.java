package k7i3.code.tnc.transport.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import k7i3.code.tnc.transport.R;
import k7i3.code.tnc.transport.model.Route;

/**
 * Created by k7i3 on 13.08.15.
 */
public class RoutesDataAdapter extends RecyclerView.Adapter<RoutesDataAdapter.RouteViewHolder> {
    List<Route> routes;

    public RoutesDataAdapter(List<Route> routes) {
        this.routes = routes;
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

        public RouteViewHolder(View itemView) {
            super(itemView);

            routeNumber = (TextView)itemView.findViewById(R.id.routeNumber);
            routeName = (TextView)itemView.findViewById(R.id.routeName);
            routeDistance = (TextView)itemView.findViewById(R.id.routeDistance);
            routeDuration = (TextView)itemView.findViewById(R.id.routeDuration);
        }
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
