package modelo;

public class PiezaNormal extends Pieza{
    public PiezaNormal(String nombre, int rango, Bando bando){
        super(nombre, rango, bando, TipoPieza.NORMAL);
    }

    @Override
    public boolean puedeMover(){
        return true;
    }

    @Override
    public int getAlcance(){
        return 1;
    }

    @Override
    public String getDescripcion(){
        return "Pieza de poder " + rango;
    }

    @Override
    public ResultadoCombate atacar(Pieza defensora){
        this.revelada = true;
        defensora.setRevelada(true);

        switch (defensora.getTipo()){
            case TIERRA:
                return ResultadoCombate.GANA;
            case BOMBA:
                return (rango == 3) ? ResultadoCombate.GANA : ResultadoCombate.PIERDE;
            default:
                if (rango > defensora.getRango()){
                    return ResultadoCombate.GANA;
                } else if (rango < defensora.getRango()){
                    return ResultadoCombate.PIERDE;
                }
                return ResultadoCombate.EMPATE;
        }
    }
}
