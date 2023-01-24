package com.example.menulearning.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menulearning.R;
import com.example.menulearning.activities.MainActivity;
import com.example.menulearning.adapters.ResultsListAdapter;
import com.example.menulearning.constants.Routes;
import com.example.menulearning.entities.BaseEntity;
import com.example.menulearning.managers.DbManager;

public class ResultsFragment extends BaseFragment {
    private DbManager dbManager;

    public ResultsFragment(Routes fragmentViewId) {
        this.fragmentViewId = fragmentViewId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_results, container, false);

        MainActivity parentActivity = (MainActivity) getActivity();
        dbManager = DbManager.getInstance(getContext(), parentActivity.getPrefs());

        RecyclerView resultsList = fragmentView.findViewById(R.id.resultsList);
        resultsList.setHasFixedSize(true);
        resultsList.setLayoutManager(new LinearLayoutManager(getContext()));
        resultsList.setAdapter(new ResultsListAdapter(getContext(), dbManager.getResults()));

        TextView noResultsText = fragmentView.findViewById(R.id.noResultsText);
        ImageButton sortButton = fragmentView.findViewById(R.id.sortButton);

        if (resultsList.getAdapter().getItemCount() != 0) {
            sortButton.setVisibility(View.VISIBLE);
            noResultsText.setVisibility(View.INVISIBLE);
            resultsList.setVisibility(View.VISIBLE);

            sortButton.setOnClickListener(view -> {
                ResultsListAdapter adapter = (ResultsListAdapter) resultsList.getAdapter();
                adapter.setSortAsc(!adapter.isSortAsc());
                adapter.sortResults();

                sortButton.setImageResource(adapter.isSortAsc()? R.drawable.ic_sort_asc :
                        R.drawable.ic_sort_desc);
            });
        }
        else {
            sortButton.setVisibility(View.INVISIBLE);
            resultsList.setVisibility(View.INVISIBLE);
            noResultsText.setVisibility(View.VISIBLE);
        }

        return fragmentView;
    }

}
