package com.example.demo2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class ConexionBD {
    public static Connection conectar() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/PokemonGaster?serverTimezone=UTC",
                    "root", // tu usuario
                    "root"  // tu contraseña
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
