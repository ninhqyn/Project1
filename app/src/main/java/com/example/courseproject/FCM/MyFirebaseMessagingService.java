package com.example.courseproject.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.courseproject.Activity.NotificationActivity;
import com.example.courseproject.Activity.SplashActivity;
import com.example.courseproject.Activity.ui.coursedetail.CourseDetailActivity;
import com.example.courseproject.Activity.LoginActivity;
import com.example.courseproject.Activity.MainActivity;
import com.example.courseproject.Activity.TrangChuActitvity;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Model.Course;
import com.example.courseproject.R;
import com.example.courseproject.Service.NotificationService;
import com.example.courseproject.SharedPerferences.DataLocalManager;
import com.example.courseproject.SharedPerferences.MyApplication;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d("Notification fcm", "Message received");
        if(DataLocalManager.isUserLoggedIn()){

            RemoteMessage.Notification notification = message.getNotification();
            if(message.getData().size()>0){
                String courseId = message.getData().get("course_id");
                String body = message.getData().get("body");
                String title = message.getData().get("title");
                sendNotification(title, body,courseId);
            }else {
                Log.d("Notification", "data is null");
                String title = notification.getTitle();
                String body = notification.getBody();
                sendNotificationDaily(title, body);
            }
        }


    }

    private void sendNotificationDaily(String title, String body) {
        Intent resultIntent;
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        resultIntent = new Intent(this, TrangChuActitvity.class);
        stackBuilder.addNextIntentWithParentStack(new Intent(this, SplashActivity.class))
                .addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(getNotificationId(),
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(getNotificationId(), notification);

        Log.d("Notification", "Notification sent");
    }



    private void sendNotification(String title, String messageBody, String courseId) {
        callApiAddNotification(title,messageBody,courseId);
        Intent resultIntent;
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        resultIntent = new Intent(this, TrangChuActitvity.class);
        stackBuilder.addNextIntentWithParentStack(new Intent(this, SplashActivity.class))
                    .addNextIntent(resultIntent);
        resultIntent.putExtra("course_id", courseId);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(getNotificationId(),
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(getNotificationId(), notification);

        Log.d("Notification", "Notification sent");
    }

    private void callApiAddNotification(String title, String messageBody,String courseId) {
        com.example.courseproject.Model.Notification notification = new com.example.courseproject.Model.Notification(0,DataLocalManager.getUserId(),Integer.parseInt(courseId),title,messageBody,"2024-11-03T10:42:19.239Z",false);
        NotificationService apiService = ApiClient.getClient(true).create(NotificationService.class);
        apiService.addNotification(notification).enqueue(new Callback<Unit>() {
            @Override
            public void onResponse(Call<Unit> call, Response<Unit> response) {
                if(response.isSuccessful()){
                    //Cập nhật trạng thái thông báo
                    Log.d("Notification", "Notification add sent");
                    updateNotificationCount();
                }
            }
            @Override
            public void onFailure(Call<Unit> call, Throwable t) {

            }
        });
    }
    private void updateNotificationCount() {
        NotificationService apiService = ApiClient.getClient(true).create(NotificationService.class);
        apiService.getNotifications(DataLocalManager.getUserId()).enqueue(new Callback<List<com.example.courseproject.Model.Notification>>() {
            @Override
            public void onResponse(Call<List<com.example.courseproject.Model.Notification>> call, Response<List<com.example.courseproject.Model.Notification>> response) {
                Log.d("Notification", response.message() + DataLocalManager.getUserId());
                if(response.isSuccessful() && response.body()!=null){
                    List<com.example.courseproject.Model.Notification> notifications = response.body();
                    List<com.example.courseproject.Model.Notification> list = new ArrayList<>();
                    for(com.example.courseproject.Model.Notification notification : notifications){
                        if(!notification.isRead()){
                            list.add(notification);
                        }
                    }
                    Intent updateIntent = new Intent("UPDATE_TEXT_VIEW");
                    updateIntent.putExtra("newText", list.size()+"");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(updateIntent); // Gửi broadcast
                }
            }

            @Override
            public void onFailure(Call<List<com.example.courseproject.Model.Notification>> call, Throwable t) {

            }
        });
    }


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("refreshedToken", "Refreshed token: " + token);
        //sendRegistrationToServer(token);
    }

    private int getNotificationId() {
        int notificationId = new Random().nextInt();
        return notificationId;
    }
}
