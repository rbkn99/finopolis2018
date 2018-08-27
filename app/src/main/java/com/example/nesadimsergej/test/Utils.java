package com.example.nesadimsergej.test;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import static android.support.v4.app.NotificationCompat.DEFAULT_ALL;

public class Utils {
    public static final String CHANNEL_ID = "1";

    public static void createNotificationChannel(Context ctx) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "blp";
            String description = "blockchain loyalty program";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Utils.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = ctx.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void longLoadingNotification(Context ctx, String cause, int not_id) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setDefaults(DEFAULT_ALL)
                .setContentTitle("Blockchain loyalty")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Идёт " + cause + ", это может занять несколько минут. Вы можете " +
                                "свернуть приложение, но, пожалуйста, не выключайте его."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
        notificationManager.notify(not_id, mBuilder.build());
    }
}
