package ru.homecatering;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebasePusher extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("MESSAGE", remoteMessage.getFrom());
        Log.d("MESSAGE", remoteMessage.getNotification().getTitle());
        Log.d("MESSAGE", remoteMessage.getNotification().getBody());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), "channel");
        builder.setSmallIcon(R.mipmap.favicon)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true);
//                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel",
                    "Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(26, builder.build());
    }
}
