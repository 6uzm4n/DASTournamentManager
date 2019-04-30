package com.example.tournamentmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.example.tournamentmanager.tournaments.TournamentInfoActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseManager extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            System.out.println("MESSAGE");
        }

        if (remoteMessage.getNotification() != null) {
            System.out.println("NOTIFICATION");

            if (remoteMessage.getData().containsKey("id")){
                RemoteMessage.Notification notification = remoteMessage.getNotification();

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                Bundle args = new Bundle();
//                args.putString("id", "8");
                args.putString("id", remoteMessage.getData().get("id"));
                System.out.println("ID DE LA NOTIFICACIÓN:" + remoteMessage.getData().get("id"));
                Intent intent = new Intent(this, TournamentInfoActivity.class);
                intent.putExtras(args);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "UpcomingTournaments")
                        .setContentTitle(notification.getTitle())
                        .setContentText(notification.getBody())
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.tm_icon);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel("TournamentManager", "UpcomingTournaments", NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(notificationChannel);
                }


                notificationManager.notify(0, notificationBuilder.build());
            }
        }
    }
}
