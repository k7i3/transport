package k7i3.code.tnc.transport.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import k7i3.code.tnc.transport.R;
import k7i3.code.tnc.transport.adapter.RoutesDataAdapter;
import k7i3.code.tnc.transport.data.Route;

/**
 * Created by k7i3 on 11.08.15.
 */
public class RoutesTabAll extends Fragment {
    private List<Route> routes;

    private RecyclerView recyclerView;
    private RoutesDataAdapter routesDataAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routes_tab_all, container, false);
        initRoutes();
        initInstances(view);
        return view;
    }

    private void initInstances(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true); if size won't be changing (for performance)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        routesDataAdapter = new RoutesDataAdapter(routes);
        recyclerView.setAdapter(routesDataAdapter);
    }

    private void initRoutes() {
        routes = new ArrayList<>();
        routes.add(new Route("110", "ВАЗ", "Аэропорт"));
        routes.add(new Route("110с", "ДОК", "Аэропорт"));
        routes.add(new Route("51", "Точка А", "Точка Б"));
        routes.add(new Route("51А", "Точка А", "Точка Б"));
        routes.add(new Route("69", "Точка А", "Точка Б"));
        routes.add(new Route("74", "Точка А", "Точка Б"));
        routes.add(new Route("57", "Точка А", "Точка Б"));
        routes.add(new Route("290", "Точка А", "Точка Б"));
        routes.add(new Route("226", "Точка А", "Точка Б"));
    }
}
