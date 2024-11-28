package com.example.courseproject.Activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.CustomViewPager.CourseDetailAdapter;
import com.example.courseproject.CustomViewPager.LessonDetailAdapter;
import com.example.courseproject.Model.Lesson;
import com.example.courseproject.Model.MyWebChromeClient;
import com.example.courseproject.Model.Progress;
import com.example.courseproject.R;
import com.example.courseproject.Service.ProgressService;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonDetailActivity extends AppCompatActivity {
    ImageButton img_btn_back;
    TextView tvTitle;
    //WebView webView;
    SimpleExoPlayer player;
    PlayerView playerView;
    Lesson lesson;
    private FrameLayout videoContainer;
    //
    ImageButton btnFullscreen;
    private boolean isFullScreen = false;
    int enrollmentId ;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ConstraintLayout mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lesson_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //
        mainLayout = findViewById(R.id.main);
        videoContainer = findViewById(R.id.video_container);
        img_btn_back = findViewById(R.id.img_lesson_detalis_back);
        tvTitle = findViewById(R.id.tv_title_lesson_details);
        //webView = findViewById(R.id.video_lesson);
        btnFullscreen = findViewById(R.id.btn_fullscreen);
        //
        tabLayout = findViewById(R.id.tab_layout_lesson_details);
        viewPager2 = findViewById(R.id.view_pager_lesson_details);

        //
        img_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //
        btnFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                int orientation =LessonDetailActivity.this.getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    //fullscreenButton.setImageDrawable(ContextCompat.getDrawable(context,
                            //R.drawable.fullscreen_exit));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    //fullscreenButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fullscreen));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


                }*/
                toggleFullscreen();

            }
        });
        //
        lesson = (Lesson) getIntent().getSerializableExtra("object_lesson");
        enrollmentId = getIntent().getIntExtra("enrollmentId",0);
        if(lesson!=null){
            tvTitle.setText(lesson.getTitle());

            /*
            // Thiết lập WebView
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true); // Bật JavaScript
            webView.setWebChromeClient(new MyWebChromeClient(LessonDetailActivity.this));
            String videoUrl = lesson.getVideoUrl();
            // Thay VIDEO_ID bằng ID video của bạn
            String videoId = getYouTubeVideoId(videoUrl);
            Log.d("Video ID", "Video ID: " + videoId);
            Log.d("Video ID", "Viedo URL: " + lesson.getVideoUrl());
            String embedUrl = "https://www.youtube.com/embed/" + videoId +"?enablejsapi=1";;

            // Tải URL nhúng
            webView.loadUrl(embedUrl);*/

            //
            playerView = findViewById(R.id.video_player);
            player = new SimpleExoPlayer.Builder(this).build();
            playerView.setPlayer(player);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            if (lesson.getVideoUrl() != null && !lesson.getVideoUrl().isEmpty()) {
                // Use videoUrl from lesson object
                MediaItem mediaItem = MediaItem.fromUri(lesson.getVideoUrl());
                player.setMediaItem(mediaItem);
            } else {
                // Fallback to Cloudinary link
                String cloudinaryVideo = "https://res.cloudinary.com/depram2im/video/upload/v1729511925/T%E1%BA%ADp_6___Huy%E1%BB%81n_V%C5%A9_T%E1%BB%A9_T%C6%B0%E1%BB%A3ng_-_Th%E1%BA%BF_Gi%E1%BB%9Bi_S%C6%A1n_H%E1%BA%A3i___Tu_Ti%C3%AAn_Review_-_Tu_Ti%C3%AAn_Review_360p_h264_youtube_is8vdg.mp4";
                MediaItem mediaItem = MediaItem.fromUri(cloudinaryVideo);
                player.setMediaItem(mediaItem);
            }
            player.prepare();
            player.play();
            // Thêm Listener cho player
            player.addListener(new Player.Listener() {
                @Override
                public void onPlaybackStateChanged(int playbackState) {
                    switch (playbackState) {
                        case Player.STATE_READY:
                            Log.d("ExoPlayer", "Video started");
                            break;
                        case Player.STATE_ENDED:
                            Log.d("ExoPlayer", "Video completed");
                            callApiProgress();
                            break;
                        case Player.STATE_BUFFERING:
                            Log.d("ExoPlayer", "Video buffering");
                            break;
                        // Không cần kiểm tra STATE_PAUSED, sử dụng playWhenReady
                        default:
                            break;
                    }
                }
            });
        }

        //
        LessonDetailAdapter adapter = new LessonDetailAdapter(this);
        adapter.setLesson(lesson);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position)->{
            switch (position){
                case 0:
                    tab.setText("Nội dung");
                    break;
                case 1:
                    tab.setText("Script");
                    break;
            }
        }
        ).attach();

    }

    // Toggle fullscreen mode
    private void toggleFullscreen() {
        if (isFullScreen) {
            // Exit fullscreen: change orientation back to portrait
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            restoreLayoutForPortrait();
        } else {
            // Enter fullscreen: change orientation to landscape
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            enterFullScreen();
        }
        isFullScreen = !isFullScreen;
    }

    // Enter fullscreen mode: change layout params to match parent
    private void enterFullScreen() {
        // Hide system UI like status bar and navigation bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide toolbar or action bar
        //getSupportActionBar().hide();

        // Adjust the player container layout
        ViewGroup.LayoutParams params = videoContainer.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        videoContainer.setLayoutParams(params);
    }

    // Restore layout when exiting fullscreen
    private void restoreLayoutForPortrait() {
        // Show the system UI back (status bar, navigation bar)
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Show toolbar or action bar
        //getSupportActionBar().show();

        // Adjust the player container layout for portrait mode
        ViewGroup.LayoutParams params = videoContainer.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = getResources().getDimensionPixelSize(R.dimen.video_player_height); // Original height (e.g., 300dp)
        videoContainer.setLayoutParams(params);
    }


    private void callApiProgress() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS");
        String formattedDate = now.format(formatter);
        Log.d("data",lesson.getLessonId()+"");
        Log.d("data",enrollmentId+"");
        Progress progress = new Progress(0,enrollmentId,lesson.getLessonId(),true,100,formattedDate);
        ProgressService apiService = ApiClient.getClient(false).create(ProgressService.class);
        apiService.createProgress(progress).enqueue(new Callback<Unit>() {
            @Override
            public void onResponse(Call<Unit> call, Response<Unit> response) {
                Log.d("API response",response.message());
                if(response.isSuccessful()){
                    Log.d("Success","Success");
                }
            }

            @Override
            public void onFailure(Call<Unit> call, Throwable t) {
                Log.d("Success","Error call api");
            }
        });
    }







    @Override
    protected void onStart() {
        super.onStart();
        if (player != null) {
            player.setPlayWhenReady(true); // Tiếp tục phát khi Activity được khôi phục
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.setPlayWhenReady(false); // Dừng phát khi Activity bị ngừng
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
        if (webView != null) {
            webView.destroy();
        }*/
        if (player != null) {
            player.release(); // Giải phóng tài nguyên của ExoPlayer
        }
    }


}