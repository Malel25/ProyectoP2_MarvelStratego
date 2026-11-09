package modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Jugador implements Serializable{
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private int puntos;
    private int partidasHeroes;
    private int partidasVillanos;
    private ArrayList<String> historialPartidas;

    public Jugador(String username, String password){
        this.username = username;
        this.password = password;
        this.puntos = 0;
        this.partidasHeroes = 0;
        this.partidasVillanos = 0;
        this.historialPartidas = new ArrayList<>();
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public int getPuntos(){
        return puntos;
    }

    public void sumarPuntos(int puntos){
        this.puntos += puntos;
    }

    public int getPartidasHeroes(){
        return partidasHeroes;
    }

    public void incrementarHeroes(){
        this.partidasHeroes++;
    }

    public int getPartidasVillanos(){
        return partidasVillanos;
    }

    public void incrementarVillanos(){
        this.partidasVillanos++;
    }

    public ArrayList<String> getHistorialPartidas(){
        return historialPartidas;
    }

    public void agregarLogPartida(String log){
        historialPartidas.add(0, log);
    }
}
