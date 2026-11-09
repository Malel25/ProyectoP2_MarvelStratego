package gfx;

import excepciones.MovimientoInvalidoException;
import modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TableroVisual extends JFrame implements Refrescable{
    private static final Color COLOR_HEROES = new Color(25, 85, 190);
    private static final Color COLOR_VILLANOS = new Color(150, 25, 40);

    private MenuPrincipal menu;
    private Partida partida;
    private Tablero tablero;

    private JButton[][] botones = new JButton[10][10];
    private JPanel panelTablero;
    private JLabel lblTurno;
    private JPanel panelVencidosHeroes;
    private JPanel panelVencidosVillanos;
    private static final int TAM_CELDA = 62;
    private static final int TAM_MINI = 34;

    private int selFila = -1;
    private int selCol = -1;
    private boolean ocultando = false;

    public TableroVisual(MenuPrincipal menu, Partida partida){
        this.menu = menu;
        this.partida = partida;
        this.tablero = partida.getTablero();

        setTitle("Marvel Stratego - Partida");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        PanelFondo contenido = new PanelFondo(Imagenes.getFondo());
        contenido.setPreferredSize(new Dimension(940, 650));
        setContentPane(contenido);

        construirTablero();
        construirPanelLateral();
        refrescar();
        pack();
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosed(WindowEvent e){
                TableroVisual.this.menu.volverDePartida();
            }
        });
    }


    private void construirTablero(){
        final Image fondo = Imagenes.getTablero();
        panelTablero = new JPanel(new GridLayout(10, 10)){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                if (fondo != null){
                    g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panelTablero.setBounds(15, 15, TAM_CELDA * 10, TAM_CELDA * 10);
        panelTablero.setBackground(Color.DARK_GRAY);
        add(panelTablero);

        for (int f = 0; f < 10; f++){
            for (int c = 0; c < 10; c++){
                JButton btn = new JButton();
                btn.setMargin(new Insets(0, 0, 0, 0));
                btn.setFocusPainted(false);
                btn.setOpaque(false);
                btn.setContentAreaFilled(false);
                btn.setBorder(BorderFactory.createEmptyBorder());
                btn.setDisabledIcon(null);
                if(tablero.esZonaProhibida(f, c)){
                    btn.setEnabled(false);
                }
                final int fila = f;
                final int col = c;
                btn.addActionListener(e -> clicCasilla(fila, col));
                botones[f][c] = btn;
                panelTablero.add(btn);
            }
        }
    }

    private JPanel crearPanelVencidos(JPanel destino, int x, int y){
        JPanel iconos = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        iconos.setBackground(new Color(20, 24, 40));
        JScrollPane scroll = new JScrollPane(iconos);
        scroll.setBounds(x, y, 245, 150);
        destino.add(scroll);
        return iconos;
    }

    private void construirPanelLateral(){
        JPanel panel = new JPanel(null);
        panel.setBounds(655, 15, 265, 620);
        panel.setBackground(new Color(225, 228, 238));
        add(panel);

        lblTurno = new JLabel();
        lblTurno.setBounds(10, 10, 245, 25);
        lblTurno.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblTurno);

        JLabel lblHeroes = new JLabel("HEROES: " + partida.getJugador(Bando.HEROES).getUsername());
        lblHeroes.setBounds(10, 38, 245, 20);
        lblHeroes.setForeground(COLOR_HEROES);
        panel.add(lblHeroes);

        JLabel lblVillanos = new JLabel("VILLANOS: " + partida.getJugador(Bando.VILLANOS).getUsername());
        lblVillanos.setBounds(10, 58, 245, 20);
        lblVillanos.setForeground(COLOR_VILLANOS);
        panel.add(lblVillanos);

        JLabel lblModo = new JLabel("Modo: " + partida.getModo().name());
        lblModo.setBounds(10, 78, 245, 20);
        panel.add(lblModo);

        JLabel t1 = new JLabel("Heroes vencidos:");
        t1.setBounds(10, 110, 245, 20);
        panel.add(t1);
        panelVencidosHeroes = crearPanelVencidos(panel, 10, 132);

        JLabel t2 = new JLabel("Villanos vencidos:");
        t2.setBounds(10, 295, 245, 20);
        panel.add(t2);
        panelVencidosVillanos = crearPanelVencidos(panel, 10, 317);

        JButton btnRetiro = new JButton("RETIRO");
        btnRetiro.setBounds(10, 540, 245, 45);
        panel.add(btnRetiro);
        btnRetiro.addActionListener(e -> retirarse());
    }

    @Override
    public void refrescar(){
        Bando observador = partida.getTurno();

        for (int f = 0; f < 10; f++) {
            for (int c = 0; c < 10; c++) {
                JButton b = botones[f][c];
                boolean seleccionada = (f == selFila && c == selCol);
                b.setBorder(seleccionada
                        ? BorderFactory.createLineBorder(Color.YELLOW, 3)
                        : BorderFactory.createEmptyBorder());

                Pieza p = tablero.getPieza(f, c);
                if(p == null){
                    b.setIcon(null);
                    b.setToolTipText(null);
                    continue;
                }
                boolean visible = !ocultando && partida.esVisiblePara(p, observador);
                b.setIcon(Imagenes.getFicha(p, TAM_CELDA, !visible));
                b.setToolTipText(visible ? p.getNombre() + " - " + p.getDescripcion() : null);
            }
        }

        lblTurno.setText("Turno: " + observador.getNombre() + " ("
                + partida.getJugador(observador).getUsername() + ")");
        llenarVencidos(panelVencidosHeroes, Bando.HEROES);
        llenarVencidos(panelVencidosVillanos, Bando.VILLANOS);
        panelTablero.repaint();
    }

    private void llenarVencidos(JPanel panel, Bando bando){
        panel.removeAll();
        for(Pieza p : partida.getVencidas(bando)){
            JLabel lbl = new JLabel(Imagenes.getFicha(p, TAM_MINI, false));
            lbl.setToolTipText(p.getNombre() + " (" + p.getRangoTexto() + ")");
            panel.add(lbl);
        }
        panel.revalidate();
        panel.repaint();
    }

    private void clicCasilla(int fila, int col){
        if (partida.isTerminada() || ocultando){
            return;
        }
        Pieza p = tablero.getPieza(fila, col);
        boolean esPropia = (p != null && p.getBando() == partida.getTurno());

        if (selFila == -1 || esPropia){
            if (!esPropia) {
                JOptionPane.showMessageDialog(this, "Seleccione una de sus piezas");
                return;
            }
            if (!p.puedeMover()){
                JOptionPane.showMessageDialog(this, "Las bombas y la Tierra no se pueden mover");
                return;
            }
            selFila = fila;
            selCol = col;
            refrescar();
            return;
        }

        String resultado;
        try{
            resultado = partida.mover(selFila, selCol, fila, col);
        }catch(MovimientoInvalidoException ex){
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Movimiento invalido", JOptionPane.WARNING_MESSAGE);
            selFila = -1;
            selCol = -1;
            refrescar();
            return;
        }
        selFila = -1;
        selCol = -1;

        boolean clasico = (partida.getModo() == ModoJuego.CLASICO);
        ocultando = clasico && !partida.isTerminada();
        refrescar();
        if(!resultado.isEmpty()){
            JOptionPane.showMessageDialog(this, resultado, "Combate", JOptionPane.INFORMATION_MESSAGE);
        }

        if(partida.isTerminada()){
            ocultando = false;
            refrescar();
            JOptionPane.showMessageDialog(this, partida.getMensajeFinal(), "¡Fin de la partida!",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
            return;
        }

        if(clasico){
            Bando siguiente = partida.getTurno();
            JOptionPane.showMessageDialog(this, "Turno de " + partida.getJugador(siguiente).getUsername()
                    + " (" + siguiente.getNombre() + ").\nPresione Aceptar cuando este listo.",
                    "Cambio de turno", JOptionPane.INFORMATION_MESSAGE);
            ocultando = false;
        }
        refrescar();
    }

    private void retirarse(){
        if(partida.isTerminada()){
            return;
        }
        int op = JOptionPane.showConfirmDialog(this, "¿Esta seguro que desea retirarse?\nEl oponente ganara 3 puntos.",
                "Retiro", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION){
            partida.retirarse();
            JOptionPane.showMessageDialog(this, partida.getMensajeFinal(), "Partida terminada",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}
