package com.example.courseproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Interface.IClickItemCourseListener;
import com.example.courseproject.Model.Course;
import com.example.courseproject.Model.DateUtils;
import com.example.courseproject.Model.Instructor;
import com.example.courseproject.R;
import com.example.courseproject.Service.InstructorService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseSearchAdapter extends  RecyclerView.Adapter<CourseSearchAdapter.HomeCourseViewHolder>{
    private Context context;
    private List<Course> courseList;

    private IClickItemCourseListener iClickItemCourseListener;

    public CourseSearchAdapter(Context context, IClickItemCourseListener iClickItemCourseListener) {
        this.context = context;
        this.iClickItemCourseListener = iClickItemCourseListener;
    }
    public void setData(List<Course> list){
        this.courseList = list;

    }

    @NonNull
    @Override
    public HomeCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_search_course, parent, false);

        return new HomeCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        if(course == null){
            return;
        }
        //
        InstructorService apiService = ApiClient.getClient(true).create(InstructorService.class);
        apiService.getInstructorById(course.getInstructorId()).enqueue(new Callback<Instructor>() {
            @Override
            public void onResponse(Call<Instructor> call, Response<Instructor> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Instructor instructor = response.body();
                    holder.textInstructorName.setText(instructor.getName());
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
        holder.textTitle.setText(course.getTitle());
        if(DateUtils.convertToDate(course.getCreatedAt())!=null){
            holder.textDate.setText( DateUtils.convertToDate(course.getCreatedAt()) +"");
        }else {
            holder.textDate.setText("25/09/2024");
        }



        holder.courseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemCourseListener.onClickItemCourse(course);
            }
        });



    }

    @Override
    public int getItemCount() {
        if(courseList!=null){
            return courseList.size();
        }
        return 0;
    }

    public  class  HomeCourseViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgCourse,imgInstructor;
        private TextView textTitle,textInstructorName,textDate;
        private RelativeLayout courseLayout;
        public HomeCourseViewHolder(@NonNull View itemView) {
            super(itemView);



            imgCourse = itemView.findViewById(R.id.img_course);
            imgInstructor = itemView.findViewById(R.id.img_instructor);
            textTitle = itemView.findViewById(R.id.txt_title);
            textInstructorName = itemView.findViewById(R.id.txt_name_instructor);
            textDate = itemView.findViewById(R.id.txt_time);

            courseLayout = itemView.findViewById(R.id.layout_item_course);
        }
    }
}
