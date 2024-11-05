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
        if(notification==null){
            return;
        }
        holder.tvTitle.setText(notification.getTitle());
        holder.tvContent.setText(notification.getMessage());

        //TODO: set image

        String time = notification.getCreatedAt();

        // Định dạng chuỗi thời gian
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS");
        Date date = null;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Tạo Calendar từ Date
        Calendar setDate = Calendar.getInstance();
        setDate.setTime(date);

        // Lấy ngày hiện tại
        Calendar currentDate = Calendar.getInstance();

        // Tính khoảng cách
        long differenceMillis = currentDate.getTimeInMillis() - setDate.getTimeInMillis();
        long differenceSeconds = differenceMillis / 1000;
        long differenceMinutes = differenceSeconds / 60;
        long differenceHours = differenceMinutes / 60;
        long differenceDays = differenceHours / 24;
        long differenceMonths = differenceDays / 30;
        long differenceYears = differenceMonths / 12;

        String displayText;

        if (differenceYears > 0) {
            displayText = String.format("%d năm", differenceYears);
        } else if (differenceMonths > 0) {
            displayText = String.format("%d tháng", differenceMonths);
        } else if (differenceDays > 0) {
            displayText = String.format("%d ngày", differenceDays);
        } else if (differenceHours > 0) {
            displayText = String.format("%d giờ", differenceHours);
        } else if (differenceMinutes > 0) {
            displayText = String.format("%d phút", differenceMinutes);
        } else {
            displayText = "Vừa mới";
        }

        holder.tvDate.setText(displayText);

        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.btnMenu);
                popupMenu.getMenuInflater().inflate(R.menu.notification_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.option_1){
                            //TODO: Xóa
                            menuListener.onClickMenuNotification(notification);
                        }else {

                        }
                        return false;
                    }
                });

                // Hiện menu
                popupMenu.show();

            }
        });

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickItemNotification(notification);
            }
        });
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickItemNotification(notification);
            }
        });
        holder.tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickItemNotification(notification);
            }
        });
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickItemNotification(notification);
            }
        });
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
