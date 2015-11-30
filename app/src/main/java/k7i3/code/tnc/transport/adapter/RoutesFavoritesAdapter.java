package k7i3.code.tnc.transport.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import k7i3.code.tnc.transport.R;
import k7i3.code.tnc.transport.adapter.base.CursorRecyclerViewAdapter;
import k7i3.code.tnc.transport.model.Label;

/**
 * Created by k7i3 on 30.11.15.
 */
public class RoutesFavoritesAdapter extends CursorRecyclerViewAdapter<RoutesFavoritesAdapter.ViewHolder> {

    public RoutesFavoritesAdapter(Cursor cursor){
        super(cursor);
    }

    //LIFECYCLE

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_routes_favorites, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        String text = cursor.getString(cursor.getColumnIndex("text"));
        viewHolder.label.setText(text);
    }

    //VH

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView label;

        public ViewHolder(View view) {
            super(view);
            label = (TextView) view.findViewById(R.id.label);
        }
    }
}
