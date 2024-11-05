package com.example.courseproject.Activity;

import static com.example.courseproject.Activity.ForgotCodeActivity.KEY_EMAIL;

import android.content.Intent;
import android.os.Bundle;
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

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText editPassword,editConfirmPassword;
    private TextView tvError,tvErrorConfirmPassword;
    private TextView tvSignIn;
    private Button btnReset;
    static final String KEY_EMAIL = "email";
    static final String KEY_CODE = "code";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editPassword = findViewById(R.id.edit_password);
        editConfirmPassword = findViewById(R.id.edit_confirm_password);
        btnReset = findViewById(R.id.btn_reset_password);
        tvSignIn = findViewById(R.id.tv_sign_in);
        addAction();
    }

    private void addAction() {
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApiResetPassword();
            }
        });
    }

    private void callApiResetPassword() {
        String newPassword = editPassword.getText().toString();
        AccountService apiService = ApiClient.getClient(false).create(AccountService.class);
        apiService.resetPassword(getIntent().getStringExtra(KEY_EMAIL),newPassword,getIntent().getStringExtra(KEY_CODE)).enqueue(new Callback<Unit>() {
            @Override
            public void onResponse(Call<Unit> call, Response<Unit> response) {

                Log.d("Response", "Response code:"+getIntent().getStringExtra(KEY_EMAIL)+ "new pass:"+newPassword);
                Log.d("Response", "Response code: " + response.code() + " Response message: " + response.message());
                if(response.isSuccessful()){
                    Toast.makeText(ResetPasswordActivity.this,"Mật khẩu đã được thay đổi",Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(ResetPasswordActivity.this,TrangChuActitvity.class);
                    //startActivity(intent);
                }else {
                    Toast.makeText(ResetPasswordActivity.this,"Error message",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Unit> call, Throwable t) {
                Toast.makeText(ResetPasswordActivity.this,"Error call api",Toast.LENGTH_SHORT).show();
            }
        });
    }

}