package com.yuan.hexgame.ui.widget;

import android.content.Context;

import com.yuan.hexgame.game.Player;

/**
 * A proxy of HexView
 * Created by Yuan Sun on 2015/9/10.
 */
public class HexChessView implements HexChess {

    private HexView mHexView;

    public HexChessView(Context context, int size, float x, float y) {
        mHexView = new HexView(context, size);
        mHexView.setX(x);
        mHexView.setX(y);
    }

    @Override
    public void setOwner(Player player) {
        mHexView.setOwner(player);
    }

    @Override
    public Player getOwner() {
        return mHexView.getOwner();
    }

    @Override
    public boolean isOccupied() {
        return mHexView.isOccupied();
    }

    @Override
    public void reset() {
        mHexView.reset();
    }
}
