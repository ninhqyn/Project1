package com.example.courseproject.Activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.R;
import com.example.courseproject.Service.UserService;
import com.example.courseproject.SharedPerferences.DataLocalManager;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        create();
    }
    private void create() {
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tvOldPassword = findViewById(R.id.tv_old_password);
        TextView tvNewPassword = findViewById(R.id.tv_new_password);
        TextView tvConfirmPassword = findViewById(R.id.tv_confirm_password);
        TextView tvErrorOldPass = findViewById(R.id.tv_error_old_pass);
        TextView tvErrorNewPass = findViewById(R.id.tv_error_new_pass);
        TextView tvErrorConfirmPass = findViewById(R.id.tv_error_confirm_pass);
        Button dialogButton = findViewById(R.id.btn_continue);
        dialogButton.setBackgroundColor(getResources().getColor(R.color.gray));
        dialogButton.setEnabled(false); // Vô hiệu hóa nút

        // Thêm TextWatcher cho tất cả các trường
        TextWatcher passwordTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validatePassword(tvErrorOldPass, tvErrorNewPass, tvErrorConfirmPass, tvOldPassword, tvNewPassword, tvConfirmPassword, dialogButton);
            }
        };

        tvOldPassword.addTextChangedListener(passwordTextWatcher);
        tvNewPassword.addTextChangedListener(passwordTextWatcher);
        tvConfirmPassword.addTextChangedListener(passwordTextWatcher);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserService apiService = ApiClient.getClient(false).create(UserService.class);
                apiService.changePassword(DataLocalManager.getUserId(),tvOldPassword.getText().toString(),tvNewPassword.getText().toString()).enqueue(new Callback<Unit>() {
                    @Override
                    public void onResponse(Call<Unit> call, Response<Unit> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(ChangePasswordActivity.this,"Thanh cong",Toast.LENGTH_LONG).show();
                            finish();

                        }
                    }

                    @Override
                    public void onFailure(Call<Unit> call, Throwable t) {
                        Toast.makeText(ChangePasswordActivity.this,"password cũ not match",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private void validatePassword(TextView tvErrorOldPass, TextView tvErrorNewPass, TextView tvErrorConfirmPass, TextView tvOldPassword, TextView tvNewPassword, TextView tvConfirmPassword, Button dialogButton) {
        String oldPassword = tvOldPassword.getText().toString();
        String newPassword = tvNewPassword.getText().toString();
        String confirmPassword = tvConfirmPassword.getText().toString();
        boolean isValid = true;

        // Kiểm tra nếu trường mật khẩu cũ không được điền
        if (oldPassword.isEmpty()) {
            tvErrorOldPass.setText("Mật khẩu cũ không được để trống.");
            tvErrorOldPass.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            tvErrorOldPass.setVisibility(View.GONE);
        }

        // Kiểm tra nếu trường mật khẩu mới không được điền
        if (newPassword.isEmpty()) {
            tvErrorNewPass.setText("Mật khẩu mới không được để trống.");
            tvErrorNewPass.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            tvErrorNewPass.setVisibility(View.GONE);

            // Kiểm tra độ dài mật khẩu
            if (newPassword.length() < 8) {
                tvErrorNewPass.setText("Mật khẩu phải có ít nhất 8 ký tự.");
                tvErrorNewPass.setVisibility(View.VISIBLE);
                isValid = false;
            }

            // Kiểm tra có chữ hoa
            if (!newPassword.matches(".*[A-Z].*")) {
                tvErrorNewPass.setText("Mật khẩu phải có ít nhất một chữ cái hoa.");
                tvErrorNewPass.setVisibility(View.VISIBLE);
                isValid = false;
            }

            // Kiểm tra có ký tự đặc biệt
            if (!newPassword.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
                tvErrorNewPass.setText("Mật khẩu phải có ít nhất một ký tự đặc biệt.");
                tvErrorNewPass.setVisibility(View.VISIBLE);
                isValid = false;
            }
        }

        // Kiểm tra mật khẩu xác nhận
        if (confirmPassword.isEmpty()) {
            tvErrorConfirmPass.setText("Mật khẩu xác nhận không được để trống.");
            tvErrorConfirmPass.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            tvErrorConfirmPass.setVisibility(View.GONE);
            if (!newPassword.equals(confirmPassword)) {
                tvErrorConfirmPass.setText("Mật khẩu xác nhận không khớp.");
                tvErrorConfirmPass.setVisibility(View.VISIBLE);
                isValid = false;
            }
        }

        // Cập nhật trạng thái nút
        dialogButton.setEnabled(isValid);
        dialogButton.setBackgroundColor(isValid ? getResources().getColor(R.color.oneColor) : getResources().getColor(R.color.gray));
    }
}