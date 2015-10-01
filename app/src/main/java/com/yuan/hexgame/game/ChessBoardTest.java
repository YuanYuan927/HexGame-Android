package com.yuan.hexgame.game;

import com.yuan.hexgame.util.LogUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Yuan Sun on 2015/10/2.
 */
public class ChessBoardTest {

    private static final int TEST_TIMES = 1000;
    private Random random = new Random(System.currentTimeMillis());

    public ChessBoardTest(Board board) {
        List<Integer> ids = new LinkedList<>();
        for (int t = 0; t < TEST_TIMES; t++) {
            Board testBoard = board.clone();
            Player current = Player.A;
            for (int i = 1; i <= testBoard.getChessNum(); i++) {
                int pos = randomGenPos(testBoard);
                ids.add(pos);
                testBoard.setOwner(pos, current);
                current = current.component();
            }
            if (!(testBoard.isAWin() || testBoard.isBWin())) {
                LogUtil.i("Test", "Test Fail");
                LogUtil.i("Test", "Fail case: " + ids);
            }
            ids.clear();
        }
        LogUtil.i("Test", "Test Over");
    }

    private int randomGenPos(Board board) {
        int pos;
        do {
            pos = random.nextInt(board.getChessNum()) + 1;
        } while (board.isOccupied(pos));
        return pos;
    }
}
