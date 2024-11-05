package com.example.courseproject.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Model.SignUpRequest;
import com.example.courseproject.R;
import com.example.courseproject.Service.AccountService;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    TextView txtSignIn;
    TextView tvUsername,tvEmail,tvPassword,tvConfirmPassword;
    TextView tvErrorUserName,tvErrorEmail,tvErrorPass,tvErrorConfirmPass;
    Button btnSignUp;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //
        txtSignIn = findViewById(R.id.txtSignIn);
        tvUsername = findViewById(R.id.tv_username_signup);
        tvEmail = findViewById(R.id.tv_email_signup);
        tvPassword = findViewById(R.id.tv_password_signup);
        tvConfirmPassword = findViewById(R.id.tv_confirm_password_signup);
        btnSignUp = findViewById(R.id.btn_signup);
        tvErrorUserName = findViewById(R.id.tv_error_user_name);
        tvErrorEmail = findViewById(R.id.tv_error_email);
        tvErrorPass = findViewById(R.id.tv_error_pass);
        tvErrorConfirmPass = findViewById(R.id.tv_error_confirm_pass);
        btnSignUp.setEnabled(false);
        btnSignUp.setAlpha(0.5f);

        //
        txtSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Thêm TextWatcher cho tất cả các trường
        TextWatcher passwordTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validatePassword(tvErrorUserName, tvErrorEmail, tvErrorPass, tvErrorConfirmPass, tvUsername, tvEmail, tvPassword, tvConfirmPassword, btnSignUp);
            }
        };
        tvUsername.addTextChangedListener(passwordTextWatcher);
        tvEmail.addTextChangedListener(passwordTextWatcher);
        tvPassword.addTextChangedListener(passwordTextWatcher);
        tvConfirmPassword.addTextChangedListener(passwordTextWatcher);

        //
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = tvUsername.getText().toString();
                String email = tvEmail.getText().toString();
                String password = tvPassword.getText().toString();
              /* if(tvUsername.getText().toString().isEmpty() || tvEmail.getText().toString().isEmpty() || tvPassword.getText().toString().isEmpty() || tvConfirmPassword.getText().toString().isEmpty()){
                    Toast.makeText(SignUpActivity.this,"Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                String username = tvUsername.getText().toString();
                String email = tvEmail.getText().toString();
                String password = tvPassword.getText().toString();
                String confirmPassword = tvConfirmPassword.getText().toString();
                if(!email.contains("@")){
                    Toast.makeText(SignUpActivity.this,"Email không hợp lệ",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.equals(confirmPassword)){
                    Toast.makeText(SignUpActivity.this,"Mật khẩu không khớp",Toast.LENGTH_SHORT).show();
                    return;
                }*/
                signUp(username,email,password);


            }
        });

    }
    private void createDialogLoading() {
        loadingDialog = new Dialog(SignUpActivity.this);
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
    private void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
    private void validatePassword(TextView tvErrorUserName, TextView tvErrorEmail, TextView tvErrorPass, TextView tvErrorConfirmPass, TextView tvUsername, TextView tvEmail, TextView tvPassword, TextView tvConfirmPassword, Button btnSignUp) {
        // Reset trạng thái hiển thị lỗi
        tvErrorUserName.setVisibility(View.GONE);
        tvErrorEmail.setVisibility(View.GONE);
        tvErrorPass.setVisibility(View.GONE);
        tvErrorConfirmPass.setVisibility(View.GONE);

        String username = tvUsername.getText().toString().trim();
        String email = tvEmail.getText().toString().trim();
        String password = tvPassword.getText().toString();
        String confirmPassword = tvConfirmPassword.getText().toString();

        boolean isValid = true;

        // Kiểm tra trường username
        if (username.isEmpty()) {
            tvErrorUserName.setText("Username không được để trống");
            tvErrorUserName.setVisibility(View.VISIBLE); // Hiển thị lỗi
            isValid = false;
        } else if (username.contains(" ")) {
            tvErrorUserName.setText("Username không được có khoảng trắng");
            tvErrorUserName.setVisibility(View.VISIBLE); // Hiển thị lỗi
            isValid = false;
        }

        // Kiểm tra trường email
        if (email.isEmpty()) {
            tvErrorEmail.setText("Email không được để trống");
            tvErrorEmail.setVisibility(View.VISIBLE); // Hiển thị lỗi
            isValid = false;
        } else if (!email.contains("@") || !email.contains(".")) {
            tvErrorEmail.setText("Email không hợp lệ");
            tvErrorEmail.setVisibility(View.VISIBLE); // Hiển thị lỗi
            isValid = false;
        }

        // Kiểm tra trường password
        if (password.isEmpty()) {
            tvErrorPass.setText("Mật khẩu không được để trống");
            tvErrorPass.setVisibility(View.VISIBLE); // Hiển thị lỗi
            isValid = false;
        }

        // Kiểm tra trường confirm password
        if (confirmPassword.isEmpty()) {
            tvErrorConfirmPass.setText("Xác nhận mật khẩu không được để trống");
            tvErrorConfirmPass.setVisibility(View.VISIBLE); // Hiển thị lỗi
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            tvErrorConfirmPass.setText("Mật khẩu không khớp");
            tvErrorConfirmPass.setVisibility(View.VISIBLE); // Hiển thị lỗi
            isValid = false;
        }

        // Kích hoạt hoặc vô hiệu hóa nút đăng ký
        btnSignUp.setEnabled(isValid);
        btnSignUp.setAlpha(1.0f);
    }


    private void signUp(String username, String email, String password) {
        createDialogLoading();
        SignUpRequest signUpRequest = new SignUpRequest(username,email,password,password);
        AccountService apiService = ApiClient.getClient(false).create(AccountService.class);
        apiService.signUp(signUpRequest).enqueue(new Callback<Unit>() {
            @Override
            public void onResponse(Call<Unit> call, Response<Unit> response) {
                if(response.isSuccessful()){
                    dismissLoadingDialog();
                    Intent intent = new Intent(SignUpActivity.this, VerificationCodeActivity.class);
                    intent.putExtra("email",email);
                    startActivity(intent);
                }
                else {
                    dismissLoadingDialog();
                    Toast.makeText(SignUpActivity.this,"Tài khoản đã tồn tại",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Unit> call, Throwable t) {
                dismissLoadingDialog();
                Toast.makeText(SignUpActivity.this,"error call api",Toast.LENGTH_SHORT).show();
            }
        });
    }
}