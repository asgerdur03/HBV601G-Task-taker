package com.example.hbv601G.ui.pomodoro;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.example.hbv601G.R;

import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class Pomodoro {
    private long timeLeft = 60 * 1000;

    private boolean isRunning = false;

    private CountDownTimer countDownTimer;

    private TextView timerDisplay;
    private Button startButton;
    private Context context;


    public Pomodoro(Context context, TextView timerDisplay, Button startButton){
        this.context = context;
        this.timerDisplay = timerDisplay;
        this.startButton=startButton;
        updateDisplay();

        startButton.setOnClickListener(v-> {
            if (isRunning) pauseTimer();
            else startTimer();
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateDisplay();
            }

            @Override
            public void onFinish() {
                isRunning = false;
                startButton.setText("Start");
            }
        }.start();

        isRunning = true;
        startButton.setText("Pause");
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isRunning = false;
        startButton.setText("Start");
    }

    private void updateDisplay() {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;
        String formatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerDisplay.setText(formatted);
    }









}

