package com.example.courseproject.Adapter;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.courseproject.R;
import com.example.courseproject.Model.*;
import java.util.List;


public class HomeBannerAdapter extends RecyclerView.Adapter<HomeBannerAdapter.HomeBannerViewHolder> {

    private Context context;
    private List<Banner> bannerList;
    public HomeBannerAdapter(Context context) {

        this.context = context;
    }
    public  void setData(List<Banner> list){
        this.bannerList = list;
    }

    @NonNull
    @Override
    public HomeBannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_banner_home, parent, false);

        return new HomeBannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBannerViewHolder holder, int position) {

        Banner  banner = bannerList.get(position);
        if(banner == null){
            return;
        }

        Glide.with(holder.itemView.getContext())
                .load(banner.getImage())
                .placeholder(android.R.drawable.ic_delete)
                .error(R.drawable.img_avatar)
                .into(holder.imgBanner);
        holder.tvName.setText(banner.getTitle());
        holder.btnUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(banner.getButtonUrl()));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(bannerList != null){
            return bannerList.size();
        }else {
            return 0;
        }
    }

    public class HomeBannerViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgBanner;
        private TextView tvName;
        private TextView btnUrl;

        public HomeBannerViewHolder(@NonNull View itemView) {
            super(itemView);

            imgBanner = itemView.findViewById(R.id.img_banner);
            tvName = itemView.findViewById(R.id.tv_name);
            btnUrl = itemView.findViewById(R.id.btnUrl);

        }
    }

}
