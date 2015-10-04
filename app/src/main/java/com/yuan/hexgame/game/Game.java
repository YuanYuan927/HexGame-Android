package com.yuan.hexgame.game;

/**
 * Created by Yuan Sun on 2015/9/30.
 */
public interface Game {

    public void start();

    public void putPiece(int id);

    public boolean isAWin();

    public boolean isBWin();

    public void restart();

    public void setOnGameOverListener(OnGameOverListener onGameOverListener);

    public interface OnGameOverListener {
        public void onGameOver(Player winner);
    }
}
