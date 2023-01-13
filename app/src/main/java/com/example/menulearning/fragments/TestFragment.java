package com.example.menulearning.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menulearning.R;
import com.example.menulearning.adapters.AnswersListAdapter;
import com.example.menulearning.constants.PrefsValues;
import com.example.menulearning.entities.Answer;
import com.example.menulearning.entities.BaseEntity;
import com.example.menulearning.entities.Question;
import com.example.menulearning.entities.QuestionView;
import com.example.menulearning.managers.DbManager;
import com.example.menulearning.managers.PrefsManager;
import com.example.menulearning.managers.TestManager;

import java.util.ArrayList;
import java.util.Arrays;

public class TestFragment extends BaseFragment {
    private DbManager dbManager;
    private TestManager testManager;

    public TestFragment(int fragmentViewId) {
        this.fragmentViewId = fragmentViewId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_test, container, false);

        ((TextView) fragmentView.findViewById(R.id.resultText)).setVisibility(View.INVISIBLE);
        ((TextView) fragmentView.findViewById(R.id.resultFooterText)).setVisibility(View.INVISIBLE);
        ((ImageView) fragmentView.findViewById(R.id.appLogo)).setVisibility(View.INVISIBLE);

        RecyclerView answersList = fragmentView.findViewById(R.id.answersList);
        answersList.setHasFixedSize(true);
        answersList.setLayoutManager(new LinearLayoutManager(getContext()));
        answersList.setAdapter(new AnswersListAdapter(null, Arrays.asList(this)));

        dbManager = DbManager.getInstance(getContext());
        ArrayList<Question> questions = dbManager.getQuestionsWithAnswers(8);

        testManager = new TestManager(questions);
        updateView(null, null);

        return fragmentView;
    }

    @Override
    public void updateView(BaseEntity<Integer> relatedEntity, View relatedView) {
        if (relatedEntity != null && relatedView != null) {
            if (relatedEntity instanceof Answer) {
                int backgroundId = testManager.answerQuestion(relatedEntity.getKey())?
                        R.drawable.correct_answer_style : R.drawable.wrong_answer_style;
                ((TextView) relatedView).setBackground(AppCompatResources
                        .getDrawable(getContext(), backgroundId));

                ((Runnable) () -> {
                    try {
                        Thread.sleep(3000);
                    }
                    catch (InterruptedException ignored) {

                    }
                }).run();
            }
        }

        TextView questionNumber = fragmentView.findViewById(R.id.questionNumber);
        TextView questionText = fragmentView.findViewById(R.id.questionText);

        AnswersListAdapter adapter = (AnswersListAdapter) ((RecyclerView)
                fragmentView.findViewById(R.id.answersList)).getAdapter();

        QuestionView questionView = testManager.getQuestionView();
        if (questionView != null) {
            String newQuestionNumber = String.format("%s %d", getString(R.string.question_header),
                    questionView.getQuestionNumber());
            questionNumber.setText(newQuestionNumber);
            questionText.setText(questionView.getQuestion().getText());
            adapter.setAnswers(questionView.getQuestion().getAnswers());
        }
        else {
            PrefsManager<Boolean> prefsManager = new PrefsManager<>(
                    getContext(),
                    PrefsValues.PREFS_NAME.getStringValue()
            );
            prefsManager.putItem(PrefsValues.TEST_FLAG_KEY.getStringValue(), false);

            String result = testManager.makeStatistics();
            int percentage = testManager.getPercentage();
            int colorId = percentage < 50? R.color.bad_result : percentage < 80? R.color.ok_result :
                    R.color.good_result;
            TextView resultText = fragmentView.findViewById(R.id.resultText);
            resultText.setTextColor(getContext().getColor(colorId));
            resultText.setText(String.format("%s %s",
                    getString(R.string.result_header), result));

            resultText.setVisibility(View.VISIBLE);
            ((TextView) fragmentView.findViewById(R.id.resultFooterText)).setVisibility(View.VISIBLE);
            ((ImageView) fragmentView.findViewById(R.id.appLogo)).setVisibility(View.VISIBLE);
        }
    }
}
