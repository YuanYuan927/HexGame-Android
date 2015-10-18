package com.yuan.hexgame.util;

import java.util.Random;

/**
 * Created by Yuan Sun on 2015/10/18.
 */
public class RandomUtil {

    private static Random sRandom = new Random(System.currentTimeMillis());

    private RandomUtil() {}

    public static int getInt(int n) {
        return sRandom.nextInt(n);
    }
}
