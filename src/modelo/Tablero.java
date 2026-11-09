package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Tablero{
    private Pieza[][] casillas;
    private static final int[][] DIRECCIONES = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public Tablero(){
        casillas = new Pieza[10][10];
    }

    public Pieza getPieza(int fila, int columna){
        return casillas[fila][columna];
    }

    public void setPieza(int fila, int columna, Pieza p){
        casillas[fila][columna] = p;
    }

    public boolean enTablero(int fila, int columna){
        return fila >= 0 && fila < 10 && columna >= 0 && columna < 10;
    }

    public boolean esZonaProhibida(int fila, int columna){
        return (fila == 4 || fila == 5) && (columna == 2 || columna == 3 || columna == 6 || columna == 7);
    }

    public void colocarFichasAleatorias(Bando bando){
        Random r = new Random();
        boolean heroes = (bando == Bando.HEROES);
        int filaFondo = heroes ? 9 : 0;
        int filaFondo2 = heroes ? 8 : 1;
        int filaAtras = heroes ? 7 : 2;
        int filaFrente = heroes ? 6 : 3;

        ArrayList<Pieza> ejercito = FabricaPiezas.crearEjercito(bando);

        Pieza tierra = null;
        ArrayList<Pieza> bombas = new ArrayList<>();
        ArrayList<Pieza> misticos = new ArrayList<>();
        for (Pieza p : ejercito) {
            if (p.getTipo() == TipoPieza.TIERRA) {
                tierra = p;
            } else if (p.getTipo() == TipoPieza.BOMBA) {
                bombas.add(p);
            } else if (p instanceof Mistico) {
                misticos.add(p);
            }
        }
        ejercito.remove(tierra);
        ejercito.removeAll(bombas);
        ejercito.removeAll(misticos);

        int colTierra = r.nextInt(8) + 1;
        casillas[filaFondo][colTierra] = tierra;

        ArrayList<int[]> alrededor = new ArrayList<>();
        alrededor.add(new int[]{filaFondo, colTierra - 1});
        alrededor.add(new int[]{filaFondo, colTierra + 1});
        alrededor.add(new int[]{filaFondo2, colTierra});
        colocarRecursivo(bombas, alrededor, r);

        colocarRecursivo(bombas, celdasLibres(filaFondo, filaFondo2), r);

        colocarRecursivo(misticos, celdasLibres(filaAtras, filaFrente), r);

        Collections.shuffle(ejercito, r);
        colocarRecursivo(ejercito, celdasLibres(filaFrente), r);

        colocarRecursivo(ejercito, celdasLibres(filaFondo, filaFondo2, filaAtras, filaFrente), r);
    }

    private ArrayList<int[]> celdasLibres(int... filas){
        ArrayList<int[]> libres = new ArrayList<>();
        for (int f : filas){
            for (int c = 0; c < 10; c++){
                if (casillas[f][c] == null){
                    libres.add(new int[]{f, c});
                }
            }
        }
        return libres;
    }

    private void colocarRecursivo(ArrayList<Pieza> piezas, ArrayList<int[]> celdas, Random r){
        if (piezas.isEmpty() || celdas.isEmpty()){
            return;
        }
        Pieza p = piezas.remove(0);
        int[] celda = celdas.remove(r.nextInt(celdas.size()));
        casillas[celda[0]][celda[1]] = p;
        colocarRecursivo(piezas, celdas, r);
    }

    public boolean caminoDespejado(int fila, int col, int filaDestino, int colDestino, int pasoFila, int pasoCol){
        int sigFila = fila + pasoFila;
        int sigCol = col + pasoCol;

        if(sigFila == filaDestino && sigCol == colDestino){
            return true;
        }
        if(!enTablero(sigFila, sigCol) || casillas[sigFila][sigCol] != null || esZonaProhibida(sigFila, sigCol)){
            return false;
        }
        return caminoDespejado(sigFila, sigCol, filaDestino, colDestino, pasoFila, pasoCol);
    }

    public boolean tieneMovimiento(Bando bando, int indice){
        if(indice >= 100){
            return false;
        }
        int fila = indice / 10;
        int col = indice % 10;
        Pieza p = casillas[fila][col];

        if(p != null && p.getBando() == bando && p.puedeMover() && puedeMoverseAlgunaCasilla(fila, col)){
            return true;
        }
        return tieneMovimiento(bando, indice + 1);
    }

    private boolean puedeMoverseAlgunaCasilla(int fila, int col){
        Pieza p = casillas[fila][col];
        for (int[] d : DIRECCIONES){
            int nf = fila + d[0];
            int nc = col + d[1];
            if (enTablero(nf, nc) && !esZonaProhibida(nf, nc)){
                Pieza destino = casillas[nf][nc];
                if (destino == null || destino.getBando() != p.getBando()){
                    return true;
                }
            }
        }
        return false;
    }
}
