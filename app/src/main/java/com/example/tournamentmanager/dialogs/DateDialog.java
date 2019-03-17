package com.example.tournamentmanager.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;
import com.example.tournamentmanager.R;
import java.util.Calendar;

public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DateDialogListener listener;

    // Interfaz listener
    public interface DateDialogListener {
        void dateSet(int year, int month, int day);
    }

    int currentYear;
    int currentMonth;
    int currentDay;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        listener = (DateDialogListener) getActivity();

        // El día actual aparecerá de forma predeterminada
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, currentYear, currentMonth, currentDay);

        return dialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        // Comprobamos que la fecha no es menor que la actual
        if (year > currentYear || (year == currentYear && month > currentMonth) ||
                (year == currentYear && month == currentMonth && day > currentDay)) {
            listener.dateSet(year, month, day);
        } else {
            Toast.makeText(getContext(), getString(R.string.tournament_date_invalid), Toast.LENGTH_LONG).show();
        }
    }


}
