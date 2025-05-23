package com.example.demo2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RankingScreen {

    private TableView<UsuarioRanking> tabla;
    private ObservableList<UsuarioRanking> datosRanking;


    public void start(Stage stage, Runnable onVolver) {
        tabla = new TableView<>();
        datosRanking = FXCollections.observableArrayList();

        TableColumn<UsuarioRanking, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNombre()));

        TableColumn<UsuarioRanking, Integer> puntosCol = new TableColumn<>("Puntos");
        puntosCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getPuntos()).asObject());

        TableColumn<UsuarioRanking, String> tiempoCol = new TableColumn<>("Tiempo");
        tiempoCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTiempo()));

        TableColumn<UsuarioRanking, String> fechaCol = new TableColumn<>("Última Fecha");
        fechaCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getUltimaFecha()));

        tabla.getColumns().addAll(nombreCol, puntosCol, tiempoCol, fechaCol);
        tabla.setItems(datosRanking);
        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(e -> onVolver.run());

        BorderPane layout = new BorderPane();
        layout.setCenter(tabla);
        layout.setBottom(btnVolver);
        BorderPane.setMargin(btnVolver, new Insets(10));

        stage.setScene(new Scene(layout, 600, 400));
        stage.setTitle("Ranking de Jugadores");
        stage.show();

        cargarDatos();
    }

    private void cargarDatos() {
        datosRanking.clear();

        String sql = "SELECT nombre, puntuacion, tiempo, fecha_entrada FROM jugadores ORDER BY puntuacion DESC";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int puntos = rs.getInt("puntuacion");
                String tiempo = rs.getString("tiempo");
                String fecha = rs.getString("fecha_entrada");

                datosRanking.add(new UsuarioRanking(nombre, puntos, tiempo, fecha));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
