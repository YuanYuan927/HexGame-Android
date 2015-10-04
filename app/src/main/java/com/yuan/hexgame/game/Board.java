package com.yuan.hexgame.game;

/**
 * Created by Yuan Sun on 2015/9/13.
 */
public interface Board {

    public int getChessNum();

    public int getBoardN();

    public void setOwner(int i, Player player);

    public Player getOwner(int i);

    public boolean isOccupied(int i);

    /**
     * Used for Robot. Test whether wins if i is owned by him.
     *
     * @param i
     * @param player
     * @return
     */
    public boolean isWinIfOwned(int i, Player player);

    public boolean isAWin();

    public boolean isBWin();

    public void restart();

    public Board clone();
}
