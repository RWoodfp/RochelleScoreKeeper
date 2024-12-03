package org.forestpark.rochellescorekeeper;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    private TextView team1Score, team2Score, timerTextView;
    private Button team1FreeThrow, team1Layup, team1ThreePointer;
    private Button team2FreeThrow, team2Layup, team2ThreePointer;
    private Button resetScoresButton, startTimerButton, resetTimerButton;
    private EditText team1Name, team2Name;


    private int scoreTeam1 = 0;
    private int scoreTeam2 = 0;


    private MediaPlayer mediaPlayer; // Declare MediaPlayer instance for the sound


    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 600000; // 10 minutes


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize the MediaPlayer with the sound file from the raw folder
        mediaPlayer = MediaPlayer.create(this, R.raw.swish); // Replace with your actual sound file name


        // Initialize Views
        team1Score = findViewById(R.id.team1Score);
        team2Score = findViewById(R.id.team2Score);
        timerTextView = findViewById(R.id.timerTextView);
        team1Name = findViewById(R.id.team1Name);
        team2Name = findViewById(R.id.team2Name);


        team1FreeThrow = findViewById(R.id.team1FreeThrow);
        team1Layup = findViewById(R.id.team1Layup);
        team1ThreePointer = findViewById(R.id.team1ThreePointer);


        team2FreeThrow = findViewById(R.id.team2FreeThrow);
        team2Layup = findViewById(R.id.team2Layup);
        team2ThreePointer = findViewById(R.id.team2ThreePointer);


        resetScoresButton = findViewById(R.id.resetScoresButton);
        startTimerButton = findViewById(R.id.startTimerButton);
        resetTimerButton = findViewById(R.id.resetTimerButton);


        // Scoring Logic
        team1FreeThrow.setOnClickListener(v -> updateScore(1, 1));
        team1Layup.setOnClickListener(v -> updateScore(1, 2));
        team1ThreePointer.setOnClickListener(v -> {
            updateScore(1, 3);  // Update team 1 score by 3
            playSound(); // Play the sound when Team 1 makes a 3-point shot
        });


        team2FreeThrow.setOnClickListener(v -> updateScore(2, 1));
        team2Layup.setOnClickListener(v -> updateScore(2, 2));
        team2ThreePointer.setOnClickListener(v -> {
            updateScore(2, 3);  // Update team 2 score by 3
            playSound(); // Play the sound when Team 2 makes a 3-point shot
        });


        resetScoresButton.setOnClickListener(v -> resetScores());


        // Timer Logic
        startTimerButton.setOnClickListener(v -> {
            if (isTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });


        resetTimerButton.setOnClickListener(v -> resetTimer());
    }


    private void updateScore(int team, int points) {
        if (team == 1) {
            scoreTeam1 += points;
            team1Score.setText(String.valueOf(scoreTeam1));
        } else {
            scoreTeam2 += points;
            team2Score.setText(String.valueOf(scoreTeam2));
        }
    }


    private void resetScores() {
        scoreTeam1 = 0;
        scoreTeam2 = 0;
        team1Score.setText(String.valueOf(scoreTeam1));
        team2Score.setText(String.valueOf(scoreTeam2));
    }


    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }


            @Override
            public void onFinish() {
                isTimerRunning = false;
                startTimerButton.setText("Start Timer");
            }
        }.start();


        isTimerRunning = true;
        startTimerButton.setText("Pause Timer"); // Change to "Pause Timer" when started
    }


    private void pauseTimer() {
        countDownTimer.cancel(); // Cancel the countdown
        isTimerRunning = false;
        startTimerButton.setText("Resume Timer"); // Change text to "Resume Timer" when paused
    }


    private void resetTimer() {
        timeLeftInMillis = 600000;
        updateTimerText();
        if (isTimerRunning) {
            countDownTimer.cancel();
            isTimerRunning = false;
            startTimerButton.setText("Start Timer");
        }
    }


    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        timerTextView.setText(String.format("%02d:%02d", minutes, seconds));
    }


    private void playSound() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(0); // If the sound is still playing, restart from the beginning
            } else {
                mediaPlayer.start(); // Play the sound
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause(); // Pause the sound if the app is paused
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release resources when the activity is destroyed
        }
    }
}
