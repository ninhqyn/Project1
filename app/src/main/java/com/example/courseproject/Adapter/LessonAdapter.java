package com.example.courseproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.Interface.IClickItemLessonListener;
import com.example.courseproject.Model.Lesson;
import com.example.courseproject.R;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {
    private Context context;
    private List<Lesson> lessonList;
    private IClickItemLessonListener clickItemLessonListener;

    public LessonAdapter(Context context, IClickItemLessonListener clickItemLessonListener) {
        this.context = context;
        this.clickItemLessonListener = clickItemLessonListener;
    }
    public void setData(List<Lesson> list){
        this.lessonList = list;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_lesson, parent, false);
        return new LessonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessonList.get(position);
        if(lesson == null){
            return;
        }
        holder.tvTitle.setText(lesson.getTitle());
        holder.cardItemLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickItemLessonListener.onClickItemLesson(lesson);
            }
        });
        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickItemLessonListener.onClickItemLesson(lesson);
            }
        });
        holder.btnImageCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickItemLessonListener.onClickItemLesson(lesson);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(lessonList!=null){
            return lessonList.size();
        }
        return 0;
    }

    public  class LessonViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private CardView cardItemLesson;
        private Button btnPlay;
        private ImageButton btnImageCourse;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title_lesson);
            cardItemLesson = itemView.findViewById(R.id.card_item_lesson);
            btnPlay = itemView.findViewById(R.id.play_video);
            btnImageCourse = itemView.findViewById(R.id.img);
        }
    }
}
