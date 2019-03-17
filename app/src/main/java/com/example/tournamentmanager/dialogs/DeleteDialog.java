package com.example.tournamentmanager.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.tournamentmanager.R;

public class DeleteDialog extends DialogFragment {

    DeleteDialogListener listener;

    // Interfaz listener
    public interface DeleteDialogListener {
        void pressDelete();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener = (DeleteDialogListener) getTargetFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.exit_dialog_title));
        builder.setMessage(getString(R.string.delete_dialog_message));

        builder.setPositiveButton(getString(R.string.option_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.pressDelete();
            }
        });

        builder.setNegativeButton(getString(R.string.option_no), null);

        return builder.create();
    }
}
