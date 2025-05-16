package com.example.demo2;

public class Bomb {
    private int x, y;
    private long placedTime;
    private final long fuse = 2000; // ms

    public Bomb(int x, int y, long time) {
        this.x = x;
        this.y = y;
        this.placedTime = time;
    }

    public boolean shouldExplode() {
        return System.currentTimeMillis() - placedTime > fuse;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
