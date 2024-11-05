package com.example.courseproject.CustomViewPager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.courseproject.Activity.ui.coursedetail.ui.IntroductionFragment;
import com.example.courseproject.Activity.ui.coursedetail.ui.LessonFragment;
import com.example.courseproject.Activity.ui.coursedetail.ui.QuizFragment;
import com.example.courseproject.Model.Course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CourseDetailAdapter extends FragmentStateAdapter {
    private Course course;
    private boolean isEnroll;
    private HashMap<Integer, Fragment> fragmentMap = new HashMap<>();
    public void setEnroll(boolean enroll) {
        isEnroll = enroll;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    public CourseDetailAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        IntroductionFragment introductionFragment = new IntroductionFragment();
        switch (position){
            case 0:
                Bundle bundle1 = new Bundle();
                bundle1.putString("courseDescription",course.getDescription());
                introductionFragment.setArguments(bundle1);
                fragmentMap.put(position, introductionFragment);
                return introductionFragment;
            case 1:
                LessonFragment lessonFragment = new LessonFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("courseId",course.getCourseId());
                bundle.putBoolean("isEnroll",isEnroll);
                lessonFragment.setArguments(bundle);
                fragmentMap.put(position, lessonFragment);
                return lessonFragment;
            case 2:
                QuizFragment quizFragment = new QuizFragment();
                Bundle bundle3 = new Bundle();
                bundle3.putBoolean("isEnroll",isEnroll);
                bundle3.putInt("courseId",course.getCourseId());
                quizFragment.setArguments(bundle3);
                fragmentMap.put(position, quizFragment);
                return quizFragment;
            default:
                return introductionFragment;

        }


    }
    public Fragment getFragment(int position) {
        return fragmentMap.get(position);
    }
    @Override
    public int getItemCount() {
        return 3;
    }
}
