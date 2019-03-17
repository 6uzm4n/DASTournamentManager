package com.example.tournamentmanager.menus;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tournamentmanager.DatabaseManager;
import com.example.tournamentmanager.R;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout inputUsername;
    TextInputLayout inputPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputUsername = findViewById(R.id.textInputLayout_tournament_name);
        inputPassword = findViewById(R.id.textInputLayout_password);

        TextView registerLink = findViewById(R.id.textView_register);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        final Button loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateLogin()) {
                    SharedPreferences session = getSharedPreferences("session", 0); // TODO: Almacenar estas variables?
                    SharedPreferences.Editor editor = session.edit();
                    editor.putString("session", getUsername());
                    editor.commit();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    notifyUpcomingTournaments(getUsername());
                    finish();
                }
            }
        });
    }

    private boolean validateLogin() {
        if (!validateUsername() | !validatePassword()) {
            return false;
        }
        DatabaseManager dbManager = new DatabaseManager(this);
        if (dbManager.checkLogin(getUsername(), getPassword())) {
            return true;
        } else {
            inputPassword.setError(getString(R.string.error_login));
            return false;
        }
    }

    private boolean validateUsername() {
        String username = getUsername();
        if (username.isEmpty()) {
            inputUsername.setError(getString(R.string.error_empty));
            return false;
        }
        inputUsername.setError("");
        return true;
    }

    private boolean validatePassword() {
        String password = getPassword();
        if (password.isEmpty()) {
            inputPassword.setError(getString(R.string.error_empty));
            return false;
        }
        inputPassword.setError("");
        return true;
    }

    private String getUsername() {
        return inputUsername.getEditText().getText().toString().trim(); //Es bueno utilizar trim() ya que los correctores pueden introducir un espacio indeseado al final de los inputs.

    }

    private String getPassword() {
        return inputPassword.getEditText().getText().toString().trim(); //Es bueno utilizar trim() ya que los correctores pueden introducir un espacio indeseado al final de los inputs.
    }

    public void notifyUpcomingTournaments(String user) {
        DatabaseManager db = new DatabaseManager(this);
        HashMap<String, ArrayList<String>> tournaments;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "TournamentManager");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("TournamentManager", "UpcomingTournaments", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        tournaments = db.getNextTournamentsByParticipation(user);
        ArrayList<String> tournamentNames = tournaments.get("name");
        for (int i = 0; i < tournamentNames.size(); i++) {
            notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                    .setContentTitle(tournamentNames.get(i))
                    .setSubText(getString(R.string.notification_upcoming_tournament))
                    .setContentText(getString(R.string.notification_body))
                    .setVibrate(new long[]{0, 500, 100, 500})
                    .setAutoCancel(true);
            notificationManager.notify(i, notificationBuilder.build());
        }
    }
}
