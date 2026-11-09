package gfx;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Font;

public class MenuInicial extends JFrame{
    private BotonMarvel btnLogin;
    private BotonMarvel btnCrearPlayer;

    public MenuInicial(){
        setTitle("Marvel Stratego - Menu Inicial");
        setSize(560, 420);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PanelFondo panel = new PanelFondo(Imagenes.getFondo());
        setContentPane(panel);

        JLabel lblTitulo = new JLabel("MARVEL STRATEGO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 34));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(30, 40, 490, 50);
        panel.add(lblTitulo);

        JLabel lblSub = new JLabel("Heroes contra Villanos", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.ITALIC, 16));
        lblSub.setForeground(new Color(255, 215, 80));
        lblSub.setBounds(30, 92, 490, 25);
        panel.add(lblSub);

        btnLogin = new BotonMarvel("LOG IN", null, new Color(40, 110, 230), new Color(15, 50, 140));
        btnLogin.setBounds(150, 150, 260, 75);
        panel.add(btnLogin);

        btnCrearPlayer = new BotonMarvel("CREAR JUGADOR", null, new Color(200, 40, 55), new Color(110, 15, 30));
        btnCrearPlayer.setBounds(150, 250, 260, 75);
        panel.add(btnCrearPlayer);

        btnLogin.addActionListener(e -> new Login(this).setVisible(true));
        btnCrearPlayer.addActionListener(e -> new CrearJugador().setVisible(true));
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new MenuInicial().setVisible(true));
    }
}
