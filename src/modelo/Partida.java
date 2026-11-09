package modelo;

import excepciones.MovimientoInvalidoException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Partida {
    private static final int PUNTOS_VICTORIA = 3;

    private Tablero tablero;
    private Jugador jugadorHeroes;
    private Jugador jugadorVillanos;
    private ModoJuego modo;
    private Bando turno;
    private ArrayList<Pieza> vencidasHeroes;
    private ArrayList<Pieza> vencidasVillanos;
    private boolean terminada;
    private Bando ganador;
    private String mensajeFinal;

    public Partida(Jugador heroes, Jugador villanos, ModoJuego modo){
        this.jugadorHeroes = heroes;
        this.jugadorVillanos = villanos;
        this.modo = modo;
        this.turno = Bando.HEROES;
        this.vencidasHeroes = new ArrayList<>();
        this.vencidasVillanos = new ArrayList<>();
        this.terminada = false;

        tablero = new Tablero();
        tablero.colocarFichasAleatorias(Bando.VILLANOS);
        tablero.colocarFichasAleatorias(Bando.HEROES);
    }

    public Tablero getTablero(){
        return tablero;
    }

    public Bando getTurno(){
        return turno;
    }

    public ModoJuego getModo(){
        return modo;
    }

    public boolean isTerminada(){
        return terminada;
    }

    public Bando getGanador(){
        return ganador;
    }

    public String getMensajeFinal(){
        return mensajeFinal;
    }

    public Jugador getJugador(Bando bando){
        return (bando == Bando.HEROES) ? jugadorHeroes : jugadorVillanos;
    }

    public ArrayList<Pieza> getVencidas(Bando bando){
        return (bando == Bando.HEROES) ? vencidasHeroes : vencidasVillanos;
    }

    public boolean esVisiblePara(Pieza p, Bando observador){
        return terminada || modo == ModoJuego.TUTORIAL || p.getBando() == observador || p.isRevelada();
    }

    public String mover(int f1, int c1, int f2, int c2) throws MovimientoInvalidoException{
        if (terminada){
            throw new MovimientoInvalidoException("La partida ya termino.");
        }
        if(!tablero.enTablero(f1, c1) || !tablero.enTablero(f2, c2)){
            throw new MovimientoInvalidoException("Movimiento fuera del tablero.");
        }
        Pieza pieza = tablero.getPieza(f1, c1);
        if(pieza == null || pieza.getBando() != turno){
            throw new MovimientoInvalidoException("Debe seleccionar una de sus piezas.");
        }
        if(!pieza.puedeMover()){
            throw new MovimientoInvalidoException("Las bombas y la Tierra no se pueden mover.");
        }
        if(tablero.esZonaProhibida(f2, c2)){
            throw new MovimientoInvalidoException("No se puede entrar a una zona prohibida.");
        }
        int df = f2 - f1;
        int dc = c2 - c1;
        if (df == 0 && dc == 0) {
            throw new MovimientoInvalidoException("Debe elegir una casilla distinta.");
        }
        if (df != 0 && dc != 0){
            throw new MovimientoInvalidoException("No se permite mover en diagonal.");
        }
        Pieza destino = tablero.getPieza(f2, c2);
        if (destino != null && destino.getBando() == turno){
            throw new MovimientoInvalidoException("Esa casilla esta ocupada por una pieza propia.");
        }
        int distancia = Math.abs(df) + Math.abs(dc);
        if(distancia > pieza.getAlcance()){
            throw new MovimientoInvalidoException("Esa pieza solo puede avanzar " + pieza.getAlcance() + " casilla(s).");
        }
        if (distancia > 1){
            if (destino != null){
                throw new MovimientoInvalidoException("Solo se puede atacar a una pieza adyacente.");
            }
            if(!tablero.caminoDespejado(f1, c1, f2, c2, Integer.signum(df), Integer.signum(dc))) {
                throw new MovimientoInvalidoException("El camino no esta despejado.");
            }
            pieza.setRevelada(true);
        }

        String mensaje = "";
        if (destino == null){
            tablero.setPieza(f2, c2, pieza);
            tablero.setPieza(f1, c1, null);
        }else{
            ResultadoCombate resultado = pieza.atacar(destino);
            mensaje = pieza.getNombre() + " (" + pieza.getRangoTexto() + ") ataca a "
                    + destino.getNombre() + " (" + destino.getRangoTexto() + ").\n";
            switch(resultado){
                case GANA:
                    vencidas(destino);
                    tablero.setPieza(f2, c2, pieza);
                    tablero.setPieza(f1, c1, null);
                    mensaje += pieza.getNombre() + " gana el combate.";
                    if(destino.getTipo() == TipoPieza.TIERRA){
                        finalizar(turno, MotivoFin.TIERRA);
                        return mensaje + "\n¡Se capturo la TIERRA!";
                    }
                    break;
                case PIERDE:
                    vencidas(pieza);
                    tablero.setPieza(f1, c1, null);
                    mensaje += destino.getNombre() + " gana el combate.";
                    break;
                default:
                    vencidas(pieza);
                    vencidas(destino);
                    tablero.setPieza(f1, c1, null);
                    tablero.setPieza(f2, c2, null);
                    mensaje += "Empate: ambas piezas son eliminadas.";
                    break;
            }
        }

        turno = turno.opuesto();
        if (!tablero.tieneMovimiento(turno, 0)){
            finalizar(turno.opuesto(), MotivoFin.SIN_MOVIMIENTOS);
        }
        return mensaje;
    }

    private void vencidas(Pieza p){
        getVencidas(p.getBando()).add(p);
    }

    public void retirarse(){
        finalizar(turno.opuesto(), MotivoFin.RETIRO);
    }

    private void finalizar(Bando bandoGanador, MotivoFin motivo){
        terminada = true;
        ganador = bandoGanador;

        Jugador jGanador = getJugador(bandoGanador);
        Jugador jPerdedor = getJugador(bandoGanador.opuesto());
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        String ganadorTxt = jGanador.getUsername() + " usando " + bandoGanador.getNombre();
        String perdedorTxt = jPerdedor.getUsername() + " usando " + bandoGanador.opuesto().getNombre();
        String texto;
        switch (motivo){
            case TIERRA:
                String verbo = (bandoGanador == Bando.HEROES) ? "SALVADO" : "CAPTURADO";
                texto = ganadorTxt + " ha " + verbo + " la TIERRA! Venciendo a "
                        + jPerdedor.getUsername() + " — " + fecha;
                break;
            case SIN_MOVIMIENTOS:
                texto = perdedorTxt + " ha perdido por no tener movimientos validos disponibles ante "
                        + jGanador.getUsername() + " — " + fecha;
                break;
            default:
                texto = ganadorTxt + " ha ganado ya que " + perdedorTxt
                        + " se ha retirado del juego — " + fecha;
                break;
        }

        jGanador.sumarPuntos(PUNTOS_VICTORIA);
        jugadorHeroes.incrementarHeroes();
        jugadorVillanos.incrementarVillanos();
        jGanador.agregarLogPartida(texto);
        jPerdedor.agregarLogPartida(texto);
        SistemaJugadores.registrarPartida(new RegistroPartida(texto, bandoGanador));
        mensajeFinal = texto;
    }
}
