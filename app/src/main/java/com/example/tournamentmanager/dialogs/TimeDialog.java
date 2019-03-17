package com.example.tournamentmanager.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;
import java.util.Calendar;

public class TimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    TimeDialogListener listener;

    // Interfaz listener
    public interface TimeDialogListener {
        void timeSet( int hour, int minute);
    }

    int currentHour;
    int currentMinute;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Asignamos el listener
        listener = (TimeDialogListener) getActivity();

        // La hora actual aparecerá de forma predeterminada
        Calendar calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(getActivity(), this, currentHour, currentMinute, true);

        return dialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {

        // Notificamos la hora escogida
        listener.timeSet(hour, minute);
    }


}
