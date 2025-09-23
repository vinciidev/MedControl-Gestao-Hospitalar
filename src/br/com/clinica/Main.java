package br.com.clinica;

import br.com.clinica.view.Login;

import javax.swing.*;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("MedControl - Login");

            try {
                URL iconeURL = Main.class.getResource("/logos/icon.png");
                if (iconeURL != null) {
                    ImageIcon icone = new ImageIcon(iconeURL);
                    frame.setIconImage(icone.getImage());
                } else {
                    System.err.println("Recurso do ícone não encontrado: icon.png");
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar o ícone: " + e.getMessage());
            }

            Login loginController = new Login();

            frame.setContentPane(loginController.getMainPanel());

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }
}