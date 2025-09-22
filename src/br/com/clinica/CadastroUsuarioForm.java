package br.com.clinica;

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

    // Construtor
    public CadastroUsuarioForm() {
        // O IntelliJ vai chamar $$$setupUI$$$() aqui
        popularComboBoxRoles();
        configurarEventos();
    }

    public JPanel getPainel() {
        return cadastroUsuarioPainel;
    }

    private void popularComboBoxRoles() {
        // Limpa o ComboBox antes de adicionar para evitar duplicatas
        tipoComboBox.removeAllItems();
        for (Usuario.Role role : Usuario.Role.values()) {
            tipoComboBox.addItem(role);
        }
    }

    private void configurarEventos() {
        cadastrarButton.addActionListener(e -> cadastrarUsuario());
    }

    private void cadastrarUsuario() {
        // Validação de Nome
        String nome = nomeField.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(cadastroUsuarioPainel, "O nome completo é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validação de Username
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(cadastroUsuarioPainel, "O nome de usuário é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validação de Senha
        char[] password = senhaField.getPassword();
        if (password.length == 0) {
            JOptionPane.showMessageDialog(cadastroUsuarioPainel, "A senha é obrigatória.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validação da Role
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

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                new UsuarioDAO().salvarNovoUsuario(novoUsuario);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(cadastroUsuarioPainel, "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparCampos();
                } catch (Exception e) {
                    String mensagemErro = "Ocorreu um erro inesperado.";
                    if (e.getCause() != null) {
                        mensagemErro = e.getCause().getMessage();
                        if (mensagemErro.contains("usuarios_username_key")) {
                            mensagemErro = "Este nome de usuário já está em uso.";
                        }
                    }
                    JOptionPane.showMessageDialog(cadastroUsuarioPainel, "Erro ao salvar usuário: " + mensagemErro, "Erro", JOptionPane.ERROR_MESSAGE);
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
        tipoComboBox.setSelectedIndex(-1);
        nomeField.requestFocusInWindow();
    }

    {
        $$$setupUI$$$();
    }

    private void $$$setupUI$$$() {
        cadastroUsuarioPainel = new JPanel();
        cadastroUsuarioPainel.setLayout(new GridLayoutManager(6, 2, new Insets(10, 10, 10, 10), -1, -1));

        final JLabel labelTitle = new JLabel();
        labelTitle.setFont(new Font(labelTitle.getFont().getName(), Font.BOLD, 16));
        labelTitle.setText("Cadastro de Usuário");
        cadastroUsuarioPainel.add(labelTitle, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        final JLabel labelNome = new JLabel();
        labelNome.setText("Nome Completo:");
        cadastroUsuarioPainel.add(labelNome, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nomeField = new JTextField();
        cadastroUsuarioPainel.add(nomeField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));

        final JLabel labelUsername = new JLabel();
        labelUsername.setText("Username:");
        cadastroUsuarioPainel.add(labelUsername, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usernameField = new JTextField();
        cadastroUsuarioPainel.add(usernameField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));

        final JLabel labelSenha = new JLabel();
        labelSenha.setText("Senha:");
        cadastroUsuarioPainel.add(labelSenha, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        senhaField = new JPasswordField();
        cadastroUsuarioPainel.add(senhaField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));

        final JLabel labelTipo = new JLabel();
        labelTipo.setText("Tipo de Perfil:");
        cadastroUsuarioPainel.add(labelTipo, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tipoComboBox = new JComboBox<>();
        cadastroUsuarioPainel.add(tipoComboBox, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        cadastrarButton = new JButton();
        cadastrarButton.setText("Cadastrar Usuário");
        cadastroUsuarioPainel.add(cadastrarButton, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    public JComponent $$$getRootComponent$$$() {
        return cadastroUsuarioPainel;
    }
}