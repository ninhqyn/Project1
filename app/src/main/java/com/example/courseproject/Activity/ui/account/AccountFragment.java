package com.example.courseproject.Activity.ui.account;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.courseproject.Activity.CertificateActivity;
import com.example.courseproject.Activity.ChangePasswordActivity;
import com.example.courseproject.Activity.EditProfileActivity;
import com.example.courseproject.Activity.LoginActivity;
import com.example.courseproject.Activity.PrivacyActivity;
import com.example.courseproject.Activity.QuizActivity;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Model.UpLoadRequestModel;
import com.example.courseproject.Model.UserUpdateRequest;
import com.example.courseproject.R;
import com.example.courseproject.Service.ImageUploadService;
import com.example.courseproject.Service.UserService;
import com.example.courseproject.SharedPerferences.DataLocalManager;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import kotlin.Unit;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    LinearLayout btnLogout,btnChangePassword;
    ShapeableImageView imgAvatar;
    TextView tvUserName,tvFullName,tvEmail,tvPhone,tvAddress;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private ImageButton btnCertificate,btnAgreement;
    private TextView tvEditProfile;
    ImageButton btnSetting;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        btnChangePassword = v.findViewById(R.id.btn_change_password);
        btnLogout = v.findViewById(R.id.btn_logout);
        imgAvatar = v.findViewById(R.id.img_avatar_user);
        tvUserName = v.findViewById(R.id.tv_username);
        btnCertificate = v.findViewById(R.id.btn_certificate);
        btnAgreement = v.findViewById(R.id.btn_user_agreement);
        tvEditProfile = v.findViewById(R.id.tv_edit_profile);
        //
        tvEmail = v.findViewById(R.id.tv_email);
        tvPhone = v.findViewById(R.id.tv_phone);
        tvAddress = v.findViewById(R.id.tv_location);
        //tvFullName = v.findViewById(R.id.tv_full_name);
        tvEmail.setText(DataLocalManager.getEmail());
        tvPhone.setText(DataLocalManager.getPhoneNumber());
        tvAddress.setText(DataLocalManager.getLocation());
        btnSetting = v.findViewById(R.id.btn_setting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Setting",Toast.LENGTH_LONG).show();
            }});


        //tvFullName.setText(DataLocalManager.getFullName());

        //
        Glide.with(getContext())
                .load(DataLocalManager.getImage())
                .placeholder(android.R.drawable.ic_delete)
                .error(R.drawable.img_avatar)
                .into(imgAvatar);
        tvUserName.setText(DataLocalManager.getFullName());
        tvEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        //action
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirm();

            }
        });
        // Inflate the layout for this fragment

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionSelectImage();
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        btnCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CertificateActivity.class);
                startActivity(intent);
            }
        });
        btnAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PrivacyActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        Glide.with(getContext())
                .load(DataLocalManager.getImage())
                .placeholder(android.R.drawable.ic_delete)
                .error(R.drawable.img_avatar)
                .into(imgAvatar);
        tvUserName.setText(DataLocalManager.getFullName());
        tvEmail.setText(DataLocalManager.getEmail());
        tvPhone.setText(DataLocalManager.getPhoneNumber());
        tvAddress.setText(DataLocalManager.getLocation());
    }
    private void dialogConfirm() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_logout);
        Window window = dialog.getWindow();

        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = 0.5f;
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setBackgroundDrawableResource(R.drawable.background_loading);
        }
        dialog.setCancelable(false);

        TextView tvConfirm,tvCancel;
        tvConfirm = dialog.findViewById(R.id.tv_submit);
        tvCancel = dialog.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();;
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataLocalManager.setUserLoggedIn(false);
                DataLocalManager.setEmail("");
                DataLocalManager.setUserName("");
                DataLocalManager.setToken("");
                DataLocalManager.setPhoneNumber("");
                DataLocalManager.setLocation("");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        dialog.show();
    }







    private void actionSelectImage() {
        String[] options = {"Chọn từ thư viện", "Chụp ảnh"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Chọn từ thư viện
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            } else if (which == 1) {
                // camera
                clickCheckPermission();
            }
        });
        builder.show();
    }

    private void clickCheckPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }else {
                Toast.makeText(getContext(),"permission denied",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void uploadImage(Uri imageUri) {

        try {
            ContentResolver contentResolver = getActivity().getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(imageUri);
            if (inputStream == null) {
                Toast.makeText(getContext(), "Không thể mở luồng ảnh", Toast.LENGTH_SHORT).show();
                return;
            }

            byte[] fileBytes = getBytes(inputStream); // Phương thức hỗ trợ để đọc byte từ InputStream

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), fileBytes);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", "image.jpg", requestFile);

            // ... (Phần còn lại của code uploadImage vẫn giữ nguyên)
            ImageUploadService service = ApiClient.getClient(true).create(ImageUploadService.class);
            Call<UpLoadRequestModel> call = service.uploadImage(body);
            call.enqueue(new Callback<UpLoadRequestModel>() {
                @Override
                public void onResponse(Call<UpLoadRequestModel> call, Response<UpLoadRequestModel> response) {
                    if (response.isSuccessful()){
                        UpLoadRequestModel upLoadRequestModel = response.body();
                        DataLocalManager.setImage(upLoadRequestModel.getUrl());
                        Glide.with(getContext())
                                .load(upLoadRequestModel.getUrl())
                                .placeholder(android.R.drawable.ic_delete)
                                .error(R.drawable.img_avatar)
                                .into(imgAvatar);
                        callApiUpdateUser();
                    }
                }

                @Override
                public void onFailure(Call<UpLoadRequestModel> call, Throwable t) {
                    Log.e("API response upload", t.getMessage());
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Lỗi upload ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri;
            if (requestCode == PICK_IMAGE) {
                selectedImageUri = data.getData();
                Log.d("Image URI", "Selected Image URI: " + selectedImageUri);
                if (selectedImageUri != null) {
                    uploadImage(selectedImageUri);
                } else {
                    Toast.makeText(getContext(), "URI không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null) {
                    selectedImageUri = getImageUriFromBitmap(imageBitmap);
                    uploadImage(selectedImageUri);
                }
            }
        }
    }
    private void callApiUpdateUser() {
        UserUpdateRequest user = new UserUpdateRequest(DataLocalManager.getUserId(), DataLocalManager.getImage(),DataLocalManager.getFullName(),DataLocalManager.getPhoneNumber(),DataLocalManager.getLocation());
        UserService apiService = ApiClient.getClient(false).create(UserService.class);
        apiService.updateUser(DataLocalManager.getUserId(),user).enqueue(new Callback<Unit>() {
            @Override
            public void onResponse(Call<Unit> call, Response<Unit> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getContext(),"Cập nhật thành công",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Unit> call, Throwable t) {
                Log.e("API response user", t.getMessage());
            }
        });

    }

    // Phương thức hỗ trợ để đọc byte từ InputStream
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        File file = new File(requireActivity().getExternalFilesDir(null), "temp_image.jpg");
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(file);
    }

}