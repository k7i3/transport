package k7i3.code.tnc.transport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import k7i3.code.tnc.transport.Constants;
import k7i3.code.tnc.transport.R;
import k7i3.code.tnc.transport.adapter.RoutesPagerAdapter;
import k7i3.code.tnc.transport.fragment.RoutesFragment;
import k7i3.code.tnc.transport.model.Route;
import k7i3.code.tnc.transport.widget.SlidingTabLayout;

/**
 * Created by k7i3 on 07.08.15.
 */
public class RoutesActivity extends BaseActivity {

    private CharSequence titles[] = {"Избранные", "Рядом", "На линии"};
    private RoutesPagerAdapter routesPagerAdapter;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private SlidingTabLayout slidingTabLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ViewPager viewPager;
    private FloatingActionButton mapsFAB;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_routes;
    }

    //LIFECYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTAG("====> RoutesActivity");
        initInstances();
    }

    //HELPERS

    private void initInstances() {
        drawerToggle.setDrawerIndicatorEnabled(false);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("Маршруты");

        routesPagerAdapter = new RoutesPagerAdapter(getSupportFragmentManager(), titles);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(routesPagerAdapter);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slidingTabLayout);
        slidingTabLayout.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        slidingTabLayout.setViewPager(viewPager);

        // disable refreshing
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setEnabled(false);

        mapsFAB = (FloatingActionButton) findViewById(R.id.mapsFAB);
        mapsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getBaseContext(), TransportActivity.class));
//                RoutesFragment routesFragment = (RoutesFragment) (routesPagerAdapter.getItem(viewPager.getCurrentItem())); // doesn't work (return new instance of Fragment instead of current)
                RoutesFragment routesFragment = (RoutesFragment) (routesPagerAdapter.instantiateItem(viewPager, viewPager.getCurrentItem()));
//                Toast.makeText(v.getContext(), "routesFragment.getSelectedRoutes().size(): " + routesFragment.getSelectedRoutes().size(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(); //getIntent()?;
                intent.putParcelableArrayListExtra(Constants.ROUTES, (ArrayList<Route>) routesFragment.getSelectedRoutes());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

//        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
    }

//    public int getStatusBarHeight() {
//        int result = 0;
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = getResources().getDimensionPixelSize(resourceId);
//        }
//        return result;
//    }
}
