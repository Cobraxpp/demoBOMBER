package com.example.demo2;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginScreen {

    public interface LoginHandler {
        void onLoginSuccess(String username);
    }

    // Añadimos un tercer parámetro: onVerRankingClicked
    public void start(Stage primaryStage, LoginHandler handler, Runnable onRegisterClicked, Runnable onVerRankingClicked) {
        Label userLabel = new Label("Usuario:");
        TextField userField = new TextField();
        Label passLabel = new Label("Contraseña:");
        PasswordField passField = new PasswordField();
        Button loginButton = new Button("Iniciar");
        Button registerButton = new Button("Registrarse");
        Button rankingButton = new Button("Ver Ranking"); // Botón nuevo

        VBox vbox = new VBox(10, userLabel, userField, passLabel, passField, loginButton, registerButton, rankingButton);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #cce5ff;");
        vbox.setPrefWidth(300);

        Scene loginScene = new Scene(new StackPane(vbox), 600, 500);

        loginButton.setOnAction(e -> {
            String user = userField.getText().trim();
            String pass = passField.getText().trim();
            if (!user.isEmpty() && !pass.isEmpty()) {
                handler.onLoginSuccess(user);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Introduce usuario y contraseña");
                alert.showAndWait();
            }
        });

        registerButton.setOnAction(e -> onRegisterClicked.run());
        rankingButton.setOnAction(e -> onVerRankingClicked.run()); // Acción del botón nuevo

        primaryStage.setTitle("Inicio de Sesión");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
}
