package com.example.tournamentmanager.menus;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.example.tournamentmanager.DatabaseManager;
import com.example.tournamentmanager.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SplashScreenActivity extends AppCompatActivity {

    private final int SPLASH_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Se comprueba el tema que el usuario ha seleccionado en sus preferencias y se aplica en la app
        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        String theme = settings.getString("theme", "light");
        if (theme.equals("light")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        // Se comprueba si el usuario ya ha iniciado sesión y se le redirige a la actividad correspondiente después de unos segundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences session = getSharedPreferences("session", 0); // TODO: Almacenar estas variables?
                Intent i;
                String user = session.getString("session", null);
                if (user == null) {
                    i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                } else {
                    notifyUpcomingTournaments(user);
                    i = new Intent(SplashScreenActivity.this, MainActivity.class);
                }
                startActivity(i);
                finish();
            }
        }, SPLASH_DURATION);
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
        for (int i = 0; i<tournamentNames.size(); i++){
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
