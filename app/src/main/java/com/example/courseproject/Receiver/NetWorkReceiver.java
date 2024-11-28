package com.example.courseproject.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetWorkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected()) {
            // Nếu có kết nối internet
            Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
        } else {
            // Nếu mất kết nối internet
            Toast.makeText(context, "Internet Disconnected", Toast.LENGTH_SHORT).show();
        }
    }
}
