package modelo;

public abstract class Pieza implements Movible{
    protected String nombre;
    protected int rango;
    protected Bando bando;
    protected TipoPieza tipo;
    protected boolean revelada;

    public Pieza(String nombre, int rango, Bando bando, TipoPieza tipo){
        this.nombre = nombre;
        this.rango = rango;
        this.bando = bando;
        this.tipo = tipo;
        this.revelada = false;
    }

    public abstract ResultadoCombate atacar(Pieza defensora);

    public abstract String getDescripcion();

    public String getNombre(){
        return nombre;
    }

    public int getRango(){
        return rango;
    }

    public Bando getBando(){
        return bando;
    }

    public TipoPieza getTipo(){
        return tipo;
    }

    public boolean isRevelada(){
        return revelada;
    }

    public void setRevelada(boolean revelada){
        this.revelada = revelada;
    }

    public String getRangoTexto(){
        switch(tipo){
            case BOMBA:
                return "B";
            case TIERRA:
                return "T";
            default:
                return String.valueOf(rango);
        }
    }
}
