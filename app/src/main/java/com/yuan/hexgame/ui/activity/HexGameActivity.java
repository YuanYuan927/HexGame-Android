package com.yuan.hexgame.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.yuan.hexgame.R;
import com.yuan.hexgame.game.Board;
import com.yuan.hexgame.game.ChessBoard;
import com.yuan.hexgame.game.MonteCarloRobot;
import com.yuan.hexgame.game.Player;
import com.yuan.hexgame.game.Robot;
import com.yuan.hexgame.ui.dialog.GameResultDialogFragment;
import com.yuan.hexgame.ui.widget.HexView;
import com.yuan.hexgame.util.LogUtil;


public class HexGameActivity extends Activity {

    private static final String TAG = "HexGameActivity";

    private ViewGroup mRootLayout;
    private int mScreenWidth;
    private int mScreenHeight;

    private Player mCurrentPlayer = Player.A;

    private Board mChessBoard;

    private Robot mRobot;

    private HexView[] mHexViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hex_game);
        mRootLayout = (ViewGroup) findViewById(R.id.rl_hex_game);

        // Get the screen size
        DisplayMetrics dm = getResources().getDisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        LogUtil.i(TAG, "width=" + dm.widthPixels + " height=" + dm.heightPixels);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

        // Draw the chess board
        int CHESS_SIZE = 70;
        int CHESS_NUM = 9;
        int xDelta = (int) (CHESS_SIZE * Math.sqrt(3));
        int yDelta = CHESS_SIZE * 3 / 2;
        int xOffset = xDelta / 2;
        CHESS_NUM = (int) (mScreenHeight / (CHESS_SIZE * 1.7));
        int boardHeight = CHESS_NUM * CHESS_SIZE * 3 / 2 + CHESS_SIZE / 2;
        int boardWidth = xDelta * CHESS_NUM + xOffset * (CHESS_NUM - 1);
        int leftTopX = (mScreenWidth - boardWidth) / 2;
        int leftTopY = (mScreenHeight - boardHeight) / 2;
        mHexViews = new HexView[CHESS_NUM * CHESS_NUM + 1];
        for (int i = 1; i <= CHESS_NUM; i++) {
            for (int j = 1; j <= CHESS_NUM; j++) {
                int x = leftTopX + (j - 1) * xDelta + (i - 1) * xOffset;
                int y = leftTopY + (i - 1) * yDelta;
                int id = (i - 1) * CHESS_NUM + j;
                mHexViews[id] = new HexView(this, CHESS_SIZE);
                mHexViews[id].setX(x);
                mHexViews[id].setY(y);
                mHexViews[id].setTag(id);
                mHexViews[id].setOnClickListener(mHexChessOnClickListener);
                mRootLayout.addView(mHexViews[id]);
            }
        }

        mChessBoard = new ChessBoard(CHESS_NUM, Player.A);

        mRobot = new MonteCarloRobot(Player.B);
    }

    private View.OnClickListener mHexChessOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int chessId = (int) v.getTag();
            LogUtil.i(TAG, "Chess " + chessId + " is clicked.");
            ((HexView)v).setOwner(mCurrentPlayer);
            mChessBoard.setOwner(chessId, mCurrentPlayer);
            int robotChessPos = mRobot.getChessPos(mChessBoard);
            mHexViews[robotChessPos].setOwner(mCurrentPlayer.component());
            mChessBoard.setOwner(robotChessPos, mCurrentPlayer.component());
            if (mChessBoard.isAWin()) {
                new GameResultDialogFragment().show(getFragmentManager(), "A Win");
                LogUtil.i(TAG, "A win!");
            } else if (mChessBoard.isBWin()) {
                new GameResultDialogFragment().show(getFragmentManager(), "B Win");
                LogUtil.i(TAG, "B win!");
            }
//            if (mCurrentPlayer == Player.A) {
//                mCurrentPlayer = Player.B;
//            } else {
//                mCurrentPlayer = Player.A;
//            }
        }
    };

}
