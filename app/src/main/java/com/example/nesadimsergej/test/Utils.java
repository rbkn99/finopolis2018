package com.example.nesadimsergej.test;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import static android.support.v4.app.NotificationCompat.DEFAULT_ALL;

public class Utils {
    public static final String CHANNEL_ID = "1";

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
