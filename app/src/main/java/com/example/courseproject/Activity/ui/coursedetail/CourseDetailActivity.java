package com.example.courseproject.Activity.ui.coursedetail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.courseproject.Activity.LessonDetailActivity;
import com.example.courseproject.Activity.LoginActivity;
import com.example.courseproject.Activity.QuizActivity;
import com.example.courseproject.Activity.TrangChuActitvity;
import com.example.courseproject.Activity.ui.coursedetail.ui.LessonFragment;
import com.example.courseproject.Activity.ui.coursedetail.ui.QuizFragment;
import com.example.courseproject.Adapter.LessonAdapter;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.CustomViewPager.CourseDetailAdapter;
import com.example.courseproject.CustomViewPager.MyViewPagerLearningAdapter;
import com.example.courseproject.Interface.IClickItemLessonListener;
import com.example.courseproject.Model.Course;
import com.example.courseproject.Model.Enrollment;
import com.example.courseproject.Model.FavoriteCourse;
import com.example.courseproject.Model.Instructor;
import com.example.courseproject.Model.Lesson;
import com.example.courseproject.R;
import com.example.courseproject.Service.CourseFavoriteService;
import com.example.courseproject.Service.CourseService;
import com.example.courseproject.Service.EnrollmentService;
import com.example.courseproject.Service.InstructorService;
import com.example.courseproject.Service.LessonService;
import com.example.courseproject.SharedPerferences.DataLocalManager;
import com.example.courseproject.ZaloPay.OrderActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDetailActivity extends AppCompatActivity {
    private TextView textTitle;
    private ImageButton btnBack;
    private Button btnDangKy;
    private Course course;
    private boolean isCertificate = false;
    private boolean isEnroll = false;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ImageView imgCourse,imgInstructor;
    private TextView tvNameInstructor;
    private CourseDetailAdapter adapter;
    private ImageButton imgFavorite;
    private boolean isFavorite = false;
    private Dialog loadingDialog;
    private  final int REQUEST_CODE_PAYMENT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        createWidget();
        course = (Course) getIntent().getSerializableExtra("object_course");
        isEnroll();

        if(course != null){
            textTitle.setText(course.getTitle());
            Glide.with(this)
                    .load(course.getImage())
                    .placeholder(android.R.drawable.ic_delete)
                    .error(R.drawable.lesson)
                    .into(imgCourse);
        }
        callApiInstructor();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!DataLocalManager.isPayment()){
                    setResult(Activity.RESULT_OK);
                    finish();
                }
                else {
                    DataLocalManager.setPayment(false);
                    Intent intent = new Intent(CourseDetailActivity.this, TrangChuActitvity.class);
                    startActivity(intent);
                }

            }
        });
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (course != null) {
                    if (isEnroll) {
                        //createDialogCertificate();
                        Toast.makeText(CourseDetailActivity.this,"Đã đăng ký",Toast.LENGTH_SHORT).show();
                    } else{
                        createDialogDangKy();
                    }

                }
            }
        });

        callApiQuizResult();


        //Tab layout and viewpager2
        tabLayout = findViewById(R.id.tab_layout_learning);
        viewPager2 = findViewById(R.id.view_pager);
        adapter = new CourseDetailAdapter(this);
        adapter.setCourse(course);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position)->{
            switch (position){
                case 0:
                    tab.setText("introduction");
                    break;
                case 1:
                    tab.setText("Lesson");
                    break;
                case 2:
                    tab.setText("Quiz");
                    break;
            }
        }
        ).attach();

        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    callApiFavorite();

            }
        });
    }
    private void createDialogLoading() {
        loadingDialog = new Dialog(CourseDetailActivity.this);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.dialog_loading);
        Window window = loadingDialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = 0.5f;
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setBackgroundDrawableResource(R.drawable.background_loading);
        }
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    // Create a method to dismiss the loading dialog
    private void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    private void callApiFavorite() {
        CourseFavoriteService courseFavoriteService = ApiClient.getClient(false).create(CourseFavoriteService.class);
        FavoriteCourse favoriteCourse = new FavoriteCourse(0, DataLocalManager.getUserId(), course.getCourseId(), "2024-10-28T07:16:13.701Z");
        if(!isFavorite){

            courseFavoriteService.addFavoriteCourse(favoriteCourse).enqueue(new Callback<Unit>() {
                @Override
                public void onResponse(Call<Unit> call, Response<Unit> response) {
                    Log.d("Response",response.message());
                    if (response.isSuccessful()) {
                        Toast.makeText(CourseDetailActivity.this, "Add favorite success", Toast.LENGTH_SHORT).show();
                        // Cập nhật drawable
                        imgFavorite.setImageResource(R.drawable.baseline_favorite_24); // Thay đổi drawable thành đã yêu thích
                        isFavorite =true;
                    } else {
                        Toast.makeText(CourseDetailActivity.this, "Add favorite fail", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Unit> call, Throwable t) {
                    Toast.makeText(CourseDetailActivity.this, "Fail to call api", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Xóa khỏi danh sách yêu thích
            courseFavoriteService.deleteFavoriteCourse(DataLocalManager.getUserId(), course.getCourseId()).enqueue(new Callback<Unit>() {
                @Override
                public void onResponse(Call<Unit> call, Response<Unit> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CourseDetailActivity.this, "Delete favorite success", Toast.LENGTH_SHORT).show();
                        isFavorite = false;
                        imgFavorite.setImageResource(R.drawable.baseline_favorite_border_24); // Thay đổi drawable về chưa yêu thích
                    } else {
                        Log.d("resopnse", "onResponse: " + response.message());
                        Toast.makeText(CourseDetailActivity.this, "Delete favorite fail", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Unit> call, Throwable t) {
                    Toast.makeText(CourseDetailActivity.this, "Fail to call api", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void callApiInstructor() {
        InstructorService apiService = ApiClient.getClient(true).create(InstructorService.class);
        apiService.getInstructorById(course.getInstructorId()).enqueue(new Callback<Instructor>() {
            @Override
            public void onResponse(Call<Instructor> call, Response<Instructor> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Instructor instructor = response.body();
                    tvNameInstructor.setText(instructor.getName());
                    Glide.with(CourseDetailActivity.this)
                            .load(instructor.getImage())
                            .placeholder(android.R.drawable.ic_delete)
                            .error(R.drawable.lesson)
                            .into(imgInstructor);
                }
            }

            @Override
            public void onFailure(Call<Instructor> call, Throwable t) {

            }
        });

    }

    private void callApiQuizResult() {

    }

    private void createWidget() {
        textTitle = findViewById(R.id.tv_title_course);
        btnBack = findViewById(R.id.ima_course_detalis_back);
        btnDangKy = findViewById(R.id.button_dang_ky);
        imgCourse = findViewById(R.id.img_course);
        imgInstructor = findViewById(R.id.img_instructor);
        tvNameInstructor = findViewById(R.id.name_instructor);
        imgFavorite = findViewById(R.id.img_btn_favorite);
        setIsFavorite();
        if(isFavorite){
            imgFavorite.setImageResource(R.drawable.baseline_favorite_24);
        }else{
            imgFavorite.setImageResource(R.drawable.baseline_favorite_border_24);
        }

    }
    private void isEnroll() {
       EnrollmentService apiService =ApiClient.getClient(false).create(EnrollmentService.class);
       apiService.getEnrollmentByUserIdAndCourseId(DataLocalManager.getUserId(),course.getCourseId()).enqueue(new Callback<Enrollment>() {
           @Override
           public void onResponse(Call<Enrollment> call, Response<Enrollment> response) {
               if(response.isSuccessful()){
                   isEnroll = true;
                   adapter.setEnroll(true);
                   Log.d("status", "onCreate: "+isEnroll+"");
                   btnDangKy.setText("Đã đăng ký");

               }
           }

           @Override
           public void onFailure(Call<Enrollment> call, Throwable t) {

           }
       });
    }
    private void setIsFavorite() {
        CourseFavoriteService apiService = ApiClient.getClient(true).create(CourseFavoriteService.class);
        apiService.getFavoriteCourseByUserId(DataLocalManager.getUserId()).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if(response.isSuccessful() && response.body()!=null){
                   List<Course> courseList = response.body();
                   for (Course c : courseList) {
                       if (c.getCourseId() == course.getCourseId()) {
                           imgFavorite.setImageResource(R.drawable.baseline_favorite_24);
                           isFavorite = true;
                           break;
                       }
                   }
                }else {
                    Log.d("response", "onResponse: "+response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {

            }
        });
    }

    private void createDialogCertificate() {
        final Dialog dialog = new Dialog(CourseDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_certification);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.setCancelable(true);
        TextView intructionName = dialog.findViewById(R.id.tv_intro_course_without_certificate);
        TextView tvPriceCer = dialog.findViewById(R.id.tv_price_certification_without_certificate);
        Button dialogButton = dialog.findViewById(R.id.btn_continue_without_certificate);
        intructionName.setText("Introduction of " + course.getTitle());
        tvPriceCer.setText("Purchase Course " + course.getPrice());


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CourseDetailActivity.this,"Certificate enroll",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void createDialogDangKy() {
        final Dialog dialog = new Dialog(CourseDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_enroll);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        TextView intructionName = dialog.findViewById(R.id.tv_intro_course);
        TextView tvPriceCer = dialog.findViewById(R.id.tv_price_certification);
        Button dialogButton = dialog.findViewById(R.id.btn_continue);
        RadioButton rdpur = dialog.findViewById(R.id.radio_button_purchase);
        RadioButton rdfull = dialog.findViewById(R.id.radio_button_full);
        intructionName.setText("Introduction of " + course.getTitle());
        tvPriceCer.setText("Purchase Course " + course.getPrice());


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rdpur.isChecked()){
                    Intent intent = new Intent(CourseDetailActivity.this, OrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("course",course);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                    dialog.dismiss();
                }
                if(rdfull.isChecked()){
                    //createDialogLoading();
                    //callApiEnrollment();

                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }
    private void dialogSuccess() {
        final Dialog dialog = new Dialog(CourseDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_enroll_no_certificate);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = 0.5f;
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setBackgroundDrawableResource(R.drawable.background_loading);
        }
        dialog.setCancelable(true);
        Button btnGoToCourse = dialog.findViewById(R.id.btn_go_to_course);
        TextView tvIntro = dialog.findViewById(R.id.tv_intro_course);
        tvIntro.setText("Introduction to " +course.getTitle());

        btnGoToCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDangKy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createDialogCertificate();
                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void callApiEnrollment() {
        Enrollment a = new Enrollment(DataLocalManager.getUserId(),course.getCourseId(),"enrolled",false);
        EnrollmentService enrollmentService = ApiClient.getClient(true).create(EnrollmentService.class);
        enrollmentService.enrollToCourse(a).enqueue(new Callback<Unit>() {
            @Override
            public void onResponse(Call<Unit> call, Response<Unit> response) {
                if(response.isSuccessful()){
                    isEnroll = true;
                    btnDangKy.setText("Đã đăng ký");
                    LessonFragment lessonFragment = (LessonFragment) adapter.getFragment(1);
                    lessonFragment.setEnrollStatus(isEnroll); // Gọi phương thức refresh
                    QuizFragment quizFragment = (QuizFragment) adapter.getFragment(2);
                    quizFragment.setEnrollStatus(isEnroll); // Gọi phương thức refresh
                    dismissLoadingDialog();
                    dialogSuccess();
                }
                else{
                    Toast.makeText(CourseDetailActivity.this,"Enroll fail",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Unit> call, Throwable t) {
                Log.d("Error call api",t.getMessage());
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PAYMENT && resultCode == RESULT_OK) {
            String result = data.getStringExtra("result");


            if ("success".equals(result)) {
                // Xử lý kết quả thanh toán thành công
                Log.d("result back","success");
                isEnroll = true;
                btnDangKy.setText("Đã đăng ký");
                Fragment lessonFragment = getSupportFragmentManager().findFragmentByTag(LessonFragment.class.getSimpleName());

                if (lessonFragment != null && lessonFragment instanceof LessonFragment) {
                    // Nếu fragment tồn tại, cast và gọi phương thức setEnrollStatus
                    ((LessonFragment) lessonFragment).setEnrollStatus(true); // Hoặc false tùy vào trạng thái
                } else {
                    // Fragment không tồn tại, có thể là do ViewPager2 đã không giữ fragment, bạn có thể xử lý lại bằng cách gọi setEnrollStatus khi fragment được tạo lại
                    Log.e("CourseDetailActivity", "LessonFragment is not found.");
                }
                Fragment quizzFragment = getSupportFragmentManager().findFragmentByTag(QuizFragment.class.getSimpleName());

                if (quizzFragment != null && quizzFragment instanceof QuizFragment) {
                    // Nếu fragment tồn tại, cast và gọi phương thức setEnrollStatus
                    ((QuizFragment) quizzFragment).setEnrollStatus(true); // Hoặc false tùy vào trạng thái
                } else {
                    // Fragment không tồn tại, có thể là do ViewPager2 đã không giữ fragment, bạn có thể xử lý lại bằng cách gọi setEnrollStatus khi fragment được tạo lại
                    Log.e("CourseDetailActivity", "QuizFragment is not found.");
                }
                Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();
                // Cập nhật lại dữ liệu khóa học nếu cần
            } else if ("canceled".equals(result)) {
                Toast.makeText(this, "Payment Canceled", Toast.LENGTH_SHORT).show();
            } else if ("error".equals(result)) {
                Toast.makeText(this, "Payment Error", Toast.LENGTH_SHORT).show();
            }
        }
    }


}