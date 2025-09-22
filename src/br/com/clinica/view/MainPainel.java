package br.com.clinica.view;

import br.com.clinica.ImagePanel;
import br.com.clinica.model.Usuario;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.JInternalFrame;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class MainPainel {
    // Componentes
    private JPanel mainPainel;
    private JButton sairButton;
    private JButton cadastrarNovoPacienteButton;
    private JButton adminButton;
    private JButton prontuarioEButton;
    private JButton marcacoesButton;
    private JLabel labelBoasVindas;
    private JLabel labelHora;
    private JLabel labelDia;
    private JLabel labelTemperatura;
    private JLabel labelRole;
    private JPanel customPanel;
    private JDesktopPane desktopPane;

    private final Usuario usuarioLogado;

    public MainPainel(Usuario usuario) {
        this.usuarioLogado = usuario;

        $$$setupUI$$$();

        desktopPane = new JDesktopPane();

        desktopPane.setLayout(new BorderLayout());

        desktopPane.add(customPanel, BorderLayout.CENTER);

        desktopPane.setLayer(customPanel, Integer.valueOf(Integer.MIN_VALUE));

        mainPainel.remove(customPanel);
        mainPainel.add(desktopPane, new GridConstraints(7, 0, 1, 11,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null, 0, false));

        configurarLabelsDinamicos();
        configurarEventos();
    }

    public JPanel getMainPainel() {
        return mainPainel;
    }

    private void configurarLabelsDinamicos() {
        // Label de Boas-vindas com o nome do usuario
        labelBoasVindas.setText("Olá, " + usuarioLogado.getNome());
        labelRole.setText("Função: " + usuarioLogado.getRole().toString());

        // Configura data e hora
        atualizarData();
        Timer timerHora = new Timer(1000, e -> atualizarHora());
        timerHora.start();

        // Função para buscar a temperatura (Com API)
        buscarTemperatura();
    }


    private void atualizarData() {
        LocalDate hoje = LocalDate.now();
        Locale localeBrasil = new Locale("pt", "BR");
        String diaDaSemana = hoje.getDayOfWeek().getDisplayName(TextStyle.FULL, localeBrasil);
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", localeBrasil);
        labelDia.setText(diaDaSemana + ", " + hoje.format(formatadorData));
    }

    private void atualizarHora() {
        DateTimeFormatter formatadorHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        labelHora.setText("Hora: " + LocalDateTime.now().format(formatadorHora));
    }

    private void buscarTemperatura() {
        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                URL url = new URL("https://wttr.in/Feira%20de%20Santana?format=%t");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                    return in.readLine();
                }
            }

            @Override
            protected void done() {
                try {
                    String temperatura = get();
                    labelTemperatura.setText(temperatura != null && !temperatura.isEmpty() ? "Temperatura: " + temperatura : "Temperatura: N/A");
                } catch (Exception e) {
                    labelTemperatura.setText("Temperatura: Erro");
                }
            }
        };
        worker.execute();
    }

    private void configurarEventos() {
        sairButton.addActionListener(e -> {
            int resposta = JOptionPane.showConfirmDialog(mainPainel.getTopLevelAncestor(), "Você realmente deseja sair?", "Confirmar Saída", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        cadastrarNovoPacienteButton.addActionListener(e -> {
            JFrame cadastroFrame = new JFrame("Cadastro de Novo Paciente");

            // Pega o ícone da janela principal e aplica na nova
            JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPainel);
            if (mainFrame != null) {
                cadastroFrame.setIconImages(mainFrame.getIconImages());
            }

            cadastroFrame.setContentPane(new CadastroPacienteForm().getPainel());
            cadastroFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // DISPOSE_ON_CLOSE fecha só esta janela
            cadastroFrame.setSize(450, 500);
            cadastroFrame.setLocationRelativeTo(mainFrame); // Centraliza em relação à tela principal
            cadastroFrame.setVisible(true);
        });

        adminButton.addActionListener(e -> {
            JFrame cadastroFrame = new JFrame("Cadastro de Usuário");
            JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPainel);
            if (mainFrame != null) {
                cadastroFrame.setIconImages(mainFrame.getIconImages());
            }

            cadastroFrame.setContentPane(new CadastroUsuarioForm().getPainel());
            cadastroFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // DISPOSE_ON_CLOSE fecha só esta janela
            cadastroFrame.setSize(450, 500);
            cadastroFrame.setLocationRelativeTo(mainFrame); // Centraliza em relação à tela principal
            cadastroFrame.setVisible(true);
        });

        prontuarioEButton.addActionListener(e -> {
            // Este botão agora abre a tela de CONSULTAS
            // onde o usuário poderá selecionar uma e abrir o prontuário.
            JInternalFrame[] frames = desktopPane.getAllFrames();
            for (JInternalFrame frame : frames) {
                if (frame instanceof ConsultaView) {
                    frame.moveToFront();
                    return;
                }
            }
            ConsultaView consultaView = new ConsultaView();
            desktopPane.add(consultaView);

            consultaView.setVisible(true);
        });

        marcacoesButton.addActionListener(e -> {
            // Procura por uma janela de consulta já aberta para não abrir várias iguais
            JInternalFrame[] frames = desktopPane.getAllFrames();
            for (JInternalFrame frame : frames) {
                if (frame instanceof ConsultaView) {
                    frame.moveToFront(); // Se já estiver aberta, traz para frente
                    return;
                }
            }


            ConsultaView consultaView = new ConsultaView();
            desktopPane.add(consultaView); // Adiciona a janela de consultas ao painel principal
            consultaView.setVisible(true); // Exibe a janela
        });

    }


    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPainel = new JPanel();
        mainPainel.setLayout(new GridLayoutManager(10, 11, new Insets(0, 0, 0, 0), -1, -1));
        mainPainel.setBackground(new Color(-15649182));
        final Spacer spacer1 = new Spacer();
        mainPainel.add(spacer1, new GridConstraints(1, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        sairButton = new JButton();
        sairButton.setText("Sair");
        mainPainel.add(sairButton, new GridConstraints(0, 9, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cadastrarNovoPacienteButton = new JButton();
        cadastrarNovoPacienteButton.setText("Cadastrar Novo Paciente");
        mainPainel.add(cadastrarNovoPacienteButton, new GridConstraints(0, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        adminButton = new JButton();
        adminButton.setText("Admin");
        mainPainel.add(adminButton, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        prontuarioEButton = new JButton();
        prontuarioEButton.setText("Prontuário E.");
        mainPainel.add(prontuarioEButton, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPainel.add(spacer2, new GridConstraints(2, 0, 1, 11, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        mainPainel.add(spacer3, new GridConstraints(3, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        labelBoasVindas = new JLabel();
        labelBoasVindas.setForeground(new Color(-262149));
        labelBoasVindas.setText("Olá, ");
        mainPainel.add(labelBoasVindas, new GridConstraints(8, 0, 2, 6, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelHora = new JLabel();
        labelHora.setForeground(new Color(-262149));
        labelHora.setText("Hora:");
        mainPainel.add(labelHora, new GridConstraints(4, 8, 1, 3, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelDia = new JLabel();
        labelDia.setForeground(new Color(-262149));
        labelDia.setText("Dia:");
        mainPainel.add(labelDia, new GridConstraints(5, 10, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelTemperatura = new JLabel();
        labelTemperatura.setForeground(new Color(-262149));
        labelTemperatura.setText("Temperatura:");
        mainPainel.add(labelTemperatura, new GridConstraints(6, 9, 1, 2, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        marcacoesButton = new JButton();
        marcacoesButton.setText("Marcações");
        mainPainel.add(marcacoesButton, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelRole = new JLabel();
        labelRole.setForeground(new Color(-262149));
        labelRole.setText("Role");
        mainPainel.add(labelRole, new GridConstraints(8, 9, 2, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mainPainel.add(customPanel, new GridConstraints(7, 0, 1, 11, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPainel;
    }

    private void createUIComponents() {
        try {
            URL imageURL = getClass().getResource("/images/homeImage.png");
            Image backgroundImage = new ImageIcon(imageURL).getImage();

            customPanel = new ImagePanel(backgroundImage); //

        } catch (Exception e) {
            customPanel = new JPanel(); //
            System.err.println("Imagem de fundo não encontrada.");
        }
    }
}