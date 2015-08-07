package k7i3.code.tnc.transport;

import android.os.Bundle;
import android.support.design.widget.TabLayout;

/**
 * Created by k7i3 on 07.08.15.
 */
public class RoutesActivity extends BaseActivity {

    private TabLayout tabLayout;

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
    }
}
