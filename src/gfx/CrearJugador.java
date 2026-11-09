package gfx;

import excepciones.PasswordInvalidoException;
import excepciones.UsuarioDuplicadoException;
import modelo.SistemaJugadores;
import javax.swing.*;

public class CrearJugador extends JFrame{
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JPasswordField txtConfirmar;

    public CrearJugador(){
        setTitle("Marvel Stratego - Crear Jugador");
        setSize(470, 290);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(30, 30, 100, 25);
        add(lblUser);

        txtUser = new JTextField();
        txtUser.setBounds(180, 30, 170, 25);
        add(txtUser);

        JLabel lblPass = new JLabel("Password (5 caracteres):");
        lblPass.setBounds(30, 70, 150, 25);
        add(lblPass);

        txtPass = new JPasswordField();
        txtPass.setBounds(180, 70, 170, 25);
        add(txtPass);
        
        VerificacionPassword ver = new VerificacionPassword(txtPass);
        ver.setBounds(360, 70, 70, 25);
        add(ver);

        JLabel lblConfirmar = new JLabel("Confirmar password:");
        lblConfirmar.setBounds(30, 110, 150, 25);
        add(lblConfirmar);

        txtConfirmar = new JPasswordField();
        txtConfirmar.setBounds(180, 110, 170, 25);
        add(txtConfirmar);

        VerificacionPassword verConfirmar = new VerificacionPassword(txtConfirmar);
        verConfirmar.setBounds(360, 110, 70, 25);
        add(verConfirmar);
        
        JLabel lblReglas = new JLabel("Debe tener mayuscula, minuscula, numero y simbolo (ej: Ab1#x)");
        lblReglas.setBounds(30, 145, 420, 20);
        add(lblReglas);
        
        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(80, 180, 120, 30);
        add(btnRegistrar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(230, 180, 120, 30);
        add(btnCancelar);

        btnRegistrar.addActionListener(e -> registrar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void registrar(){
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());
        String confirmar = new String(txtConfirmar.getPassword());
        if(user.isEmpty()){
            JOptionPane.showMessageDialog(this, "El username no puede estar vacio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try{
            SistemaJugadores.validarConfirmacion(pass, confirmar);
            SistemaJugadores.registrarJugador(user, pass);
            JOptionPane.showMessageDialog(this, "Jugador creado con exito!");
            dispose();
        }catch(UsuarioDuplicadoException | PasswordInvalidoException ex){
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
