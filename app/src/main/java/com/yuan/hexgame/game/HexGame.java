package com.yuan.hexgame.game;

import com.yuan.hexgame.ui.widget.HexView;
import com.yuan.hexgame.util.LogUtil;

/**
 * Created by Yuan Sun on 2015/9/30.
 */
public class HexGame implements Game {

    private static final String TAG = "HexGame";

    private Board mBoard;

    private Player mCurrentPlayer;

    private GameSettings mSettings;

    private Robot mRobot;

    private HexView[] mHexViews;

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
        mBoard.setOwner(id, mCurrentPlayer);
        mHexViews[id].setOwner(mCurrentPlayer);
        mCurrentPlayer = mCurrentPlayer.component();
        if (mSettings.getGameMode() == GameSettings.MODE_HUMAN_VS_ROBOT) {
            int robotChessPos = mRobot.getChessPos(mBoard);
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
        // TODO
    }

}
