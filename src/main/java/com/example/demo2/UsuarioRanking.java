package com.example.demo2;

public class UsuarioRanking {
    private String nombre;
    private int puntos;
    private String tiempo;
    private String ultimaFecha;

    public UsuarioRanking(String nombre, int puntos, String tiempo, String ultimaFecha) {
        this.nombre = nombre;
        this.puntos = puntos;
        this.tiempo = tiempo;
        this.ultimaFecha = ultimaFecha;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getTiempo() {
        return tiempo;
    }

    public String getUltimaFecha() {
        return ultimaFecha;
    }
}
