package k7i3.code.tnc.transport.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import k7i3.code.tnc.transport.fragment.FavoritesRoutesFragment;
import k7i3.code.tnc.transport.fragment.RoutesFragment;

/**
 * Created by k7i3 on 11.08.15.
 */
public class RoutesPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "====> RoutesPagerAdapter";
    CharSequence titles[];

    public RoutesPagerAdapter(FragmentManager fm, CharSequence titles[]) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem()");
        switch (position) {
            case 0: return RoutesFragment.newInstance(position);
            case 1: return FavoritesRoutesFragment.newInstance(position);
            default: return RoutesFragment.newInstance(position);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }


}
