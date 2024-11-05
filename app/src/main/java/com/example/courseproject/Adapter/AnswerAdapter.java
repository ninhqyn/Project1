package com.example.courseproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.Interface.IClickAnswerListener;
import com.example.courseproject.Model.Answer;
import com.example.courseproject.R;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    private Context context;
    private List<Answer> answerList;
    private IClickAnswerListener iClickAnswerListener;
    private int selectedPosition = -1;
    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public AnswerAdapter(Context context, IClickAnswerListener iClickAnswerListener) {
        this.iClickAnswerListener = iClickAnswerListener;
        this.context = context;
    }
    public void setData(List<Answer> answerList){
        this.answerList = answerList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.viewholder_answer,parent,false);

        return new AnswerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Answer answer = answerList.get(position);
        if(answer == null){
            return;
        }
        // Thay đổi màu sắc của đáp án đã chọn
        if (selectedPosition == position) {
            holder.tvAnswer.setBackgroundResource(R.drawable.tv_answer_selected);
        }else {
            holder.tvAnswer.setBackgroundResource(R.drawable.tv_answer);
        }

        holder.tvAnswer.setText(answer.getContent());
        holder.tvAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Update selected position
                    int oldPosition = selectedPosition;
                    selectedPosition = adapterPosition;
                    // Notify only the items that changed
                    notifyItemChanged(oldPosition); // Update old position
                    notifyItemChanged(selectedPosition); // Update new position
                    iClickAnswerListener.onClickItemAnswer(answer);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        if(answerList!=null){
            return answerList.size();
        }
        return 0;
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder {

        private TextView tvAnswer;
        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAnswer = itemView.findViewById(R.id.answerTxt);
        }
    }
}
