package com.yuan.hexgame.ui.activity;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.yuan.hexgame.BuildConfig;
import com.yuan.hexgame.R;
import com.yuan.hexgame.ui.fragment.SettingsFragment;
import com.yuan.hexgame.util.LogUtil;

/**
 * Created by Yuan Sun on 2015/10/5.
 */
public class SettingsActivity extends PreferenceActivity {

    private static final String TAG = "SettingsActivity";
    private ImageView mIvBackBackground;
    private ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mIvBackBackground = (ImageView) findViewById(R.id.iv_back_background);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mIvBackBackground.setBackgroundColor(getResources().getColor(R.color.indigo_500_dark));
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mIvBackBackground.setBackgroundColor(getResources().getColor(R.color.indigo_500));
                }
                return false;
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i(TAG, "Click Back");
                finish();
            }
        });
    }


}