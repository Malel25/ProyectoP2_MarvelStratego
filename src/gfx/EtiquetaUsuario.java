
package gfx;

import modelo.Jugador;
import modelo.SistemaJugadores;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

public class EtiquetaUsuario extends JLabel{
    public EtiquetaUsuario() {
        Jugador actual = SistemaJugadores.getJugadorLogueado();
        setText("Usuario: " + (actual != null ? actual.getUsername() : "-"));
        setFont(new Font("Arial", Font.BOLD, 12));
        setForeground(new Color(25, 85, 190));
    }
}
