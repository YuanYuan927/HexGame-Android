package com.yuan.hexgame.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.yuan.hexgame.R;

/**
 * Created by Yuan Sun on 2015/9/30.
 */
public class GameSettings {

    public static final int CHESS_BOARD_N_MIN = 5;

    public static final int MODE_HUMAN_VS_ROBOT = 0;
    public static final int MODE_HUMAN_VS_HUMAN = 1;

    public static final int FIRST_PLAYER_A = 0;
    public static final int FIRST_PLAYER_B = 1;
    public static final int FIRST_PLAYER_RANDOM = 2;

    private static final String SETTINGS_KEY_CHESS_BOARD_N_MAX = "settings-key-chess-board-n-max";
    private static final String SETTINGS_KEY_BOARD_PADDING_TOP = "settings-key-padding-top";
    private static final String KEY_VERSION_CODE = "version-code";

    private final String SETTINGS_KEY_BOARD_N;
    private final String SETTINGS_KEY_GAME_MODE;
    private final String SETTINGS_KEY_FIRST_PLAYER;

    private Context mContext;
    private int mGameMode;
    private Player mWhoPlayFirst;
    private int mBoardSize;
    private int mPlayerAColor;
    private int mPlayerBColor;

    private static GameSettings mSettings;

    private GameSettings(Context context) {
        mContext = context;
        mGameMode = MODE_HUMAN_VS_HUMAN;
        mWhoPlayFirst = Player.A;
//        mBoardSize = ;
//        mPlayerAColor = ;
//        mPlayerBColor = ;
        Resources res = context.getResources();
        SETTINGS_KEY_BOARD_N = res.getString(R.string.settings_key_board_size);
        SETTINGS_KEY_GAME_MODE = res.getString(R.string.settings_key_game_mode);
        SETTINGS_KEY_FIRST_PLAYER = res.getString(R.string.settings_key_first_player);
    }

    public static GameSettings newInstance(Context context) {
        if (mSettings == null) {
            mSettings = new GameSettings(context);
        }
        return mSettings;
    }

    public static GameSettings getInstance() {
        return mSettings;
    }

    public void setBoardNMax(int n) {
        putInt(SETTINGS_KEY_CHESS_BOARD_N_MAX, n);
    }

    public int getBoardNMax() {
        return getInt(SETTINGS_KEY_CHESS_BOARD_N_MAX, CHESS_BOARD_N_MIN);
    }

    public void setBoardN(int n) {
        putString(SETTINGS_KEY_BOARD_N, String.valueOf(n));
    }

    public int getBoardN() {
        return Integer.valueOf(getString(SETTINGS_KEY_BOARD_N));
    }

    public int getGameMode() {
        return Integer.valueOf(getString(SETTINGS_KEY_GAME_MODE));
    }

//    public int getFirstPlayer() {
//        return Integer.valueOf(getString(SETTINGS_KEY_FIRST_PLAYER));
//    }

    public Player getFirstPlayer() {
        return mWhoPlayFirst;
    }

    public void setBoardPaddingTop(int paddingTop) {
        putInt(SETTINGS_KEY_BOARD_PADDING_TOP, paddingTop);
    }

    public int getBoardPaddingTop() {
        return getInt(SETTINGS_KEY_BOARD_PADDING_TOP, 0);
    }

    public void setVersionCode(int versionCode) {
        putInt(KEY_VERSION_CODE, versionCode);
    }

    public int getVersionCode() {
        return getInt(KEY_VERSION_CODE, 0);
    }

    private int getInt(String key, int defValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getInt(key, defValue);
    }

    private String getString(String key) {
        return getString(key, "0");
    }

    private String getString(String key, String defValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getString(key, defValue);
    }

    private void putInt(String key, int value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private void putString(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        editor.putString(key, value);
        editor.apply();
    }
}
