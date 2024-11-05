package com.example.courseproject.CustomViewPager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.courseproject.Activity.fragment.LessonContentFragment;
import com.example.courseproject.Activity.fragment.LessonScriptFragment;
import com.example.courseproject.Model.Lesson;

public class LessonDetailAdapter extends FragmentStateAdapter {
    private Lesson lesson;

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public LessonDetailAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        LessonContentFragment lessonContentFragment = new LessonContentFragment();
        switch (position){
            case 0:
                Bundle bundle = new Bundle();
                bundle.putString("lessonContent",lesson.getContent());
                lessonContentFragment.setArguments(bundle);
                return lessonContentFragment;
            case 1:
                return new LessonScriptFragment();

            default:
                return lessonContentFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
