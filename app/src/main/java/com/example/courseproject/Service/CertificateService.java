package com.example.courseproject.Service;

import com.example.courseproject.Model.Certificate;
import com.example.courseproject.Model.CertificateTemplate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CertificateService {
    @GET("Certification/user/{userId}")
    Call<List<Certificate>> getAllCertificateByUserId(@Path("userId") int userId);
    @GET("CertificationTemplate/{templateId}")
    Call<CertificateTemplate> getCertificateTemplateById(@Path("templateId") int templateId);
}