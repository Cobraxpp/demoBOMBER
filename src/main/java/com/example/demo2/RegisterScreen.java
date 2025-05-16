package com.example.demo2;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RegisterScreen {

    public interface RegisterHandler {
        void onRegister(String username, String password);
    }

    public void start(Stage stage, RegisterHandler handler) {
        Label titleLabel = new Label("Registro de Usuario");
        Label userLabel = new Label("Usuario:");
        TextField userField = new TextField();
        Label passLabel = new Label("Contraseña:");
        PasswordField passField = new PasswordField();
        Label repeatPassLabel = new Label("Repetir Contraseña:");
        PasswordField repeatPassField = new PasswordField();
        Button registerButton = new Button("Registrarse");

        VBox vbox = new VBox(10, titleLabel, userLabel, userField, passLabel, passField, repeatPassLabel, repeatPassField, registerButton);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #d4edda;");
        vbox.setPrefWidth(350);

        Scene registerScene = new Scene(new StackPane(vbox), 600, 500);

        registerButton.setOnAction(e -> {
            String user = userField.getText().trim();
            String pass = passField.getText().trim();
            String repeatPass = repeatPassField.getText().trim();

            if (user.isEmpty() || pass.isEmpty() || repeatPass.isEmpty()) {
                showAlert("Todos los campos son obligatorios");
            } else if (!pass.equals(repeatPass)) {
                showAlert("Las contraseñas no coinciden");
            } else {
                handler.onRegister(user, pass);
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
}
