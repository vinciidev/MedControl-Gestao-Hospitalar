package br.com.clinica.view;

import br.com.clinica.db.UsuarioDAO;
import br.com.clinica.model.Usuario;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;

public class CadastroUsuarioForm {
    private JPanel cadastroUsuarioPainel;
    private JTextField nomeField;
    private JTextField usernameField;
    private JPasswordField senhaField;
    private JComboBox<Usuario.Role> tipoComboBox;
    private JButton cadastrarButton;

    public CadastroUsuarioForm() {
        $$$setupUI$$$(); // Método gerado pelo IntelliJ GUI Designer
        popularComboBoxRoles();
        configurarEventos();
    }

    public JPanel getPainel() {
        return cadastroUsuarioPainel;
    }

    private void popularComboBoxRoles() {
        tipoComboBox.removeAllItems();
        for (Usuario.Role role : Usuario.Role.values()) {
            tipoComboBox.addItem(role);
        }
    }

    private void configurarEventos() {
        cadastrarButton.addActionListener(e -> cadastrarUsuario());
    }

    private void cadastrarUsuario() {
        String nome = nomeField.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(cadastroUsuarioPainel, "O nome completo é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(cadastroUsuarioPainel, "O nome de usuário é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        char[] password = senhaField.getPassword();
        if (password.length == 0) {
            JOptionPane.showMessageDialog(cadastroUsuarioPainel, "A senha é obrigatória.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario.Role selectedRole = (Usuario.Role) tipoComboBox.getSelectedItem();
        if (selectedRole == null) {
            JOptionPane.showMessageDialog(cadastroUsuarioPainel, "Selecione um perfil para o usuário.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome);
        novoUsuario.setUsername(username);
        novoUsuario.setPassword(new String(password));
        novoUsuario.setRole(selectedRole);

        cadastrarButton.setEnabled(false);
        cadastrarButton.setText("Salvando...");

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                UsuarioDAO dao = new UsuarioDAO();

                if (dao.usuarioJaExiste(novoUsuario.getUsername())) {
                    return "USUARIO_EXISTENTE";
                }

                dao.salvarUsuario(novoUsuario);
                return "SUCESSO";
            }

            @Override
            protected void done() {
                try {
                    String resultado = get();

                    if ("SUCESSO".equals(resultado)) {
                        JOptionPane.showMessageDialog(cadastroUsuarioPainel, "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        limparCampos();
                    } else if ("USUARIO_EXISTENTE".equals(resultado)) {
                        JOptionPane.showMessageDialog(cadastroUsuarioPainel, "Erro ao salvar usuário: Este nome de usuário já está em uso.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception e) {
                    String mensagemErro = "Ocorreu um erro inesperado ao tentar salvar o usuário.";
                    if (e.getCause() != null) {
                        mensagemErro = e.getCause().getMessage();
                    }
                    JOptionPane.showMessageDialog(cadastroUsuarioPainel, "Erro: " + mensagemErro, "Erro", JOptionPane.ERROR_MESSAGE);
                } finally {
                    cadastrarButton.setEnabled(true);
                    cadastrarButton.setText("Cadastrar Usuário");
                }
            }
        };
        worker.execute();
    }

    private void limparCampos() {
        nomeField.setText("");
        usernameField.setText("");
        senhaField.setText("");
        tipoComboBox.setSelectedIndex(0);
        nomeField.requestFocusInWindow();
    }

    private void $$$setupUI$$$() {
        cadastroUsuarioPainel = new JPanel();
        cadastroUsuarioPainel.setLayout(new GridLayoutManager(6, 2, new Insets(10, 10, 10, 10), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setFont(new Font(label1.getFont().getName(), Font.BOLD, 16));
        label1.setText("Cadastro de Usuário");
        cadastroUsuarioPainel.add(label1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Nome Completo:");
        cadastroUsuarioPainel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nomeField = new JTextField();
        cadastroUsuarioPainel.add(nomeField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Username:");
        cadastroUsuarioPainel.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usernameField = new JTextField();
        cadastroUsuarioPainel.add(usernameField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Senha:");
        cadastroUsuarioPainel.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        senhaField = new JPasswordField();
        cadastroUsuarioPainel.add(senhaField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Tipo de Perfil:");
        cadastroUsuarioPainel.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tipoComboBox = new JComboBox();
        cadastroUsuarioPainel.add(tipoComboBox, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cadastrarButton = new JButton();
        cadastrarButton.setText("Cadastrar Usuário");
        cadastroUsuarioPainel.add(cadastrarButton, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    public JComponent $$$getRootComponent$$$() {
        return cadastroUsuarioPainel;
    }
}