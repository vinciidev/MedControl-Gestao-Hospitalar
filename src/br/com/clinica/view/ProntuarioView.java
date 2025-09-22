package br.com.clinica.view;

import br.com.clinica.db.ProntuarioDAO;
import br.com.clinica.model.Consulta;
import br.com.clinica.model.Prontuario;
import javax.swing.*;
import java.awt.*;

// Adicionado "extends JInternalFrame" para que seja uma janela interna
public class ProntuarioView extends JInternalFrame {

    // --- 1. DECLARAÇÃO DAS VARIÁVEIS (Estava faltando) ---
    private final JTextArea txtAnotacoes;
    private final JTextArea txtPrescricao;
    private final JTextArea txtExames;
    private final ProntuarioDAO prontuarioDAO;
    private final Consulta consulta;
    private Prontuario prontuarioAtual;

    // --- 2. CONSTRUTOR CORRIGIDO (Agora recebe a Consulta) ---
    public ProntuarioView(Consulta consulta) {
        // Usa o parâmetro 'consulta' para construir o título da janela
        super("Prontuário - Paciente: " + consulta.getPaciente().getNome(), true, true, true, true);

        // Inicializa a variável da classe com o parâmetro recebido
        this.consulta = consulta;
        this.prontuarioDAO = new ProntuarioDAO();
        setSize(700, 600);

        // --- O resto do seu código para construir a UI ---
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        mainPanel.add(new JLabel("Anotações Clínicas:"), gbc);
        gbc.gridy = 1;
        gbc.weighty = 0.4;
        txtAnotacoes = new JTextArea(10, 50); // Agora esta linha funciona
        mainPanel.add(new JScrollPane(txtAnotacoes), gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.0;
        mainPanel.add(new JLabel("Prescrição:"), gbc);
        gbc.gridy = 3;
        gbc.weighty = 0.3;
        txtPrescricao = new JTextArea(7, 50); // Agora esta linha funciona
        mainPanel.add(new JScrollPane(txtPrescricao), gbc);

        gbc.gridy = 4;
        gbc.weighty = 0.0;
        mainPanel.add(new JLabel("Exames Solicitados:"), gbc);
        gbc.gridy = 5;
        gbc.weighty = 0.3;
        txtExames = new JTextArea(7, 50); // Agora esta linha funciona
        mainPanel.add(new JScrollPane(txtExames), gbc);

        JButton btnSalvar = new JButton("Salvar Prontuário");
        btnSalvar.addActionListener(e -> salvar());

        setLayout(new BorderLayout(10, 10));
        add(mainPanel, BorderLayout.CENTER);
        add(btnSalvar, BorderLayout.SOUTH);

        carregarDadosProntuario();
    }

    private void carregarDadosProntuario() {
        // Este método agora funciona porque as variáveis foram declaradas
        prontuarioAtual = prontuarioDAO.buscarPorConsulta(consulta.getId());
        if (prontuarioAtual != null) {
            txtAnotacoes.setText(prontuarioAtual.getAnotacoesMedico());
            txtPrescricao.setText(prontuarioAtual.getPrescricao());
            txtExames.setText(prontuarioAtual.getExamesSolicitados());
        }
    }

    private void salvar() {
        // E este método também
        if (prontuarioAtual == null) {
            prontuarioAtual = new Prontuario();
            prontuarioAtual.setConsultaId(consulta.getId());
        }
        prontuarioAtual.setAnotacoesMedico(txtAnotacoes.getText());
        prontuarioAtual.setPrescricao(txtPrescricao.getText());
        prontuarioAtual.setExamesSolicitados(txtExames.getText());
        try {
            prontuarioDAO.salvar(prontuarioAtual);
            JOptionPane.showMessageDialog(this, "Prontuário salvo com sucesso!");
            dispose(); // Fecha a janela de prontuário
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar prontuário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}