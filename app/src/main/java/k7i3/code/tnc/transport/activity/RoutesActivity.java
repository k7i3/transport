package k7i3.code.tnc.transport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import k7i3.code.tnc.transport.AnalyticsApplication;
import k7i3.code.tnc.transport.Constants;
import k7i3.code.tnc.transport.R;
import k7i3.code.tnc.transport.adapter.RoutesPagerAdapter;
import k7i3.code.tnc.transport.fragment.FavoritesRoutesDialogFragment;
import k7i3.code.tnc.transport.fragment.FavoritesRoutesFragment;
import k7i3.code.tnc.transport.fragment.RoutesFragment;
import k7i3.code.tnc.transport.model.Route;
import k7i3.code.tnc.transport.widget.SlidingTabLayout;

/**
 * Created by k7i3 on 07.08.15.
 */
public class RoutesActivity extends BaseActivity {

    private CharSequence titles[] = {"Избранные", "На линии"};
    private RoutesPagerAdapter routesPagerAdapter;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private SlidingTabLayout slidingTabLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ViewPager viewPager;
    private FloatingActionButton mapsFAB;
    private FloatingActionButton favoritesFAB;

    private boolean wereAllSelected;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_routes;
    }

    //LIFECYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTAG("====> RoutesActivity");
        Log.d(TAG, "onCreate()");
        initInstances();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected()");
        int id = item.getItemId();
        switch (id) {
            case R.id.refresh:
//                selectAll();
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
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
//                RoutesFragment routesFragment = (RoutesFragment) (routesPagerAdapter.getItem(viewPager.getCurrentItem())); // doesn't work (return new instance of Fragment instead of current)
                Object object = routesPagerAdapter.instantiateItem(viewPager, viewPager.getCurrentItem());
                if (object instanceof RoutesFragment) {
                    RoutesFragment routesFragment = (RoutesFragment) object;
                    Intent intent = new Intent(); //getIntent()?;
                    intent.putParcelableArrayListExtra(Constants.ROUTES, (ArrayList<Route>) routesFragment.getSelectedRoutes());
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (object instanceof FavoritesRoutesFragment) {
                    FavoritesRoutesFragment favoritesRoutesFragment = (FavoritesRoutesFragment) object;
                    Intent intent = new Intent(); //getIntent()?;
                    intent.putParcelableArrayListExtra(Constants.ROUTES, (ArrayList<Route>) favoritesRoutesFragment.getSelectedRoutes());
                    setResult(RESULT_OK, intent);
                    finish();
                }

                //Analytics
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UI")
                        .setAction("transport(maps)_FAB_was_clicked")
                        .setLabel("FAB")
                        .build());
            }
        });

        favoritesFAB = (FloatingActionButton) findViewById(R.id.favoritesFAB);
        favoritesFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoutesFragment routesFragment = (RoutesFragment) (routesPagerAdapter.instantiateItem(viewPager, viewPager.getCurrentItem()));
                List<Route> selectedRoutes = routesFragment.getSelectedRoutes();
                if (selectedRoutes.size() != 0) {
                    Toast.makeText(v.getContext(), "выбрано маршрутов: " + routesFragment.getSelectedRoutes().size(), Toast.LENGTH_SHORT).show();
                    DialogFragment dialogFragment = new FavoritesRoutesDialogFragment();
                    Bundle bundle = new Bundle(1);
                    bundle.putParcelableArrayList(Constants.ROUTES, (ArrayList<Route>) selectedRoutes);
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getSupportFragmentManager(), "FavoritesRoutesDialogFragment");
                } else {
                    Toast.makeText(v.getContext(), "выберите маршруты для сохранения", Toast.LENGTH_SHORT).show();
                }

                //Analytics
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UI")
                        .setAction("favorites_FAB_was_clicked")
                        .setLabel("FAB")
                        .build());
            }
        });
        favoritesFAB.setVisibility(View.GONE); // because first tab is favorites
        //Analytics
        tracker.setScreenName("routes-favorite-app");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
              @Override
              public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                      Log.d(TAG, "onPageScrolled(): " + "position: " + position + " positionOffset: " + positionOffset + " positionOffsetPixels: " + positionOffsetPixels);
              }

              @Override
              public void onPageSelected(int position) {
                  Log.d(TAG, "onPageSelected(): " + "position: " + position);
                  switch (position) {
                      case 0:
                          favoritesFAB.setVisibility(View.GONE);
                          mapsFAB.setVisibility(View.VISIBLE);
                          //Analytics
                          tracker.setScreenName("routes-favorite-user");
                          tracker.send(new HitBuilders.ScreenViewBuilder().build());
                          break;
                      case 1:
                          favoritesFAB.setVisibility(View.VISIBLE);
                          mapsFAB.setVisibility(View.VISIBLE);
                          //Analytics
                          tracker.setScreenName("routes-all-user");
                          tracker.send(new HitBuilders.ScreenViewBuilder().build());
                          break;
                  }
              }

              @Override
              public void onPageScrollStateChanged(int state) {
                  Log.d(TAG, "onPageScrollStateChanged(): " + "state: " + state);
              }
        });
    }

    private void selectAll() {
//        TODO is it needed?
        Object object = routesPagerAdapter.instantiateItem(viewPager, viewPager.getCurrentItem());
        if (!wereAllSelected) {
            if (object instanceof RoutesFragment) {
                RoutesFragment routesFragment = (RoutesFragment) object;
//                routesFragment.selectAll();
            } else if (object instanceof FavoritesRoutesFragment) {
                FavoritesRoutesFragment favoritesRoutesFragment = (FavoritesRoutesFragment) object;
//                favoritesRoutesFragment.selectAll();
            }
            wereAllSelected = true;
        } else {
            if (object instanceof RoutesFragment) {
                RoutesFragment routesFragment = (RoutesFragment) object;
//                routesFragment.unselectAll();
            } else if (object instanceof FavoritesRoutesFragment) {
                FavoritesRoutesFragment favoritesRoutesFragment = (FavoritesRoutesFragment) object;
//                favoritesRoutesFragment.unselectAll();
            }
            wereAllSelected = false;
        }
    }
}
