package com.example.menulearning.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menulearning.R;
import com.example.menulearning.entities.Result;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class ResultsListAdapter extends
        RecyclerView.Adapter<ResultsListAdapter.ResultsListViewHolder> {

    private Context context;
    private ArrayList<Result> results;
    private boolean sortAsc;

    public ResultsListAdapter(Context context, ArrayList<Result> results) {
        this.context = context;
        this.results = results;
        sortAsc = true;

        sortResults();
    }

    public void setSortAsc(boolean sortAsc) {
        this.sortAsc = sortAsc;
    }

    public boolean isSortAsc() {
        return sortAsc;
    }

    @NonNull
    @Override
    public ResultsListAdapter.ResultsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.results_list_item, parent, false);

        return new ResultsListAdapter.ResultsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsListAdapter.ResultsListViewHolder holder, int position) {
        Result result = results.get(position);

        String[] splittedResult = result.getResult().split("[/]+");
        int correctAnswers = Integer.parseInt(splittedResult[0]);
        int answersAmount = Integer.parseInt(splittedResult[1]);
        int percentage = correctAnswers * 100 / answersAmount;

        int colorId = percentage < 50? R.color.bad_result : percentage < 80? R.color.ok_result :
                R.color.good_result;
        holder.resultHeaderText.setBackgroundColor(context.getColor(colorId));
        holder.resultHeaderText.setText(result.getResult());
        holder.nameText.setText(result.getName());

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ROOT);
        holder.dateText.setText(format.format(new Date((long) result.getDate())));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void sortResults() {
        if (sortAsc) {
            results.sort(Comparator.comparingLong(Result::getDate).reversed());
        }
        else {
            results.sort(Comparator.comparingLong(Result::getDate));
        }
        notifyDataSetChanged();
    }

    public static class ResultsListViewHolder extends RecyclerView.ViewHolder {
        private TextView resultHeaderText;
        private TextView dateText;
        private TextView nameText;

        public ResultsListViewHolder(@NonNull View itemView) {
            super(itemView);

            resultHeaderText = itemView.findViewById(R.id.resultHeaderText);
            dateText = itemView.findViewById(R.id.dateText);
            nameText = itemView.findViewById(R.id.nameText);
        }
    }
}
