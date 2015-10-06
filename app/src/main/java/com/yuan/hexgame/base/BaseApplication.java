package com.yuan.hexgame.base;

import android.app.Application;
import android.util.DisplayMetrics;

import com.yuan.hexgame.game.GameSettings;
import com.yuan.hexgame.util.LogUtil;

/**
 * Created by Yuan Sun on 2015/10/6.
 */
public class BaseApplication extends Application {

    private static final String TAG = "BaseApplication";

    private static final int CHESS_MIN_SIZE_DP = 24;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i(TAG, "onCreate");
        GameSettings.newInstance(this);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        LogUtil.i(TAG, "Width=" + dm.widthPixels + " Height=" + dm.heightPixels
                + " Density=" + dm.density + " DensityDpi=" + dm.densityDpi);

        computeBoardParams(dm.widthPixels, dm.heightPixels, dm.density);
    }

    private void computeBoardParams(int width, int height, float density) {
        // Draw the chess board
        int chessSize = (int) (CHESS_MIN_SIZE_DP * density);
        int xDelta = (int) (chessSize * Math.sqrt(3));
        int yDelta = chessSize * 3 / 2;
        int xOffset = xDelta / 2;
        int boardN = (int) (height / (chessSize * 1.6));
        int boardHeight = boardN * yDelta + chessSize / 2;
        int boardWidth = xDelta * boardN + xOffset * (boardN - 1);

        if (boardWidth > width) {
            boardN = (width * 2 / xDelta + 1) / 3;
            boardHeight = boardN * chessSize * 3 / 2 + chessSize / 2;
//            boardWidth = xDelta * boardN + xOffset * (boardN - 1);
        }

//        int leftTopX = (width - boardWidth) / 2;
        int boardPaddingTop = (height - boardHeight) / 2;

        LogUtil.i(TAG, "boardN=" + boardN + " chessSize=" + chessSize);
        GameSettings.getInstance().setBoardNMax(boardN);
        GameSettings.getInstance().setBoardPaddingTop(boardPaddingTop);
    }
}
