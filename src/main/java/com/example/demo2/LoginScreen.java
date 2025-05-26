package com.example.demo2;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginScreen {

    public interface LoginHandler {
        void onLoginSuccess(String username);
    }

    public void start(Stage primaryStage, LoginHandler handler, Runnable onRegisterClicked, Runnable onVerRankingClicked) {
        Label userLabel = new Label("Usuario:");
        TextField userField = new TextField();
        Label passLabel = new Label("Contraseña:");
        PasswordField passField = new PasswordField();
        Button loginButton = new Button("Iniciar");
        Button registerButton = new Button("Registrarse");
        Button rankingButton = new Button("Ver Ranking");

        VBox vbox = new VBox(10, userLabel, userField, passLabel, passField, loginButton, registerButton, rankingButton);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #cce5ff;");
        vbox.setPrefWidth(300);

        Scene loginScene = new Scene(new StackPane(vbox), 600, 500);

        loginButton.setOnAction(e -> {
            String user = userField.getText().trim();
            String pass = passField.getText().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                showAlert("Introduce usuario y contraseña");
                return;
            }

            try (Connection conn = ConexionBD.conectar()) {
                String query = "SELECT contrasena FROM jugadores WHERE nombre = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, user);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String passEnBD = rs.getString("contrasena");
                    if (passEnBD.equals(hashPassword(pass))) {
                        showInfo("Inicio de sesión correcto");
                        handler.onLoginSuccess(user);
                    } else {
                        showAlert("Contraseña incorrecta");
                    }
                } else {
                    showAlert("Usuario no encontrado");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error al conectar con la base de datos");
            }
        });

        registerButton.setOnAction(e -> onRegisterClicked.run());
        rankingButton.setOnAction(e -> onVerRankingClicked.run());

        primaryStage.setTitle("Inicio de Sesión");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al cifrar la contraseña", e);
        }
    }
}
