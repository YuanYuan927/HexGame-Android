package com.yuan.hexgame.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yuan.hexgame.R;

/**
 * Created by Yuan Sun on 2015/10/11.
 */
public class GameWizardPopupWindow {

    public static final int CENTER = -1;

    private static final int WIZARD_NUM_IMGS[] = {
            R.drawable.wizard_num_deep_orange_500,
            R.drawable.wizard_num_indigo_500,
            R.drawable.wizard_num_pink_500,
            R.drawable.wizard_num_blue_500,
            R.drawable.wizard_num_light_green_500,
            R.drawable.wizard_num_deep_purlple_500,
            R.drawable.wizard_num_red_500,
            R.drawable.wizard_num_purlple_500,
            R.drawable.wizard_num_green_500
    };
    private Activity mActivity;
    private PopupWindow mPopupWindow;

    private TextView mTvWizardNum;
    private ImageView mIvWizardNum;
    private TextView mTvWizardTitle;
    private TextView mTvWizardMsg;
    private TextView mTvPrevious;
    private TextView mTvNext;

    private View mRootView;

    private int mIndex;
    private int mX, mY;

    public GameWizardPopupWindow(Activity activity, int i, CharSequence title, CharSequence msg, int x, int y) {
        mActivity = activity;
        LayoutInflater inflater = LayoutInflater.from(activity);
        mRootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        View layout = inflater.inflate(R.layout.pop_window_game_wizard, null);

        mTvWizardNum = (TextView) layout.findViewById(R.id.tv_wizard_num);
        mTvWizardNum.setText(i + "");

        mIvWizardNum = (ImageView) layout.findViewById(R.id.iv_wizard_num);
        mIvWizardNum.setImageDrawable(activity.getResources().getDrawable(WIZARD_NUM_IMGS[(i - 1) % WIZARD_NUM_IMGS.length]));

        mTvWizardTitle = (TextView) layout.findViewById(R.id.tv_wizard_title);
        mTvWizardMsg = (TextView) layout.findViewById(R.id.tv_wizard_msg);
        mTvPrevious = (TextView) layout.findViewById(R.id.tv_previous);
        mTvNext = (TextView) layout.findViewById(R.id.tv_next);

        mTvWizardTitle.setText(title);
        mTvWizardMsg.setText(msg);

        mIndex = i;
        mX = x;
        mY = y;
        mPopupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setOnNextClickListener(View.OnClickListener onClickListener) {
        mTvNext.setOnClickListener(onClickListener);
    }

    public void setOnPreviousClickListener(View.OnClickListener onClickListener) {
        mTvPrevious.setOnClickListener(onClickListener);
    }

    public void setPreviousButtonVisibility(int visibility) {
        mTvPrevious.setVisibility(visibility);
    }

    public void setNextButtonText(int resid) {
        mTvNext.setText(resid);
    }

    public void show() {
        if (mX == CENTER && mY == CENTER) {
            mPopupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
        } else {
            mPopupWindow.showAtLocation(mRootView, Gravity.TOP | Gravity.LEFT, mX, mY);
        }
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }

    public int getIndex() {
        return mIndex;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }
}
