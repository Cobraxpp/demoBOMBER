package com.example.demo2;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class IntroScreen {

    public void start(Stage stage, Runnable onStart) {
        // Imagen de fondo como nodo (más flexible que BackgroundImage)
        ImageView backgroundView = new ImageView(new Image(getClass().getResource("/com/example/demo2/intro.png").toExternalForm()));
        backgroundView.setFitWidth(800);
        backgroundView.setFitHeight(600);
        backgroundView.setPreserveRatio(false);

        // Título estilo retro
        Label title = new Label("Bomberman");
        title.setFont(Font.font("Arial", 50));
        title.setTextFill(Color.CYAN);
        title.setEffect(new DropShadow(10, Color.BLUE));

        // Botón "PRESS START"
        Button startBtn = new Button("PRESS START");
        startBtn.setFont(Font.font("Consolas", 26));
        startBtn.setStyle("""
            -fx-background-color: black;
            -fx-text-fill: yellow;
            -fx-border-color: yellow;
            -fx-border-width: 3;
        """);
        startBtn.setOnAction(e -> onStart.run());

        // Efecto de parpadeo
        FadeTransition fade = new FadeTransition(Duration.seconds(1.2), startBtn);
        fade.setFromValue(0.2);
        fade.setToValue(1.0);
        fade.setCycleCount(FadeTransition.INDEFINITE);
        fade.setAutoReverse(true);
        fade.play();

        // Contenedor central
        VBox content = new VBox(30, title, startBtn);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(220, 0, 0, 0));

        StackPane root = new StackPane(backgroundView, content);

        Scene scene = new Scene(root, 450, 600);
        stage.setScene(scene);
        stage.setTitle("Intro Retro");
        stage.show();
    }
}
