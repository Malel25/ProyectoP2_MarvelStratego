package modelo;

public class Agente extends PiezaNormal{
    public Agente(String nombre, Bando bando){
        super(nombre, 1, bando);
    }

    @Override
    public String getDescripcion(){
        return "Agente (rango 1): derrota al rango 10 si ataca primero";
    }

    @Override
    public ResultadoCombate atacar(Pieza defensora){
        if (defensora.getTipo() == TipoPieza.NORMAL && defensora.getRango() == 10){
            this.revelada = true;
            defensora.setRevelada(true);
            return ResultadoCombate.GANA;
        }
        return super.atacar(defensora);
    }
}
