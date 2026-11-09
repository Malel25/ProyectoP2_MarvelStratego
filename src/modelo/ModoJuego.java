package modelo;

public enum ModoJuego{
    TUTORIAL("TUTORIAL (todas las fichas visibles)"),
    CLASICO("CLASICO (fichas enemigas ocultas)");

    private final String descripcion;

    ModoJuego(String descripcion){
        this.descripcion = descripcion;
    }

    @Override
    public String toString(){
        return descripcion;
    }
}
