
package gfx;

import javax.swing.JCheckBox;
import javax.swing.JPasswordField;

public class VerificacionPassword extends JCheckBox{
     public VerificacionPassword(JPasswordField campo){
        super("Ver");
        final char ocultar = campo.getEchoChar();
        addActionListener(e -> campo.setEchoChar(isSelected() ? (char) 0 : ocultar));
    }
}
