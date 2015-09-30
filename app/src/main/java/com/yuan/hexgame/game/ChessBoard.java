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

    private Player[] mBoard;
    private int mNum; // The amount of hex pieces.
    private int mN;   // The board size is NxN

    private UnionFind mUFA, mUFB;

    public ChessBoard(int n) {
        this.mN = n;
        this.mNum = n * n;
        mBoard = new Player[mNum + 1];

        mUFA = new WeightedQuickUnionPathCompressionUF(mNum + 2);
        mUFB = new WeightedQuickUnionPathCompressionUF(mNum + 2);
        for (int i = 0; i < n; i++) {
            mUFA.union(0, i * n + 1);
            mUFA.union(mNum + 1, (i + 1) * n);
            mUFB.union(0, i + 1);
            mUFB.union(mNum + 1, (n - 1) * n + i + 1);
        }
    }

    @Override
    public int getChessNum() {
        return mNum;
    }

    @Override
    public void setOwner(int i, Player player) {
        validate(i);
        if (mBoard[i] != null)
            return;
        mBoard[i] = player;
        int row = (i - 1) / mN;
        int col = (i - 1) % mN;
        UnionFind uf = player == Player.A ? mUFA : mUFB;
        if (i == 1) { // top-left
            if (mBoard[i + 1] == player) {
                uf.union(i, i + 1);
            }
            if (mBoard[i + mN] == player) {
                uf.union(i, i + mN);
            }
        } else if (i == mNum) { // bottom-right
            if (mBoard[i - 1] == player) {
                uf.union(i, i - 1);
            }
            if (mBoard[i - mN] == player) {
                uf.union(i, i - mN);
            }
        } else if (i == mN) { // top-right
            if (mBoard[i - 1] == player) {
                uf.union(i, i - 1);
            }
            if (mBoard[i + mN - 1] == player) {
                uf.union(i, i + mN - 1);
            }
            if (mBoard[i + mN] == player) {
                uf.union(i, i + mN);
            }
        } else if (i == mNum - mN + 1) { // bottom-left
            if (mBoard[i + 1] == player) {
                uf.union(i, i + 1);
            }
            if (mBoard[i - mN] == player) {
                uf.union(i, i - mN);
            }
            if (mBoard[i - mN + 1] == player) {
                uf.union(i, i - mN + 1);
            }
        } else if (row == 0) { // top
            if (mBoard[i - 1] == player) {
                uf.union(i, i - 1);
            }
            if (mBoard[i + 1] == player) {
                uf.union(i, i + 1);
            }
            if (mBoard[i + mN] == player) {
                uf.union(i, i + mN);
            }
            if (mBoard[i + mN + 1] == player) {
                uf.union(i, i + mN + 1);
            }
        } else if (row == mN - 1) { // bottom
            if (mBoard[i - 1] == player) {
                uf.union(i, i - 1);
            }
            if (mBoard[i + 1] == player) {
                uf.union(i, i + 1);
            }
            if (mBoard[i - mN] == player) {
                uf.union(i, i - mN);
            }
            if (mBoard[i - mN + 1] == player) {
                uf.union(i, i - mN + 1);
            }
        } else if (col == 0) { // left
            if (mBoard[i - mN] == player) {
                uf.union(i, i - mN);
            }
            if (mBoard[i + mN] == player) {
                uf.union(i, i + mN);
            }
            if (mBoard[i - mN + 1] == player) {
                uf.union(i, i - mN + 1);
            }
            if (mBoard[i + 1] == player) {
                uf.union(i, i + 1);
            }
        } else if (col == mN - 1) { // right
            if (mBoard[i - mN] == player) {
                uf.union(i, i - mN);
            }
            if (mBoard[i + mN] == player) {
                uf.union(i, i + mN);
            }
            if (mBoard[i - mN - 1] == player) {
                uf.union(i, i - mN - 1);
            }
            if (mBoard[i - 1] == player) {
                uf.union(i, i - 1);
            }
        } else {
            if (mBoard[i - mN] == player) {
                uf.union(i, i - mN);
            }
            if (mBoard[i + mN] == player) {
                uf.union(i, i + mN);
            }
            if (mBoard[i - mN + 1] == player) {
                uf.union(i, i - mN + 1);
            }
            if (mBoard[i + mN - 1] == player) {
                uf.union(i, i + mN - 1);
            }
            if (mBoard[i - 1] == player) {
                uf.union(i, i - 1);
            }
            if (mBoard[i + 1] == player) {
                uf.union(i, i + 1);
            }
        }
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
        return mUFA.isConnected(0, mNum + 1);
    }

    @Override
    public boolean isBWin() {
        return mUFB.isConnected(0, mNum + 1);
    }

    @Override
    public void restart() {
        for (int i = 0; i < mNum; i++) {
            mBoard[i] = null;
        }
        mUFA = new WeightedQuickUnionPathCompressionUF(mNum + 2);
        mUFB = new WeightedQuickUnionPathCompressionUF(mNum + 2);
        for (int i = 0; i < mN; i++) {
            mUFA.union(0, i * mN + 1);
            mUFA.union(mNum + 1, (i + 1) * mN);
            mUFB.union(0, i + 1);
            mUFB.union(mNum + 1, (mN - 1) * mN + i + 1);
        }
    }

    private void validate(int i) {
        if (i < 1 || i > mNum) {
            throw new IndexOutOfBoundsException("Index " + i + " is not between 1 and " + mNum);
        }
    }
}
