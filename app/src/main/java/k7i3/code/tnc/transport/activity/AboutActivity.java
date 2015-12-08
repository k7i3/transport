package k7i3.code.tnc.transport.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;

import k7i3.code.tnc.transport.AnalyticsApplication;
import k7i3.code.tnc.transport.R;

/**
 * Created by k7i3 on 02.12.15.
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_about;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTAG("====> RoutesActivity");
        Log.d(TAG, "onCreate()");
//        ((AnalyticsApplication) getApplication()).getTracker(AnalyticsApplication.TrackerName.XML_APP_TRACKER);

        drawerToggle.setDrawerIndicatorEnabled(false);

        TextView tnc = (TextView) findViewById(R.id.tnc);
        tnc.setMovementMethod(LinkMovementMethod.getInstance());

        TextView email = (TextView) findViewById(R.id.email);
        email.setMovementMethod(LinkMovementMethod.getInstance());

        TextView market = (TextView) findViewById(R.id.market);
//        working to
//        market.setMovementMethod(LinkMovementMethod.getInstance());
        market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
//                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
//                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
//                    throw new ActivityNotFoundException();
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
//        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
        //Stop the analytics tracking
//        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}
