package modelo;

import java.util.ArrayList;

public class FabricaPiezas{
    private static final String[][] NOMBRES_HEROES ={
        {},
        {"Black Widow"},
        {"Gambit", "Spider-Girl", "Iceman", "Storm", "Phoenix", "Dr. Strange", "Elektra", "Nightcrawler"},
        {"Emma Frost", "She-Hulk", "Giant Man", "Beast", "Colossus"},
        {"Ghost Rider", "Punisher", "Blade", "Thing"},
        {"Thor", "Human Torch", "Cyclops", "Invisible Woman"},
        {"Daredevil", "Silver Surfer", "Hulk", "Iron Man"},
        {"Spider-Man", "Wolverine", "Namor"},
        {"Professor X", "Nick Fury"},
        {"Captain America"},
        {"Mr. Fantastic"}
    };

    private static final String[][] NOMBRES_VILLANOS ={
        {},
        {"Black Widow"},
        {"Mr. Sinister", "Sentinel 1", "Ultron", "Sandman", "Leader", "Viper", "Sentinel 2", "Electro"},
        {"Juggernaut", "Rhino", "Carnage", "Mole Man", "Lizard"},
        {"Abomination", "Thanos", "Black Cat", "Sabretooth"},
        {"Mystique", "Mysterio", "Dr. Octopus", "Deadpool"},
        {"Bullseye", "Omega Red", "Onslaught", "Red Skull"},
        {"Apocalypse", "Green Goblin", "Venom"},
        {"Kingpin", "Magneto"},
        {"Galactus"},
        {"Dr. Doom"}
    };

    public static ArrayList<Pieza> crearEjercito(Bando bando){
        String[][] nombres = (bando == Bando.HEROES) ? NOMBRES_HEROES : NOMBRES_VILLANOS;
        ArrayList<Pieza> lista = new ArrayList<>();

        for(int rango = 1; rango <= 10; rango++){
            for (String nombre : nombres[rango]){
                if (rango == 1) {
                    lista.add(new Agente(nombre, bando));
                } else if (rango == 2){
                    lista.add(new Mistico(nombre, bando));
                }else{
                    lista.add(new PiezaNormal(nombre, rango, bando));
                }
            }
        }

        String nombreBomba = (bando == Bando.HEROES) ? "Nova Blast" : "Pumpkin Bomb";
        for(int i = 0; i < 6; i++){
            lista.add(new Bomba(nombreBomba, bando));
        }
        lista.add(new Tierra(bando));
        return lista;
    }
}
