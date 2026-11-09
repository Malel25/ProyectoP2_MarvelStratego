package gfx;

import modelo.SistemaJugadores;
import javax.swing.*;
import java.awt.Font;

public class Batallas extends JFrame implements Refrescable{
    private JTextArea txtStats;

    public Batallas(){
        setTitle("Marvel Stratego - Batallas");
        setSize(420, 330);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        txtStats = new JTextArea();
        txtStats.setEditable(false);
        txtStats.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtStats.setBounds(20, 20, 365, 200);
        add(txtStats);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBounds(150, 245, 120, 30);
        add(btnCerrar);
        btnCerrar.addActionListener(e -> dispose());

        refrescar();
    }

    @Override
    public void refrescar(){
        txtStats.setText(
                "Usuarios activos:            " + SistemaJugadores.getUsuariosActivos() + "\n"
                + "Usuarios historicos:         " + SistemaJugadores.getUsuariosHistoricos() + "\n"
                + "Partidas jugadas:            " + SistemaJugadores.getTotalPartidasJugadas() + "\n"
                + "   Victorias de HEROES:      " + SistemaJugadores.getVictoriasHeroes() + "\n"
                + "   Victorias de VILLANOS:    " + SistemaJugadores.getVictoriasVillanos() + "\n");
    }
}
