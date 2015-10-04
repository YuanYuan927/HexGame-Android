package com.yuan.hexgame.game;

/**
 * Created by Yuan Sun on 2015/9/13.
 */
public interface Robot {

    /**
     * Get the position where Robot puts the hex piece
     * @param board
     * @return
     */
    public int getChessPos(Board board);

    public void compute(Board board, RobotStatusListener robotStatusListener);

    public interface RobotStatusListener {
        public void onStart();
        public void onCompleted(int optimalPos);
    }
}
