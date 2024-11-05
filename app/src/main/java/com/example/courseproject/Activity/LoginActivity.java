package com.example.courseproject.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.courseproject.Activity.ui.coursedetail.CourseDetailActivity;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Model.Course;
import com.example.courseproject.Model.LoginRequest;
import com.example.courseproject.Model.TokenModel;
import com.example.courseproject.Model.User;
import com.example.courseproject.R;
import com.example.courseproject.Service.AccountService;
import com.example.courseproject.Service.CourseService;
import com.example.courseproject.Service.UserService;
import com.example.courseproject.SharedPerferences.DataLocalManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView txtSignUp;
    Button btnLogin;
    TextView tvForgotPassword;
    EditText edit_text_email,edit_text_password;
    private AccountService accountService;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        btnLogin= findViewById(R.id.btnSignUpLogin);
        txtSignUp = findViewById(R.id.txtSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        edit_text_email = findViewById(R.id.edit_text_email);
        edit_text_password = findViewById(R.id.edit_text_password);
        accountService = ApiClient.getClient(true).create(AccountService.class);
        //
        btnLogin.setOnClickListener(view -> {
            createDialogLoading(); // Show loading dialog
            String email = edit_text_email.getText().toString();
            String password = edit_text_password.getText().toString();
            if(email.isEmpty() || password.isEmpty()){
                dismissLoadingDialog(); // Dismiss on error
                Toast.makeText(this,"Vui lòng nhập đầy đủ thông tin",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!email.contains("@")){
                dismissLoadingDialog(); // Dismiss on error
                Toast.makeText(this,"Email không hợp lệ",Toast.LENGTH_SHORT).show();
                return;
            }
            signIn(email,password);
        });

        txtSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
        tvForgotPassword.setOnClickListener(view -> {

            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });


    }

    private void createDialogLoading() {
        loadingDialog = new Dialog(LoginActivity.this);
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

    private void signIn(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email,password);
        Call<TokenModel> call = accountService.signIn(loginRequest);
        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                dismissLoadingDialog();
                if (response.isSuccessful() && response.body() != null) {
                    TokenModel tokenModel = response.body();
                    DataLocalManager.setToken(tokenModel.getAccessToken());
                    DataLocalManager.setEmail(email);
                    DataLocalManager.setUserLoggedIn(true);
                    callAPIUserId(email);
                    if(getIntent().getBooleanExtra("from_notification",false)){
                        String courseId = getIntent().getStringExtra("course_id");
                        callApiGetCourse(courseId);
                    }else {

                        Intent intent = new Intent(LoginActivity.this, TrangChuActitvity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            private void callAPIUserId(String email) {
                UserService apiService = ApiClient.getClient(true).create(UserService.class);
                apiService.getUserByEmail(email).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful() && response.body() != null){
                            User user = response.body();
                            DataLocalManager.userId(user.getUserId());
                            DataLocalManager.setImage(user.getImage());
                            DataLocalManager.setFullName(user.getFullName());
                            DataLocalManager.setPhoneNumber(user.getPhoneNumber());
                            DataLocalManager.setLocation(user.getLocation());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        dismissLoadingDialog();
                        Toast.makeText(LoginActivity.this, "Fail to call api ", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Fail to call api Login", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void callApiGetCourse(String courseId) {
        CourseService apiService = ApiClient.getClient(true).create(CourseService.class);
        apiService.getCourseById(Integer.parseInt(courseId)).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if(response.body()!=null && response.isSuccessful()){
                    Course course = response.body();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("course",course);
                    Intent intent = new Intent(LoginActivity.this, CourseDetailActivity.class);
                    intent.putExtras(bundle);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(LoginActivity.this);
                    stackBuilder.addNextIntentWithParentStack(new Intent(LoginActivity.this,TrangChuActitvity.class));
                    stackBuilder.addNextIntent(intent);
                    stackBuilder.startActivities();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {

            }
        });
    }
}