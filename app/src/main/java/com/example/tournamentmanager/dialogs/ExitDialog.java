package com.example.tournamentmanager.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.tournamentmanager.R;

public class ExitDialog extends DialogFragment {

    ExitDialogListener listener;

    // Interfaz listener
    public interface ExitDialogListener {
        void pressExit();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        listener = (ExitDialogListener) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.exit_dialog_title));
        builder.setMessage(getString(R.string.exit_dialog_message));

        builder.setPositiveButton(getString(R.string.option_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.pressExit();
            }
        });

        builder.setNegativeButton(getString(R.string.option_no), null);

        return builder.create();
    }
}
