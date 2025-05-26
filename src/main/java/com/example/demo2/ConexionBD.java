package com.example.demo2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionBD {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String Base = "bomber";
    private static final String USUARIO = "root";
    private static final String CONTRA = "mysql";

    public static Connection conectar() {
        Connection conn = null;
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL + Base, USUARIO, CONTRA);


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

        }
        return conn;
    }
}
