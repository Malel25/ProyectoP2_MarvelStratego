package modelo;

public class Bomba extends Pieza{
    public Bomba(String nombre, Bando bando){
        super(nombre, 0, bando, TipoPieza.BOMBA);
    }

    @Override
    public boolean puedeMover(){
        return false;
    }

    @Override
    public int getAlcance(){
        return 0;
    }

    @Override
    public String getDescripcion(){
        return "Bomba: destruye a quien la ataque (excepto rango 3)";
    }

    @Override
    public ResultadoCombate atacar(Pieza defensora){
        return ResultadoCombate.PIERDE;
    }
}
