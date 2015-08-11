package k7i3.code.tnc.transport.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import k7i3.code.tnc.transport.R;

/**
 * Created by k7i3 on 11.08.15.
 */
public class RoutesTabFavorites extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_routes_tab_favorites, container, false);
    }
}
