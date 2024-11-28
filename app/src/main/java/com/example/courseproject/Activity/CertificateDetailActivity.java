package com.example.courseproject.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.courseproject.Model.Certificate;
import com.example.courseproject.Model.CertificateTemplate;
import com.example.courseproject.Model.DateUtils;
import com.example.courseproject.R;

public class CertificateDetailActivity extends AppCompatActivity {
    TextView tvTitle,tvDate;
    ImageView imgCertificate;
    Button btnShare,btnDownload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_certificate_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvTitle = findViewById(R.id.textView6);
        tvDate = findViewById(R.id.textView7);
        imgCertificate = findViewById(R.id.textView3);
        btnShare = findViewById(R.id.button);
        btnDownload = findViewById(R.id.button2);
        Certificate certificate = (Certificate) getIntent().getSerializableExtra("certificate");
        CertificateTemplate template = (CertificateTemplate) getIntent().getSerializableExtra("template");
        tvTitle.setText(template.getTemplateName());
        String date = certificate.getIssuedAt();

        tvDate.setText("Ngày cấp : "+ DateUtils.convertToDate(date) );
        Glide.with(this).load(template.getImage()).into(imgCertificate);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CertificateDetailActivity.this, "Share", Toast.LENGTH_SHORT).show();
            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CertificateDetailActivity.this, "Download", Toast.LENGTH_SHORT).show();

            }
        });
    }
}