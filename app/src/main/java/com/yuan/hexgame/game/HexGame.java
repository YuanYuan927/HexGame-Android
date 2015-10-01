package com.yuan.hexgame.game;

import android.animation.ObjectAnimator;

import com.yuan.hexgame.ui.widget.HexView;
import com.yuan.hexgame.util.LogUtil;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Yuan Sun on 2015/9/30.
 */
public class HexGame implements Game {

    private static final String TAG = "HexGame";

    private Board mBoard;

    private Player mCurrentPlayer;

    private GameSettings mSettings;

    private Robot mRobot;

    private HexView[] mHexViews; // 1 ~ NxN

    private List<Integer> mOccupiedViewIds = new LinkedList<>();

    public HexGame(int n, HexView[] hexViews) {
        mBoard = new ChessBoard(n);
        mHexViews = hexViews;
        mSettings = GameSettings.getInstance();
        mCurrentPlayer = mSettings.getFirstPlayer();
        if (mSettings.getGameMode() == GameSettings.MODE_HUMAN_VS_ROBOT) {
            mRobot = new MonteCarloRobot(mCurrentPlayer.component());
        }
    }

    @Override
    public void putPiece(int id) {
        if (mBoard.isOccupied(id)) {
            LogUtil.i(TAG, "Chess " + id + " is occupied.");
            return;
        }
        mOccupiedViewIds.add(id);
        mBoard.setOwner(id, mCurrentPlayer);
        mHexViews[id].setOwner(mCurrentPlayer);
        mCurrentPlayer = mCurrentPlayer.component();
        if (mSettings.getGameMode() == GameSettings.MODE_HUMAN_VS_ROBOT) {
            int robotChessPos = mRobot.getChessPos(mBoard);
            mOccupiedViewIds.add(robotChessPos);
            mBoard.setOwner(robotChessPos, mCurrentPlayer);
            mHexViews[robotChessPos].setOwner(mCurrentPlayer);
            mCurrentPlayer = mCurrentPlayer.component();
        }
    }

    @Override
    public boolean isAWin() {
        return mBoard.isAWin();
    }

    @Override
    public boolean isBWin() {
        return mBoard.isBWin();
    }

    @Override
    public void restart() {
        mBoard.restart();
        int i = 0;
        Collections.shuffle(mOccupiedViewIds);
        for (Integer id : mOccupiedViewIds) {
            final HexView view = mHexViews[id];
            view.postDelayed(new Runnable() {

                @Override
                public void run() {
                    ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
                    anim.setDuration(100);
                    anim.start();
                }
            }, 20 * i);
            i++;
        }
        mOccupiedViewIds.clear();
    }

}
