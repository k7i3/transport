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

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.List;

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
        String labelText = editText.getText().toString();

//        TODO 1. + check: is label already exist? show dialog - replace? delete...
        Label label;
        if ((label = Label.getLabelByText(labelText)) != null) {
            LabelRoute.deleteLabelRouteByLable(label); // will remove the binding (label - route) without removing routes and label
        } else {
            label = new Label(labelText);
            label.save();
        }

//        TODO 2. + save
        ActiveAndroid.beginTransaction();
        try {
            for (Route route : routes) {
                route.save();
                new LabelRoute(label, route).save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }

        //TEST
        Log.d(TAG, "Route(fromDb): " + new Select().from(Route.class).execute().size());
        Log.d(TAG, "Label(fromDb): " + new Select().from(Label.class).execute().size());
        Log.d(TAG, "LabelRoute(fromDb): " + new Select().from(LabelRoute.class).execute().size());
        Log.d(TAG, "RoutesByLabel(fromDb): " + label.getRoutes().size());
//        Log.d(TAG, "RoutesByLabel=test1(fromDb): " + Label.getRoutesByLabelText("test1").size()); // may be NULL

        areRoutesSaved = true;
    }
}
