package com.example.demo2;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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

        TableColumn<UsuarioRanking, Integer> partidasCol = new TableColumn<>("Partidas");
        partidasCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getPartidas()).asObject());

        TableColumn<UsuarioRanking, String> fechaCol = new TableColumn<>("Última Fecha");
        fechaCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getUltimaFecha()));

        tabla.getColumns().addAll(nombreCol, puntosCol, partidasCol, fechaCol);
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
        // Simulando datos de prueba. Sustituir por lectura de base de datos.
        datosRanking.add(new UsuarioRanking("Noelia", 1200, 14, "2025-05-11"));
        datosRanking.add(new UsuarioRanking("Alex", 980, 10, "2025-05-10"));
        datosRanking.add(new UsuarioRanking("Marcos", 1350, 16, "2025-05-12"));
        datosRanking.add(new UsuarioRanking("Zoe", 875, 9, "2025-05-08"));
    }
}
