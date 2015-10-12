package com.yuan.hexgame.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.yuan.hexgame.R;
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
                mIvBackBackground.setBackgroundColor(getResources().getColor(R.color.indigo_600));
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
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingsActivity.this, HexGameActivity.class));
        finish();
    }
}
