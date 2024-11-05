package com.example.courseproject.Activity.ui.favorite;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courseproject.Activity.ui.coursedetail.CourseDetailActivity;
import com.example.courseproject.Adapter.CertificateAdapter;
import com.example.courseproject.Adapter.CourseSearchAdapter;
import com.example.courseproject.Adapter.FavoriteAdapter;
import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Interface.IClickFavoriteListener;
import com.example.courseproject.Interface.IClickItemCourseListener;
import com.example.courseproject.Model.Course;
import com.example.courseproject.Model.FavoriteCourse;
import com.example.courseproject.R;
import com.example.courseproject.Service.CourseFavoriteService;
import com.example.courseproject.SharedPerferences.DataLocalManager;

import java.util.List;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends Fragment {
    private RecyclerView rcvFavorite;
    private ProgressBar loadingFavorite;
    private FavoriteAdapter adapter;
    private TextView tvNoFavorite;
    private ImageView imgFavorite;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favorite, container, false);
        rcvFavorite = v.findViewById(R.id.rcv_favorite);
        loadingFavorite = v.findViewById(R.id.loading_favorite);
        tvNoFavorite = v.findViewById(R.id.tv_no_favorite);
        imgFavorite = v.findViewById(R.id.img_favorite);
        adapter = new FavoriteAdapter(getContext(), new IClickFavoriteListener() {
            @Override
            public void onClickItemFavorite(Course course,int position) {
                CourseFavoriteService courseFavoriteService = ApiClient.getClient(true).create(CourseFavoriteService.class);
                courseFavoriteService.deleteFavoriteCourse(DataLocalManager.getUserId(), course.getCourseId()).enqueue(new Callback<Unit>() {
                    @Override
                    public void onResponse(Call<Unit> call, Response<Unit> response) {
                        if (response.isSuccessful()) {
                            adapter.removeItem(position);  // Create this method in your adapter
                            adapter.notifyItemRemoved(position);
                            if (adapter.getItemCount() == 0) {
                                tvNoFavorite.setVisibility(View.VISIBLE);
                                imgFavorite.setVisibility(View.VISIBLE);
                            } else {
                                tvNoFavorite.setVisibility(View.GONE);
                                imgFavorite.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Unit> call, Throwable t) {
                    }
                });
            }
        });
        rcvFavorite.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvFavorite.setLayoutManager(linearLayoutManager);
        callApiFavorite();
        adapter.notifyDataSetChanged();
        return v;
    }

    private void callApiFavorite() {
        CourseFavoriteService apiService = ApiClient.getClient(true).create(CourseFavoriteService.class);
        apiService.getFavoriteCourseByUserId(DataLocalManager.getUserId()).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    adapter.setData(response.body());
                    adapter.notifyDataSetChanged();
                    loadingFavorite.setVisibility(View.INVISIBLE);
                    if(response.body().isEmpty()){
                        tvNoFavorite.setVisibility(View.VISIBLE);
                        imgFavorite.setVisibility(View.VISIBLE);
                    }else {
                        tvNoFavorite.setVisibility(View.GONE);
                        imgFavorite.setVisibility(View.GONE);
                    }
                }else {

                    Log.d("response", "onResponse: "+response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                loadingFavorite.setVisibility(View.INVISIBLE);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        callApiFavorite();
    }
}