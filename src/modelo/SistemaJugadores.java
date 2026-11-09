package modelo;

import excepciones.CredencialesInvalidasException;
import excepciones.PasswordInvalidoException;
import excepciones.UsuarioDuplicadoException;
import java.io.*;
import java.util.ArrayList;

public class SistemaJugadores{
    private static final String ARCHIVO = "stratego_datos.dat";

    private static ArrayList<Jugador> listaJugadores = new ArrayList<>();
    private static ArrayList<Jugador> listaHistorica = new ArrayList<>();
    private static ArrayList<RegistroPartida> registros = new ArrayList<>();
    private static Jugador jugadorLogueado = null;

    static {
        cargarDatos();
    }

    public static void guardarDatos(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(listaJugadores);
            oos.writeObject(listaHistorica);
            oos.writeObject(registros);
        } catch (IOException e) {
            System.out.println("Error al guardar los datos del sistema: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void cargarDatos(){
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))){
            ArrayList<Jugador> activos = (ArrayList<Jugador>) ois.readObject();
            ArrayList<Jugador> historicos = (ArrayList<Jugador>) ois.readObject();
            ArrayList<RegistroPartida> regs = (ArrayList<RegistroPartida>) ois.readObject();
            listaJugadores = activos;
            listaHistorica = historicos;
            registros = regs;
        } catch (IOException | ClassNotFoundException e){
            System.out.println("No se pudieron cargar los datos guardados: " + e.getMessage());
        }
    }

    public static void validarPassword(String password) throws PasswordInvalidoException{
        if (password == null || password.length() != 5){
            throw new PasswordInvalidoException("El password debe tener exactamente 5 caracteres.");
        }
    }

    public static Jugador buscarJugador(String username){
        for (Jugador j : listaJugadores) {
            if (j.getUsername().equalsIgnoreCase(username)){
                return j;
            }
        }
        return null;
    }

    public static void registrarJugador(String username, String password)
            throws UsuarioDuplicadoException, PasswordInvalidoException{
        validarPassword(password);
        if (buscarJugador(username) != null) {
            throw new UsuarioDuplicadoException("El username '" + username + "' ya existe. Ingrese uno nuevo.");
        }
        Jugador nuevo = new Jugador(username, password);
        listaJugadores.add(nuevo);
        listaHistorica.add(nuevo);
        guardarDatos();
    }

    public static Jugador login(String username, String password) throws CredencialesInvalidasException{
        if (listaJugadores.isEmpty()) {
            throw new CredencialesInvalidasException("No hay jugadores registrados. Cree un player primero.");
        }
        Jugador j = buscarJugador(username);
        if (j == null || !j.getPassword().equals(password)) {
            throw new CredencialesInvalidasException("Username o password incorrectos. Intente de nuevo.");
        }
        jugadorLogueado = j;
        return j;
    }

    public static void verificarPassword(Jugador j, String password) throws CredencialesInvalidasException{
        if (j == null || !j.getPassword().equals(password)) {
            throw new CredencialesInvalidasException("El password actual es incorrecto.");
        }
    }

    public static void cambiarPassword(Jugador j, String actual, String nuevo)
            throws CredencialesInvalidasException, PasswordInvalidoException{
        verificarPassword(j, actual);
        validarPassword(nuevo);
        j.setPassword(nuevo);
        guardarDatos();
    }

    public static void eliminarJugador(Jugador j){
        listaJugadores.remove(j);
        if (jugadorLogueado == j) {
            jugadorLogueado = null;
        }
        guardarDatos();
    }

    public static Jugador getJugadorLogueado(){
        return jugadorLogueado;
    }

    public static void setJugadorLogueado(Jugador j){
        jugadorLogueado = j;
    }

    public static ArrayList<Jugador> getListaJugadores(){
        return listaJugadores;
    }

    public static ArrayList<Jugador> obtenerRanking(){
        ArrayList<Jugador> ranking = new ArrayList<>();
        for(Jugador j : listaJugadores) {
            insertarEnRanking(ranking, j, 0);
        }
        return ranking;
    }

    private static void insertarEnRanking(ArrayList<Jugador> ranking, Jugador nuevo, int pos){
        if(pos >= ranking.size() || nuevo.getPuntos() > ranking.get(pos).getPuntos()){
            ranking.add(pos, nuevo);
            return;
        }
        insertarEnRanking(ranking, nuevo, pos + 1);
    }

    public static void registrarPartida(RegistroPartida registro){
        registros.add(registro);
        guardarDatos();
    }

    public static int contarVictorias(ArrayList<RegistroPartida> lista, int indice, Bando bando){
        if (indice >= lista.size()){
            return 0;
        }
        int suma = (lista.get(indice).getBandoGanador() == bando) ? 1 : 0;
        return suma + contarVictorias(lista, indice + 1, bando);
    }

    public static int getVictoriasHeroes(){
        return contarVictorias(registros, 0, Bando.HEROES);
    }

    public static int getVictoriasVillanos(){
        return contarVictorias(registros, 0, Bando.VILLANOS);
    }

    public static int getTotalPartidasJugadas(){
        return registros.size();
    }

    public static int getUsuariosActivos(){
        return listaJugadores.size();
    }

    public static int getUsuariosHistoricos(){
        return listaHistorica.size();
    }
}
