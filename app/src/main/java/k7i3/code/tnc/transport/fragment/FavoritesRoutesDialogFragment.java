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
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import k7i3.code.tnc.transport.Constants;
import k7i3.code.tnc.transport.R;
import k7i3.code.tnc.transport.activity.RoutesActivity;
import k7i3.code.tnc.transport.model.FavoriteRoute;
import k7i3.code.tnc.transport.model.Route;

/**
 * Created by k7i3 on 20.11.15.
 */
public class FavoritesRoutesDialogFragment extends DialogFragment {
    private static final String TAG = "====> FavoritesRoutesDialogFragment";
    private View view;
    private EditText label;
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
        label = (EditText)view.findViewById(R.id.label);
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
        String labelText = label.getText().toString();
        Toast.makeText(getActivity(), labelText + ": " + routes.size(), Toast.LENGTH_SHORT).show();

//        TODO 1. foreach routes - .save() // ActiveAndroid
        // Bulk insert https://github.com/pardom/ActiveAndroid/wiki/Saving-to-the-database
        ActiveAndroid.beginTransaction();
        try {
            for (Route route : routes) {
                route.save();
            }
            ActiveAndroid.setTransactionSuccessful();
            Log.d(TAG, "ActiveAndroid.setTransactionSuccessful()");
        }
        finally {
            ActiveAndroid.endTransaction();
        }

        Toast.makeText(getActivity(), labelText + " (fromDb): " + new Select().from(Route.class).execute().size(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, labelText + " (fromDb): " + new Select().from(Route.class).execute().size());

//        TODO 2. foreach routes - {_id - label - route_remote?_id - num?)} - .save() // DbHelper


//        TODO check: is label already exist? show dialog - replace? delete...
//        try {
//            FavoriteRoute favoriteRoute;
//            for (Route route : routes) {
//                favoriteRoute = new FavoriteRoute(labelText, route);
//                favoriteRoute.save();
//            }
//            ActiveAndroid.setTransactionSuccessful();
//            Log.d(TAG, "ActiveAndroid.setTransactionSuccessful()");
//        }
//        finally {
//            ActiveAndroid.endTransaction();
//        }
//
//        Toast.makeText(getActivity(), labelText + " (fromDb): " + new Select().from(FavoriteRoute.class).execute().size(), Toast.LENGTH_SHORT).show();
//        Log.d(TAG, labelText + " (fromDb): " + new Select().from(FavoriteRoute.class).execute().size());



        areRoutesSaved = true;
    }
}
