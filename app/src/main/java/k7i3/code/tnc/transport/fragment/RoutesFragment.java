package k7i3.code.tnc.transport.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;

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
    private static final String TAG = "=====> RoutesFragment";
    private static final int LOADER_ROUTES = 1;
    private List<Route> routes;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RoutesDataAdapter routesDataAdapter;
    private View view;
    private int position;

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

    private void initMockRoutes() {
        routes.add(new Route(position + "", "Точка А", "Точка Б"));
        routes.add(new Route("110", "ВАЗ", "Аэропорт"));
        routes.add(new Route("110с", "ДОК", "Аэропорт"));
        routes.add(new Route("51", "Точка А", "Точка Б"));
        routes.add(new Route("51а", "Точка А", "Точка Б"));
        routes.add(new Route("69", "Точка А", "Точка Б"));
        routes.add(new Route("74", "Точка А", "Точка Б"));
        routes.add(new Route("57", "Точка А", "Точка Б"));
        routes.add(new Route("290", "Точка А", "Точка Б"));
        routes.add(new Route("226", "Точка А", "Точка Б"));

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
            Log.d(TAG, "if (loader.getId() == LOADER_ROUTES) {");
            routes = data;
            routesDataAdapter.setRoutes(routes);
            routesDataAdapter.notifyDataSetChanged();
//            initInstances(); // can be used instead of {routesDataAdapter.setRoutes(routes); routesDataAdapter.notifyDataSetChanged();} but in this way adapter will be recreate
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Route>> loader) {

    }
}
