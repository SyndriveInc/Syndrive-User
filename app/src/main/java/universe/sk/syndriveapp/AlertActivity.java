package universe.sk.syndriveapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import java.util.Locale;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class AlertActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    TextToSpeech tts;
    private TextView tvTime;
    private FloatingActionButton fabSend, fabDismiss;
    MaterialProgressBar pbCountdown;
    private CountDownTimer countDownTimer;

    private boolean isDismissed = false;
    private boolean isSent = false;
    private boolean isRunning = true;
    private long millisInFuture;
    private long timeLeftInMillis;
    private long endTime;
    private long countDownInterval = 1000; //1s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.alert);
        actionBar.setTitle(" Send SOS?");
        actionBar.setDisplayUseLogoEnabled(true);

        tts = new TextToSpeech(AlertActivity.this, AlertActivity.this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.getString("index", "20");

        tvTime = findViewById(R.id.tvTime);
        fabSend = findViewById(R.id.fabSend);
        fabDismiss = findViewById(R.id.fabDismiss);
        pbCountdown = findViewById(R.id.pbCountdown);

        fabDismiss.setEnabled(true);
        fabSend.setEnabled(true);

        millisInFuture = 20000; //20s
        timeLeftInMillis = millisInFuture;

        startTimer();

    } // end of onCreate

    private void startTimer() {
        endTime = System.currentTimeMillis() + timeLeftInMillis;
        pbCountdown.setMax((int) millisInFuture);
//        isRunning = true;
        //pbCountdown.setProgress((int) millisInFuture);

        countDownTimer = new CountDownTimer(timeLeftInMillis, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                if (isDismissed || isSent || !isRunning) cancel();
                else {
                    tvTime.setText("" + millisUntilFinished / 1000);
                    timeLeftInMillis = millisUntilFinished;

                    if (millisUntilFinished/1000 > 5)
                        tvTime.setTextColor(getResources().getColor(R.color.black));
                    else
                        tvTime.setTextColor(getResources().getColor(R.color.red));

                    pbCountdown.setProgress( (int) (millisInFuture - millisUntilFinished) );
                    speakOut();
                }
            }

            @Override
            public void onFinish() {
                isRunning = false;
                tvTime.setText(R.string.tv_sent);
                tvTime.setTextColor(getResources().getColor(R.color.green));
                fabDismiss.setEnabled(false);
                fabSend.setEnabled(false);
                pbCountdown.setProgress((int) millisInFuture);
                speakOut();

                Intent mIntent = new Intent();
                mIntent.setClass(AlertActivity.this, SendSMSActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
            }
        }.start(); // end of CountDownTimer

        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSent = true;
                isRunning = false;
                tvTime.setText(R.string.tv_sent);
                tvTime.setTextColor(getResources().getColor(R.color.green));
                fabSend.setEnabled(false);
                fabDismiss.setEnabled(false);
                pbCountdown.setProgress((int) millisInFuture);
                speakOut();

                Intent mIntent = new Intent();
                mIntent.setClass(AlertActivity.this, SendSMSActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
            }
        }); //end of fabSend

        fabDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDismissed = true;
                isRunning = false;
                tvTime.setText(R.string.tv_dismissed);
                tvTime.setTextColor(getResources().getColor(R.color.black));
                fabSend.setEnabled(false);
                fabDismiss.setEnabled(false);
                pbCountdown.setProgress(0);
                speakOut();

                startActivity(new Intent(AlertActivity.this, NavigationActivity.class));
            }
        }); //end of fabDismiss
    } // end of startTimer

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong("millisLeft", timeLeftInMillis);
        outState.putBoolean("isRunning", isRunning);
        outState.putLong("endTime", endTime);
    } // end of onSaveInstanceState

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        timeLeftInMillis = savedInstanceState.getLong("millisLeft");
        isRunning = savedInstanceState.getBoolean("isRunning");

        if (isRunning) {
            endTime = savedInstanceState.getLong("endTime");
            timeLeftInMillis = endTime - System.currentTimeMillis();
            startTimer();
        }
    } // end of onRestoreInstanceState

    private void speakOut() {
        String text = tvTime.getText().toString().trim();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        if (tts!=null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.UK);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported!");
            }
            else {
                speakOut();
            }
        }
        else {
            Log.e("TTS", "Initialization failed!");
        }
    }
} // end of AlertActivity