package com.yuan.hexgame.game;

/**
 * Created by Yuan Sun on 2015/9/13.
 */
public enum Player {
    A, B;
    public Player component() {
        return this.equals(A) ? B : A;
    }
}
