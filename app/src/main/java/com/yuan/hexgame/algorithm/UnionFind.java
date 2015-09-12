package com.yuan.hexgame.algorithm;

/**
 * Created by Yuan Sun on 2015/9/13.
 */
public interface UnionFind {

    public void union(int i, int j);

    public int find(int i);

    /**
     * Are component i and j in the same component?
     * @param i
     * @param j
     * @return
     */
    public boolean isConnected(int i, int j);

    /**
     * number of components
     * @return
     */
    public int count();
}
