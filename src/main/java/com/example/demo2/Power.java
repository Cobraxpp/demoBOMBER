package com.example.demo2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static com.example.demo2.Game.TILE_SIZE;

public class Power{

        private int x, y;
        private boolean recogido = false;
        private PowerType tipo;

        public Power(int x, int y, PowerType tipo) {
            this.x = x;
            this.y = y;
            this.tipo = tipo;
        }
    public void render(GraphicsContext gc) {
        if (!recogido) {
            Image Vel = null;
            Image Rank =null;
            if (tipo == PowerType.RANGO) {
                Rank = new Image(getClass().getResource("/com/example/demo2/ItemBlastRadius.png").toExternalForm());
                gc.setFill(Color.ORANGE);
            } else if (tipo == PowerType.VELOCIDAD) {
                Vel = new Image(getClass().getResource("/com/example/demo2/ItemSpeedIncrease.png").toExternalForm());
                gc.setFill(Color.AQUA);
            }
            gc.fillRect(x * TILE_SIZE + 10, y * TILE_SIZE + 10, TILE_SIZE - 20, TILE_SIZE - 20);
            gc.drawImage(Rank, x * TILE_SIZE + 10, y * TILE_SIZE + 10, TILE_SIZE - 20, TILE_SIZE - 20);
            gc.drawImage(Vel, x * TILE_SIZE + 10, y * TILE_SIZE + 10, TILE_SIZE - 20, TILE_SIZE - 20);

        }
    }

    public void checkRecolectado(Jugador jugador) {
        if (!recogido && jugador.getX() == x && jugador.getY() == y) {
            recogido = true;
            if (tipo == PowerType.RANGO) {
                jugador.aumentarRangoBomba();
                System.out.println("¡Rango de bomba aumentado!");
            } else if (tipo == PowerType.VELOCIDAD) {
                jugador.aumentarVelocidad();
                System.out.println("¡Velocidad aumentada!");
            }
        }
    }


}
