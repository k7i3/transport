package k7i3.code.tnc.transport;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;

import k7i3.code.tnc.transport.adapter.RoutesAdapter;
import k7i3.code.tnc.transport.widget.SlidingTabLayout;

/**
 * Created by k7i3 on 07.08.15.
 */
public class RoutesActivity extends BaseActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private SlidingTabLayout slidingTabLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ViewPager viewPager;

    private CharSequence titles[] = {"Избранные", "Рядом", "Все"};
    private RoutesAdapter routesAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_routes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInstances();
    }

    private void initInstances() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("Маршруты");

        routesAdapter = new RoutesAdapter(getSupportFragmentManager(), titles);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(routesAdapter);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slidingTabLayout);
        slidingTabLayout.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        slidingTabLayout.setViewPager(viewPager);

        // disable refreshing
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setEnabled(false);
    }
}
