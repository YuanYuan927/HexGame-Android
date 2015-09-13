package com.yuan.hexgame.game;

/**
 * Created by Yuan Sun on 2015/9/13.
 */
public interface Robot {

    /**
     * Set which player is Robot
     * @param player
     */
    public void setPlayer(Player player);

    /**
     * Get the position where Robot puts the hex piece
     * @param board
     * @return
     */
    public int getChessPos(Board board);
}
