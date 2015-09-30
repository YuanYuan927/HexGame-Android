package com.yuan.hexgame.game;

/**
 * Created by Yuan Sun on 2015/9/13.
 */
public interface Board {

    public int getChessNum();

    public void setOwner(int i, Player player);

    public Player getOwner(int i);

    public boolean isOccupied(int i);

    public boolean isAWin();

    public boolean isBWin();

    public void restart();
}
