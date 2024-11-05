package com.example.courseproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.courseproject.Activity.ui.coursedetail.CourseDetailActivity;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Model.Course;
import com.example.courseproject.Model.DateUtils;
import com.example.courseproject.Model.Instructor;
import com.example.courseproject.R;
import com.example.courseproject.Service.InstructorService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseByCategoryAdapter extends RecyclerView.Adapter<CourseByCategoryAdapter.CourseCategoryViewHolder> {
    private Context context;
    private List<Course> courseList;
    public CourseByCategoryAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<Course> list) {
        this.courseList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_holder_course_by_category, parent, false);
        return new CourseCategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseCategoryViewHolder holder, int position) {
        Course course = courseList.get(position);
        if(course == null){
            return;
        }
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

        if(DateUtils.convertToDate(course.getCreatedAt())!=null){
            holder.tvTime.setText( DateUtils.convertToDate(course.getCreatedAt()) +"");
        }else {
            holder.tvTime.setText("26/08/2024");
        }
        holder.tvNameCourse.setText(course.getTitle());

        holder.tvNameCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CourseDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_course",course);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
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
    }

    @Override
    public int getItemCount() {
        if(courseList != null){
            return courseList.size();
        }
        return 0;
    }

    public class CourseCategoryViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCourse;
        TextView tvNameCourse;
        ImageView imgInstructor;
        TextView tvNameInstructor;
        TextView tvTime;


        public CourseCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCourse = itemView.findViewById(R.id.img_course_learning);
            tvNameCourse = itemView.findViewById(R.id.tv_name_course_learning);
            imgInstructor = itemView.findViewById(R.id.img_view);
            tvNameInstructor = itemView.findViewById(R.id.tv_name_instructor_learning);
            tvTime = itemView.findViewById(R.id.txt_time);

        }
    }
}