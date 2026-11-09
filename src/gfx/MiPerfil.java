package gfx;

import modelo.Jugador;
import modelo.SistemaJugadores;
import javax.swing.*;

public class MiPerfil extends JFrame implements Refrescable{
    private MenuPrincipal menu;
    private JLabel lblDatos;
    private JLabel lblPartidas;
    private DefaultListModel<String> modeloLista;

    public MiPerfil(MenuPrincipal menu){
        this.menu = menu;

        setTitle("Marvel Stratego - Mi Perfil");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        lblDatos = new JLabel();
        lblDatos.setBounds(20, 15, 640, 25);
        add(lblDatos);

        lblPartidas = new JLabel();
        lblPartidas.setBounds(20, 42, 640, 25);
        add(lblPartidas);

        JLabel lblHist = new JLabel("Historial de partidas (de la mas reciente a la mas vieja):");
        lblHist.setBounds(20, 75, 400, 25);
        add(lblHist);

        modeloLista = new DefaultListModel<>();
        JList<String> lista = new JList<>(modeloLista);
        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBounds(20, 105, 645, 215);
        add(scroll);

        JButton btnPass = new JButton("Cambiar Password");
        btnPass.setBounds(20, 340, 180, 35);
        add(btnPass);

        JButton btnEliminar = new JButton("Eliminar mi Cuenta");
        btnEliminar.setBounds(220, 340, 180, 35);
        add(btnEliminar);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBounds(540, 340, 125, 35);
        add(btnCerrar);

        btnPass.addActionListener(e -> menu.cambiarPassword());
        btnEliminar.addActionListener(e -> {
            if(menu.eliminarCuenta()){
                dispose();
            }
        });
        btnCerrar.addActionListener(e -> dispose());
        
        EtiquetaUsuario lblUsuario = new EtiquetaUsuario();
        lblUsuario.setBounds(20, 385, 250, 20);
        add(lblUsuario);
        
        refrescar();
    }

    @Override
    public void refrescar(){
        Jugador j = SistemaJugadores.getJugadorLogueado();
        modeloLista.clear();
        if(j == null) {
            return;
        }
        lblDatos.setText("Username: " + j.getUsername() + "   |   Puntos: " + j.getPuntos());
        lblPartidas.setText("Partidas con Heroes: " + j.getPartidasHeroes()
                + "   |   Partidas con Villanos: " + j.getPartidasVillanos());
        for (String log : j.getHistorialPartidas()){
            modeloLista.addElement(log);
        }
    }
}
