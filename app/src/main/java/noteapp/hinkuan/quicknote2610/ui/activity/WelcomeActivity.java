package noteapp.hinkuan.quicknote2610.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import noteapp.hinkuan.quicknote2610.R;
import noteapp.hinkuan.quicknote2610.config.Constants;
import noteapp.hinkuan.quicknote2610.config.QuickNote;

public class WelcomeActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        delay();
    }

    private void delay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (QuickNote.isSetPatternLock()) {
                    intent = new Intent(WelcomeActivity.this, LockActivity.class);
                    intent.putExtra(Constants.INTENT_FROM_WELCOME_ACTIVITY, true);
                } else {
                    intent = new Intent(WelcomeActivity.this, MainActivity.class);
                }
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }, 200);
    }
}
