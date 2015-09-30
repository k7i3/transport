package k7i3.code.tnc.transport.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import k7i3.code.tnc.transport.Constants;
import k7i3.code.tnc.transport.R;
import k7i3.code.tnc.transport.adapter.RoutesDataAdapter;
import k7i3.code.tnc.transport.loader.RoutesLoader;
import k7i3.code.tnc.transport.model.Route;
import k7i3.code.tnc.transport.listener.RecyclerItemClickListener;
import k7i3.code.tnc.transport.widget.decorator.DividerItemDecoration;


/**
 * Created by k7i3 on 11.08.15.
 */
public class RoutesFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<Route>> {
    private static final String TAG = "====> RoutesFragment";
    private static final int LOADER_ROUTES = 1;
    private List<Route> routes;
    private List<Route> selectedRoutes;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RoutesDataAdapter routesDataAdapter;
    private View view;
    private int position;
    private TextView noInternet;

    public static RoutesFragment newInstance(int position) {
        Log.d(TAG, "newInstance()");
        RoutesFragment routesFragment = new RoutesFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.KEY_POSITION, position);
        routesFragment.setArguments(args);
        return (routesFragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        view = inflater.inflate(R.layout.fragment_routes, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
        position = getArguments().getInt(Constants.KEY_POSITION, -1);
        Log.d(TAG, "position: " + position);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        noInternet = (TextView) view.findViewById(R.id.noInternet);
        loadRoutes();
        initInstances();
    }

    private void initInstances() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        routesDataAdapter = new RoutesDataAdapter(routes);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(routesDataAdapter);

//        TODO waiting for built-in implementation http://stackoverflow.com/questions/24471109/recyclerview-onclick/26826692#26826692
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(view.getContext(), "onItemClick => position = " + position, Toast.LENGTH_SHORT).show();
                ((CheckBox) view.findViewById(R.id.checkBox)).toggle();
                routesDataAdapter.getRoutes().get(position).toggle();

                //TEST
                filterRoutes(routesDataAdapter.getRoutes());
                Toast.makeText(view.getContext(), "selectedRoutes.size(): " + selectedRoutes.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
//                Toast.makeText(view.getContext(), "onItemLongClick => position = " + position, Toast.LENGTH_SHORT).show();
            }
        }));

//        TODO waiting for built-in implementation of decorators http://stackoverflow.com/questions/24618829/how-to-add-dividers-and-spaces-between-items-in-recyclerview
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext())); //(getActivity()));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));
//        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(48));
    }

    private void loadRoutes() {
        routes = new ArrayList<>();

        //TODO switch
        if (position == 2) {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.KEY_POSITION, 2);
            getLoaderManager().initLoader(LOADER_ROUTES, bundle, this);
        } else {
            initMockRoutes();
        }
    }

    private void filterRoutes(SortedList<Route> routes) {
        selectedRoutes = new ArrayList<>();
        Route route;
        for (int i = 0; i < routes.size(); i++) {
            route = routes.get(i);
            if (route.isChecked()) selectedRoutes.add(route);
        }
    }

    private void initMockRoutes() {
        Log.d(TAG, "initMockRoutes()");
        routes.add(new Route(1, position + "", 30, 60, "описание маршрута"));
//        routes.add(new Route(2, "105", 40, 89, "105 К.Рынок-в.Изяк"));
//        routes.add(new Route(3, "104С", 77, 120, "104С Уфа-Благовещенск"));
//        routes.add(new Route(4, "111", 11, 111, "111 Название маршрута"));
//        routes.add(new Route(5, "111", 11, 111, "111 Название маршрута"));
//        routes.add(new Route(6, "111", 11, 111, "111 Название маршрута"));
//        routes.add(new Route(7, "111", 11, 111, "111 Название маршрута"));
//        routes.add(new Route(8, "111", 11, 111, "111 Название маршрута"));
//        routes.add(new Route(9, "111", 11, 111, "111 Название маршрута"));
//        routes.add(new Route(10, "111", 11, 111, "111 Название маршрута"));
//        routes.add(new Route(0, "test1", 11, 111, "test1 Название маршрута"));
//        routes.add(new Route(0, "test2", 11, 111, "test2 Название маршрута"));
//        routes.add(new Route(0, "111", 11, 111, "111 Название маршрута"));
//        routes.add(new Route(0, "111", 11, 111, "111 Название маршрута"));
//        routes.add(new Route(0, "111", 11, 111, "111 Название маршрута"));
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    @Override
    public Loader<List<Route>> onCreateLoader(int id, Bundle args) {
        Loader<List<Route>> loader = null;
        //TODO switch if needed
        if (id == LOADER_ROUTES) {
            Log.d(TAG, "onCreateLoader() if (id == LOADER_ROUTES) + args.getInt(Constants.KEY_POSITION, -1) = " + args.getInt(Constants.KEY_POSITION, -1));
            loader = new RoutesLoader(getActivity(), args);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Route>> loader, List<Route> data) {
        Log.d(TAG, "onLoadFinished()");
        //TODO switch if needed
        if (loader.getId() == LOADER_ROUTES) {
            Log.d(TAG, "if (loader.getId() == LOADER_ROUTES)");
            routes = data;
            routesDataAdapter.setRoutes(routes);
//            routesDataAdapter.notifyDataSetChanged(); // not needed when used SortedList at routesDataAdapter (it has built-in callbacks)
//            initInstances(); // can be used instead of {routesDataAdapter.setRoutes(routes); routesDataAdapter.notifyDataSetChanged();} but in this way adapter will be recreate
            progressBar.setVisibility(ProgressBar.INVISIBLE);

            if (routes == null) {
                noInternet.setVisibility(View.VISIBLE);
            } else {
                noInternet.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Маршрутов на линии: " + routes.size(), Toast.LENGTH_SHORT).show();
            }

            // TEST
//            final Handler handler = new Handler();
//            final Runnable r = new Runnable() {
//                public void run() {
//                    routesDataAdapter.getRoutes().removeItemAt(0);
//                    routesDataAdapter.getRoutes().add(new Route(39427972044L, "103", 111, 111, "test"));
//                    handler.postDelayed(this, 3000);
//                }
//            };
//            if (routesDataAdapter.getRoutes().size() > 0) handler.postDelayed(r, 3000);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Route>> loader) {

    }

    public List<Route> getSelectedRoutes() {
        filterRoutes(routesDataAdapter.getRoutes());
        return selectedRoutes;
    }
}
