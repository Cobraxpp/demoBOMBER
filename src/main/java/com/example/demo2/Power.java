package com.example.demo2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.example.demo2.PowerType;
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
            if (tipo == PowerType.RANGO) {
                gc.setFill(Color.ORANGE);
            } else if (tipo == PowerType.VELOCIDAD) {
                gc.setFill(Color.AQUA);
            }
            gc.fillRect(x * Game.TILE_SIZE + 10, y * Game.TILE_SIZE + 10, Game.TILE_SIZE - 20, Game.TILE_SIZE - 20);
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

    public boolean isRecogido() {
        return recogido;
    }
}
