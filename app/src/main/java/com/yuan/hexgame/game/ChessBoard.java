package com.yuan.hexgame.game;

import com.yuan.hexgame.algorithm.UnionFind;
import com.yuan.hexgame.algorithm.WeightedQuickUnionPathCompressionUF;

/**
 * Created by Yuan Sun on 2015/9/13.
 *
 * Player A : Left -> Right
 * Player B : Up   -> Down
 */
public class ChessBoard implements Board {

    private static final char A = 'A';
    private static final char B = 'B';
    private static final char EMPTY = 'O';

    private Player[] mBoard;
    private int num;
    private Player mFirstPlayer;
    private Player mCurrentPlayer;

    private UnionFind mUFA, mUFB;

    public ChessBoard(int n, Player firstPlayer) {
        this.num = n * n;
        mBoard = new Player[num + 1];

        mUFA = new WeightedQuickUnionPathCompressionUF(num + 2);
        mUFB = new WeightedQuickUnionPathCompressionUF(num + 2);
        for (int i = 0; i < n; i++) {
            mUFA.union(0, i * n + 1);
            mUFA.union(n + 1, (i + 1) * n);
            mUFB.union(0, i + 1);
            mUFB.union(n + 1, (n - 1) * n + i + 1);
        }

        mFirstPlayer = firstPlayer;
        mCurrentPlayer = firstPlayer;
    }

    @Override
    public void setFirstPlayer(Player player) {
        mFirstPlayer = player;
    }

    @Override
    public Player getFirstPlayer() {
        return mFirstPlayer;
    }

    @Override
    public void setOwner(int i, Player player) {
        // TODO
        validate(i);
        if (player == mCurrentPlayer)
            return;
        mBoard[i] = player;
        mCurrentPlayer = player;
    }

    @Override
    public Player getOwner(int i) {
        validate(i);
        return mBoard[i];
    }

    @Override
    public boolean isOccupied(int i) {
        validate(i);
        return mBoard[i] != null;
    }

    @Override
    public boolean isAWin() {
        return mUFA.isConnected(0, num + 1);
    }

    @Override
    public boolean isBWin() {
        return mUFB.isConnected(0, num + 1);
    }

    private void validate(int i) {
        if (i < 1 || i > num) {
            throw new IndexOutOfBoundsException("Index " + i + " is not between 1 and " + num);
        }
    }
}
