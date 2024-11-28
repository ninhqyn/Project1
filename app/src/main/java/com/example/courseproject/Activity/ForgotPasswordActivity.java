package com.example.courseproject.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.R;
import com.example.courseproject.Service.AccountService;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    ImageButton btn_back;
    EditText edit_email;
    Button btn_send;
    String EMAIL;
    TextView tvError;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btn_back = findViewById(R.id.btn_back);
        edit_email = findViewById(R.id.edit_email);
        btn_send = findViewById(R.id.btn_send);
        tvError = findViewById(R.id.tv_error);
        edit_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used in this case
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not used in this case
            }

            @Override
            public void afterTextChanged(Editable s) {
                String email = s.toString().trim();
                if (!isValidEmail(email)) {
                    tvError.setText("Định dạng email không hợp lệ");
                    tvError.setVisibility(TextView.VISIBLE);
                } else {
                    tvError.setVisibility(TextView.GONE);
                }
            }
        });
        btn_back.setOnClickListener(v -> finish());
        btn_send.setOnClickListener(v -> {
            EMAIL = edit_email.getText().toString();
            createDialogLoading();
            CallApiForgotPassword();
        });
    }
    private void createDialogLoading() {
        loadingDialog = new Dialog(ForgotPasswordActivity.this);
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

    private void CallApiForgotPassword() {
        AccountService accountService = ApiClient.getClient(false).create(AccountService.class);
        Call<Unit> call = accountService.forgotPassword(EMAIL);
        call.enqueue(new Callback<Unit>() {
            @Override
            public void onResponse(Call<Unit> call, Response<Unit> response) {
                dismissLoadingDialog();
                if (response.isSuccessful()) {
                    Intent intent = new Intent(ForgotPasswordActivity.this, ForgotCodeActivity.class);
                    intent.putExtra("email", EMAIL);
                    startActivity(intent);
                    finish();
                } else {
                    tvError.setText("Không tồn tại tài khoản nào như vậy!!");
                    tvError.setVisibility(TextView.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Unit> call, Throwable t) {
                tvError.setText("Lỗi kết nối đến máy chủ");
                tvError.setVisibility(TextView.VISIBLE);
            }
        });

    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}