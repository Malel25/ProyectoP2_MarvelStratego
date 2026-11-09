package gfx;

import excepciones.CredencialesInvalidasException;
import excepciones.PasswordInvalidoException;
import modelo.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

public class MenuPrincipal extends JFrame{
    private JLabel lblBienvenida;
    private BotonMarvel btnStratego;
    private BotonMarvel btnPerfil;
    private BotonMarvel btnUniverso;
    private BotonMarvel btnCerrar;

    private ArrayList<JFrame> ventanas = new ArrayList<>();

    public MenuPrincipal(){
        setTitle("Marvel Stratego - Menu Principal");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        PanelFondo panel = new PanelFondo(Imagenes.getFondo());
        setContentPane(panel);

        lblBienvenida = new JLabel("", SwingConstants.CENTER);
        lblBienvenida.setBounds(40, 40, 700, 60);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 38));
        lblBienvenida.setForeground(Color.WHITE);
        panel.add(lblBienvenida);
        actualizarBienvenida();

        btnStratego = new BotonMarvel("STRATEGO – MARVEL HEROES!", "Partida Nueva",
                new Color(200, 40, 55), new Color(110, 15, 30));
        btnStratego.setBounds(50, 140, 330, 160);
        panel.add(btnStratego);

        btnPerfil = new BotonMarvel("MI PERFIL", "Log / Password / Eliminar cuenta",
                new Color(40, 110, 230), new Color(15, 50, 140));
        btnPerfil.setBounds(410, 140, 330, 160);
        panel.add(btnPerfil);

        btnUniverso = new BotonMarvel("UNIVERSO MARVEL", "Ranking / Batallas",
                new Color(110, 60, 190), new Color(50, 20, 110));
        btnUniverso.setBounds(50, 330, 330, 160);
        panel.add(btnUniverso);

        btnCerrar = new BotonMarvel("CERRAR SESION", "Volver al menu inicial",
                new Color(70, 80, 100), new Color(25, 30, 45));
        btnCerrar.setBounds(410, 330, 330, 160);
        panel.add(btnCerrar);

        btnStratego.setEnabled(SistemaJugadores.getListaJugadores().size() >= 2);

        btnStratego.addActionListener(e -> nuevaPartida());
        btnPerfil.addActionListener(e -> abrirPerfil());
        btnUniverso.addActionListener(e -> abrirUniverso());
        btnCerrar.addActionListener(e -> cerrarSesion());
    }

    private void actualizarBienvenida(){
        Jugador actual = SistemaJugadores.getJugadorLogueado();
        lblBienvenida.setText("Bienvenido \"" + (actual != null ? actual.getUsername() : "") + "\"");
    }

    private void abrirPerfil(){
        String[] opciones = {"Log de Mis Ultimos Juegos", "Cambiar mi Password", "Eliminar mi Cuenta", "Cancelar"};
        int op = JOptionPane.showOptionDialog(this, "Que desea hacer?", "MI PERFIL",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
        switch(op){
            case 0:
                abrirVentana(new MiPerfil(this));
                break;
            case 1:
                cambiarPassword();
                break;
            case 2:
                eliminarCuenta();
                break;
            default:
                break;
        }
    }

    private void abrirUniverso(){
        String[] opciones = {"Ranking", "Batallas", "Cancelar"};
        int op = JOptionPane.showOptionDialog(this, "Que desea ver?", "UNIVERSO MARVEL",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
        if(op == 0){
            abrirVentana(new Ranking());
        }else if(op == 1){
            abrirVentana(new Batallas());
        }
    }

    private void abrirVentana(JFrame ventana){
        ventanas.add(ventana);
        ventana.setVisible(true);
    }

    public void refrescarVentanas(){
        for(JFrame v : ventanas){
            if(v instanceof Refrescable){
                ((Refrescable) v).refrescar();
            }
        }
    }

    private void nuevaPartida(){
        Jugador yo = SistemaJugadores.getJugadorLogueado();
        ArrayList<Jugador> todos = SistemaJugadores.getListaJugadores();
        if(todos.size() < 2){
            JOptionPane.showMessageDialog(this, "Se necesitan al menos 2 jugadores registrados");
            return;
        }

        JComboBox<String> cmbOponente = new JComboBox<>();
        for (Jugador j : todos){
            if(j != yo){
                cmbOponente.addItem(j.getUsername());
            }
        }
        JComboBox<Bando> cmbBando = new JComboBox<>(Bando.values());
        JComboBox<ModoJuego> cmbModo = new JComboBox<>(ModoJuego.values());

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Oponente:"));
        panel.add(cmbOponente);
        panel.add(new JLabel("Tu bando:"));
        panel.add(cmbBando);
        panel.add(new JLabel("Modalidad de juego:"));
        panel.add(cmbModo);

        int op = JOptionPane.showConfirmDialog(this, panel, "Partida Nueva",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (op != JOptionPane.OK_OPTION){
            return;
        }

        Jugador rival = SistemaJugadores.buscarJugador((String) cmbOponente.getSelectedItem());
        Bando miBando = (Bando) cmbBando.getSelectedItem();
        ModoJuego modo = (ModoJuego) cmbModo.getSelectedItem();
        
        String passRival = pedirPassword("Login del rival (" + rival.getUsername() + ").\nIngrese su password para iniciar la partida:");
        if (passRival == null) {
            return;
        }
        try {
            SistemaJugadores.verificarPassword(rival, passRival);
        } catch (CredencialesInvalidasException ex) {
            JOptionPane.showMessageDialog(this, "Password del rival incorrecto. No se inicia la partida.",
                    "Error de Log In", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Partida partida;
        if(miBando == Bando.HEROES) {
            partida = new Partida(yo, rival, modo);
        } else {
            partida = new Partida(rival, yo, modo);
        }

        TableroVisual tablero = new TableroVisual(this, partida);
        setVisible(false);
        tablero.setVisible(true);
    }

    public void volverDePartida(){
        setVisible(true);
        btnStratego.setEnabled(SistemaJugadores.getListaJugadores().size() >= 2);
        refrescarVentanas();
    }

    private String pedirPassword(String mensaje) {
        JPasswordField campo = new JPasswordField();
        VerificacionPassword verificacion = new VerificacionPassword(campo);
        int op = JOptionPane.showConfirmDialog(this, new Object[]{mensaje, campo, verificacion}, "Password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (op != JOptionPane.OK_OPTION){
            return null;
        }
        return new String(campo.getPassword());
    }

    public void cambiarPassword() {
        Jugador actual = SistemaJugadores.getJugadorLogueado();
        String passActual = pedirPassword("Ingrese su password actual:");
        if(passActual == null){
            return;
        }
        String nueva = pedirPassword("Ingrese su nuevo password (5 caracteres con mayuscula, minuscula, numero y simbolo):");
        if (nueva == null){
            return;
        }
        String confirmar = pedirPassword("Confirme su nuevo password:");
        if(confirmar == null){
            return;
        }
        try {
            SistemaJugadores.verificarPassword(actual, passActual);
            SistemaJugadores.validarConfirmacion(nueva, confirmar);
            SistemaJugadores.cambiarPassword(actual, passActual, nueva);
            JOptionPane.showMessageDialog(this, "Password actualizado con exito.");
        } catch (CredencialesInvalidasException | PasswordInvalidoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean eliminarCuenta(){
        Jugador actual = SistemaJugadores.getJugadorLogueado();
        String pass = pedirPassword("Ingrese su password para confirmar la eliminacion:");
        if (pass == null){
            return false;
        }
        try{
            SistemaJugadores.verificarPassword(actual, pass);
        }catch(CredencialesInvalidasException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        int conf = JOptionPane.showConfirmDialog(this, "Seguro que desea eliminar su cuenta permanentemente?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if(conf != JOptionPane.YES_OPTION){
            return false;
        }
        SistemaJugadores.eliminarJugador(actual);
        JOptionPane.showMessageDialog(this, "Cuenta eliminada, regresando al Menu Inicial.");
        volverAlMenuInicial();
        return true;
    }

    private void cerrarSesion() {
        int resp = JOptionPane.showConfirmDialog(this, "Desea cerrar sesion?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION){
            SistemaJugadores.setJugadorLogueado(null);
            volverAlMenuInicial();
        }
    }

    private void volverAlMenuInicial(){
        new MenuInicial().setVisible(true);
        for(JFrame v : ventanas){
            v.dispose();
        }
        dispose();
    }
}
