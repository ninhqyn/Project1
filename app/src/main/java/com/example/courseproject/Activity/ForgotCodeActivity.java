package com.example.courseproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.R;
import com.example.courseproject.Service.AccountService;

import java.util.Locale;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotCodeActivity extends AppCompatActivity {

    static final String KEY_EMAIL = "email";
    static final String KEY_CODE = "code";
    private EditText tvCode1, tvCode2, tvCode3, tvCode4, tvCode5, tvCode6;
    private TextView tvEmail;
    private Button btnVerity;
    private TextView btnSignUp;
    private TextView tvCountdown;
    private Button btnResend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verification_code);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addWidgets();
        addAction();
        setupTextWatchers();
    }

    private void addAction() {
        btnVerity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle verification logic here
                String verificationCode = tvCode1.getText().toString() +
                        tvCode2.getText().toString() +
                        tvCode3.getText().toString() +
                        tvCode4.getText().toString() +
                        tvCode5.getText().toString() +
                        tvCode6.getText().toString();
                AccountService apiService = ApiClient.getClient(false).create(AccountService.class);
                apiService.verifyForgotPassword(getIntent().getStringExtra(KEY_EMAIL),verificationCode).enqueue(new Callback<Unit>() {
                    @Override
                    public void onResponse(Call<Unit> call, Response<Unit> response) {
                        Log.d("Response", "Response code: " + response.code() + " Response message: " + response.message());
                        if (response.isSuccessful()) {
                            Toast.makeText(ForgotCodeActivity.this,"Xác thực thành công",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotCodeActivity.this, ResetPasswordActivity.class);
                            intent.putExtra(KEY_EMAIL,getIntent().getStringExtra(KEY_EMAIL));
                            intent.putExtra(KEY_CODE,verificationCode);
                            startActivity(intent);
                            finish();
                        } else {
                            // Handle verification failure
                            Toast.makeText(ForgotCodeActivity.this,"Sai mã .Xác thực thất bại",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Unit> call, Throwable t) {
                        Toast.makeText(ForgotCodeActivity.this,"error call api",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotCodeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountService apiService = ApiClient.getClient(false).create(AccountService.class);
                apiService.resendEmail(getIntent().getStringExtra(KEY_EMAIL)).enqueue(new Callback<Unit>() {
                    @Override
                    public void onResponse(Call<Unit> call, Response<Unit> response) {
                        if (response.isSuccessful()){
                            new CountDownTimer(300000, 1000) { // 300000 milliseconds = 5 minutes
                                public void onTick(long millisUntilFinished) {
                                    long minutes = millisUntilFinished / 60000;
                                    long seconds = (millisUntilFinished % 60000) / 1000;
                                    String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                                    tvCountdown.setText(timeLeftFormatted);
                                }

                                public void onFinish() {
                                    tvCountdown.setText("Time's up!");
                                    // Perform any action you want after the timer finishes
                                    btnResend.setVisibility(View.VISIBLE);
                                    btnVerity.setVisibility(View.GONE);
                                }
                            }.start();
                            btnResend.setVisibility(View.GONE);
                            btnVerity.setVisibility(View.VISIBLE);
                        }else {
                            Toast.makeText(ForgotCodeActivity.this,"error",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Unit> call, Throwable t) {
                        Toast.makeText(ForgotCodeActivity.this,"error call api",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void addWidgets() {
        tvCode1 = findViewById(R.id.tv_code1);
        tvCode2 = findViewById(R.id.tv_code2);
        tvCode3 = findViewById(R.id.tv_code3);
        tvCode4 = findViewById(R.id.tv_code4);
        tvCode5 = findViewById(R.id.tv_code5);
        tvCode6 = findViewById(R.id.tv_code6);
        tvEmail = findViewById(R.id.tv_email);
        btnVerity = findViewById(R.id.btn_verity);
        btnSignUp = findViewById(R.id.tv_sign_up);
        tvEmail.setText(getIntent().getStringExtra(KEY_EMAIL));
        btnVerity.setEnabled(false);
        btnVerity.setAlpha(0.5f);
        tvCountdown = findViewById(R.id.tv_countdown);
        btnResend = findViewById(R.id.btn_resend);

        new CountDownTimer(300000, 1000) { // 300000 milliseconds = 5 minutes

            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                tvCountdown.setText(timeLeftFormatted);
            }

            public void onFinish() {
                tvCountdown.setText("Time's up!");
                // Perform any action you want after the timer finishes
                btnResend.setVisibility(View.VISIBLE);
                btnVerity.setVisibility(View.GONE);
            }
        }.start();
    }

    private void setupTextWatchers() {
        EditText[] editTexts = {tvCode1, tvCode2, tvCode3, tvCode4, tvCode5, tvCode6};

        // Đặt InputFilter để chỉ cho phép nhập 1 ký tự
        for (EditText editText : editTexts) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        }

        for (int i = 0; i < editTexts.length; i++) {
            final int index = i;
            editTexts[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Nếu người dùng nhập ký tự và ô không phải cuối cùng, di chuyển focus tới ô tiếp theo
                    if (s.length() == 1 && index < editTexts.length - 1) {
                        editTexts[index + 1].requestFocus();
                    }

                    // Nếu người dùng xóa ký tự và ô không phải đầu tiên, di chuyển focus về ô trước đó
                    // Trường hợp đặc biệt: Nếu đang ở ô kế tiếp mà ô đó chưa nhập ký tự, focus chuyển về ô trước đó
                    if (s.length() == 0) {
                        if (index > 0) {
                            // Nếu ô trước đã có ký tự, cho phép xóa ở ô đó
                            if (editTexts[index - 1].getText().length() > 0) {
                                editTexts[index - 1].requestFocus();
                            } else {
                                // Nếu ô trước cũng trống, giữ focus lại ô hiện tại
                                editTexts[index].requestFocus();
                            }
                        }
                    }

                    updateButtonState(); // Cập nhật trạng thái của nút nếu cần
                }

                @Override
                public void afterTextChanged(Editable s) { }
            });
        }
    }




    private void updateButtonState() {
        boolean allFilled = !tvCode1.getText().toString().isEmpty() &&
                !tvCode2.getText().toString().isEmpty() &&
                !tvCode3.getText().toString().isEmpty() &&
                !tvCode4.getText().toString().isEmpty() &&
                !tvCode5.getText().toString().isEmpty() &&
                !tvCode6.getText().toString().isEmpty();

        if (allFilled) {
            btnVerity.setEnabled(true);
            btnVerity.setAlpha(1.0f); // Set alpha to 1 for full opacity
        } else {
            btnVerity.setEnabled(false);
            btnVerity.setAlpha(0.5f);
        }
    }
}