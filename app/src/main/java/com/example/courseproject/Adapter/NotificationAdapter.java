package com.example.courseproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Interface.IClickMenuNotificationListener;
import com.example.courseproject.Interface.IClickNotificationListener;
import com.example.courseproject.Model.Notification;
import com.example.courseproject.R;
import com.example.courseproject.Service.NotificationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private Context context;
    private List<Notification> notificationList;
    private IClickNotificationListener listener;
    private IClickMenuNotificationListener menuListener;
    public NotificationAdapter(Context context, IClickNotificationListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setMenuListener(IClickMenuNotificationListener menuListener) {
        this.menuListener = menuListener;
    }

    public void setData(List<Notification> notificationList){
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_holder_notification,parent,false);
        return new NotificationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        if (notification == null) {
            return;
        }

        // Set title and content
        holder.tvTitle.setText(notification.getTitle());
        holder.tvContent.setText(notification.getMessage());

        // TODO: set image

        String time = notification.getCreatedAt();

        // Define a formatter that can handle the input time format (with milliseconds)
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        // Parse the time string to LocalDateTime (assume it's UTC for now)
        LocalDateTime notificationDateTime = null;
        try {
            notificationDateTime = LocalDateTime.parse(time, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }

        if (notificationDateTime != null) {
            // Assume the time is in UTC, and convert it to the device's local time zone
            ZonedDateTime utcDateTime = ZonedDateTime.of(notificationDateTime, ZoneOffset.UTC);
            ZonedDateTime localDateTime = utcDateTime.withZoneSameInstant(ZoneId.systemDefault());

            // Calculate the time difference from now
            Duration duration = Duration.between(localDateTime, ZonedDateTime.now());

            // Display the time difference in a readable format
            String displayText = formatDuration(duration);
            holder.tvDate.setText(displayText);
        }

        // Set click listeners for various views
        holder.btnMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.btnMenu);
            popupMenu.getMenuInflater().inflate(R.menu.notification_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.option_1) {
                    //TODO: Xóa
                    menuListener.onClickMenuNotification(notification);
                }
                return false;
            });

            popupMenu.show();
        });

        // Set onClickListeners for item views
        View.OnClickListener itemClickListener = v -> listener.onClickItemNotification(notification);
        holder.img.setOnClickListener(itemClickListener);
        holder.tvTitle.setOnClickListener(itemClickListener);
        holder.tvContent.setOnClickListener(itemClickListener);
        holder.layoutItem.setOnClickListener(itemClickListener);
    }

    // Helper method to format the duration as a human-readable string
    private String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        if (seconds < 60) {
            return "Vừa mới";
        } else if (seconds < 3600) {
            return String.format("%d phút", seconds / 60);
        } else if (seconds < 86400) {
            return String.format("%d giờ", seconds / 3600);
        } else if (seconds < 2592000) { // less than a month
            return String.format("%d ngày", seconds / 86400);
        } else if (seconds < 31536000) { // less than a year
            return String.format("%d tháng", seconds / 2592000);
        } else {
            return String.format("%d năm", seconds / 31536000);
        }
    }




    @Override
    public int getItemCount() {
        if(notificationList!=null){
            return notificationList.size();
        }
        return 0;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView btnMenu;
        TextView tvTitle;
        TextView tvContent;
        TextView tvDate;
        ImageView img;
        RelativeLayout layoutItem;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            btnMenu = itemView.findViewById(R.id.img_btn_menu);
            tvTitle = itemView.findViewById(R.id.title);
            tvContent = itemView.findViewById(R.id.message);
            tvDate = itemView.findViewById(R.id.tv_time);
            img = itemView.findViewById(R.id.img_notification);
            layoutItem = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
