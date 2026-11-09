package gfx;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;


public class PanelFondo extends JPanel{
    private Image fondo;

    public PanelFondo(Image fondo){
        this.fondo = fondo;
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(fondo != null){
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
