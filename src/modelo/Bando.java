package modelo;

public enum Bando{
    HEROES("HEROES"), VILLANOS("VILLANOS");

    private final String nombre;

    Bando(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return nombre;
    }

    public Bando opuesto(){
        return (this == HEROES) ? VILLANOS : HEROES;
    }

    @Override
    public String toString(){
        return nombre;
    }
}
