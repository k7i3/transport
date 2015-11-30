package k7i3.code.tnc.transport.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
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

import com.activeandroid.content.ContentProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import k7i3.code.tnc.transport.Constants;
import k7i3.code.tnc.transport.R;
import k7i3.code.tnc.transport.adapter.RoutesDataAdapter;
import k7i3.code.tnc.transport.adapter.RoutesFavoritesAdapter;
import k7i3.code.tnc.transport.loader.RoutesLoader;
import k7i3.code.tnc.transport.model.Label;
import k7i3.code.tnc.transport.model.Route;
import k7i3.code.tnc.transport.listener.RecyclerItemClickListener;
import k7i3.code.tnc.transport.widget.decorator.DividerItemDecoration;

/**
 * Created by k7i3 on 30.11.15.
 */
public class FavoritesRoutesFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "====> FavoritesRoutesFragment";
    private static final int LOADER_LABELS = 1;
    private int position;
    private Set<String> selectedLabels;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private View view;
    private TextView noDb;

    public static FavoritesRoutesFragment newInstance(int position) {
        Log.d(TAG, "newInstance()");
        FavoritesRoutesFragment favoritesRoutesFragment = new FavoritesRoutesFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.KEY_POSITION, position);
        favoritesRoutesFragment.setArguments(args);
        return (favoritesRoutesFragment);
    }

    //LIFECYCLE

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        view = inflater.inflate(R.layout.fragment_routes_favorites, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
        position = getArguments().getInt(Constants.KEY_POSITION, -1);
        Log.d(TAG, "position: " + position);

        selectedLabels = new HashSet<>();

        initInstances();
        startLabelsLoader();
    }

    //LOADER

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader = null;
        //TODO switch if needed
        if (id == LOADER_LABELS) {
            Log.d(TAG, "onCreateLoader() if (id == LOADER_LABELS) + args.getInt(Constants.KEY_POSITION, -1) = " + args.getInt(Constants.KEY_POSITION, -1));
            loader = new CursorLoader(getActivity(), ContentProvider.createUri(Label.class, null), null, null, null, null);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished()");
        //TODO switch if needed
        if (loader.getId() == LOADER_LABELS) {
            Log.d(TAG, "if (loader.getId() == LOADER_LABELS)");
            ((RoutesFavoritesAdapter) recyclerView.getAdapter()).swapCursor(data);
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            if (data == null) {
                noDb.setVisibility(View.VISIBLE);
                Log.d(TAG, "LOADER: cursor == null");
            } else {
                if (data.getCount() == 0) {
                    noDb.setVisibility(View.VISIBLE);
                } else {
                    noDb.setVisibility(View.INVISIBLE);
                }
                Toast.makeText(getActivity(), "избранных коллекций: " + data.getCount(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset()");
        //TODO switch if needed
        if (loader.getId() == LOADER_LABELS) {
            Log.d(TAG, "if (loader.getId() == LOADER_LABELS)");
            ((RoutesFavoritesAdapter) recyclerView.getAdapter()).swapCursor(null);
        }
    }

    //HELPERS

    private void initInstances() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        noDb = (TextView) view.findViewById(R.id.noDb);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new RoutesFavoritesAdapter(null));
//        TODO waiting for built-in implementation http://stackoverflow.com/questions/24471109/recyclerview-onclick/26826692#26826692
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView label = (TextView) view.findViewById(R.id.label);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                checkBox.toggle();
                if (checkBox.isChecked()) {
                    selectedLabels.add(label.getText().toString());
                } else {
                    selectedLabels.remove(label.getText().toString());
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                TextView label = (TextView) view.findViewById(R.id.label);
                Label.deleteLabelByText(label.getText().toString());
                Toast.makeText(getActivity(), "коллекция удалена: " + label.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        }));
//        TODO waiting for built-in implementation of decorators http://stackoverflow.com/questions/24618829/how-to-add-dividers-and-spaces-between-items-in-recyclerview
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext())); //(getActivity()));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));
//        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(48));
    }

    private void startLabelsLoader() {
        //TODO switch
        if (position == 0) {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.KEY_POSITION, 2);
            getLoaderManager().initLoader(LOADER_LABELS, bundle, this);
        } else {
            initMockLabels();
        }
    }

    private void initMockLabels() {
        Log.d(TAG, "initMockRoutes()");
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    //GETTERS & SETTERS

    public List<Route> getSelectedRoutes() {

        Set<Route> routes = new HashSet<>();
        List<Route> routesByLabel;
        for (String label: selectedLabels) {
            routesByLabel = Label.getRoutesByLabelText(label);
            if (routesByLabel != null) {
                routes.addAll(routesByLabel);
            }
        }
        return new ArrayList<>(routes);
    }
}
