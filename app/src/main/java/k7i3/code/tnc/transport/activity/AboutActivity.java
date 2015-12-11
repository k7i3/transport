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
import com.google.android.gms.analytics.HitBuilders;

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

        drawerToggle.setDrawerIndicatorEnabled(false);

        //TNC_URL
        TextView tnc = (TextView) findViewById(R.id.tnc);
//        working to, but switch-off onClick() with Analytics
//        tnc.setMovementMethod(LinkMovementMethod.getInstance());
        tnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.tncrb.ru");
                Intent goTo = new Intent(Intent.ACTION_VIEW, uri);

                //Analytics TODO working?
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UI")
                        .setAction("uri_tnc_was_clicked")
                        .setLabel("about")
                        .build());
                try {
                    startActivity(goTo);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.tncrb.ru")));
                }
            }
        });

        //SUPPORT_EMAIL
        TextView email = (TextView) findViewById(R.id.email);
//        working to, but switch-off onClick() with Analytics
//        email.setMovementMethod(LinkMovementMethod.getInstance());
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("mailto:tnc.develop@gmail.com");
                Intent goTo = new Intent(Intent.ACTION_VIEW, uri);

                //Analytics TODO working?
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UI")
                        .setAction("uri_email_was_clicked")
                        .setLabel("about")
                        .build());
                try {
                    startActivity(goTo);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("mailto:tnc.develop@gmail.com")));
                }
            }
        });

        //MARKET
        TextView market = (TextView) findViewById(R.id.market);
//        working to, but switch-off onClick() with Analytics
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

                //Analytics TODO working
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UI")
                        .setAction("uri_market_was_clicked")
                        .setLabel("about")
                        .build());
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
}
