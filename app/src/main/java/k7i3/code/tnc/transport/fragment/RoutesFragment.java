package k7i3.code.tnc.transport.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import k7i3.code.tnc.transport.Constants;
import k7i3.code.tnc.transport.R;
import k7i3.code.tnc.transport.adapter.RoutesDataAdapter;
import k7i3.code.tnc.transport.data.Route;
import k7i3.code.tnc.transport.listener.RecyclerItemClickListener;
import k7i3.code.tnc.transport.widget.decorator.DividerItemDecoration;
import k7i3.code.tnc.transport.widget.decorator.VerticalSpaceItemDecoration;


/**
 * Created by k7i3 on 11.08.15.
 */
public class RoutesFragment extends Fragment {
    private List<Route> routes;

    private RecyclerView recyclerView;
    private RoutesDataAdapter routesDataAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routes, container, false);
        int position = getArguments().getInt(Constants.KEY_POSITION, -1);
        initRoutes(position);
        initInstances(view);
        return view;
    }

    private void initInstances(final View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true); if size won't be changing (for performance)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        routesDataAdapter = new RoutesDataAdapter(routes);
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

    private void initRoutes(int position) {
        routes = new ArrayList<>();
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
    }

    public static RoutesFragment newInstance(int position) {
        RoutesFragment routesFragment = new RoutesFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.KEY_POSITION, position);
        routesFragment.setArguments(args);
        return (routesFragment);
    }
}
