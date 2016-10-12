package com.ematos.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int MAX_TIME = 30;

    CountDownTimer countDownTimer;
    Button start;
    Button giveup;
    TextView currentScore;
    TextView finalScore;
    TextView operation;
    GridLayout choicesContainer;
    Button firstChoice;
    Button secondChoice;
    Button thirdChoice;
    Button fourthChoice;
    ArrayList<Button> choiceButtons;

    int userCorrect = 0;
    int userAnswers = 0;

    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.start);
        giveup = (Button) findViewById(R.id.giveup);
        currentScore = (TextView) findViewById(R.id.currentScore);
        finalScore = (TextView) findViewById(R.id.finalScore);
        operation = (TextView) findViewById(R.id.operation);
        choicesContainer = (GridLayout) findViewById(R.id.choicesContainer);
        firstChoice = (Button) findViewById(R.id.firstChoice);
        secondChoice = (Button) findViewById(R.id.secondChoice);
        thirdChoice = (Button) findViewById(R.id.thirdChoice);
        fourthChoice = (Button) findViewById(R.id.fourthChoice);

        choiceButtons = new ArrayList<Button>();
        choiceButtons.add(firstChoice);
        choiceButtons.add(secondChoice);
        choiceButtons.add(thirdChoice);
        choiceButtons.add(fourthChoice);

        updateTimer(MAX_TIME);
    }

    public void startGame(View view) {
        updateScore();
        start.setEnabled(false);
        giveup.setEnabled(true);
        choicesContainer.setVisibility(View.VISIBLE);
        finalScore.setVisibility(View.INVISIBLE);

        nextChallange();

        countDownTimer = new CountDownTimer(MAX_TIME * 1000 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimer((int) millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                updateTimer(0);
                timeUp();
            }
        };

        countDownTimer.start();
    }

    public void nextChallange() {
        int correctResult = generateOperation();
        generateChoices(correctResult);
    }

    public void giveUp(View view) {
        start.setEnabled(true);
        giveup.setEnabled(false);
        choicesContainer.setVisibility(View.INVISIBLE);
        countDownTimer.cancel();
        userCorrect = 0;
        userAnswers = 0;
        updateScore();
        updateTimer(MAX_TIME);
    }

    public void timeUp() {
        start.setEnabled(true);
        giveup.setEnabled(false);
        choicesContainer.setVisibility(View.INVISIBLE);
        finalScore.setVisibility(View.VISIBLE);
        finalScore.setText(String.format("%d/%d", userCorrect, userAnswers));
        userAnswers = 0;
        userCorrect = 0;
    }

    public void choose(View view) {
        Button btn = (Button) view;

        if(btn.getTag().toString().equals("1")) {
            userCorrect++;
        }
        userAnswers++;

        updateScore();
        nextChallange();
    }

    private void updateTimer(int seconds) {
        TextView timer = (TextView) findViewById(R.id.timer);
        timer.setText(String.format(":%02d", seconds));
    }

    public void updateScore() {
        currentScore.setText(String.format("%d/%d", userCorrect, userAnswers));
    }

    public int generateOperation() {
        int firstNumber = random.nextInt(10) + 1;
        int secondNumber = random.nextInt(10) + 1;

        operation.setText(String.format("%d + %d", firstNumber, secondNumber));

        return firstNumber + secondNumber;
    }

    public void generateChoices(int correctGuess) {
        HashSet<Integer> wrongGuesses = new HashSet<Integer>();
        int minValue = 2;
        int maxValue = 20;

        while(wrongGuesses.size() < 3) {
            int wrongAnswerCandidate = random.nextInt(maxValue + 1) + minValue;
            if (wrongAnswerCandidate != correctGuess) {
                wrongGuesses.add(wrongAnswerCandidate);
            }
        }

        Collections.shuffle(choiceButtons);

        Button btn;
        int loop = 0;
        for (int value : wrongGuesses) {
            btn = choiceButtons.get(loop);
            btn.setText(Integer.toString(value));
            btn.setTag(0);

            loop++;
        }

        btn = choiceButtons.get(3);
        btn.setText(Integer.toString(correctGuess));
        btn.setTag(1);
    }
}
