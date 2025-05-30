package com.example.demo2;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

public class Game {
    private boolean gameOver = false;
    private StackPane root;
    private List<int[]> explosionLugar = new ArrayList<>();
    public static final int TILE_SIZE = 40;
    public static final int WIDTH = 15;
    public static final int HEIGHT = 13;
    private final String playerName;
    private Jugador jugador;
    private List<Power> powerUps = new ArrayList<>();
    private List<Bomb> bombs = new ArrayList<>();
    private Block[][] map = new Block[HEIGHT][WIDTH];
    private long ultimoMovimiento = 0;
    private List<Enemigo> enemigos = new ArrayList<>();
    private static final long tiempoMovimiento = 150_000_000;
    private long crono;
    private boolean victory = false;
    private int bloquesRestantes;
    private int bloqueDestruido=0;

    private Runnable onVolver;



    public Game(String playerName) {
        this.playerName = playerName;
    }

    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        crono = System.currentTimeMillis();

        root = new StackPane(canvas);
        Scene scene = new Scene(root);

        initMap();

        jugador = new Jugador(1, 1 ,this);
        enemigos.add(new Enemigo("Enemigo 1", 5, 5,this));
        enemigos.add(new Enemigo("Enemigo 2", 7, 7,this
        ));
        Set<KeyCode> teclaPress = new HashSet<>();

