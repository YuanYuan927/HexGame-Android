package com.yuan.hexgame.game;

import android.os.Handler;
import android.os.Message;

import com.yuan.hexgame.util.LogUtil;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Yuan Sun on 2015/9/13.
 */
public class MonteCarloRobot implements Robot {
    private static final String TAG = "MonteCarloRobot";
    private static final int MONTE_CARLO_TEST_TIMES = 200;

    private final Player mPlayer;
    private Player mCurrentPlayer;
    private Random random = new Random(System.currentTimeMillis());

    private static InternalHandler sHandler = new InternalHandler();

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

    /**
     * Create N threads to compute the optimal position.
     *
     * @param board
     * @param robotStatusListener
     */
    @Override
    public void compute(final Board board, final RobotStatusListener robotStatusListener) {
        if (robotStatusListener != null) {
            robotStatusListener.onStart();;
        }
        final int N = board.getBoardN();
        final ComputeResult[] computeResults = new ComputeResult[N];
        final CountDownLatch latch = new CountDownLatch(N);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int optimalPos = 0;
                float maxRobotWinProbability = 0f;
                for (int i = 1; i <= N; i++) {
                    final int row = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LogUtil.i(TAG, "Thread-Row-" + row + " is computing.");
                            computeResults[row - 1] = compute(board, row);
                            latch.countDown();
                        }
                    }, "Thread-Row-" + row).start();
                }
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    LogUtil.i(TAG, e.getMessage());
                }
                LogUtil.i(TAG, N + " threads compute completely.");
                for (int i = 0; i < N; i++) {
                    if (computeResults[i] != null) {
                        if (computeResults[i].probability > maxRobotWinProbability) {
                            maxRobotWinProbability = computeResults[i].probability;
                            optimalPos = computeResults[i].pos;
                        }
                    }
                }
                Message msg = Message.obtain();
                msg.what = optimalPos;
                msg.obj = robotStatusListener;
                sHandler.sendMessage(msg);
            }
        }).start();
    }

    private ComputeResult compute(Board board, int row) {
        int optimalPos = 0;
        int n = board.getBoardN();
        float maxRobotWinProbability = 0f;
        int start = (row - 1) * n + 1;
        int end = row * n;
        for (int i = start; i <= end; i++) {
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
        return new ComputeResult(optimalPos, maxRobotWinProbability);
    }

    private class ComputeResult {
        private int pos;
        private float probability;

        public ComputeResult(int pos, float probability) {
            this.pos = pos;
            this.probability = probability;
        }

    }

    private static class InternalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int optimalPos =  msg.what;
            RobotStatusListener robotStatusListener = (RobotStatusListener) msg.obj;
            if (robotStatusListener != null) {
                robotStatusListener.onCompleted(optimalPos);
            }
        }
    }

    private int randomGenPos(Board board) {
        int pos;
        do {
            pos = random.nextInt(board.getChessNum()) + 1;
        } while (board.isOccupied(pos));
        return pos;
    }
}
