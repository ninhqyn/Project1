package com.example.courseproject.ZaloPay;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Model.Course;
import com.example.courseproject.Model.Enrollment;
import com.example.courseproject.R;
import com.example.courseproject.Service.EnrollmentService;
import com.example.courseproject.SharedPerferences.DataLocalManager;
import com.example.courseproject.VnPay.VNPAYService;
import com.example.courseproject.ZaloPay.Api.CreateOrder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class OrderActivity extends AppCompatActivity {
    Button btnOrder;
    Button btnVnPay;
    TextView tvAmount;
    Course course;
    TextView tvNameCourse;
    VNPAYService vnpayService = new VNPAYService();
    private boolean ZaloPay = false;
    private boolean VnPay = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnVnPay = findViewById(R.id.btnVnpay);
        //get data from intent
        course = (Course) getIntent().getSerializableExtra("course");
        double price = course.getPrice();

        int priceAsInt = (int) price;

        String amount = String.valueOf(priceAsInt);
         Log.d("zalo pay",amount);
        //binding
        createWidgets();

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(554, Environment.SANDBOX);

        btnOrder = findViewById(R.id.button3);

        btnOrder.setOnClickListener(view -> {
            ZaloPay = true;
            CreateOrder orderApi = new CreateOrder();

            try {
                JSONObject data = orderApi.createOrder(amount);

                String code = data.getString("return_code");
                Log.d("zalo pay",code);
                if (code.equals("1")) {
                    String token = data.getString("zp_trans_token");
                    Log.d("zptranstoken", data.getString("zp_trans_token")+ "token"+ token);
                    ZaloPaySDK.getInstance().payOrder(OrderActivity.this, data.getString("zp_trans_token"), "demozpdk://app", new PayOrderListener() {
                            @Override
                            public void onPaymentSucceeded(String s, String s1, String s2) {
                               actionSuccess();
                            }

                            @Override
                            public void onPaymentCanceled(String s, String s1) {
                               actionCancel();
                            }

                            @Override
                            public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                              actionError();
                            }
                        });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        btnVnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VnPay = true;
                openSdk(Integer.parseInt(amount));
            }
        });
    }

    private void actionError() {
        Intent intent1 = new Intent(OrderActivity.this,ResultActivity.class);
        intent1.putExtra("result","error");
        //handle error
        startActivity(intent1);
    }

    private void actionCancel() {
        Intent intent1 = new Intent(OrderActivity.this,ResultActivity.class);
        intent1.putExtra("result","canceled");
        //handle canceled
        startActivity(intent1);
    }

    private void actionSuccess() {
        Intent intent1 = new Intent(OrderActivity.this,ResultActivity.class);
        intent1.putExtra("result","success");
        Enrollment a = new Enrollment(DataLocalManager.getUserId(),course.getCourseId(),"enrolled",true);
        EnrollmentService enrollmentService = ApiClient.getClient(true).create(EnrollmentService.class);
        enrollmentService.enrollToCourse(a).enqueue(new Callback<Unit>() {
            @Override
            public void onResponse(Call<Unit> call, Response<Unit> response) {
                if(response.isSuccessful()){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("course",course);
                    intent1.putExtras(bundle);
                    startActivity(intent1);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("result", "success");
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
                else{
                    Log.d("response",response.message());
                }
            }

            @Override
            public void onFailure(Call<Unit> call, Throwable t) {
                Log.d("Error call api",t.getMessage());
            }
        });

    }

    private void openSdk(int amount) {
        String url = vnpayService.createOrder(amount,course.getTitle(),"demozpdk://app/vnpay_return");
        Log.d("vnpay","url:"+url);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void createWidgets() {
        tvAmount = findViewById(R.id.tv_amount);
        tvAmount.setText(course.getPrice()+" VNĐ");
        tvNameCourse = findViewById(R.id.name_course);
        tvNameCourse.setText(course.getTitle());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(ZaloPay){
            ZaloPaySDK.getInstance().onResult(intent);

        }
       if(VnPay){
           Uri uri = intent.getData(); // Lấy URI từ Intent
           if (uri != null) {
               String responseCode = uri.getQueryParameter("vnp_ResponseCode");
               String transactionStatus = uri.getQueryParameter("vnp_TransactionStatus");
               if ("00".equals(responseCode) && "00".equals(transactionStatus)) {
                   Log.d("VNPAY", "Giao dịch thành công");
                   actionSuccess();

               } else if("24".equals(responseCode)){
                   actionCancel();
                   Log.d("VNPAY", "Giao dịch thất bại, mã lỗi: " + responseCode);
               }else {
                   actionError();
                   Log.d("VNPAY", "Giao dịch thất bại, mã lỗi: " + responseCode);


               }

           }
       }
    }



}