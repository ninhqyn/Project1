package com.example.courseproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Model.Course;
import com.example.courseproject.Model.Instructor;
import com.example.courseproject.R;
import com.example.courseproject.Service.InstructorService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CourseCompletedAdapter extends RecyclerView.Adapter<CourseCompletedAdapter.CourseProgressViewHolder> {

    private Context context;
    private List<Course> courseList;

    public CourseCompletedAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<Course> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_course_completed,parent,false);

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
        public CourseProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCourse = itemView.findViewById(R.id.img_course_completed);
            tvNameInstructor = itemView.findViewById(R.id.tv_name_instructor_completed);
            tvNameCourse = itemView.findViewById(R.id.tv_name_course_completed);

        }
    }
}
