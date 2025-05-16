package com.example.demo2;

public class UsuarioRanking {
    private String nombre;
    private int puntos;
    private int partidas;
    private String ultimaFecha;

    public UsuarioRanking(String nombre, int puntos, int partidas, String ultimaFecha) {
        this.nombre = nombre;
        this.puntos = puntos;
        this.partidas = partidas;
        this.ultimaFecha = ultimaFecha;
    }

    public String getNombre() { return nombre; }
    public int getPuntos() { return puntos; }
    public int getPartidas() { return partidas; }
    public String getUltimaFecha() { return ultimaFecha; }
}
