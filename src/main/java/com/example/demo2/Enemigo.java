package com.example.demo2;

import java.util.Random;

public class Enemigo {
    private String nombre;
    private int x, y;
    private boolean estaVivo;
    private long MoveTime = 0;  // Tiempo del último movimiento
    private static final long MoveEnemigo = 500_000_000; // Retraso de 500ms para el enemigo 1
    private Game game;

    public Enemigo(String nombre, int x, int y, Game game) {
        this.nombre = nombre;
        this.x = x;
        this.y = y;
        this.estaVivo = true;
        this.game = game;
    }


    public void verificarGolpeJugador(Jugador jugador, long tiempoActual) {
        if (this.estaVivo() && jugador.getX() == this.getX() && jugador.getY() == this.getY()) {
            jugador.perderVida(tiempoActual);
        }
    }



    public void serGolpeadoPorBomba() {
        if (estaVivo) {
            estaVivo = false;
            System.out.println(nombre + " ha sido destruido por una bomba. ¡+1!");
        }
    }


    public void moverAleatoriamente(int maxX, int maxY, Block[][] mapa) {
        if (!estaVivo) return;

        if (System.nanoTime() - MoveTime < MoveEnemigo) {
            return;
        }

        Random rand = new Random();
        int direccion = rand.nextInt(4);
        int nuevoX = x, nuevoY = y;

        switch (direccion) {
            case 0: // Arriba
                nuevoY--;
                break;
            case 1: // Abajo
                nuevoY++;
                break;
            case 2: // Izquierda
                nuevoX--;
                break;
            case 3: // Derecha
                nuevoX++;
                break;
        }


        if (nuevoX >= 0 && nuevoY >= 0 && nuevoX < maxX && nuevoY < maxY &&
                mapa[nuevoY][nuevoX] != Block.WALL &&
                mapa[nuevoY][nuevoX] != Block.DESTRUCTIBLE &&
                !game.isBombAt(nuevoX, nuevoY)) {
            x = nuevoX;
            y = nuevoY;
        }


        MoveTime = System.nanoTime();
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean estaVivo() {
        return estaVivo;
    }
}
