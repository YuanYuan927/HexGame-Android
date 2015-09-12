package com.yuan.hexgame.ui.widget;

import com.yuan.hexgame.game.Player;

/**
 * Created by Yuan Sun on 2015/9/10.
 */
public interface HexChess {

    public void setOwner(Player player);

    public Player getOwner();

    public boolean isOccupied();

    public void reset();
}
