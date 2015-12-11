package k7i3.code.tnc.transport.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import k7i3.code.tnc.transport.AnalyticsApplication;
import k7i3.code.tnc.transport.Constants;
import k7i3.code.tnc.transport.R;
import k7i3.code.tnc.transport.model.Label;
import k7i3.code.tnc.transport.model.LabelRoute;
import k7i3.code.tnc.transport.model.Route;

/**
 * Created by k7i3 on 30.11.15.
 */
public class DeleteFavoritesRoutesDialogFragment extends DialogFragment {
    private static final String TAG = "====> DeleteFavoritesRoutesDialogFragment";
    private String text;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog()");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Удалить коллекцию?")
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        deleteLabel();
                    }
                })
                .setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DeleteFavoritesRoutesDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
        text = getArguments().getString(Constants.LABEL);
    }

    private void deleteLabel() {
        Log.d(TAG, "deleteLabel()");
        Label.deleteLabelByText(text);
        Toast.makeText(getActivity(), "коллекция удалена: " + text, Toast.LENGTH_SHORT).show();

        //Analytics 1.3
        Tracker tracker = ((AnalyticsApplication) getActivity().getApplication()).getTracker(AnalyticsApplication.TrackerName.PROGRAMMATICALLY_APP_TRACKER);
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("DB")
                .setAction("collection_was_deleted")
                .setLabel("delete_favorites_routes_dialog")
//                .setValue(routes.size()) //ценность события
                .setCustomDimension(1, text)
//                .setCustomDimension(2, routes.size() + "")
                .build());
    }
}