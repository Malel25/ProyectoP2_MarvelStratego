package gfx;

import modelo.Bando;
import modelo.Pieza;
import modelo.TipoPieza;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;


public class Imagenes{
    private static final Color COLOR_HEROES = new Color(25, 85, 190);
    private static final Color COLOR_VILLANOS = new Color(150, 25, 40);

    private static HashMap<String, ImageIcon> cache = new HashMap<>();
    private static HashMap<String, BufferedImage> originales = new HashMap<>();

    public static String slug(String nombre){
        return nombre.toLowerCase().replaceAll("[^a-z0-9]+", "_");
    }

    private static BufferedImage leer(String ruta){
        if (originales.containsKey(ruta)){
            return originales.get(ruta);
        }
        BufferedImage img = null;
        URL url = Imagenes.class.getResource(ruta);
        if(url != null) {
            try {
                img = ImageIO.read(url);
            } catch (IOException e) {
                img = null;
            }
        }
        originales.put(ruta, img);
        return img;
    }

    public static Image getTablero(){
        return leer("/imagenes/tablero.jpg");
    }

    public static Image getFondo(){
        return leer("/imagenes/fondo_menu.jpg");
    }

    public static ImageIcon getFicha(Pieza p, int tam, boolean oculta){
        String clave = p.getBando() + "|" + p.getNombre() + "|" + tam + "|" + oculta;
        ImageIcon icono = cache.get(clave);
        if (icono != null){
            return icono;
        }

        BufferedImage lienzo = new BufferedImage(tam, tam, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = lienzo.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Color colorBando = (p.getBando() == Bando.HEROES) ? COLOR_HEROES : COLOR_VILLANOS;
        int margen = 3;
        int lado = tam - margen * 2;

        if (oculta){
            dibujarReverso(g, colorBando, margen, lado);
        }else{
            String ruta = "/imagenes/fichas/" + p.getBando().name().toLowerCase() + "/" + slug(p.getNombre()) + ".png";
            BufferedImage foto = leer(ruta);
            if (foto != null){
                g.drawImage(foto, margen, margen, lado, lado, null);
            }else{
                g.setColor(colorBando);
                g.fillRect(margen, margen, lado, lado);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, tam / 4));
                String ini = p.getNombre().substring(0, Math.min(2, p.getNombre().length()));
                int ancho = g.getFontMetrics().stringWidth(ini);
                g.drawString(ini, (tam - ancho) / 2, tam / 2 + tam / 10);
            }
            dibujarRango(g, p, tam);
        }

        g.setColor(colorBando);
        g.setStroke(new BasicStroke(3f));
        g.drawRect(margen, margen, lado, lado);
        g.dispose();

        icono = new ImageIcon(lienzo);
        cache.put(clave, icono);
        return icono;
    }

    private static void dibujarReverso(Graphics2D g, Color colorBando, int margen, int lado){
        g.setPaint(new GradientPaint(0, 0, colorBando.brighter(), lado, lado, colorBando.darker().darker()));
        g.fillRect(margen, margen, lado, lado);
        g.setColor(new Color(255, 255, 255, 60));
        g.setStroke(new BasicStroke(2f));
        g.drawLine(margen, margen, margen + lado, margen + lado);
        g.drawLine(margen + lado, margen, margen, margen + lado);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, lado / 2));
        int ancho = g.getFontMetrics().stringWidth("?");
        g.drawString("?", margen + (lado - ancho) / 2, margen + lado / 2 + lado / 6);
    }

    private static void dibujarRango(Graphics2D g, Pieza p, int tam){
        int d = Math.max(16, tam / 3);
        g.setColor(new Color(15, 15, 25, 230));
        g.fillOval(1, 1, d, d);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(1.5f));
        g.drawOval(1, 1, d, d);
        g.setFont(new Font("Arial", Font.BOLD, d * 2 / 3));
        String txt = p.getRangoTexto();
        FontMetrics fm = g.getFontMetrics();
        g.drawString(txt, 1 + (d - fm.stringWidth(txt)) / 2, 1 + (d + fm.getAscent() - fm.getDescent()) / 2);
    }
}
