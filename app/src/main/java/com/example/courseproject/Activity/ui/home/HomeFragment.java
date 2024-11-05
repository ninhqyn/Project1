package com.example.courseproject.Activity.ui.home;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courseproject.Activity.AllCategoryActivity;
import com.example.courseproject.Activity.AllCourseActivity;
import com.example.courseproject.Activity.NotificationActivity;
import com.example.courseproject.Activity.ui.coursedetail.CourseDetailActivity;
import com.example.courseproject.Activity.SearchCourseActivity;
import com.example.courseproject.Adapter.CategoryAdapter;
import com.example.courseproject.Adapter.CourseAdapter;
import com.example.courseproject.Adapter.HomeBannerAdapter;
import com.example.courseproject.Adapter.NewCourseAdapter;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Interface.IClickItemCourseListener;
import com.example.courseproject.Model.Banner;
import com.example.courseproject.Model.Category;
import com.example.courseproject.Model.Course;
import com.example.courseproject.Model.Notification;
import com.example.courseproject.R;
import com.example.courseproject.Service.BannerService;
import com.example.courseproject.Service.CategoryService;
import com.example.courseproject.Service.CourseService;
import com.example.courseproject.Service.NotificationService;
import com.example.courseproject.SharedPerferences.DataLocalManager;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import me.relex.circleindicator.CircleIndicator2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewCourse,recyclerCategoryCourse,recyclerCourseFree;
    private CourseAdapter courseAdapter, courseAdapterFree;
    private static final long SCROLL_DELAY = 5000; // Thời gian delay giữa các lần cuộn (5 giây)
    private Handler handler = new Handler();
    private Runnable runnableNewCourse;
    private CategoryAdapter categoryAdapter;
    private ProgressBar loadingCategory,loadingCourse,loadingCourseFree;
    private TextView tvSeeAllCategory,tvSeeAllCourse,tvSeeAllCourseFree;
    private TextView tvSearch;
    private RecyclerView recyclerViewNewCourse;
    private ProgressBar loadingNewCourse;
    private NewCourseAdapter newCourseAdapter;
    private ImageView notification;
    private TextView tvCountNotification;
    private  int count = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter("UPDATE_TEXT_VIEW"));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Hủy đăng ký BroadcastReceiver
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newText = intent.getStringExtra("newText");
            count = Integer.parseInt(newText);
            tvCountNotification.setText(newText);
            tvCountNotification.setVisibility(View.VISIBLE);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //search
        notification = v.findViewById(R.id.notification);
        tvCountNotification = v.findViewById(R.id.tv_count_notfication);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCountNotification.setVisibility(View.INVISIBLE);
                if(count == 0){
                    Intent intent = new Intent(getContext(), NotificationActivity.class);
                    startActivity(intent);
                }else {
                    count = 0;
                    callApiUpdateAllNotification();
                }


            }
        });
        tvSearch = v.findViewById(R.id.tv_search);
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), SearchCourseActivity.class);
                startActivity(intent);
            }
        });
        //Loading

        loadingCategory = v.findViewById(R.id.loading_category);
        loadingCourse = v.findViewById(R.id.loading_course);
        loadingCourseFree = v.findViewById(R.id.loading_course_free);
        loadingNewCourse = v.findViewById(R.id.loading_new_course);
        //See all
        tvSeeAllCategory = v.findViewById(R.id.tv_see_all_category);
        tvSeeAllCourse = v.findViewById(R.id.tv_see_all_course);
        tvSeeAllCourseFree = v.findViewById(R.id.tv_see_all_course_free);

        //Slide Image Banner

        //List popular course
        recyclerViewCourse =v.findViewById(R.id.recycler_view_course);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);

        recyclerViewCourse.setLayoutManager(linearLayoutManager2);
        courseAdapter = new CourseAdapter(getContext(), new IClickItemCourseListener() {
            @Override
            public void onClickItemCourse(Course course) {
                startCourseDetail(course);
            }
        });
        courseAdapter.setHot(true);
        recyclerViewCourse.setAdapter(courseAdapter);
        getListCourse();

        //List category
        recyclerCategoryCourse = v.findViewById(R.id.recycler_category_course);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        recyclerCategoryCourse.setLayoutManager(linearLayoutManager1);
        categoryAdapter = new CategoryAdapter(getContext());
        recyclerCategoryCourse.setAdapter(categoryAdapter);
        getListCategory();

        //list course free
        recyclerCourseFree = v.findViewById(R.id.rcv_course_free);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        recyclerCourseFree.setLayoutManager(linearLayoutManager3);
        courseAdapterFree = new CourseAdapter(getContext(), new IClickItemCourseListener() {
            @Override
            public void onClickItemCourse(Course course) {

                Intent intent = new Intent(getContext(), CourseDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_course",course);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerCourseFree.setAdapter(courseAdapterFree);

        //See all action
        tvSeeAllCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AllCourseActivity.class);
                startActivity(intent);
            }
        });
        tvSeeAllCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AllCategoryActivity.class);
                startActivity(intent);
            }
        });

        //New course
        recyclerViewNewCourse = v.findViewById(R.id.recycler_new_course);
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        recyclerViewNewCourse.setLayoutManager(linearLayoutManager4);
        newCourseAdapter = new NewCourseAdapter(getContext());
        recyclerViewNewCourse.setAdapter(newCourseAdapter);
        //getListNewCourse();

        //
        CircleIndicator2 indicatorNewCourse = v.findViewById(R.id.indicator_new_course);

        //
        PagerSnapHelper pagerSnapHelperNewCourse = new PagerSnapHelper();
        pagerSnapHelperNewCourse.attachToRecyclerView(recyclerViewNewCourse);

        //
        indicatorNewCourse.attachToRecyclerView(recyclerViewNewCourse,pagerSnapHelperNewCourse);
        newCourseAdapter.registerAdapterDataObserver(indicatorNewCourse.getAdapterDataObserver());
        //
        runnableNewCourse = new Runnable() {
            @Override
            public void run() {
                int currentItem = linearLayoutManager4.findFirstCompletelyVisibleItemPosition();
                int itemCount = newCourseAdapter.getItemCount();

                if (itemCount > 0) {
                    if (currentItem < itemCount - 1) {
                        recyclerViewNewCourse.smoothScrollToPosition(currentItem + 1);
                    } else {
                        recyclerViewNewCourse.smoothScrollToPosition(0);
                    }
                }
                handler.postDelayed(this, SCROLL_DELAY);
            }
        };
        CallApiNotification();

        return v;
    }

    private void callApiUpdateAllNotification() {
        NotificationService apiService = ApiClient.getClient(true).create(NotificationService.class);
        apiService.markAllAsRead(DataLocalManager.getUserId()).enqueue(new Callback<Unit>() {
            @Override
            public void onResponse(Call<Unit> call, Response<Unit> response) {
                Log.d("Notification update", response.message() + DataLocalManager.getUserId());
                if(response.isSuccessful()){
                    Intent intent = new Intent(getContext(), NotificationActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Unit> call, Throwable t) {

            }
        });
    }

    private void CallApiNotification() {
        NotificationService apiService = ApiClient.getClient(true).create(NotificationService.class);
        apiService.getNotifications(DataLocalManager.getUserId()).enqueue(new Callback<List<com.example.courseproject.Model.Notification>>() {
            @Override
            public void onResponse(Call<List<com.example.courseproject.Model.Notification>> call, Response<List<com.example.courseproject.Model.Notification>> response) {
                Log.d("Notification home", response.message() + DataLocalManager.getUserId());
                if(response.isSuccessful() && response.body()!=null){
                    List<com.example.courseproject.Model.Notification> notifications = response.body();
                    List<com.example.courseproject.Model.Notification> list = new ArrayList<>();
                    for(com.example.courseproject.Model.Notification notification : notifications){
                        if(!notification.isRead()){
                            list.add(notification);
                        }
                    }
                    Log.d("Notification home", response.message() + list.size());
                    count = list.size();
                    if(list.size() !=0){
                        tvCountNotification.setText(list.size()+"");
                        tvCountNotification.setVisibility(View.VISIBLE);
                    }else {
                        tvCountNotification.setVisibility(View.INVISIBLE);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<com.example.courseproject.Model.Notification>> call, Throwable t) {

            }
        });
    }


    private void startCourseDetail(Course course) {
        Intent intent = new Intent(getContext(), CourseDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_course",course);
        intent.putExtras(bundle);
        startActivityForResult(intent,30);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 30 && resultCode == Activity.RESULT_OK) {
            // CourseDetailsActivity đã kết thúc, xử lý điều hướng back nếu cần
        }
    }

    private void getListCategory() {
        CategoryService apiService = ApiClient.getClient(true).create(CategoryService.class);
        apiService.getAllCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categoryList = response.body();
                    categoryAdapter.setData(categoryList);
                    categoryAdapter.notifyDataSetChanged();
                    loadingCategory.setVisibility(View.GONE);

                } else {
                    Toast.makeText(getContext(), "No category found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to call api get category", Toast.LENGTH_SHORT).show();
                Log.e("Error fail", t.getMessage());
            }
        });
    }

    private void getListCourse() {
        CourseService apiService = ApiClient.getClient(true).create(CourseService.class);
        apiService.getAllCourse(true).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Course> courseList = response.body();
                    courseAdapter.setData(courseList);
                    courseAdapter.notifyDataSetChanged();
                    loadingCourse.setVisibility(View.GONE);

                    List<Course> courseFreeList = new ArrayList<>();
                    //course free
                    for(int i=0;i<courseList.size();i++){
                        if(courseList.get(i).getPrice() == 0){
                            courseFreeList.add(courseList.get(i));
                        }
                    }
                    courseAdapterFree.setData(courseFreeList);
                    courseAdapterFree.notifyDataSetChanged();
                    loadingCourseFree.setVisibility(View.GONE);


                    //new course
                    int count = 0;
                    List<Course> newCourseList = new ArrayList<>();
                    for(int i=0;i<courseList.size();i++){
                        if(count < 3){
                            newCourseList.add(courseList.get(i));
                            count++;
                        }
                    }
                    newCourseAdapter.setData(newCourseList);
                    newCourseAdapter.notifyDataSetChanged();
                    loadingNewCourse.setVisibility(View.GONE);

                } else {
                    Toast.makeText(getContext(), "No courses found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Log.e("Error fail", t.getMessage());


            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        handler.postDelayed(runnableNewCourse, SCROLL_DELAY); // Bắt đầu cuộn cho khóa học mới
    }

    // Dừng cuộn trong onStop()
    @Override
    public void onStop() {
        super.onStop();

        handler.removeCallbacks(runnableNewCourse); // Dừng cuộn cho khóa học mới
    }



}