package gfx;

import modelo.Jugador;
import modelo.SistemaJugadores;
import javax.swing.*;
import java.awt.Font;
import java.util.ArrayList;

public class Ranking extends JFrame implements Refrescable{
    private JTextArea txtRanking;

    public Ranking(){
        setTitle("Marvel Stratego - Ranking");
        setSize(420, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        txtRanking = new JTextArea();
        txtRanking.setEditable(false);
        txtRanking.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(txtRanking);
        scroll.setBounds(20, 20, 365, 280);
        add(scroll);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBounds(150, 315, 120, 30);
        add(btnCerrar);
        btnCerrar.addActionListener(e -> dispose());

        refrescar();
    }

    @Override
    public void refrescar(){
        ArrayList<Jugador> ranking = SistemaJugadores.obtenerRanking();
        StringBuilder sb = new StringBuilder();
        int posicion = 1;
        for(Jugador j : ranking){
            sb.append(posicion).append(" — ").append(j.getUsername()).append(" — ")
                    .append(j.getPuntos()).append("\n");
            posicion++;
        }
        txtRanking.setText(sb.toString());
    }
}
