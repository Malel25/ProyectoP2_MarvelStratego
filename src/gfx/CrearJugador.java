package gfx;

import excepciones.PasswordInvalidoException;
import excepciones.UsuarioDuplicadoException;
import modelo.SistemaJugadores;
import javax.swing.*;

public class CrearJugador extends JFrame{
    private JTextField txtUser;
    private JPasswordField txtPass;

    public CrearJugador(){
        setTitle("Marvel Stratego - Crear Player");
        setSize(400, 230);
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

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(60, 125, 120, 30);
        add(btnRegistrar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(210, 125, 120, 30);
        add(btnCancelar);

        btnRegistrar.addActionListener(e -> registrar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void registrar(){
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());
        if(user.isEmpty()){
            JOptionPane.showMessageDialog(this, "El username no puede estar vacio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try{
            SistemaJugadores.registrarJugador(user, pass);
            JOptionPane.showMessageDialog(this, "Player creado con exito!");
            dispose();
        }catch(UsuarioDuplicadoException | PasswordInvalidoException ex){
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
