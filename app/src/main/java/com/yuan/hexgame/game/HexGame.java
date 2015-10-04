package com.yuan.hexgame.game;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

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

    private Context mContext;
    private Handler mMainThreadHandler;

    private Board mBoard;

    private Player mCurrentPlayer;

    private GameSettings mSettings;

    private Robot mRobot;

    private HexView[] mHexViews; // 1 ~ NxN

    private OnGameOverListener mOnGameOverListener;

    private boolean isGameOver = false;
    private Player mWinner;

    private List<Integer> mOccupiedViewIds = new LinkedList<>();

    public HexGame(Context context, int n, HexView[] hexViews) {
        mContext = context;
        mMainThreadHandler = new Handler(context.getMainLooper());
        mBoard = new ChessBoard(n);
        mHexViews = hexViews;
        mSettings = GameSettings.getInstance();
        mCurrentPlayer = mSettings.getFirstPlayer();
        if (mSettings.getGameMode() == GameSettings.MODE_HUMAN_VS_ROBOT) {
            mRobot = new MonteCarloRobot(Player.B);
        }
    }

    @Override
    public void start() {
        if (mSettings.getGameMode() == GameSettings.MODE_HUMAN_VS_ROBOT
                && mSettings.getFirstPlayer() == Player.B) {
            int num = mBoard.getChessNum();
            int firstPos = 0;
            if (num % 2 == 1) {
                firstPos = num / 2 + 1;
            } else {
                firstPos = num / 2 + (int) Math.sqrt(num) / 2;
            }
            doPutPiece(firstPos);
        }
    }

    @Override
    public void putPiece(int id) {
        if (mBoard.isOccupied(id)) {
            LogUtil.i(TAG, "Chess " + id + " is occupied.");
            return;
        }
//        mOccupiedViewIds.add(id);
//        mBoard.setOwner(id, mCurrentPlayer);
//        mHexViews[id].setOwner(mCurrentPlayer);
//        mCurrentPlayer = mCurrentPlayer.component();
        doPutPiece(id);
        if (mSettings.getGameMode() == GameSettings.MODE_HUMAN_VS_ROBOT && (!isGameOver)) {
//            int robotChessPos = mRobot.getChessPos(mBoard);
//            mOccupiedViewIds.add(robotChessPos);
//            mBoard.setOwner(robotChessPos, mCurrentPlayer);
//            mHexViews[robotChessPos].setOwner(mCurrentPlayer);
//            mCurrentPlayer = mCurrentPlayer.component();
            new RobotTask().execute();
//            final long startT = System.currentTimeMillis();
//            mRobot.compute(mBoard, new Robot.RobotStatusListener() {
//                @Override
//                public void onStart() {
//
//                }
//
//                @Override
//                public void onCompleted(int optimalPos) {
//                    doPutPiece(optimalPos);
//                    long endT = System.currentTimeMillis();
//                    LogUtil.i(TAG, "Robot cost " + (endT - startT) + "ms");
//                }
//            });
        }
    }

    private void doPutPiece(int id) {
        mOccupiedViewIds.add(id);
        mBoard.setOwner(id, mCurrentPlayer);
        mHexViews[id].setOwner(mCurrentPlayer);
        mCurrentPlayer = mCurrentPlayer.component();
        if (isAWin()) {
            mWinner = Player.A;
            isGameOver = true;
            if (mOnGameOverListener != null) {
                mOnGameOverListener.onGameOver(mWinner);
            }
        } else if (isBWin()) {
            mWinner = Player.B;
            isGameOver = true;
            if (mOnGameOverListener != null) {
                mOnGameOverListener.onGameOver(mWinner);
            }
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
        isGameOver = false;
        mWinner = null;
        mCurrentPlayer = mSettings.getFirstPlayer();
        mMainThreadHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                start();
            }
        }, mOccupiedViewIds.size() * 20 + 1000);
    }

    @Override
    public void setOnGameOverListener(OnGameOverListener onGameOverListener) {
        mOnGameOverListener = onGameOverListener;
    }

    private class RobotTask extends AsyncTask<Void, Integer, Integer> {
        private long startT;
        private long endT;

        @Override
        protected Integer doInBackground(Void... params) {
            startT = System.currentTimeMillis();
            return mRobot.getChessPos(mBoard);
        }

        @Override
        protected void onPostExecute(Integer id) {
            doPutPiece(id);
            endT = System.currentTimeMillis();
            LogUtil.i(TAG, "Robot cost " + (endT - startT) + "ms");
        }
    }

}
