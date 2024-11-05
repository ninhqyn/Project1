package com.example.courseproject.Activity.ui.coursedetail.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.courseproject.R;

public class IntroductionFragment extends Fragment {



    TextView tv_description;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_introduction, container, false);
        tv_description = v.findViewById(R.id.tv_description);
        String description = getArguments().getString("courseDescription");
        tv_description.setText(description);
        return v;
    }
}