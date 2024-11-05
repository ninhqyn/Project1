package com.example.courseproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.Activity.ui.coursedetail.CourseDetailActivity;
import com.example.courseproject.Adapter.NotificationAdapter;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Interface.IClickMenuNotificationListener;
import com.example.courseproject.Interface.IClickNotificationListener;
import com.example.courseproject.Model.Course;
import com.example.courseproject.Model.Notification;
import com.example.courseproject.R;
import com.example.courseproject.Service.CourseService;
import com.example.courseproject.Service.NotificationService;
import com.example.courseproject.SharedPerferences.DataLocalManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    ImageButton btnBack;
    RecyclerView rcvNotification;
    NotificationAdapter adapter;
    RecyclerView rcvNotification2;
    NotificationAdapter adapter2;
    ProgressBar loadingToday,loadingOther;
    TextView tvOtherNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvOtherNotification = findViewById(R.id.tv_other_notification);
        loadingToday = findViewById(R.id.loading_notification);
        loadingOther = findViewById(R.id.loading_notification_2);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
        rcvNotification = findViewById(R.id.rcv_notification);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvNotification.setLayoutManager(layoutManager);
        adapter = new NotificationAdapter(this, new IClickNotificationListener() {
            @Override
            public void onClickItemNotification(Notification notification) {
                callApiGetCourseById(notification.getCourseId());
            }
        });
        adapter.setMenuListener(new IClickMenuNotificationListener() {
            @Override
            public void onClickMenuNotification(Notification notification) {
                callApiDeleteNotification(notification.getId());
            }
            });

        rcvNotification.setAdapter(adapter);


        //
        rcvNotification2 = findViewById(R.id.rcv_notification_2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvNotification2.setLayoutManager(layoutManager2);
        adapter2 = new NotificationAdapter(this, new IClickNotificationListener() {
            @Override
            public void onClickItemNotification(Notification notification) {
                callApiGetCourseById(notification.getCourseId());
            }
        });
        adapter2.setMenuListener(new IClickMenuNotificationListener() {
            @Override
            public void onClickMenuNotification(Notification notification) {
                callApiDeleteNotification(notification.getId());
            }
        });
        rcvNotification2.setAdapter(adapter2);
        callApiGetNotification();

    }

    private void callApiDeleteNotification(int id) {
        NotificationService apiService = ApiClient.getClient(false).create(NotificationService.class);
        apiService.deleteNotification(id).enqueue(new Callback<Unit>() {
            @Override
            public void onResponse(Call<Unit> call, Response<Unit> response) {
                if(response.isSuccessful()){
                    callApiGetNotification();
                    Toast.makeText(NotificationActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(NotificationActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Unit> call, Throwable t) {
                Toast.makeText(NotificationActivity.this, "Fail to call api", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void callApiGetCourseById(int courseId){
        CourseService apiService = ApiClient.getClient(true).create(CourseService.class);
        apiService.getCourseById(courseId).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if(response.body()!=null && response.isSuccessful()){
                    Course course = response.body();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("object_course",course);
                    Intent intent = new Intent(NotificationActivity.this, CourseDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else {
                    Toast.makeText(NotificationActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                Toast.makeText(NotificationActivity.this, "Fail to call api", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void callApiGetNotification() {
        NotificationService apiService = ApiClient.getClient(true).create(NotificationService.class);
        apiService.getNotifications(DataLocalManager.getUserId()).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Notification> allNotifications = response.body();
                    List<Notification> todayNotifications = new ArrayList<>();
                    List<Notification> otherNotifications = new ArrayList<>();

                    // Lấy ngày hôm nay
                    LocalDate today = LocalDate.now();

                    // Tạo DateTimeFormatter linh hoạt
                    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                            .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
                            .optionalStart()
                            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 3, true)
                            .optionalEnd()
                            .toFormatter();

                    for (Notification notification : allNotifications) {
                        try {
                            LocalDateTime notificationDateTime = LocalDateTime.parse(notification.getCreatedAt(), formatter);
                            LocalDate notificationDate = notificationDateTime.toLocalDate();

                            if (notificationDate.isEqual(today)) {
                                todayNotifications.add(notification);
                            } else {
                                otherNotifications.add(notification);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // Cập nhật dữ liệu cho các adapter
                    adapter.setData(todayNotifications);
                    adapter.notifyDataSetChanged();
                    adapter2.setData(otherNotifications);
                    adapter2.notifyDataSetChanged();

                    // Kiểm tra xem có thông báo hôm nay không
                    TextView tvNoNotificationsToday = findViewById(R.id.tv_no_notifications_today);
                    if (todayNotifications.isEmpty()) {
                        loadingToday.setVisibility(View.GONE);
                        tvNoNotificationsToday.setVisibility(View.VISIBLE); // Hiển thị thông báo không có thông báo
                    } else {
                        tvNoNotificationsToday.setVisibility(View.GONE); // Ẩn thông báo
                    }
                    if (otherNotifications.isEmpty()) {
                        tvOtherNotification.setVisibility(View.GONE);
                        loadingOther.setVisibility(View.GONE);
                        rcvNotification2.setVisibility(View.GONE); // Ẩn RecyclerView trước đó
                    } else {
                        tvOtherNotification.setVisibility(View.VISIBLE);
                        loadingOther.setVisibility(View.GONE);
                        rcvNotification2.setVisibility(View.VISIBLE); // Hiện RecyclerView trước đó
                    }
                    if (allNotifications.isEmpty()){
                      loadingToday.setVisibility(View.GONE);
                      loadingOther.setVisibility(View.GONE);
                      tvNoNotificationsToday.setVisibility(View.VISIBLE);
                      tvOtherNotification.setVisibility(View.GONE);
                      rcvNotification2.setVisibility(View.GONE);
                      rcvNotification.setVisibility(View.GONE);

                      TextView tvToday = findViewById(R.id.tv_today);
                      tvToday.setVisibility(View.GONE);
                      TextView tvNoNotification = findViewById(R.id.tv_no_notification);
                      ImageView imgNoNotification = findViewById(R.id.img_no_notification);
                      tvNoNotification.setVisibility(View.VISIBLE);
                      imgNoNotification.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }





}