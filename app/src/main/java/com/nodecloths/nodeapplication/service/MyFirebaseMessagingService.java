package com.nodecloths.nodeapplication.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.helper.Common;

import java.util.Random;

import static android.app.Notification.EXTRA_BIG_TEXT;
import static com.nodecloths.nodeapplication.helper.Common.fab;
import static com.nodecloths.nodeapplication.helper.Common.fac;
import static com.nodecloths.nodeapplication.helper.Common.quo;
import static com.nodecloths.nodeapplication.helper.Common.sto;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String topic  = remoteMessage.getFrom();
        readNotification(topic,remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

    }

    private void readNotification(String topics,String title, String body){


        if (topics.contains(fab)|| topics.contains(sto)||topics.contains(fac)||topics.contains(quo)){
            showNotification(title,body);
        }

    }

    private void showNotification(String title, String body) {


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.nodecloths.nodeapplication";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("EDMT channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});

            notificationManager.createNotificationChannel(notificationChannel);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(body));
        builder.setContentInfo("Info");

        notificationManager.notify(new Random().nextInt(), builder.build());

    }

}
