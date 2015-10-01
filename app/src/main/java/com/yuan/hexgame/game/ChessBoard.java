package com.yuan.hexgame.game;

import com.yuan.hexgame.algorithm.UnionFind;
import com.yuan.hexgame.algorithm.WeightedQuickUnionPathCompressionUF;

import java.util.LinkedList;
import java.util.List;

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

    private ChessBoard() {

    }

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
        UnionFind uf = player == Player.A ? mUFA : mUFB;
        Iterable<Integer> adjs = getAdjacent(i);
        for (Integer adj : adjs) {
            if (mBoard[adj] == player) {
                uf.union(i, adj);
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
    public boolean isWinIfOwned(int i, Player player) {
        UnionFind uf = player == Player.A ? mUFA : mUFB;
        int start = uf.find(0);
        int end = uf.find(mNum + 1);
        boolean hasStart = false;
        boolean hasEnd = false;
        int current = uf.find(i);
        if (current == start) {
            hasStart = true;
        } else if (current == end) {
            hasEnd = true;
        }
        Iterable<Integer> adjs = getAdjacent(i);
        for (Integer adj : adjs) {
            if (uf.find(adj) == start) {
                hasStart = true;
            }
            if (uf.find(adj) == end) {
                hasEnd = true;
            }
            if (hasStart && hasEnd)
                return true;
        }
        return false;
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
        for (int i = 0; i <= mNum; i++) {
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

    private Iterable<Integer> getAdjacent(int i) {
        int row = (i - 1) / mN;
        int col = (i - 1) % mN;
        List<Integer> adjs = new LinkedList<>();
        if (i == 1) { // top-left
            adjs.add(i + 1);
            adjs.add(i + mN);
        } else if (i == mNum) { // bottom-right
            adjs.add(i - 1);
            adjs.add(i - mN);
        } else if (i == mN) { // top-right
            adjs.add(i - 1);
            adjs.add(i + mN - 1);
            adjs.add(i + mN);
        } else if (i == mNum - mN + 1) { // bottom-left
            adjs.add(i + 1);
            adjs.add(i - mN);
            adjs.add(i - mN + 1);
        } else if (row == 0) { // top
            adjs.add(i - 1);
            adjs.add(i + 1);
            adjs.add(i + mN);
            adjs.add(i + mN - 1);
        } else if (row == mN - 1) { // bottom
            adjs.add(i - 1);
            adjs.add(i + 1);
            adjs.add(i - mN);
            adjs.add(i - mN + 1);
        } else if (col == 0) { // left
            adjs.add(i - mN);
            adjs.add(i + mN);
            adjs.add(i - mN + 1);
            adjs.add(i + 1);
        } else if (col == mN - 1) { // right
            adjs.add(i - mN);
            adjs.add(i + mN);
            adjs.add(i + mN - 1);
            adjs.add(i - 1);
        } else {
            adjs.add(i - mN);
            adjs.add(i + mN);
            adjs.add(i - mN + 1);
            adjs.add(i + mN - 1);
            adjs.add(i - 1);
            adjs.add(i + 1);
        }
        return adjs;
    }

    @Override
    public ChessBoard clone() {
        ChessBoard board = new ChessBoard();
        board.mNum = mNum; // The amount of hex pieces.
        board.mN = mN;   // The board size is NxN
        board.mBoard = new Player[mNum + 1];
        for (int i = 0; i < mBoard.length; i++) {
            board.mBoard[i] = mBoard[i];
        }
        board.mUFA = mUFA.clone();
        board.mUFB = mUFB.clone();

        return board;
    }
}
