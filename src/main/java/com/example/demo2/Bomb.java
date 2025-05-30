package com.example.demo2;

public class Bomb {
    private int x, y;
    private long Tiempo;
    private final long tiempoExplosion = 2000; // ms

    public Bomb(int x, int y, long time) {
        this.x = x;
        this.y = y;
        this.Tiempo = time;
    }

    public boolean Explotar() {
        return System.currentTimeMillis() - Tiempo > tiempoExplosion;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
