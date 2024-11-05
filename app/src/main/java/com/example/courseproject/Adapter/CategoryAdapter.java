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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.courseproject.Activity.AllCourseByCategoryActivity;
import com.example.courseproject.Model.Category;
import com.example.courseproject.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHomeViewHolder> {

    private Context context;
    private List<Category> categoryList;
    private List<Integer> imgIcon = new ArrayList<>();
    private int i = 0;

    public CategoryAdapter(Context context) {
        this.context = context;
        imgIcon.add(R.drawable.online_learning);
        imgIcon.add(R.drawable.online_course);
        imgIcon.add(R.drawable.online_certificate);
        imgIcon.add(R.drawable.online_education);
        imgIcon.add(R.drawable.lesson);
        imgIcon.add(R.drawable.teacher);
        imgIcon.add(R.drawable.worldwide);
        imgIcon.add(R.drawable.digital_book);
        imgIcon.add(R.drawable.interactive_learning);

    }
    public void setData(List<Category> categoryList){
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_item_category_home,parent,false);

        return new CategoryHomeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHomeViewHolder holder, int position) {
        Category category = categoryList.get(position);
        if (category==null){
            return;
        }
        holder.tvNameCategory.setText(category.getName());
        Glide.with(holder.itemView.getContext())
                .load(category.getImage())
                .placeholder(android.R.drawable.ic_delete)
                .error(R.drawable.lesson)
                .into(holder.imgCategory);
        /*holder.imgCategory.setImageResource(imgIcon.get(i));
        if(i<8){
            i++;
        }else {
            i=0;
        }*/
        holder.layoutCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AllCourseByCategoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_category",category);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(categoryList!=null){
            return categoryList.size();
        }
        return 0;
    }

    public class CategoryHomeViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameCategory;
        private ImageView imgCategory;
        private CardView layoutCategory;
        public CategoryHomeViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameCategory = itemView.findViewById(R.id.tv_name_category);
            imgCategory = itemView.findViewById(R.id.img_category);
            layoutCategory = itemView.findViewById(R.id.layout_category);
        }
    }
}
