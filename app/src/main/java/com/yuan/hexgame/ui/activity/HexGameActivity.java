package com.yuan.hexgame.ui.activity;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
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

import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.yuan.hexgame.BuildConfig;
import com.yuan.hexgame.R;
import com.yuan.hexgame.game.Game;
import com.yuan.hexgame.game.GameSettings;
import com.yuan.hexgame.game.GameWizard;
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
    private static final int AVATAR_SIZE_DP = 60;

    private ViewGroup mBackground;
    private ViewGroup mRootLayout;
    private View mEmptyAnchor;
    private MenuBar mMenuBar;

    private Game mGame;

    private HexView[] mHexViews;
    private Avatar mAvatarA;
    private Avatar mAvatarB;
    private int mAvatarAX;
    private int mAvatarAY;

    private boolean isWindowFocusFirstTime = true;

    private GameSettings mSettings = GameSettings.getInstance();

    private boolean mIsAppFirstLaunched = false;
    private GameWizard mGameWizard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UMGameAgent.setDebugMode(true);
        UMGameAgent.init(this);
        MobclickAgent.updateOnlineConfig(this);

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

        // Draw the chess board
        initChessBoard();

        initAvatar();

        int boardN = GameSettings.getInstance().getBoardN();
        mGame = new HexGame(this, boardN, mHexViews, mAvatarA, mAvatarB);
        mGame.setOnGameOverListener(this);

        initGameWizard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UMGameAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UMGameAgent.onPause(this);
    }

    private void initChessBoard() {
        GameSettings settings = GameSettings.getInstance();
        int boardN = settings.getBoardN();
        if (boardN < GameSettings.CHESS_BOARD_N_MIN) {
            boardN = settings.getBoardNMax();
            if (boardN > 7) {
                boardN = 7;
            }
            settings.setBoardN(boardN);
        }
        int paddingTop = GameSettings.getInstance().getBoardPaddingTop();

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenHeight = dm.heightPixels;
        int screenWidth = dm.widthPixels;

        int chessSize = (screenHeight - 2 * paddingTop) * 2 / (3 * boardN + 1);

        int xDelta = (int) (chessSize * Math.sqrt(3));
        int yDelta = chessSize * 3 / 2;
        int xOffset = xDelta / 2;

        int boardWidth = xDelta * boardN + xOffset * (boardN - 1);

        int leftTopX = (screenWidth - boardWidth) / 2;
        int leftTopY = paddingTop;

        mHexViews = new HexView[boardN * boardN + 1];
        for (int i = 1; i <= boardN; i++) {
            for (int j = 1; j <= boardN; j++) {
                int x = leftTopX + (j - 1) * xDelta + (i - 1) * xOffset;
                int y = leftTopY + (i - 1) * yDelta;
                int id = (i - 1) * boardN + j;
                mHexViews[id] = new HexView(this, chessSize, HexView.type(boardN, id));
                mHexViews[id].setX(x);
                mHexViews[id].setY(y);
                mHexViews[id].setTag(id);
                mHexViews[id].setOnClickListener(mHexChessOnClickListener);
                mRootLayout.addView(mHexViews[id]);
            }
        }
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
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int avatarSize = (int) (AVATAR_SIZE_DP * dm.density);
        mAvatarA = new Avatar(this, avatarSize, res.getColor(R.color.indigo_500), playerAAvatar);
        mAvatarB = new Avatar(this, avatarSize, res.getColor(R.color.pink_500), playerBAvatar);

//        playerA.setBackgroundDrawable(res.getDrawable(R.drawable.avatar_player));
//        int gameMode = mSettings.getGameMode();
//        int playerBDrawableId = gameMode == GameSettings.MODE_HUMAN_VS_ROBOT ? R.drawable.avatar_android : R.drawable.avatar_player;
//        playerB.setBackgroundDrawable(res.getDrawable(playerBDrawableId));


        int padding = GameSettings.getInstance().getBoardPaddingTop();
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(padding, dm.heightPixels - padding - 120);
//        mAvatarA.setLayoutParams(layoutParams);
        mAvatarAX = padding;
        mAvatarAY = dm.heightPixels - padding - avatarSize;
        mAvatarA.setX(mAvatarAX);
        mAvatarA.setY(mAvatarAY);
        mAvatarB.setX(dm.widthPixels - padding - avatarSize);
        mAvatarB.setY(padding);
        mRootLayout.addView(mAvatarA);
        mRootLayout.addView(mAvatarB);
        mAvatarA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuBar.setVisibility(mMenuBar.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
            }
        });
        mAvatarB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuBar.setVisibility(mMenuBar.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
            }
        });
    }

    private void initGameWizard() {
        mGameWizard = new GameWizard(this);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenHeight = dm.heightPixels;
        int screenWidth = dm.widthPixels;
        int menuWizardX = (int) (screenWidth - (48 + 320 + 10) * dm.density);
        int menuWizardY = (int) (20 * dm.density);
        int menuWizardYDelta = (int) (48 * dm.density);
        mGameWizard.addWizardPopupWindow(R.string.game_wizard_1_title, R.string.game_wizard_1_msg, -1, -1);
        mGameWizard.addWizardPopupWindow(R.string.game_wizard_2_title, R.string.game_wizard_2_msg, menuWizardX, screenHeight * 2 / 5);
        mGameWizard.addWizardPopupWindow(R.string.game_wizard_3_title, R.string.game_wizard_3_msg, menuWizardX, menuWizardY);
        mGameWizard.addWizardPopupWindow(R.string.game_wizard_4_title, R.string.game_wizard_4_msg, (int) (mAvatarAX + AVATAR_SIZE_DP * dm.density), (int) (mAvatarAY - 180 * dm.density));
        mGameWizard.addWizardPopupWindow(R.string.game_wizard_5_title, R.string.game_wizard_5_msg, menuWizardX, menuWizardY);
//        mGameWizard.addWizardPopupWindow(R.string.game_wizard_6_title, R.string.game_wizard_6_msg, menuWizardX, menuWizardY + menuWizardYDelta);
        mGameWizard.addWizardPopupWindow(R.string.game_wizard_7_title, R.string.game_wizard_7_msg, menuWizardX, menuWizardY + 1 * menuWizardYDelta, true);
        mGameWizard.setGameWizardListener(new GameWizard.GameWizardListener() {
            @Override
            public void onWizardShow(int id, boolean isLast) {
                switch(id) {
                    case 1:
                        break;
                    case 2:
                        showPlayerAWinWizard();
                        break;
                    case 3:
                        showPlayerBWinWizard();
                        break;
                    case 4:
                        mMenuBar.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onWizardOver() {
                mMenuBar.setVisibility(View.INVISIBLE);
                mGame.restart();
                for (int i = 1; i < mHexViews.length; i++) {
                    if (mHexViews[i].isOccupied()) {
                        mHexViews[i].reset();
                    }
                }

            }
        });
    }

    private void showPlayerAWinWizard() {
        int boardN = mSettings.getBoardN();
        int row = boardN / 2 - 1;
        int start = (row - 1) * boardN + 1;
        int end = start + boardN - 1;
        for (int i = start; i <= end; i++) {
            mHexViews[i].setOwner(Player.A);
        }
    }

    private void showPlayerBWinWizard() {
        int boardN = mSettings.getBoardN();
        int col = boardN / 2 - 1;
        for (int i = 0; i < boardN; i++) {
            mHexViews[col + i * boardN].setOwner(Player.B);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isWindowFocusFirstTime) {
            mGame.start();
            if (mIsAppFirstLaunched) {
                mGameWizard.show();
                mSettings.setVersionCode(BuildConfig.VERSION_CODE);
            }
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
            startActivity(new Intent(HexGameActivity.this, SettingsActivity.class));
            finish();
        }

        @Override
        public void onShareClick() {
            LogUtil.i(TAG, "Click Share");
        }

        @Override
        public void onGameHelpClick() {
            mGameWizard.show();
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
        GameResultDialogFragment.newInstance(this, winner).show(getFragmentManager(), "Game Result");
//        LogUtil.i(TAG, "A win!");
    }

    private boolean isAppFirstLaunched() {
        return mSettings.getVersionCode() < BuildConfig.VERSION_CODE;
    }
}
