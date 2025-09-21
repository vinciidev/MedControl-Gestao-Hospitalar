package br.com.clinica;

import javax.swing.*;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // Cria a janela de login
            JFrame frame = new JFrame("MedControl - Login");

            // Puxa o icon que esta na pasta resources e retorna caso não encontre
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


            // Cria uma sessão de login
            Login loginController = new Login();

            // Passa o designer do MainPanel (Form de login) para a tela
            frame.setContentPane(loginController.getMainPanel());

            // Configurações e exibição da janela
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack(); // Ajusta o tamanho da janela ao conteúdo
            frame.setLocationRelativeTo(null); // Centraliza
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }
}