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
        holder.routeNumber.setText(routes.get(position).number);
        holder.routePointA.setText(routes.get(position).pointA);
        holder.routePointB.setText(routes.get(position).pointB);
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView routeNumber;
        TextView routePointA;
        TextView routePointB;

        public RouteViewHolder(View itemView) {
            super(itemView);

            routeNumber = (TextView)itemView.findViewById(R.id.routeNumber);
            routePointA = (TextView)itemView.findViewById(R.id.routePointA);
            routePointB = (TextView)itemView.findViewById(R.id.routePointB);
        }
    }
}
