package com.example.courseproject.SharedPerferences;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.example.courseproject.R;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyApplication extends Application {
    public static final String CHANNEL_ID = "CHANEL_1";
    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManager.init(getApplicationContext());
        createNotificationChannel();
        subscribeToTopic();
    }
    private void subscribeToTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("your_topic_name")
                .addOnCompleteListener(task -> {
                    String msg = "Subscribed";
                    if (!task.isSuccessful()) {
                        msg = "Subscription failed";
                    }
                    Log.d("FCM", msg);
                });
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //Chanel 1
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.

            /*
            //Chanel 2
            CharSequence name_2 = getString(R.string.channel_name_2);
            String description_2 = getString(R.string.channel_description_2);
            int importance_2 = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel_2 = new NotificationChannel(CHANNEL_ID_2, name_2, importance_2);
            channel_2.setDescription(description_2);*/

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
                //notificationManager.createNotificationChannel(channel_2);
            }
        }
    }
}
