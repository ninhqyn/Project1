package com.example.courseproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.courseproject.Activity.ui.coursedetail.CourseDetailActivity;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Interface.IClickFavoriteListener;
import com.example.courseproject.Model.Course;
import com.example.courseproject.Model.Instructor;
import com.example.courseproject.R;
import com.example.courseproject.Service.InstructorService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.NewCourseViewHolder> {
    private Context context;
    private List<Course> courseList;
    private IClickFavoriteListener listener;
    public FavoriteAdapter(Context context, IClickFavoriteListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public void setData(List<Course> list) {
        this.courseList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_holder_favorite, parent, false);
        return new NewCourseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewCourseViewHolder holder, int position) {
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


        holder.tvNameCourse.setText(course.getTitle());
        String time = course.getCreatedAt();

        // Định dạng chuỗi thời gian
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS");
        Date date = null;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Tạo Calendar từ Date
        Calendar setDate = Calendar.getInstance();
        if(date!=null){
            setDate.setTime(date);
            // Lấy ngày hiện tại
            Calendar currentDate = Calendar.getInstance();

            // Tính khoảng cách
            long differenceMillis = currentDate.getTimeInMillis() - setDate.getTimeInMillis();
            long differenceSeconds = differenceMillis / 1000;
            long differenceMinutes = differenceSeconds / 60;
            long differenceHours = differenceMinutes / 60;
            long differenceDays = differenceHours / 24;
            long differenceMonths = differenceDays / 30;
            long differenceYears = differenceMonths / 12;

            String displayText;

            if (differenceYears > 0) {
                displayText = String.format("%d năm", differenceYears);
            } else if (differenceMonths > 0) {
                displayText = String.format("%d tháng", differenceMonths);
            } else if (differenceDays > 0) {
                displayText = String.format("%d ngày", differenceDays);
            } else if (differenceHours > 0) {
                displayText = String.format("%d giờ", differenceHours);
            } else if (differenceMinutes > 0) {
                displayText = String.format("%d phút", differenceMinutes);
            } else {
                displayText = "Vừa mới";
            }

            holder.tvTime.setText(displayText);
        }




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
        holder.imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                listener.onClickItemFavorite(course,position);
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
    public void removeItem(int position) {
        if (position >= 0 && position < courseList.size()) {
            courseList.remove(position);
            notifyItemRemoved(position);
        }
    }


    public class NewCourseViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCourse;
        TextView tvNameCourse;
        TextView tvTime;
        ImageView imgInstructor;
        TextView tvNameInstructor;
        ImageButton imgFavorite;

        public NewCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCourse = itemView.findViewById(R.id.img_course_learning);
            tvNameCourse = itemView.findViewById(R.id.tv_name_course_learning);
            imgInstructor = itemView.findViewById(R.id.img_view);
            tvNameInstructor = itemView.findViewById(R.id.tv_name_instructor_learning);
            tvTime = itemView.findViewById(R.id.tv_time);
            imgFavorite = itemView.findViewById(R.id.img_btn_favorite);
        }
    }
}