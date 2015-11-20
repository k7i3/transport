package k7i3.code.tnc.transport.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import k7i3.code.tnc.transport.R;

/**
 * Created by k7i3 on 20.11.15.
 */
public class FavoritesRoutesDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_routes_favorites, null))
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FavoritesRoutesDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
