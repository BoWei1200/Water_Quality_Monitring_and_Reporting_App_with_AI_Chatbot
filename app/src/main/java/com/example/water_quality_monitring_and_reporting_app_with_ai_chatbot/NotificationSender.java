package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationSender extends Thread{
    private Context context;
    private int drawableIcon;
    private String title;
    private String content;

    private String notificationIdentifier;

    public NotificationSender(Context context, int drawableIcon, String title, String content, String notificationIdentifier) {
        this.context = context;
        this.drawableIcon = drawableIcon;
        this.title = title;
        this.content = content;
        this.notificationIdentifier = notificationIdentifier;
    }

    public void run(){
        try{
            createNotification(context, drawableIcon, title, content);

            if(notificationIdentifier.equals("WQI")){
                Thread.sleep(60000); // 1 minute
            }
        }catch (InterruptedException e){
            System.out.println(e.toString());
        }

    }

    public void createNotification(Context context, int drawableIcon, String title, String content){
        createNotificationChannel(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "hydroMyNotification")
                .setSmallIcon(drawableIcon)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, builder.build());

        Intent intent = new Intent(context, UserHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        builder.setContentIntent(pendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("hydroMyNotification", "hydroMyNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
