package com.yuan.hexgame.game;

/**
 * Created by Yuan Sun on 2015/9/30.
 */
public class GameSettings {

    public static final int MODE_HUMAN_VS_ROBOT = 0;
    public static final int MODE_HUMAN_VS_HUMAN = 1;

    public static final int FIRST_PLAYER_A = 0;
    public static final int FIRST_PLAYER_B = 1;
    public static final int FIRST_PLAYER_RANDOM = 2;

    private int mGameMode;
    private Player mWhoPlayFirst;
    private int mBoardSize;
    private int mPlayerAColor;
    private int mPlayerBColor;

    private static GameSettings mSettings = new GameSettings();

    private GameSettings() {
        mGameMode = MODE_HUMAN_VS_ROBOT;
        mWhoPlayFirst = Player.A;
//        mBoardSize = ;
//        mPlayerAColor = ;
//        mPlayerBColor = ;
    }

    public static GameSettings getInstance() {
        return mSettings;
    }

    public int getGameMode() {
        return mGameMode;
    }

    public Player getFirstPlayer() {
        return mWhoPlayFirst;
    }
}
