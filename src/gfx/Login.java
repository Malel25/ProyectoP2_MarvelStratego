package gfx;

import excepciones.CredencialesInvalidasException;
import modelo.SistemaJugadores;
import javax.swing.*;

public class Login extends JFrame{
    private MenuInicial menuInicial;
    private JTextField txtUser;
    private JPasswordField txtPass;

    public Login(MenuInicial menuInicial){
        this.menuInicial = menuInicial;

        setTitle("Marvel Stratego - Log In");
        setSize(430, 230);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(40, 30, 100, 25);
        add(lblUser);

        txtUser = new JTextField();
        txtUser.setBounds(140, 30, 170, 25);
        add(txtUser);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(40, 70, 100, 25);
        add(lblPass);

        txtPass = new JPasswordField();
        txtPass.setBounds(140, 70, 170, 25);
        add(txtPass);
        
        VerificacionPassword verP = new VerificacionPassword(txtPass);
        verP.setBounds(320,70,70,25);
        add(verP);
        
        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setBounds(70, 130, 110, 30);
        add(btnEntrar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(210, 130, 110, 30);
        add(btnCancelar);

        btnEntrar.addActionListener(e -> intentarLogin());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void intentarLogin(){
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());
        try{
            SistemaJugadores.login(user, pass);
            new MenuPrincipal().setVisible(true);
            menuInicial.dispose();
            dispose();
        }catch(CredencialesInvalidasException ex){
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Log In", JOptionPane.ERROR_MESSAGE);
            txtPass.setText("");
        }
    }
}
