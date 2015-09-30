package com.yuan.hexgame.ui.activity;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.yuan.hexgame.R;
import com.yuan.hexgame.game.Game;
import com.yuan.hexgame.game.HexGame;
import com.yuan.hexgame.game.Player;
import com.yuan.hexgame.ui.dialog.GameResultDialogFragment;
import com.yuan.hexgame.ui.widget.HexView;
import com.yuan.hexgame.ui.widget.MenuBar;
import com.yuan.hexgame.util.FastBlur;
import com.yuan.hexgame.util.LogUtil;


public class HexGameActivity extends Activity {

    private static final String TAG = "HexGameActivity";

    private ViewGroup mBackground;
    private ViewGroup mRootLayout;

    private MenuBar mMenuBar;

    private int mScreenWidth;
    private int mScreenHeight;

    private Game mGame;

    private HexView[] mHexViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hex_game);
        mBackground = (ViewGroup) findViewById(R.id.rl_hex_game_background);
        mRootLayout = (ViewGroup) findViewById(R.id.rl_background_mask);
        mRootLayout.setOnLongClickListener(mOnLongClickListener);
        mRootLayout.setOnClickListener(mOnClickListener);
        mMenuBar = (MenuBar) findViewById(R.id.menu_bar);
        mMenuBar.setVisibility(View.INVISIBLE);
        mMenuBar.setOnMenuOptionClickListener(mOnMenuOptionClickListener);

        Drawable systemBackground = WallpaperManager.getInstance(this).getDrawable();
        Bitmap bmp = blur(((BitmapDrawable) systemBackground).getBitmap());
        mBackground.setBackgroundDrawable(new BitmapDrawable(getResources(), bmp));

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

        mGame = new HexGame(CHESS_NUM, mHexViews);
    }


    private View.OnClickListener mHexChessOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int chessId = (int) v.getTag();
            LogUtil.i(TAG, "Chess " + chessId + " is clicked.");
            mGame.putPiece(chessId);
            if (mGame.isAWin()) {
                GameResultDialogFragment.newInstance(Player.A).show(getFragmentManager(), "A Win");
                LogUtil.i(TAG, "A win!");
            } else if (mGame.isBWin()) {
                GameResultDialogFragment.newInstance(Player.B).show(getFragmentManager(), "B Win");
                LogUtil.i(TAG, "B win!");
            }
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_background_mask:
                    mMenuBar.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };

    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            mMenuBar.setVisibility(View.VISIBLE);
            return true;
        }
    };

    private MenuBar.OnMenuOptionClickListener mOnMenuOptionClickListener = new MenuBar.OnMenuOptionClickListener() {
        @Override
        public void onSettingsClick() {

        }

        @Override
        public void onShareClick() {

        }
    };

    private Bitmap blur(Bitmap bmp) {
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(0.1f, 0.1f);

        Bitmap resizeBitmap = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, false);

        return FastBlur.doBlur(resizeBitmap, 2, true);
    }

}
