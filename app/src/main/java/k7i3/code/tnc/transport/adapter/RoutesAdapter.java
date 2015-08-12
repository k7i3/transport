package k7i3.code.tnc.transport.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import k7i3.code.tnc.transport.fragment.RoutesTabAll;
import k7i3.code.tnc.transport.fragment.RoutesTabFavorites;
import k7i3.code.tnc.transport.fragment.RoutesTabNearest;

/**
 * Created by k7i3 on 11.08.15.
 */
public class RoutesAdapter extends FragmentStatePagerAdapter {

    CharSequence titles[];

    public RoutesAdapter(FragmentManager fm, CharSequence titles[]) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RoutesTabFavorites();
            case 1:
                return new RoutesTabNearest();
            default:
                return new RoutesTabAll();
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
