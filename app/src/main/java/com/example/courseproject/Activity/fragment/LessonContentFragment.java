package com.example.courseproject.Activity.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.courseproject.R;

public class LessonContentFragment extends Fragment {

    private TextView tvContent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lesson_content, container, false);
        tvContent = v.findViewById(R.id.tv_content_lesson_details);
        String content = getArguments().getString("lessonContent");
        tvContent.setText(content);
        return v;
    }
}