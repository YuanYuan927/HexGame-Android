package com.yuan.hexgame.ui.activity;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.res.Resources;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.yuan.hexgame.R;
import com.yuan.hexgame.game.Game;
import com.yuan.hexgame.game.GameSettings;
import com.yuan.hexgame.game.HexGame;
import com.yuan.hexgame.game.Player;
import com.yuan.hexgame.ui.dialog.GameResultDialogFragment;
import com.yuan.hexgame.ui.widget.Avatar;
import com.yuan.hexgame.ui.widget.HexView;
import com.yuan.hexgame.ui.widget.MenuBar;
import com.yuan.hexgame.util.FastBlur;
import com.yuan.hexgame.util.LogUtil;


public class HexGameActivity extends Activity
        implements GameResultDialogFragment.GameResultDialogListener, Game.OnGameOverListener {

    private static final String TAG = "HexGameActivity";

    private ViewGroup mBackground;
    private ViewGroup mRootLayout;

    private MenuBar mMenuBar;

    private int mScreenWidth;
    private int mScreenHeight;

    private Game mGame;

    private HexView[] mHexViews;
    private Avatar mAvatarA;
    private Avatar mAvatarB;

    private boolean isWindowFocusFirstTime = true;

    private GameSettings mSettings = GameSettings.getInstance();

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
        CHESS_NUM = 9; //(int) (mScreenHeight / (CHESS_SIZE * 1.7));
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

        initAvatar();

        mGame = new HexGame(this, CHESS_NUM, mHexViews, mAvatarA, mAvatarB);
        mGame.setOnGameOverListener(this);
    }

    private void initAvatar() {
        Resources res = getResources();
        Bitmap playerAAvatar = ((BitmapDrawable) res.getDrawable(R.drawable.avatar_player)).getBitmap();
        Bitmap playerBAvatar;
        if (mSettings.getGameMode() == GameSettings.MODE_HUMAN_VS_ROBOT) {
            playerBAvatar = ((BitmapDrawable) res.getDrawable(R.drawable.avatar_android)).getBitmap();
        } else {
            playerBAvatar = playerAAvatar;
        }
        mAvatarA = new Avatar(this, 120, res.getColor(R.color.indigo_500), playerAAvatar);
        mAvatarB = new Avatar(this, 120, res.getColor(R.color.pink_500), playerBAvatar);

//        playerA.setBackgroundDrawable(res.getDrawable(R.drawable.avatar_player));
//        int gameMode = mSettings.getGameMode();
//        int playerBDrawableId = gameMode == GameSettings.MODE_HUMAN_VS_ROBOT ? R.drawable.avatar_android : R.drawable.avatar_player;
//        playerB.setBackgroundDrawable(res.getDrawable(playerBDrawableId));

        mAvatarA.setX(50);
        mAvatarA.setY(mScreenHeight - 50 - 120);
        mAvatarB.setX(mScreenWidth - 50 - 120);
        mAvatarB.setY(50);
        mRootLayout.addView(mAvatarA);
        mRootLayout.addView(mAvatarB);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isWindowFocusFirstTime) {
            mGame.start();
            isWindowFocusFirstTime = false;
        }
    }

    private View.OnClickListener mHexChessOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int chessId = (int) v.getTag();
            LogUtil.i(TAG, "Chess " + chessId + " is clicked.");
            mGame.putPiece(chessId);
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
            LogUtil.i(TAG, "Click Settings");
        }

        @Override
        public void onShareClick() {
            LogUtil.i(TAG, "Click Share");
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

    @Override
    public void onRestartClick() {
        mGame.restart();
    }

    @Override
    public void onShareResultClick() {
        mGame.restart();
    }

    @Override
    public void onGameOver(Player winner) {
        GameResultDialogFragment.newInstance(winner).show(getFragmentManager(), "Game Result");
//        LogUtil.i(TAG, "A win!");
    }
}
