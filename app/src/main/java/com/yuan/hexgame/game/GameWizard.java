package com.yuan.hexgame.game;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yuan.hexgame.R;
import com.yuan.hexgame.ui.widget.GameWizardPopupWindow;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Yuan Sun on 2015/10/11.
 */
public class GameWizard {
    private Activity mActivity;
    private LayoutInflater inflater;
    private List<GameWizardPopupWindow> mWizardPopWindowList = new ArrayList<>();
    private GameWizardListener mGameWizardListener;
    private int mLastIndex;

    public interface GameWizardListener {
        public void onWizardShow(int id, boolean isLast);
        public void onWizardOver();
    }

    public GameWizard(Activity activity) {
        mActivity = activity;
        inflater = LayoutInflater.from(activity);
    }

    public void setGameWizardListener(GameWizardListener gameWizardListener) {
        mGameWizardListener = gameWizardListener;
    }

    public void addWizardPopupWindow(int titleResId, int msgResId, int x, int y) {
        addWizardPopupWindow(mActivity.getString(titleResId), mActivity.getString(msgResId), x, y, false);
    }

    public void addWizardPopupWindow(int titleResId, int msgResId, int x, int y, boolean isLast) {
        addWizardPopupWindow(mActivity.getString(titleResId), mActivity.getString(msgResId), x, y, isLast);
    }

    public void addWizardPopupWindow(String title, String msg, int x, int y) {
        addWizardPopupWindow(title, msg, x, y, false);
    }

    public void addWizardPopupWindow(String title, String msg, int x, int y, boolean isLast) {
        final int currentIndex = mWizardPopWindowList.size() + 1; // Index starts from 1.
        final GameWizardPopupWindow wizard = new GameWizardPopupWindow(mActivity, currentIndex, title, msg, x, y);
        if (currentIndex == 1) {
            wizard.setPreviousButtonVisibility(View.INVISIBLE);
            wizard.setNextButtonText(R.string.game_wizard_go);
        } else if (currentIndex > 1) {
            final GameWizardPopupWindow previousWizard = mWizardPopWindowList.get(currentIndex - 2);
            previousWizard.setOnNextClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previousWizard.dismiss();
                    wizard.show();
                    if (mGameWizardListener != null) {
                        mGameWizardListener.onWizardShow(currentIndex, currentIndex == mLastIndex);
                    }
                }
            });
            wizard.setOnPreviousClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wizard.dismiss();
                    previousWizard.show();
                    if (mGameWizardListener != null) {
                        mGameWizardListener.onWizardShow(currentIndex - 1, false);
                    }
                }
            });
        }
        if (isLast) {
            mLastIndex = currentIndex;
            wizard.setNextButtonText(R.string.game_wizard_start_to_play);
            wizard.setOnNextClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wizard.dismiss();
                }
            });
        }
        mWizardPopWindowList.add(wizard);
    }

    public void show() {
        if (mWizardPopWindowList.size() > 0) {
            mWizardPopWindowList.get(0).show();
        }
    }
}
