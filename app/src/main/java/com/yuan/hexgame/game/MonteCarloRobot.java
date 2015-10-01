package com.yuan.hexgame.game;

import java.util.Random;

/**
 * Created by Yuan Sun on 2015/9/13.
 */
public class MonteCarloRobot implements Robot {

    private static final int MONTE_CARLO_TEST_TIMES = 1000;

    private final Player mPlayer;
    private Player mCurrentPlayer;
    private Random random = new Random(System.currentTimeMillis());

    public MonteCarloRobot(Player player) {
        mPlayer = player;
        mCurrentPlayer = player;
    }

    @Override
    public int getChessPos(Board board) {
        int optimalPos = 0;
        float maxRobotWinProbability = 0f;
        for (int i = 1; i <= board.getChessNum(); i++) {
            // Test every unoccupied places.
            if (!board.isOccupied(i)) {
                int robotWinTimes = 0;
                // Test MONTE_CARLO_TEST_TIMES times, get the win times.
                for (int t = 0; t < MONTE_CARLO_TEST_TIMES; t++) {
                    Board testBoard = board.clone();
                    mCurrentPlayer = mPlayer;
                    testBoard.setOwner(i, mCurrentPlayer);
                    mCurrentPlayer = mCurrentPlayer.component();
                    while (!(testBoard.isAWin() || testBoard.isBWin())) {
                        int pos = randomGenPos(testBoard);
                        testBoard.setOwner(pos, mCurrentPlayer);
                        mCurrentPlayer = mCurrentPlayer.component();
                    }
                    if (testBoard.isBWin()) {
                        robotWinTimes++;
                    }
                }
                // Compute the win probability.
                float robotWinProbability = (float) robotWinTimes / MONTE_CARLO_TEST_TIMES;
                if (robotWinProbability >= maxRobotWinProbability) {
                    maxRobotWinProbability = robotWinProbability;
                    optimalPos = i;
                }
            }
        }
        return optimalPos;
    }

    private int randomGenPos(Board board) {
        int pos;
        do {
            pos = random.nextInt(board.getChessNum()) + 1;
        } while (board.isOccupied(pos));
        return pos;
    }
}
