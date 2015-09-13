package com.yuan.hexgame.game;

import android.view.MotionEvent;

import java.util.Random;

/**
 * Created by Yuan Sun on 2015/9/13.
 */
public class MonteCarloRobot implements Robot {

    private Player mPlayer;

    public MonteCarloRobot(Player player) {
        mPlayer = player;
    }

    @Override
    public void setPlayer(Player player) {
        mPlayer = player;
    }

    @Override
    public int getChessPos(Board board) {
        Random random = new Random(System.currentTimeMillis());
        int optimalPos;
        do {
            optimalPos = random.nextInt(board.getChessNum()) + 1;
        } while (board.isOccupied(optimalPos));
        return optimalPos;
    }
}
