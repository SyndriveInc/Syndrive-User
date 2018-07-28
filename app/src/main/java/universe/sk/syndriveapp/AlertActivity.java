package universe.sk.syndriveapp;

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

public class AlertActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    TextToSpeech tts;
    private TextView tvTime;
    private FloatingActionButton fabSend, fabDismiss;

    private boolean isDismissed = false;
    private boolean isSent = false;

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

        fabDismiss.setEnabled(true);
        fabSend.setEnabled(true);

        long millisInFuture = 20000; //20s
        long countDownInterval = 1000; //1s
        new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isDismissed || isSent) cancel();
                else {
                    tvTime.setText("" + millisUntilFinished / 1000);
                    if(millisUntilFinished/1000 > 5) tvTime.setTextColor(getResources().getColor(R.color.black));
                    else tvTime.setTextColor(getResources().getColor(R.color.red));
                    speakOut();
                }
            }

            @Override
            public void onFinish() {
                tvTime.setText(R.string.tv_sent);
                tvTime.setTextColor(getResources().getColor(R.color.green));
                fabDismiss.setEnabled(false);
                fabSend.setEnabled(false);
                speakOut();
            }
        }.start();

        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSent = true;
                tvTime.setText(R.string.tv_sent);
                tvTime.setTextColor(getResources().getColor(R.color.green));
                fabSend.setEnabled(false);
                fabDismiss.setEnabled(false);

                speakOut();
            }
        }); //end of Send button

        fabDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDismissed = true;
                tvTime.setText(R.string.tv_dismissed);
                tvTime.setTextColor(getResources().getColor(R.color.black));
                fabSend.setEnabled(false);
                fabDismiss.setEnabled(false);

                speakOut();
            }
        }); //end of Dismiss button

    } // end of onCreate

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
}
