package com.example.menulearning.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menulearning.R;
import com.example.menulearning.entities.Answer;
import com.example.menulearning.fragments.BaseFragment;
import com.example.menulearning.fragments.IUpdatable;

import java.util.ArrayList;
import java.util.List;

public class AnswersListAdapter extends
        RecyclerView.Adapter<AnswersListAdapter.AnswersListViewHolder> {

    private ArrayList<Answer> answers;
    private List<IUpdatable> observers;
    private boolean isClickable;

    public AnswersListAdapter(ArrayList<Answer> answers, List<IUpdatable> observers) {
        this.answers = answers;
        this.observers = observers;
        isClickable = true;
        notifyDataSetChanged();
    }

    @NonNull
    public AnswersListAdapter.AnswersListViewHolder
        onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.answers_list_item, parent, false);

        return new AnswersListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersListViewHolder holder, int position) {
        Answer answer = answers.get(position);

        holder.answerText.setText(answer.getText());
        holder.answerText.setOnClickListener(view -> {
            if (isClickable) {
                observers.forEach(observer -> observer.updateView(answer, holder.answerText));
            }
        });
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers.clear();
        this.answers.addAll(answers);

        notifyDataSetChanged();
    }

    public void setClickable(boolean isClickable) {
        this.isClickable = isClickable;
    }

    static class AnswersListViewHolder extends RecyclerView.ViewHolder {
        private TextView answerText;

        public AnswersListViewHolder(@NonNull View itemView) {
            super(itemView);

            answerText = itemView.findViewById(R.id.answerText);
        }
    }
}
