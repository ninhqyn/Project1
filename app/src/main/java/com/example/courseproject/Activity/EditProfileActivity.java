package com.example.courseproject.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Model.UpLoadRequestModel;
import com.example.courseproject.Model.User;
import com.example.courseproject.Model.UserUpdateRequest;
import com.example.courseproject.R;
import com.example.courseproject.Service.AccountService;
import com.example.courseproject.Service.ImageUploadService;
import com.example.courseproject.Service.UserService;
import com.example.courseproject.SharedPerferences.DataLocalManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
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

public class EditProfileActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private  ImageButton btnBack;
    private ImageView imgAvatar;
    private  ImageView imgCamera;
    private TextView btnSave;
    private EditText edtFullName;
    private EditText edtAddress;
    private EditText edtPhone;

    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //anh xa
        btnBack = findViewById(R.id.imageButton);
        imgAvatar = findViewById(R.id.img_avatar_user);
        imgCamera = findViewById(R.id.img_camera);
        btnSave = findViewById(R.id.btn_save);
        edtFullName = findViewById(R.id.edit_full_name);
        edtAddress = findViewById(R.id.edit_address);
        edtPhone = findViewById(R.id.edit_phone);


        Glide.with(EditProfileActivity.this)
                .load(DataLocalManager.getImage())
                .placeholder(android.R.drawable.ic_delete)
                .error(R.drawable.img_avatar)
                .into(imgAvatar);

        //action
        btnBack.setOnClickListener(v -> {
            finish();
        });

        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               actionSelectImage();
            }
        });
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionSelectImage();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApiUpdateUser();
            }
        });



    }



    private void actionSelectImage() {
        String[] options = {"Chọn từ thư viện", "Chụp ảnh"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
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
                Toast.makeText(this,"permission denied",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedImageUri;
            if (requestCode == PICK_IMAGE) {
                selectedImageUri = data.getData();
                Log.d("Image URI", "Selected Image URI: " + selectedImageUri);
                if (selectedImageUri != null) {
                    uploadImage(selectedImageUri);
                } else {
                    Toast.makeText(this, "URI không hợp lệ", Toast.LENGTH_SHORT).show();
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



    private void uploadImage(Uri imageUri) {

        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            if (inputStream == null) {
                Toast.makeText(EditProfileActivity.this, "Không thể mở luồng ảnh", Toast.LENGTH_SHORT).show();
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
                        Glide.with(EditProfileActivity.this)
                                .load(upLoadRequestModel.getUrl())
                                .placeholder(android.R.drawable.ic_delete)
                                .error(R.drawable.img_avatar)
                                .into(imgAvatar);
                    }
                }

                @Override
                public void onFailure(Call<UpLoadRequestModel> call, Throwable t) {
                    Log.e("API response", t.getMessage());
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(EditProfileActivity.this, "Lỗi upload ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void callApiUpdateUser() {
        String fullName = edtFullName.getText().toString();
        String address = edtAddress.getText().toString();
        String phone = edtPhone.getText().toString();
        UserUpdateRequest user = new UserUpdateRequest(DataLocalManager.getUserId(), DataLocalManager.getImage(),fullName,phone,address);
        UserService apiService = ApiClient.getClient(false).create(UserService.class);
        apiService.updateUser(DataLocalManager.getUserId(),user).enqueue(new Callback<Unit>() {
            @Override
            public void onResponse(Call<Unit> call, Response<Unit> response) {
                if (response.isSuccessful()){
                    DataLocalManager.setFullName(fullName);
                    DataLocalManager.setLocation(address);
                    DataLocalManager.setPhoneNumber(phone);
                    Toast.makeText(EditProfileActivity.this,"Cập nhật thành công",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Unit> call, Throwable t) {
                Log.e("API response", t.getMessage());
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
        File file = new File(getExternalFilesDir(null), "temp_image.jpg");
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(file);
    }



}