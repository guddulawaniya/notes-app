package com.example.secure_notes;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    public static int notificationId;
    public static final String channelId = "channel1";

    @Override
    public void onReceive(Context context, Intent intent) {
     notificationId = (int) System.currentTimeMillis();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(intent.getStringExtra("titleExtra"))
                .setContentText(intent.getStringExtra("messageExtra"))
                .setAutoCancel(true);

//        send data
        Intent intent1 = new Intent(context, notification_activity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("NOTIFICATION_TITLE",intent.getStringExtra("titleExtra"));
        intent1.putExtra("NOTIFICATION_MESSAGE",intent.getStringExtra("messageExtra"));


        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                notificationId, intent1, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationId, builder.build());
//        manager.cancel(notificationId);

    }


}
