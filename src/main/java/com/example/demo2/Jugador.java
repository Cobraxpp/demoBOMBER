package com.example.demo2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.Set;

public class Jugador {
    private int x, y;
    private int bombaRango = 1;
    private int velocidad =4;
    private Game game;
    private int vidas = 3;
    private long tiempoUltimoDaño = 0;
    private static final long INVULNERABILIDAD_MS = 1000; // 1 segundo de invulnerabilidad



    public Jugador(int x, int y, Game game) {
        this.x = x;
        this.y = y;
        this.game = game;
    }



    public void update(Set<KeyCode> keys, Block[][] map) {
        if (keys.contains(KeyCode.W)) move(0, -1, map);
        else if (keys.contains(KeyCode.S)) move(0, 1, map);
        else if (keys.contains(KeyCode.A)) move(-1, 0, map);
        else if (keys.contains(KeyCode.D)) move(1, 0, map);
    }
    public int getBombaRango() {
        return bombaRango;
    }
    public void aumentarRangoBomba() {
        if (bombaRango < 5) {
            bombaRango++;
        }
    }
    public void aumentarVelocidad() {
        if (velocidad < 8) {
            velocidad++;
        }
    }

    public int getVelocidad() {
        return velocidad;
    }


    public int getVidas() {
        return vidas;
    }
    public void perderVida(long tiempoActual) {
        if (tiempoActual - tiempoUltimoDaño > INVULNERABILIDAD_MS) {
            if (vidas > 0) {
                vidas--;
                System.out.println("¡Has perdido una vida! Vidas restantes: " + vidas);
            }
            tiempoUltimoDaño = tiempoActual;
        }
    }

    public void reiniciarVidas() {
        vidas = 3;
    }

    private void move(int dx, int dy, Block[][] map) {
        int nx = x + dx;
        int ny = y + dy;
        if (nx >= 0 && ny >= 0 && nx < map[0].length && ny < map.length &&
                map[ny][nx] == Block.EMPTY &&
                !game.isBombAt(nx, ny)) {
            x = nx;
            y = ny;
        }
    }


    public void render(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillOval(x * Game.TILE_SIZE + 5, y * Game.TILE_SIZE + 5, Game.TILE_SIZE - 10, Game.TILE_SIZE - 10);
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
