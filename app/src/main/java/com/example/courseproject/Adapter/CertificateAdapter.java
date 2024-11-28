package com.example.courseproject.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Interface.IClickCertificateListener;
import com.example.courseproject.Model.Certificate;
import com.example.courseproject.Model.CertificateTemplate;
import com.example.courseproject.Model.DateUtils;
import com.example.courseproject.R;
import com.example.courseproject.Service.CertificateService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CertificateAdapter extends RecyclerView.Adapter<CertificateAdapter.CertificateViewHolder> {
    private Context context;
    private List<Certificate> certificateList;
    private IClickCertificateListener iClickCertificateListener;
    public CertificateAdapter(Context context, IClickCertificateListener iClickCertificateListener) {
        this.context = context;
        this.iClickCertificateListener = iClickCertificateListener;
    }
    public void setData(List<Certificate> certificateList) {
        this.certificateList = certificateList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CertificateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_holder_certificate,parent,false);
        return new CertificateViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificateViewHolder holder, int position) {
        Certificate certificate = certificateList.get(position);
        if(certificate==null){
            return;
        }
        callApiGetCertificateTemplate(certificate,holder);
    }

    private void callApiGetCertificateTemplate(Certificate certificate, CertificateViewHolder holder) {
        CertificateService certificateService = ApiClient.getClient(true).create(CertificateService.class);
        Call<CertificateTemplate> call = certificateService.getCertificateTemplateEnrollmentId(certificate.getEnrollmentId());
        call.enqueue(new Callback<CertificateTemplate>() {
            @Override
            public void onResponse(Call<CertificateTemplate> call, Response<CertificateTemplate> response) {
                if(response.isSuccessful()){
                    CertificateTemplate certificateTemplate = response.body();
                    if(certificateTemplate==null){
                        return;
                    }
                    holder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iClickCertificateListener.onClickCertificate(certificate,certificateTemplate);
                        }});
                    holder.tvNameCertificate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iClickCertificateListener.onClickCertificate(certificate,certificateTemplate);
                        }
                    });
                    holder.imgCertificate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iClickCertificateListener.onClickCertificate(certificate,certificateTemplate);
                        }
                    });

                    holder.tvNameCertificate.setText(certificateTemplate.getTemplateName());
                    Glide.with(context).load(certificateTemplate.getImage()).into(holder.imgCertificate);
                    if(DateUtils.convertToDate(certificate.getIssuedAt())!=null){
                        holder.tvTime.setText( DateUtils.convertToDate(certificate.getIssuedAt()) +"");
                    }
                }else {
                    Log.d("CertificateAdapter",response.message());
                }
            }

            @Override
            public void onFailure(Call<CertificateTemplate> call, Throwable t) {
                Log.d("CertificateAdapter",t.getMessage());
            }
        });

    }

    @Override
    public int getItemCount() {
        if(certificateList!=null){
            return certificateList.size();
        }
        return 0;
    }

    public class CertificateViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout view;
        private ImageView imgCertificate;
        private TextView tvNameCertificate;
        private TextView tvTime;
        public CertificateViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCertificate = itemView.findViewById(R.id.img_certificate);
            tvNameCertificate = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            view = itemView.findViewById(R.id.constraint);
        }
    }
}
