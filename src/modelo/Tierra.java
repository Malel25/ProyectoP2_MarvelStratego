package modelo;

public class Tierra extends Pieza{
    public Tierra(Bando bando){
        super("Planet Earth", 0, bando, TipoPieza.TIERRA);
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
        return "La TIERRA: si la capturan, pierdes la partida";
    }

    @Override
    public ResultadoCombate atacar(Pieza defensora){
        return ResultadoCombate.PIERDE;
    }
}
