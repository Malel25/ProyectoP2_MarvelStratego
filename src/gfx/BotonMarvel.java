package gfx;

import javax.swing.JButton;
import java.awt.*;

public class BotonMarvel extends JButton{
    private Color colorArriba;
    private Color colorAbajo;
    private String subtitulo;

    public BotonMarvel(String titulo, String subtitulo, Color colorArriba, Color colorAbajo){
        super(titulo);
        this.subtitulo = subtitulo;
        this.colorArriba = colorArriba;
        this.colorAbajo = colorAbajo;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 20));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        boolean encima = getModel().isRollover();
        boolean apretado = getModel().isPressed();
        Color c1 = isEnabled() ? colorArriba : Color.GRAY;
        Color c2 = isEnabled() ? colorAbajo : Color.DARK_GRAY;
        if(encima) {
            c1 = c1.brighter();
            c2 = c2.brighter();
        }

        int desplazo = apretado ? 3 : 0;
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRoundRect(5, 7, w - 8, h - 8, 30, 30);
        g2.setPaint(new GradientPaint(0, 0, c1, 0, h, c2));
        g2.fillRoundRect(desplazo, desplazo, w - 8, h - 8, 30, 30);
        g2.setColor(encima ? new Color(255, 215, 80) : new Color(255, 255, 255, 140));
        g2.setStroke(new BasicStroke(encima ? 4f : 2f));
        g2.drawRoundRect(desplazo + 1, desplazo + 1, w - 10, h - 10, 30, 30);

        g2.setColor(getForeground());
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        int y = (h - 8) / 2 + desplazo;
        if(subtitulo != null){
            g2.drawString(getText(), (w - 8 - fm.stringWidth(getText())) / 2 + desplazo, y);
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            FontMetrics fm2 = g2.getFontMetrics();
            g2.setColor(new Color(255, 255, 255, 210));
            g2.drawString(subtitulo, (w - 8 - fm2.stringWidth(subtitulo)) / 2 + desplazo, y + 26);
        }else{
            g2.drawString(getText(), (w - 8 - fm.stringWidth(getText())) / 2 + desplazo, y + fm.getAscent() / 3);
        }
        g2.dispose();
    }
}
