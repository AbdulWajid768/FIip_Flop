package com.example.flipflop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * The class handles the fragment_game.xml
 */
public class GameFragment extends Fragment {

    private TextView timerTxt, scoreTxt, levelTxt;
    private ProgressBar progressBar;
    private ImageView imagesViews[];
    private int[] imagesIDs = {R.drawable.circle, R.drawable.heart, R.drawable.triangle, R.drawable.square, R.drawable.hexagon, R.drawable.star, R.drawable.circle, R.drawable.heart, R.drawable.triangle, R.drawable.square, R.drawable.hexagon, R.drawable.star};
    private int maxTime, score, level, imgClickCount = 0, tempIndex1, tempIndex2, guessedCount=0;
    private CountDownTimer timer = null;
    private long currentTime;
    private ImageButton refreshBtn;
    private AlertDialog dialog = null;
    private boolean isWon = false;

    /**
     * Parameterized Constructor
     */
    GameFragment(int maxTime, int score, int level) {
        this.maxTime = maxTime;
        this.score = score;
        this.level = level;
    }
    /**
     * Create the View and return it.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        timerTxt = view.findViewById(R.id.timer);
        scoreTxt = view.findViewById(R.id.score);
        levelTxt = view.findViewById(R.id.level);
        setClickListenerOnRefreshButton(view);
        setClickListenerOnImageViews(view);
        setTimer();
        setScore();
        setLevel();
        shuffleImages();
        return view;
    }

    /**
     * Replace fragment in activity_main.xml
     * @param gameFragment GameFragment
     */
    private void replaceFragment(Fragment gameFragment){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, gameFragment);
        ft.commit();
    }

    /**
     * Set click listener on refresh button
     * @param view View
     */
    private void setClickListenerOnRefreshButton(View view) {
        refreshBtn = view.findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer();
                replaceFragment(new GameFragment(maxTime,score,level));
            }
        });
    }

    /**
     * Cancel the game timer
     */
    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * Set click listener on image views
     * @param view View
     */
    private void setClickListenerOnImageViews(View view)
    {
        imagesViews = new ImageView[]{ view.findViewById(R.id.img00),
                view.findViewById(R.id.img01),
                view.findViewById(R.id.img02),
                view.findViewById(R.id.img10),
                view.findViewById(R.id.img11),
                view.findViewById(R.id.img12),
                view.findViewById(R.id.img20),
                view.findViewById(R.id.img21),
                view.findViewById(R.id.img22),
                view.findViewById(R.id.img30),
                view.findViewById(R.id.img31),
                view.findViewById(R.id.img32)
        };
        for (int i=0;i<imagesViews.length;i++){
            setCLickListenerOnImageView(i);
        }
    }

    /**
     * Set click listener on image views with particular index
     * @param index int
     */
    private void setCLickListenerOnImageView(final int index){
        imagesViews[index].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickOnImage(index);
            }
        });
    }

    /**
     * Set the timer for the game
     */
    private void setTimer() {
        timer = new CountDownTimer(maxTime * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                currentTime = millisUntilFinished / 1000;
                timerTxt.setText("" + currentTime);
                progressBar.setProgress(((int) currentTime) * 100 / maxTime);
            }

            public void onFinish() {
                isWon = false;
                cancelTimer();
                showLevelFinishDialog();
            }
        }.start();
    }

    /**
     * Set the score Text View
     */
    private void setScore() {
        scoreTxt.setText("Score: " + score);
    }

    /**
     * Set the level Text View
     */
    private void setLevel() {
        levelTxt.setText("Level: " + level);
    }

    /**
     * Apply rotation when click on image view with a particular index
     */
    private void applyRotation(final int index, final int imageID){
        imagesViews[index].setRotationY(0f);
        imagesViews[index].animate().rotationY(90f).setListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationEnd(Animator animation){
                imagesViews[index].setImageResource(imageID);
                imagesViews[index].setRotationY(270f);
                imagesViews[index].animate().rotationY(360f).setListener(null);
            }
        });
    }

    /**
     * Handle click on image view with particular index
     */
    private void handleClickOnImage(final int index) {
        if (imgClickCount == 0) {
            imgClickCount = 1;
            applyRotation(index,imagesIDs[index]);
            tempIndex1 = index;
        } else {
            if(tempIndex1 == index) return;
            imgClickCount = 0;
            applyRotation(index,imagesIDs[index]);
            tempIndex2 = index;
            disableImageViews();
            new CountDownTimer(1200, 1000) {
                public void onFinish() {
                    if (imagesIDs[tempIndex1] == imagesIDs[tempIndex2]) {
                        imagesViews[tempIndex1].setVisibility(View.INVISIBLE);
                        imagesViews[tempIndex2].setVisibility(View.INVISIBLE);
                        score=score+10;
                        setScore();
                        guessedCount += 1;
                        if(guessedCount == 6){
                            cancelTimer();
                            level+=1;
                            isWon = true;
                            score = (int) (score + (currentTime/10)*10);
                            showLevelFinishDialog();
                        }
                    } else {
                        applyRotation(tempIndex1, R.drawable.question);
                        applyRotation(tempIndex2, R.drawable.question);
                    }
                    enableImageViews();
                }
                public void onTick(long millisUntilFinished) {
                }
            }.start();
        }
    }

    /**
     * Display an alert dialog when game finishes
     */
    private void showLevelFinishDialog() {
        if (getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_levelfinish, null);
            TextView statusTxt = view.findViewById(R.id.status);
            TextView scoreTxt = view.findViewById(R.id.score);
            if (isWon == true) {
                statusTxt.setText("YOU WON!");
                statusTxt.setTextColor(getResources().getColor(R.color.green));
            } else {
                statusTxt.setText("YOU LOSS!");
                statusTxt.setTextColor(getResources().getColor(R.color.red));
            }
            scoreTxt.setText(String.valueOf(score));
            builder.setView(view);
            setClickListenerOnReplayButton(view);
            setClickListenerOnMenuButton(view);
            disableImageViews();
            if (dialog == null) {
                dialog = builder.create();
                dialog.show();
            }
        }
    }

    /**
     * Set click listener on replay button
     * @param view View
     */
    private void setClickListenerOnReplayButton(View view) {
        view.findViewById(R.id.replayBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer();
                closeAlertDialog();
                replaceFragment(new GameFragment(maxTime,score,level));
            }
        });
    }

    /**
     * Set click listener on menu button
     * @param view View
     */
    private void setClickListenerOnMenuButton(View view) {
        view.findViewById(R.id.menuBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer();
                closeAlertDialog();
                replaceFragment(new MenuFragment());
            }
        });
    }

    /**
     * Close the alert dialog
     */
    private void closeAlertDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * Disable the image view will program is deciding whether the two image views match or not
     */
    private void disableImageViews(){
        for (int i=0;i<imagesViews.length;i++){
            imagesViews[i].setEnabled(false);
        }
    }

    /**
     * Enable the image view after program decided whether the two image views match or not
     */
    private void enableImageViews(){
        for (int i=0;i<imagesViews.length;i++){
            imagesViews[i].setEnabled(true);
        }
    }

    /**
     * Shuffle images at the start of the game
     */
    private void shuffleImages() {
        Random rand = new Random();
        for (int i = 0; i < imagesIDs.length; i++) {
            int randomIndexToSwap = rand.nextInt(imagesIDs.length);
            int temp = imagesIDs[randomIndexToSwap];
            imagesIDs[randomIndexToSwap] = imagesIDs[i];
            imagesIDs[i] = temp;
        }
    }
}
