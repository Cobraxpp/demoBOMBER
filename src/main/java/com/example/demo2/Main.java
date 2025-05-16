package com.example.demo2;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        mostrarLogin(primaryStage);
    }

    private void mostrarLogin(Stage stage) {
        LoginScreen login = new LoginScreen();
        login.start(stage, username -> {
            Game game = new Game(username);
            game.start(stage);
        }, () -> mostrarRegistro(stage),
        () -> mostrarRanking(stage));
    }
    private void mostrarRanking(Stage stage) {
        RankingScreen ranking = new RankingScreen();
        ranking.start(stage, () -> mostrarLogin(stage)); // para volver al login
    }


    private void mostrarRegistro(Stage stage) {
        RegisterScreen register = new RegisterScreen();
        register.start(stage, (username, password) -> {
            // Aquí podrías guardar el usuario en una base de datos, archivo, etc.
            // Luego redirigir al login o directamente al juego:
            Game game = new Game(username);
            game.start(stage);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
