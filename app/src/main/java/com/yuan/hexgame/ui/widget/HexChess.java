package com.yuan.hexgame.ui.widget;

/**
 * Created by Yuan Sun on 2015/9/10.
 */
public interface HexChess {

    public enum Player {A, B}

    public void setOwner(Player player);

    public Player getOwner();

    public boolean isOccupied();

    public void reset();
}
