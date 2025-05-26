package com.example.demo2;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        mostrarIntro(primaryStage); // Primero muestra la intro
    }

    private void mostrarIntro(Stage stage) {
        IntroScreen intro = new IntroScreen();
        intro.start(stage, () -> mostrarLogin(stage)); // Al pulsar "START" va al login
    }

    private void mostrarLogin(Stage stage) {
        LoginScreen login = new LoginScreen();
        login.start(stage, username -> {
                    Game game = new Game(username);
                    game.setOnVolver(() -> mostrarLogin(stage));
                    game.start(stage);
                }, () -> mostrarRegistro(stage),
                () -> mostrarRanking(stage));
    }

    private void mostrarRanking(Stage stage) {
        RankingScreen ranking = new RankingScreen();
        ranking.start(stage, () -> mostrarLogin(stage));
    }

    private void mostrarRegistro(Stage stage) {
        RegisterScreen register = new RegisterScreen();
        register.start(stage, (username, password) -> {
            Game game = new Game(username);
            game.setOnVolver(() -> mostrarLogin(stage));
            game.start(stage);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
