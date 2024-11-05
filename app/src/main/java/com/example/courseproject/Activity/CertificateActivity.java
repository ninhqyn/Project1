package com.example.courseproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.Adapter.CertificateAdapter;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Interface.IClickCertificateListener;
import com.example.courseproject.Model.Certificate;
import com.example.courseproject.Model.CertificateTemplate;
import com.example.courseproject.R;
import com.example.courseproject.Service.CertificateService;
import com.example.courseproject.SharedPerferences.DataLocalManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CertificateActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CertificateAdapter adapter;
    private ProgressBar progressBar;
    private ImageButton imageButton;
    private TextView tvNoCertificate;
    private ImageView imgNoCertificate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_certificate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        createWidgets();
        callApiGetCertificate();
    }

    private void callApiGetCertificate() {
        CertificateService certificateService = ApiClient.getClient(true).create(CertificateService.class);
        certificateService.getAllCertificateByUserId(DataLocalManager.getUserId()).enqueue(new Callback<List<Certificate>>() {
            @Override
            public void onResponse(Call<List<Certificate>> call, Response<List<Certificate>> response) {
                Log.d("CertificateActivity",DataLocalManager.getUserId()+"");
                Log.d("CertificateActivity",response.message()+"");
                if(response.isSuccessful() && response.body()!=null){
                    List<Certificate> certificateList = response.body();
                    adapter.setData(certificateList);
                    progressBar.setVisibility(View.GONE);
                    if(certificateList.isEmpty()){
                        tvNoCertificate.setVisibility(View.VISIBLE);
                        imgNoCertificate.setVisibility(View.VISIBLE);
                    }else {
                        tvNoCertificate.setVisibility(View.GONE);
                        imgNoCertificate.setVisibility(View.GONE);
                    }
                }else {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Certificate>> call, Throwable t) {
                Log.d("CertificateActivity",t.getMessage());
            }
        });

    }

    private void createWidgets() {
        recyclerView = findViewById(R.id.rcv_certificate);
        progressBar = findViewById(R.id.loading_certificate);
        tvNoCertificate = findViewById(R.id.tv_no_certificate);
        imgNoCertificate = findViewById(R.id.img_no_certificate);
        adapter = new CertificateAdapter(this, new IClickCertificateListener() {
            @Override
            public void onClickCertificate(Certificate certificate, CertificateTemplate template) {
                Intent intent = new Intent(CertificateActivity.this,CertificateDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("certificate",certificate);
                bundle.putSerializable("template",template);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        imageButton = findViewById(R.id.imageButton3);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}