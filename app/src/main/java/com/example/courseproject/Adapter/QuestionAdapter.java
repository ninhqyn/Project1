package com.example.courseproject.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.Api.ApiClient;
import com.example.courseproject.Interface.IClickAnswerListener;
import com.example.courseproject.Model.Answer;
import com.example.courseproject.Model.Question;
import com.example.courseproject.R;
import com.example.courseproject.Service.AnswerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private Context context;
    private List<Question> questionList;
    private List<Integer> selectedPositions;
    private HashMap<Question,Answer> answerHashMap;
    public QuestionAdapter(Context context) {
        this.selectedPositions = new ArrayList<>();
        this.answerHashMap = new HashMap<>();
        this.context = context;
    }
    public List<Integer> getSelectedPositions() {
        return selectedPositions;
    }

    public HashMap<Question, Answer> getAnswerHashMap() {
        return answerHashMap;
    }

    public void setData(List<Question> questionList){
        this.questionList = questionList;

        this.selectedPositions.clear(); // Đặt lại danh sách khi dữ liệu mới
        for (int i = 0; i < questionList.size(); i++) {
            selectedPositions.add(-1); // Mặc định không có đáp án nào được chọn
        }
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.viewholder_question,parent,false);

        return new QuestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        if (question == null){
            return;
        }

        holder.tvQuestion.setText(position+1+": "+question.getContent());
        holder.tvPoint.setText(question.getPoints()+" point");

            holder.answerAdapter = new AnswerAdapter(context, new IClickAnswerListener() {
                @Override
                public void onClickItemAnswer(Answer answer) {
                    // Lấy position hiện tại khi click
                    int currentPosition = holder.getAdapterPosition();
                    Question question = questionList.get(currentPosition);
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        // Cập nhật danh sách selectedPositions
                        int selectedAnswerPosition = holder.answerAdapter.getSelectedPosition();
                        selectedPositions.set(currentPosition, selectedAnswerPosition);
                        answerHashMap.put(question,answer);

                        // Chỉ thông báo cho adapter của câu hỏi

                    }
                }
            });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        holder.rcvAnswer.setLayoutManager(linearLayoutManager);
        holder.rcvAnswer.setAdapter(holder.answerAdapter);
        if (holder.answerAdapter.getItemCount() == 0) {
            callApiGetAnswerByQuestionId(question.getQuestionId(), holder.answerAdapter, holder.loadingAnswer);
        }
        holder.answerAdapter.setSelectedPosition(selectedPositions.get(position));

    }

    private void callApiGetAnswerByQuestionId(int questionId,AnswerAdapter answerAdapter,ProgressBar loadingAnswer) {
        AnswerService apiService = ApiClient.getClient(true).create(AnswerService.class);

        apiService.getAllAnswerByQuestionId(questionId).enqueue(new Callback<List<Answer>>() {
            @Override
            public void onResponse(Call<List<Answer>> call, Response<List<Answer>> response) {
                loadingAnswer.setVisibility(View.VISIBLE);
                if(response.isSuccessful() && response.body()!=null){
                    List<Answer> answerList = response.body();
                    answerAdapter.setData(answerList);

                }
                loadingAnswer.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<Answer>> call, Throwable t) {
                loadingAnswer.setVisibility(View.INVISIBLE); // Đảm bảo loading bị ẩn
                Toast.makeText(context, "Không thể tải câu trả lời. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (questionList!=null){
            return questionList.size();
        }
        return 0;
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        private TextView tvQuestion;
        private TextView tvPoint;
        private RecyclerView rcvAnswer;
        private ProgressBar loadingAnswer;
        public AnswerAdapter answerAdapter;
        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.questionTxt);
            tvPoint = itemView.findViewById(R.id.tvPoint);
            rcvAnswer = itemView.findViewById(R.id.recycler_view_answer);
            loadingAnswer = itemView.findViewById(R.id.loading_answer);
        }
    }
}
