package modelo;

import java.io.Serializable;

public class RegistroPartida implements Serializable{
    private static final long serialVersionUID = 1L;

    private String texto;
    private Bando bandoGanador;

    public RegistroPartida(String texto, Bando bandoGanador){
        this.texto = texto;
        this.bandoGanador = bandoGanador;
    }

    public String getTexto(){
        return texto;
    }

    public Bando getBandoGanador(){
        return bandoGanador;
    }
}
