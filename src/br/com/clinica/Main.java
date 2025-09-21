package br.com.clinica;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Cria a janela principal (o "frame")
            JFrame frame = new JFrame("MedControl - Login");

            // 2. Cria uma instância da sua classe de lógica do Login
            Login loginController = new Login();

            // 3. Pega o painel visual (já pronto) e coloca na janela
            frame.setContentPane(loginController.getMainPanel());

            // 4. Configura e exibe a janela
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack(); // Ajusta o tamanho da janela ao conteúdo
            frame.setLocationRelativeTo(null); // Centraliza
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }
}