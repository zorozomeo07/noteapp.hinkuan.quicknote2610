package noteapp.hinkuan.quicknote2610.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import noteapp.hinkuan.quicknote2610.R;
import noteapp.hinkuan.quicknote2610.config.Constants;
import noteapp.hinkuan.quicknote2610.config.QuickNote;
import noteapp.hinkuan.quicknote2610.util.CommonUtil;
import noteapp.hinkuan.quicknote2610.util.SharedPreferencesUtil;
import tech.huqi.quicknote.core.lockpattern.LockPatternIndicator;
import tech.huqi.quicknote.core.lockpattern.LockPatternUtil;
import tech.huqi.quicknote.core.lockpattern.LockPatternView;

public class LockActivity extends BaseActivity {
    private static final long DELAY_TIME = 1000L;
    private static final String GESTURE_PASSWORD = "gesture_lock_pwd";
    private List<LockPatternView.Cell> mChosenPattern;
    private LockPatternIndicator mLockPatternIndicator;
    private LockPatternView mLockPatternView;
    private TextView mTvForgetPwd;
    private TextView mTvMessage;
    private boolean isForSet = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        initIntent();
        initView();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            // 来自设置页面
            if (!intent.getBooleanExtra(Constants.INTENT_FROM_WELCOME_ACTIVITY, false)) {
                isForSet = true;
            }
        }
    }

    private void initView() {
        mLockPatternIndicator = (LockPatternIndicator) findViewById(R.id.lockPatterIndicator);
        mLockPatternView = (LockPatternView) findViewById(R.id.lockPatternView);
        mTvMessage = (TextView) findViewById(R.id.tv_tips);
        mTvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
        mTvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetLockPattern();
            }
        });
        mLockPatternView.setOnPatternListener(patternListener);
    }

    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            mLockPatternView.removePostClearPatternRunnable();
            //   updateStatus(Status.DEFAULT, null);
            mLockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            if (mChosenPattern == null && pattern.size() >= 4) {
                if (!isForSet && !TextUtils.isEmpty(getSavedPattern())) {
                    if (getSavedPattern().equals(LockPatternUtil.patternToString(pattern))) {
                        setLockPatternSuccess();
                    } else {
                        CommonUtil.showToastOnUiThread(QuickNote.getString(R.string.draw_gesture_pwd_incorrect));
                    }
                } else {
                    mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
                    updateStatus(Status.CORRECT, pattern);
                }
            } else if (mChosenPattern == null && pattern.size() < 4) {
                updateStatus(Status.LESSERROR, pattern);
            } else if (mChosenPattern != null) {
                if (mChosenPattern.equals(pattern)) {
                    updateStatus(Status.CONFIRMCORRECT, pattern);
                } else {
                    updateStatus(Status.CONFIRMERROR, pattern);
                }
            }
        }
    };

    private void updateStatus(Status status, List<LockPatternView.Cell> pattern) {
        mTvMessage.setTextColor(getResources().getColor(status.colorId));
        mTvMessage.setText(status.strId);
        switch (status) {
            case DEFAULT:
                mLockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CORRECT:
                updateLockPatternIndicator();
                mLockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case LESSERROR:
                mLockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CONFIRMERROR:
                mLockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                mLockPatternView.postClearPatternRunnable(DELAY_TIME);
                break;
            case CONFIRMCORRECT:
                saveChosenPattern(pattern);
                mLockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                setLockPatternSuccess();
                break;
        }
    }

    private void updateLockPatternIndicator() {
        if (mChosenPattern == null)
            return;
        mLockPatternIndicator.setIndicator(mChosenPattern);
    }

    void resetLockPattern() {
        mChosenPattern = null;
        mLockPatternIndicator.setDefaultIndicator();
        updateStatus(Status.DEFAULT, null);
        mLockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
    }

    private void setLockPatternSuccess() {
        Intent intent = new Intent(LockActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveChosenPattern(List<LockPatternView.Cell> cells) {
        SharedPreferencesUtil.getInstance().setString(GESTURE_PASSWORD, LockPatternUtil.patternToString(cells));
    }

    public static String getSavedPattern() {
        return SharedPreferencesUtil.getInstance().getString(GESTURE_PASSWORD, "");
    }

    private enum Status {
        DEFAULT(R.string.gesture_create_default, R.color.gesture_create_confirm_correct),

        CORRECT(R.string.gesture_create_correct, R.color.gesture_create_confirm_correct),

        LESSERROR(R.string.gesture_create_less_error, R.color.gesture_create_confirm_error),

        CONFIRMERROR(R.string.gesture_create_confirm_error, R.color.gesture_create_confirm_error),

        CONFIRMCORRECT(R.string.gesture_create_confirm_correct, R.color.gesture_create_confirm_correct);

        private int strId;
        private int colorId;

        Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
    }
}
