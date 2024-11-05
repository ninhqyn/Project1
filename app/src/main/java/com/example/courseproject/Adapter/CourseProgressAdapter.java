package com.example.courseproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.courseproject.Activity.ui.coursedetail.CourseDetailActivity;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Model.CalculateProgress;
import com.example.courseproject.Model.Course;
import com.example.courseproject.Model.Enrollment;
import com.example.courseproject.Model.Instructor;
import com.example.courseproject.R;
import com.example.courseproject.Service.EnrollmentService;
import com.example.courseproject.Service.InstructorService;
import com.example.courseproject.Service.ProgressService;
import com.example.courseproject.SharedPerferences.DataLocalManager;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CourseProgressAdapter extends RecyclerView.Adapter<CourseProgressAdapter.CourseProgressViewHolder> {

    private Context context;
    private List<Course> courseList;

    public CourseProgressAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<Course> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_holder_course_progress,parent,false);

        return new CourseProgressViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseProgressViewHolder holder, int position) {
        Course course = courseList.get(position);
        if (course==null){
            return;
        }
        //
        InstructorService apiService = ApiClient.getClient(true).create(InstructorService.class);
        apiService.getInstructorById(course.getInstructorId()).enqueue(new Callback<Instructor>() {
            @Override
            public void onResponse(Call<Instructor> call, Response<Instructor> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Instructor instructor = response.body();
                    holder.tvNameInstructor.setText(instructor.getName());
                    Glide.with(holder.itemView.getContext())
                            .load(instructor.getImage())
                            .placeholder(android.R.drawable.ic_delete)
                            .error(R.drawable.dashbord_img)
                            .into(holder.imgInstructor);
                }
            }

            @Override
            public void onFailure(Call<Instructor> call, Throwable t) {

            }
        });
        //
        Glide.with(holder.itemView.getContext())
                .load(course.getImage())
                .placeholder(android.R.drawable.ic_delete)
                .error(R.drawable.dashbord_img)
                .into(holder.imgCourse);


        holder.tvNameCourse.setText(course.getTitle());
        //holder.tvProgress.setText(course.getProgress()+"%");

        holder.imgCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CourseDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_course",course);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        EnrollmentService apiEnrollmentService = ApiClient.getClient(true).create(EnrollmentService.class);
        apiEnrollmentService.getEnrollmentByUserIdAndCourseId(DataLocalManager.getUserId(),course.getCourseId()).enqueue(new Callback<Enrollment>() {
            @Override
            public void onResponse(Call<Enrollment> call, Response<Enrollment> response) {
                if (response.isSuccessful() && response.body()!=null){
                    Enrollment enrollment = response.body();
                    ProgressService apiProgressService = ApiClient.getClient(true).create(ProgressService.class);
                    apiProgressService.calculateProgress(enrollment.getEnrollmentId()).enqueue(new Callback<CalculateProgress>() {
                        @Override
                        public void onResponse(Call<CalculateProgress> call, Response<CalculateProgress> response) {
                            if(response.isSuccessful() && response.body()!=null){
                                CalculateProgress calculateProgress = response.body();
                                double progress = calculateProgress.getProgress();
                                if(progress == 0){
                                    holder.tvProgress.setText("0%");
                                }else {
                                    DecimalFormat df = new DecimalFormat("#.00");
                                    String formattedProgress = df.format(progress);
                                    holder.tvProgress.setText(formattedProgress+"%");
                                    DecimalFormat d = new DecimalFormat("#");
                                    String formattedProgress1 = d.format(progress);
                                    holder.indicator.setProgress(Integer.parseInt(formattedProgress1));
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<CalculateProgress> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Enrollment> call, Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount() {
        if (courseList!=null){
            return courseList.size();
        }
        return 0;
    }

    public class CourseProgressViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgCourse;
        private TextView tvNameInstructor;
        private TextView tvNameCourse;
        private ImageView imgInstructor;
        private TextView tvProgress;
        private LinearProgressIndicator indicator;
        public CourseProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCourse = itemView.findViewById(R.id.img_course_learning);
            tvNameInstructor = itemView.findViewById(R.id.tv_name_instructor_learning);
            tvNameCourse = itemView.findViewById(R.id.tv_name_course_learning);
            imgInstructor = itemView.findViewById(R.id.img_view);
            tvProgress = itemView.findViewById(R.id.tv_progress);
            indicator = itemView.findViewById(R.id.progress_circular);

        }
    }
}
