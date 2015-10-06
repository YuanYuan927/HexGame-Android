package com.yuan.hexgame.ui.preference;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.yuan.hexgame.R;

/**
 * Created by Yuan Sun on 2015/10/6.
 */
public class ColorSelectorPreference extends DialogPreference {

    private static final int COLOR_NUM = 7;

    private ImageView[] mIvColors;
    private ImageView[] mIvColorChecks;

    private int mCurrentValue;
    private int mNewValue;

    private static final int[] COLORS = {
            R.color.red,
            R.color.orange,
            R.color.yellow,
            R.color.green,
            R.color.cyan,
            R.color.blue,
            R.color.purple
    };

    public ColorSelectorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_color_selector);
    }

    @Override
    protected View onCreateDialogView() {
        View view = super.onCreateDialogView();

        mIvColors = new ImageView[COLOR_NUM];
        mIvColors[0] = (ImageView) view.findViewById(R.id.iv_red);
        mIvColors[1] = (ImageView) view.findViewById(R.id.iv_orange);
        mIvColors[2] = (ImageView) view.findViewById(R.id.iv_yellow);
        mIvColors[3] = (ImageView) view.findViewById(R.id.iv_green);
        mIvColors[4] = (ImageView) view.findViewById(R.id.iv_cyan);
        mIvColors[5] = (ImageView) view.findViewById(R.id.iv_blue);
        mIvColors[6] = (ImageView) view.findViewById(R.id.iv_purple);

        mIvColorChecks = new ImageView[COLOR_NUM];
        mIvColorChecks[0] = (ImageView) view.findViewById(R.id.iv_red_check);
        mIvColorChecks[1] = (ImageView) view.findViewById(R.id.iv_orange_check);
        mIvColorChecks[2] = (ImageView) view.findViewById(R.id.iv_yellow_check);
        mIvColorChecks[3] = (ImageView) view.findViewById(R.id.iv_green_check);
        mIvColorChecks[4] = (ImageView) view.findViewById(R.id.iv_cyan_check);
        mIvColorChecks[5] = (ImageView) view.findViewById(R.id.iv_blue_check);
        mIvColorChecks[6] = (ImageView) view.findViewById(R.id.iv_purple_check);

        for (int i = 0; i < COLOR_NUM; i++) {
            mIvColors[i].setTag(i);
            mIvColors[i].setOnClickListener(mOnClickListener);
            mIvColorChecks[i].setTag(i);
            mIvColorChecks[i].setOnClickListener(mOnClickListener);
        }
        mCurrentValue = getPersistedInt(0);
        updateColorCheckStatus(mCurrentValue);
        return view;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            updateColorCheckStatus((Integer) v.getTag());
        }
    };

    private void updateColorCheckStatus(int colorId) {
        mNewValue = colorId;
        for (int i = 0; i < COLOR_NUM; i++) {
            mIvColorChecks[i].setVisibility(View.INVISIBLE);
        }
        mIvColorChecks[colorId].setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            persistInt(mNewValue);
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mCurrentValue = getPersistedInt(0);
        } else {
            // Set default state from the XML attribute
            if (defaultValue == null)
                return;
            int value = (Integer) defaultValue;
            mCurrentValue = (Integer) defaultValue;
            if (value < 0 || value >= COLOR_NUM) {
                mCurrentValue = 0;
            }
            persistInt(mCurrentValue);
        }
    }
}
