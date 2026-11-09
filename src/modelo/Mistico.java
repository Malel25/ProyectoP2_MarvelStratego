package modelo;

public class Mistico extends PiezaNormal{
    public Mistico(String nombre, Bando bando){
        super(nombre, 2, bando);
    }

    @Override
    public int getAlcance(){
        return 9;
    }

    @Override
    public String getDescripcion(){
        return "Mistico (rango 2): se mueve varias casillas en linea recta";
    }
}