        scene.setOnKeyPressed(e -> teclaPress.add(e.getCode()));
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                bombs.add(new Bomb(jugador.getX(), jugador.getY(), System.currentTimeMillis()));
            }
            teclaPress.remove(e.getCode());
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(teclaPress, now);
                render(gc);
            }
        }.start();

        stage.setTitle("Bomberman Base");
        stage.setScene(scene);
        stage.show();
    }


    private void initMap() {
        Random rand = new Random();
        bloquesRestantes = 0; // Inicializar en 0

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (x == 0 || y == 0 || x == WIDTH - 1 || y == HEIGHT - 1 || (x % 2 == 0 && y % 2 == 0)) {
                    map[y][x] = Block.WALL;
                } else {
                    if (rand.nextDouble() < 0.3 && !(x <= 2 && y <= 2)) {
                        map[y][x] = Block.DESTRUCTIBLE;
                        bloquesRestantes++;
                    } else {
                        map[y][x] = Block.EMPTY;
                    }
                }
            }
        }

        int powers = 0;
        while (powers < 3) {
            int x = rand.nextInt(WIDTH);
            int y = rand.nextInt(HEIGHT);

            if (map[y][x] == Block.EMPTY && !(x <= 2 && y <= 2)) {
                PowerType tipo = rand.nextBoolean() ? PowerType.RANGO : PowerType.VELOCIDAD;
                powerUps.add(new Power(x, y, tipo));
                powers++;
            }
        }
    }
    public boolean isBombAt(int x, int y) {
        for (Bomb bomb : bombs) {
            if (bomb.getX() == x && bomb.getY() == y) {
                return true;
            }
        }
        return false;
    }




    private void update(Set<KeyCode> keys, long now) {
        for (Power p : powerUps) {
            p.checkRecolectado(jugador);
        }

        if (now - ultimoMovimiento > tiempoMovimiento) {
            jugador.update(keys, map);
            ultimoMovimiento = now;
        }
        for (Enemigo enemigo : enemigos) {
            enemigo.moverAleatoriamente(WIDTH, HEIGHT ,map);
            enemigo.verificarGolpeJugador(jugador, now);
        }
        if (!victory && quedanBloquesDestructibles() == 0) {
            victory = true;
            mostrarVictoria((Stage) root.getScene().getWindow());
        }

        bombs.removeIf(bomb -> {
            if (bomb.Explotar()) {
                explode(bomb.getX(), bomb.getY());
                return true;
            }
            return false;
        });
        if (gameOver) return;

    }

    private void explode(int x, int y) {
        int rango = jugador.getBombaRango();
        long tiempoActual = System.currentTimeMillis();
        List<int[]> explosionArea = new ArrayList<>();
        explosionArea.add(new int[]{x, y});
        destruirBloque(x, y);
        verificarGolpeEnExplosion(x, y, tiempoActual);
        dañarEnemigosEn(x, y);


        // Explosión en 4 direcciones
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] d : dirs) {
            for (int i = 1; i <= rango; i++) {
                int nx = x + d[0] * i;
                int ny = y + d[1] * i;
                if (nx < 0 || ny < 0 || nx >= WIDTH || ny >= HEIGHT) break;

                if (map[ny][nx] == Block.WALL) break;

                explosionArea.add(new int[]{nx, ny});
                destruirBloque(nx, ny);
                verificarGolpeEnExplosion(nx, ny, tiempoActual);
                dañarEnemigosEn(nx, ny);



                if (map[ny][nx] == Block.DESTRUCTIBLE) break;
            }
        }

        // Añade las casillas al área de explosión visual
        explosionLugar.addAll(explosionArea);

        // Después de 300ms se limpian para que desaparezca el efecto visual
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                explosionLugar.removeAll(explosionArea);
            }
        }, 300);

    }
    private void verificarGolpeEnExplosion(int x, int y, long tiempoActual) {
        if (jugador.getX() == x && jugador.getY() == y) {
            jugador.perderVida(tiempoActual); // Se controla con invulnerabilidad
            System.out.println("¡Explosión alcanzó al jugador! Vidas: " + jugador.getVidas());

            if (jugador.getVidas() <= 0) {
                System.out.println("¡Has perdido!");
                mostrarGameOver((Stage) root.getScene().getWindow()); // Llama al método
            }
        }
    }

    private int quedanBloquesDestructibles() {
        int count = 0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (map[y][x] == Block.DESTRUCTIBLE) {
                    count++;
                }
            }
        }
        return count;
    }

    private void dañarEnemigosEn(int x, int y) {
        for (Enemigo enemigo : enemigos) {
            if (enemigo.estaVivo() && enemigo.getX() == x && enemigo.getY() == y) {
                enemigo.serGolpeadoPorBomba();  //
            }
        }
    }



    private void destruirBloque(int x, int y) {
        if (map[y][x] == Block.DESTRUCTIBLE) {
            map[y][x] = Block.EMPTY;
            bloquesRestantes--;
            bloqueDestruido++;
        }
    }
    private void guardarPartida() {
        int puntuacion =bloqueDestruido;
        long duracion = (System.currentTimeMillis() - crono) ;
        registrarPartidaJugador(playerName, puntuacion, duracion);
    }
    public static void registrarPartidaJugador(String nombre, int bloqueDestruido, long duracionSegundos) {
       int puntuacion =bloqueDestruido;
        String sql = """
        UPDATE jugadores 
        SET puntuacion = ?, tiempo = ? 
        WHERE id = (
            SELECT id FROM (
                SELECT id FROM jugadores WHERE nombre = ? ORDER BY id DESC LIMIT 1
            ) AS sub
        )
    """;

        try (Connection conn = ConexionBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, puntuacion);
            stmt.setTime(2, new java.sql.Time(duracionSegundos));
            stmt.setString(3, nombre);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setOnVolver(Runnable onVolver) {
        this.onVolver = onVolver;
    }



    private void mostrarVictoria(Stage stage) {
        gameOver = true;
        guardarPartida();
        javafx.scene.control.Label victoryLabel = new javafx.scene.control.Label("¡HAS GANADO!");
        victoryLabel.setStyle("-fx-font-size: 40px; -fx-text-fill: green;");

        javafx.scene.control.Button reiniciarBtn = new javafx.scene.control.Button("Reiniciar");
        reiniciarBtn.setOnAction(e -> {
            Game nuevoJuego = new Game(playerName);
            nuevoJuego.start(stage);
        });

        VBox victoryBox = new VBox(20, victoryLabel, reiniciarBtn);
        victoryBox.setStyle("-fx-alignment: center;");
        root.getChildren().add(victoryBox);

    }

    private void mostrarGameOver(Stage stage) {
        gameOver = true;
        guardarPartida();

        javafx.scene.control.Label gameOverLabel = new javafx.scene.control.Label("GAME OVER");
        gameOverLabel.setStyle("-fx-font-size: 40px; -fx-text-fill: red;");

        javafx.scene.control.Button reiniciarBtn = new javafx.scene.control.Button("Reiniciar");
        reiniciarBtn.setOnAction(e -> {
            Game nuevoJuego = new Game(playerName);
            nuevoJuego.start(stage);
        });

        javafx.scene.control.Button btnVolver = new javafx.scene.control.Button("Volver");
        btnVolver.setOnAction(e -> {
            if (onVolver != null) {
                onVolver.run();
            }
        });

        VBox gameOverBox = new VBox(20, gameOverLabel, reiniciarBtn, btnVolver);
        gameOverBox.setStyle("-fx-alignment: center;");
        root.getChildren().add(gameOverBox);
    }



    private void render(GraphicsContext gc) {
        Image bloqueImagen = new Image(getClass().getResource("/com/example/demo2/Ground.png").toExternalForm());


        gc.fillRect(0, 0, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);

        gc.drawImage(bloqueImagen,0,0, 15 * TILE_SIZE, 15 * TILE_SIZE);
        long elapsedMillis = System.currentTimeMillis() - crono;
        long seconds = (elapsedMillis / 1000) % 60;
        long minutes = (elapsedMillis / 1000) / 60;
        String tiempo = String.format("Tiempo: %02d:%02d", minutes, seconds);



        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (map[y][x] == Block.WALL) {
                    Image Bloque = new Image(getClass().getResource("/com/example/demo2/Block.png").toExternalForm());
                    gc.setFill(Color.DARKGRAY);
                    gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    gc.drawImage(Bloque,x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                } else if (map[y][x] == Block.DESTRUCTIBLE) {
                    Image Destru = new Image(getClass().getResource("/com/example/demo2/Brick.png").toExternalForm());

                    gc.setFill(Color.BROWN);
                    gc.fillRect(x * TILE_SIZE + 4, y * TILE_SIZE + 4, TILE_SIZE - 8, TILE_SIZE - 8);
                    gc.drawImage(Destru,x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        for (Bomb bomb : bombs) {
            Image Bombaima = new Image(getClass().getResource("/com/example/demo2/bombing.png").toExternalForm());


            gc.drawImage(Bombaima,bomb.getX() * TILE_SIZE , bomb.getY() * TILE_SIZE , TILE_SIZE - 1, TILE_SIZE - 1);
        }

        jugador.render(gc);
        for (Enemigo enemigo : enemigos) {
            if (enemigo.estaVivo()) {

                gc.setFill(Color.YELLOW);
                Image Enemigoimg = new Image(getClass().getResource("/com/example/demo2/Enemigos.png").toExternalForm());

               // gc.fillOval(enemigo.getX() * TILE_SIZE + 8, enemigo.getY() * TILE_SIZE + 8, TILE_SIZE - 16, TILE_SIZE - 16);
                gc.drawImage(Enemigoimg,enemigo.getX() * TILE_SIZE + 8, enemigo.getY() * TILE_SIZE + 8, TILE_SIZE - 6, TILE_SIZE - 1);
            }
        }
        for (Power p : powerUps) {
            p.render(gc);
        }
        // Dibujar la explosión en rojo
        for (int[] tile : explosionLugar) {
            int ex = tile[0];
            int ey = tile[1];

            gc.setFill(Color.RED);
            gc.fillRect(ex * TILE_SIZE + 4, ey * TILE_SIZE + 4, TILE_SIZE - 8, TILE_SIZE - 8);
        }



        gc.setFill(Color.BLACK);
        gc.fillText("Jugador: " + playerName +" "+ "Vidas: "+ jugador.getVidas()+" " + tiempo +" "+ bloquesRestantes, WIDTH * TILE_SIZE - 560, 20 );

        if (gameOver) return;

      /*  gc.setFill(Color.BLACK);
        gc.fillText("Jugador: " + playerName + " | Vidas: " + jugador.getVidas(), -130, HEIGHT * TILE_SIZE + 20);*/

    }
}
