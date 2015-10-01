package com.yuan.hexgame.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yuan.hexgame.R;

/**
 * Created by Yuan Sun on 2015/10/1.
 */
public class MenuBar extends RelativeLayout {

    private ImageView mIvSettings;
    private ImageView mIvShare;

    private OnMenuOptionClickListener mOnMenuOptionClickListener;

    public MenuBar(Context context) {
        this(context, null);
    }

    public MenuBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View layout = LayoutInflater.from(context).inflate(R.layout.widget_menu_bar, this, true);
        layout.setOnClickListener(mOnClickListener);
        mIvSettings = (ImageView) layout.findViewById(R.id.iv_settings);
        mIvShare = (ImageView) layout.findViewById(R.id.iv_social_share);
        mIvSettings.setOnClickListener(mOnClickListener);
        mIvShare.setOnClickListener(mOnClickListener);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_settings:
                    if (mOnMenuOptionClickListener != null)
                        mOnMenuOptionClickListener.onSettingsClick();
                    break;
                case R.id.iv_social_share:
                    if (mOnMenuOptionClickListener != null)
                        mOnMenuOptionClickListener.onShareClick();
            }
        }
    };

    public interface OnMenuOptionClickListener {
        public void onSettingsClick();
        public void onShareClick();
    }

    public void setOnMenuOptionClickListener(OnMenuOptionClickListener onMenuOptionClickListener) {
        this.mOnMenuOptionClickListener = onMenuOptionClickListener;
    }
}
