package k7i3.code.tnc.transport.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import k7i3.code.tnc.transport.R;

/**
 * Created by k7i3 on 02.12.15.
 */
public class AboutActivity extends BaseActivity {

//    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_about;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawerToggle.setDrawerIndicatorEnabled(false);

//        TODO to initInstances()
        // disable refreshing
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
//        swipeRefreshLayout.setEnabled(false);

    }
}
