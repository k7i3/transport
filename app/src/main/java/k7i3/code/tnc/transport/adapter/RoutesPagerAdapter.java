package k7i3.code.tnc.transport.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import k7i3.code.tnc.transport.fragment.RoutesFragment;

/**
 * Created by k7i3 on 11.08.15.
 */
public class RoutesPagerAdapter extends FragmentPagerAdapter {

    CharSequence titles[];

    public RoutesPagerAdapter(FragmentManager fm, CharSequence titles[]) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return RoutesFragment.newInstance(position);
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
