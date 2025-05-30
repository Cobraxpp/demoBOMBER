package com.example.demo2;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterScreen {

    public interface RegisterHandler {
        void onRegister(String username, String password);
    }

    public void start(Stage stage, RegisterHandler handler) {
        Label titleLabel = new Label("Registro de Usuario");
        Label userLabel = new Label("Usuario:");
        TextField usertext = new TextField();
        Label passLabel = new Label("Contraseña:");
        PasswordField passtext = new PasswordField();
        Label repeatPassLabel = new Label("Repetir Contraseña:");
        PasswordField repetirPasstext = new PasswordField();
        Button registerButton = new Button("Registrarse");

        VBox vbox = new VBox(10, titleLabel, userLabel, usertext, passLabel, passtext, repeatPassLabel, repetirPasstext, registerButton);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #d4edda;");
        vbox.setPrefWidth(350);

        Scene registerScene = new Scene(new StackPane(vbox), 600, 500);

        registerButton.setOnAction(e -> {
            String user = usertext.getText().trim();
            String pass = passtext.getText().trim();
            String repeatPass = repetirPasstext.getText().trim();

            if (user.isEmpty() || pass.isEmpty() || repeatPass.isEmpty()) {
                showAlert("Todos los campos son obligatorios");

            } else if (!pass.equals(repeatPass)) {
                showAlert("Las contraseñas no coinciden");
            } else {
                try (Connection conn = ConexionBD.conectar()) {
                    String query = "INSERT INTO jugadores (nombre, contrasena) VALUES (?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, user);
                    stmt.setString(2, hashPassword(pass));

                    int filasInsertadas = stmt.executeUpdate();
                    if (filasInsertadas > 0) {
                        showInfo("Usuario registrado correctamente");
                        handler.onRegister(user, pass);
                    } else {
                        showAlert("No se pudo registrar el usuario");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert("Error de base de datos. ¿Usuario ya existe?");
                }
            }
        });

        stage.setTitle("Registro");
        stage.setScene(registerScene);
        stage.show();
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
