package com.example.tournamentmanager.tournaments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tournamentmanager.DatabaseManager;
import com.example.tournamentmanager.ServerDB;
import com.example.tournamentmanager.dialogs.DateDialog;
import com.example.tournamentmanager.R;
import com.example.tournamentmanager.dialogs.TimeDialog;

import java.sql.Struct;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateTournamentActivity extends AppCompatActivity implements DateDialog.DateDialogListener, TimeDialog.TimeDialogListener {


    private TextInputLayout inputName;
    private TextInputLayout inputGame;
    private TextInputLayout inputDescription;
    private TextInputLayout inputDate;
    private TextInputLayout inputTime;
    private Spinner inputLocation;
    private Button buttonCreateTournament;

    private String selectedDate;
    private String selectedTime;

//    private DatabaseManager databaseManager;


    public CreateTournamentActivity() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tournament);

//        databaseManager = new DatabaseManager(this);

        inputName = findViewById(R.id.textInputLayout_tournament_name);
        inputGame = findViewById(R.id.textInputLayout_tournament_game);
        inputDescription = findViewById(R.id.textInputLayout_tournament_description);
        inputDate = findViewById(R.id.textInputLayout_tournament_date);
        inputTime = findViewById(R.id.textInputLayout_tournament_time);
        inputLocation = findViewById(R.id.textInputLayout_tournament_location);
        buttonCreateTournament = findViewById(R.id.button_create_tournament);

        // Listener para llamar al dialogo que introduce la fecha
        inputDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog dateDialog = new DateDialog();
                dateDialog.show(getSupportFragmentManager(), "dateDialog");
            }
        });

        // Listener para llamar al dialogo que introduce la hora
        inputTime.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeDialog timeDialog = new TimeDialog();
                timeDialog.show(getSupportFragmentManager(), "dateDialog");
            }
        });

        buttonCreateTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateCreation()) {
                    finish();
                }
            }
        });
    }

    @Override
    public void dateSet(int year, int month, int day) {
        // Añadir 0 a la izquierda del año, mes y día hasta que tengan 4, 2 y 2 dígitos
        String formatedYear = String.format("%02d", year);
        String formatedMonth = String.format("%02d", (month + 1));
        String formatedDay = String.format("%02d", day);
        // El formato con el que se almacenará en la BD y el que se muestra al usuario es diferente
        selectedDate = formatedYear + "-" + formatedMonth + "-" + formatedDay;
        inputDate.getEditText().setText(formatedDay + "-" + formatedMonth + "-" + formatedYear);
    }

    @Override
    public void timeSet(int hour, int minute) {
        // Añadir 0 a la izquierda de la hora y el minuto hasta que tengan 2 dígitos
        String formatedHour = String.format("%02d", hour);
        String formatedMinute = String.format("%02d", minute);
        selectedTime = formatedHour + ":" + formatedMinute;
        inputTime.getEditText().setText(selectedTime);
    }

    private boolean validateCreation() {
        if (!validateName() | !validateGame() | !validateDate() | !validateTime() | !validateLocation()) {
            return false;
        }
        SharedPreferences session = getSharedPreferences("session", 0);
        String user = session.getString("session", null);
        ServerDB serverDB = new ServerDB(this);
        serverDB.addTournament(getName(), getGame(), getDescription(), getDateSQLite(), getLocation(), user);
        String result = null;
        try {
            result = serverDB.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (result == null) {
            serverDB.notifyError(getWindow().getDecorView().getRootView());
            return false;
        } else if (result.equals("true")){
            return true;
        }else {
            inputName.setError("ERROR ?????????");
            return false;
        }
//        if (databaseManager.addTournament(getName(), getGame(), getDescription(), getDateSQLite(), getLocation(), user)) {
//            return true;
//        } else {
//            inputName.setError("ERROR ?????????");
//            return false;
//        }
    }

    private boolean validateName() {
        String name = getName();
        if (name.isEmpty()) {
            inputName.setError(getString(R.string.error_empty));
            return false;
        } else {
            ServerDB serverDB = new ServerDB(this);
            serverDB.checkTournamentNameExists(name);
            String result = null;
            try {
                result = serverDB.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (result != null && result.equals("true")){
                inputName.setError(getString(R.string.tournament_name_taken));
                return false;
            } else if (result != null && result.equals("false")){
                inputName.setError("");
                return true;
            }
            serverDB.notifyError(getWindow().getDecorView().getRootView());
            return false;
        }
//        else if (databaseManager.checkTournamentNameExists(name)) {
//            inputName.setError(getString(R.string.tournament_name_taken));
//            return false;
//        }
//        inputName.setError("");
//        return true;
    }

    private boolean validateGame() {
        String game = getGame();
        if (game.isEmpty()) {
            inputGame.setError(getString(R.string.error_empty));
            return false;
        }
        inputGame.setError("");
        return true;
    }

    private boolean validateDate() {
        if (selectedDate == null || selectedDate.isEmpty()) {
            inputDate.setError(getString(R.string.error_empty));
            return false;
        }
        inputDate.setError("");
        return true;
    }

    private boolean validateTime() {
        if (selectedTime == null || selectedTime.isEmpty()) {
            inputTime.setError(getString(R.string.error_empty));
            return false;
        }
        inputTime.setError("");
        return true;
    }

    private boolean validateLocation() {
        String location = getLocation();
        if (location.equals("")) {
            ((TextView) inputLocation.getSelectedView()).setError(getString(R.string.error_empty));
            return false;
        }
        return true;
    }

    private String getName() {
        return inputName.getEditText().getText().toString().trim();
    }

    private String getGame() {
        return inputGame.getEditText().getText().toString().trim();
    }

    private String getDescription() {
        return inputDescription.getEditText().getText().toString().trim();
    }

    private String getDateSQLite() {
        return String.format("%s %s", selectedDate, selectedTime);
    }


    private String getLocation() {
        return inputLocation.getSelectedItem().toString();
    }
}
