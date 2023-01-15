package com.example.menulearning.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menulearning.R;
import com.example.menulearning.activities.MainActivity;
import com.example.menulearning.adapters.AnswersListAdapter;
import com.example.menulearning.constants.PrefsValues;
import com.example.menulearning.constants.Routes;
import com.example.menulearning.entities.Answer;
import com.example.menulearning.entities.BaseEntity;
import com.example.menulearning.entities.Question;
import com.example.menulearning.entities.QuestionView;
import com.example.menulearning.entities.Result;
import com.example.menulearning.managers.Buffer;
import com.example.menulearning.managers.DbManager;
import com.example.menulearning.managers.TestManager;

import java.util.ArrayList;
import java.util.Arrays;

public class TestFragment extends BaseFragment {
    private DbManager dbManager;
    private TestManager testManager;

    public TestFragment(Routes fragmentViewId) {
        this.fragmentViewId = fragmentViewId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_test, container, false);

        fragmentView.findViewById(R.id.resultText).setVisibility(View.INVISIBLE);
        fragmentView.findViewById(R.id.resultFooterText).setVisibility(View.INVISIBLE);
        fragmentView.findViewById(R.id.appLogo).setVisibility(View.INVISIBLE);

        RecyclerView answersList = fragmentView.findViewById(R.id.answersList);
        answersList.setHasFixedSize(true);
        answersList.setLayoutManager(new LinearLayoutManager(getContext()));
        answersList.setAdapter(new AnswersListAdapter(new ArrayList<>(), Arrays.asList(this)));

        if (Buffer.getTestManager() == null) {
            dbManager = DbManager.getInstance(getContext(),
                    ((MainActivity) getActivity()).getPrefs());
            dbManager.fillRepositories();
            ArrayList<Question> questions = dbManager.getQuestionsWithAnswers(8);
            testManager = new TestManager(questions);
        }
        else {
            testManager = Buffer.getTestManager();
        }

        updateView(null, null);

        return fragmentView;
    }

    @Override
    public void updateView(BaseEntity<Integer> relatedEntity, View relatedView) {
        if (relatedEntity != null && relatedView != null) {
            if (relatedEntity instanceof Answer) {
                int backgroundId = testManager.answerQuestion(relatedEntity.getKey())?
                        R.drawable.correct_answer_style : R.drawable.wrong_answer_style;
                relatedView.setBackground(AppCompatResources
                        .getDrawable(getContext(), backgroundId));
            }

            renderComponents();
            relatedView.setBackground(AppCompatResources
                    .getDrawable(getContext(), R.drawable.answer_block_style));
        }
        else {
            renderComponents();
        }
    }

    private void renderComponents() {
        TextView questionNumber = fragmentView.findViewById(R.id.questionNumber);
        TextView questionText = fragmentView.findViewById(R.id.questionText);

        QuestionView questionView = testManager.getQuestionView();
        if (questionView != null) {
            String newQuestionNumber = String.format("%s %d", getString(R.string.question_header),
                    questionView.getQuestionNumber() + 1);

            questionNumber.setText(newQuestionNumber);
            questionText.setText(questionView.getQuestion().getText());

            AnswersListAdapter adapter = (AnswersListAdapter) ((RecyclerView)
                    fragmentView.findViewById(R.id.answersList)).getAdapter();
            adapter.setAnswers(questionView.getQuestion().getAnswers());
        }
        else {
            SharedPreferences prefs= ((MainActivity) getActivity()).getPrefs();
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putBoolean(PrefsValues.TEST_FLAG_KEY.getStringValue(), false);
            prefsEditor.apply();

            String result = testManager.makeStatistics();
            int percentage = testManager.getPercentage();
            int colorId = percentage < 50? R.color.bad_result : percentage < 80? R.color.ok_result :
                    R.color.good_result;

            TextView resultText = fragmentView.findViewById(R.id.resultText);
            resultText.setTextColor(getContext().getColor(colorId));
            resultText.setText(String.format("%s %s", getString(R.string.result_header), result));

            questionNumber.setVisibility(View.INVISIBLE);
            questionText.setVisibility(View.INVISIBLE);
            fragmentView.findViewById(R.id.answersList).setVisibility(View.INVISIBLE);

            resultText.setVisibility(View.VISIBLE);
            fragmentView.findViewById(R.id.resultFooterText).setVisibility(View.VISIBLE);
            fragmentView.findViewById(R.id.appLogo).setVisibility(View.VISIBLE);

            String userName = prefs.getString(PrefsValues.USER_NAME.getStringValue(), "");

            dbManager.addResult(new Result(0, userName, result, System.currentTimeMillis()));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        String questionNumberString = ((TextView) fragmentView
                .findViewById(R.id.questionNumber)).getText().toString();
        int number = Integer.parseInt(questionNumberString.split("[\\s]+")[1]) - 2;

        testManager.setCurrentQuestion(number);
        Buffer.setTestManager(testManager);
    }
}
