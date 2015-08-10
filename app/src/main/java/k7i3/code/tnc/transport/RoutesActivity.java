package k7i3.code.tnc.transport;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;

/**
 * Created by k7i3 on 07.08.15.
 */
public class RoutesActivity extends BaseActivity {

    private TabLayout tabLayout;
    private TabLayout tabLayoutTest;
    private CollapsingToolbarLayout collapsingToolbarLayout;


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
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Автобус"));
        tabLayout.addTab(tabLayout.newTab().setText("Троллейбус"));
        tabLayout.addTab(tabLayout.newTab().setText("Трамвай"));

        tabLayoutTest = (TabLayout) findViewById(R.id.tabLayoutTest);
        tabLayoutTest.addTab(tabLayoutTest.newTab().setText("Рядом"));
        tabLayoutTest.addTab(tabLayoutTest.newTab().setText("Все"));

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("Маршруты");
    }
}
