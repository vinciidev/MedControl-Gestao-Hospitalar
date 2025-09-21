import javax.swing.*;
import java.awt.event.ActionEvent;

public class Login {
    private JPasswordField passwordField1;
    private JButton acessarButton;
    private boolean autenticado = false;
    private Usuario usuarioAutenticado = null;

    private void realizarLogin(ActionEvent e) {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha usuário e senha.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            return;
        }
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.autenticar(username, password);
        if (usuario != null) {
            autenticado = true;
            this.usuarioAutenticado = usuario;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            txtUsername.setText("");
            txtPassword.setText("");
        }
    }

    public boolean isAutenticado() {
        return autenticado;
    }
    public Usuario getUsuarioAutenticado() {
        return this.usuarioAutenticado;
    }
}


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
