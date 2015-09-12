package com.yuan.hexgame.game;

/**
 * Created by Yuan Sun on 2015/9/13.
 */
public interface Board {

    public void setFirstPlayer(Player player);

    public Player getFirstPlayer();

    public void setOwner(int i, Player player);

    public Player getOwner(int i);

    public boolean isOccupied(int i);

    public boolean isAWin();

    public boolean isBWin();
}
