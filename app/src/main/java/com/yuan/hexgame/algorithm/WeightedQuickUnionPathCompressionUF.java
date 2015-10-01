package com.yuan.hexgame.algorithm;

/**
 * Created by Yuan Sun on 2015/9/13.
 */
public class WeightedQuickUnionPathCompressionUF implements UnionFind {

    private int[] parent;
    private int[] size;
    private int count;

    private WeightedQuickUnionPathCompressionUF() {

    }

    public WeightedQuickUnionPathCompressionUF(int n) {
        this.count = n;
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    @Override
    public void union(int i, int j) {
        int root1 = find(i);
        int root2 = find(j);
        if (root1 == root2)
            return;
        if (size[root1] >= size[root2]) {
            parent[root2] = root1;
            size[root1] += size[root2];
        } else {
            parent[root1] = root2;
            size[root2] += size[root1];
        }
        count--;
    }

    @Override
    public int find(int i) {
        validate(i);
        int root = i;
        while (parent[root] != root) {
            root = parent[root];
        }
        while (i != root) {
            int j = parent[i];
            parent[i] = root;
            i = j;
        }
        return root;
    }

    @Override
    public boolean isConnected(int i, int j) {
        return find(i) == find(j);
    }

    @Override
    public int count() {
        return count;
    }

    private void validate(int i) {
        int n = parent.length;
        if (i < 0 || i >= parent.length) {
            throw new IndexOutOfBoundsException("Index " + i + " is not between 0 and " + (n - 1));
        }
    }

    @Override
    public WeightedQuickUnionPathCompressionUF clone() {
        WeightedQuickUnionPathCompressionUF uf = new WeightedQuickUnionPathCompressionUF();
        int n = parent.length;
        uf.parent = new int[n];
        uf.size = new int[n];
        uf.count = count;
        for (int i = 0; i < n; i++) {
            uf.parent[i] = parent[i];
            uf.size[i] = size[i];
        }
        return uf;
    }
}
