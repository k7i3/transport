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
 * Created by k7i3 on 20.11.15.
 */
public class FavoritesRoutesDialogFragment extends DialogFragment {
    private static final String TAG = "====> FavoritesRoutesDialogFragment";
    private View view;
    private EditText editText;
    private List<Route> routes;
    private boolean areRoutesSaved;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog()");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        view = inflater.inflate(R.layout.dialog_routes_favorites, null);
        builder.setView(view)
                .setMessage("Создать коллекцию:")
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
//                        moved to/overridden in onStart()
                    }
                })
                .setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FavoritesRoutesDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
        routes = getArguments().getParcelableArrayList(Constants.ROUTES);
        editText = (EditText)view.findViewById(R.id.label);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        final AlertDialog dialog = (AlertDialog)getDialog();
        if(dialog != null) {
            Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    saveRoutes();
                    if(areRoutesSaved)
                        dialog.dismiss();
                }
            });
        }
    }

    private void saveRoutes() {
        Log.d(TAG, "saveRoutes()");
        String labelText = editText.getText().toString().toUpperCase();
        //Analytics
        Tracker tracker = ((AnalyticsApplication) getActivity().getApplication()).getTracker(AnalyticsApplication.TrackerName.PROGRAMMATICALLY_APP_TRACKER);

//        TODO 1. + check: is label already exist? show dialog - replace? delete...
        Label label;
        if ((label = Label.getLabelByText(labelText)) != null) {
            LabelRoute.deleteLabelRouteByLable(label); // will remove the binding (label - route) without removing routes and label
            Toast.makeText(getActivity(), "коллекция обновлена: " + labelText, Toast.LENGTH_SHORT).show();

            //Analytics 1.1-2
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("DB")
                    .setAction("collection_was_updated")
                    .setLabel("favorites_routes_dialog")
                    .setValue(routes.size()) //ценность события
                    .setCustomDimension(1, labelText) // TODO del? if no - filter 1.2 by !!!include!!! action at GA (collection_was_created) and 1.1 by !!!include!!! action at GA (collection_was_updated)
                    .setCustomDimension(2, routes.size() + "")
                    .build());
        } else {
            label = new Label(labelText);
            label.save();
            Toast.makeText(getActivity(), "коллекция создана: " + labelText, Toast.LENGTH_SHORT).show();

            //Analytics 1.2-2
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("DB")
                    .setAction("collection_was_created")
                    .setLabel("favorites_routes_dialog")
                    .setValue(routes.size()) //ценность события
                    .setCustomDimension(1, labelText)
                    .setCustomDimension(2, routes.size() + "")
                    .build());
        }

        //Analytics 2
//        tracker.send(new HitBuilders.EventBuilder()
//                .setCategory("DB")
//                .setAction("collection_size")
//                .setLabel("favorites_routes_dialog")
//                .setValue(routes.size()) //ценность события
//                .setCustomDimension(2, routes.size() + "")
//                .build());

//        TODO 2. + save
        ActiveAndroid.beginTransaction();
        try {
            for (Route route : routes) {
                route.save();
                new LabelRoute(label, route).save();

                //Analytics 1 and 3
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("DB")
                        .setAction("route_was_added_to_collection")
                        .setLabel("favorites_routes_dialog")
                        .setValue(routes.size()) //ценность события
                        .setCustomDimension(1, labelText) // TODO del? if no - filter 1.x by !!!exclude!!! action at GA (route_was_added_to_collection) and filter 3 by !!!include!!! action at GA (route_was_added_to_collection)
                        .setCustomDimension(3, route.getNum())
                        .build());
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }

        areRoutesSaved = true;
    }
}
