package br.com.clinica.view;

import br.com.clinica.db.ConsultaDAO;
import br.com.clinica.db.PacienteDAO;
import br.com.clinica.db.UsuarioDAO; // Usa UsuarioDAO
import br.com.clinica.model.Consulta;
import br.com.clinica.model.Paciente;
import br.com.clinica.model.Usuario; // Usa Usuario
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class ConsultaView extends JInternalFrame {
    private final ConsultaDAO consultaDAO;
    private final JComboBox<Paciente> cbPaciente;
    private final JComboBox<Usuario> cbMedico;
    private final JDateChooser dateChooser;
    private final JSpinner timeSpinner;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private List<Consulta> listaConsultasAtual;

    public ConsultaView() {
        super("Agendamento de Consultas", true, true, true, true);
        consultaDAO = new ConsultaDAO();
        setSize(900, 600);

        // ... (o código que cria os painéis e botões continua o mesmo)
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Agendar Nova Consulta"));
        formPanel.add(new JLabel("Paciente:"));
        cbPaciente = new JComboBox<>();
        formPanel.add(cbPaciente);
        formPanel.add(new JLabel("Médico:"));
        cbMedico = new JComboBox<>();
        formPanel.add(cbMedico);
        formPanel.add(new JLabel("Data da Consulta:"));
        dateChooser = new JDateChooser();
        formPanel.add(dateChooser);
        formPanel.add(new JLabel("Hora (HH:mm):"));
        timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date());
        formPanel.add(timeSpinner);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAgendar = new JButton("Agendar");
        JButton btnExcluir = new JButton("Excluir Agendamento");
        JButton btnAbrirProntuario = new JButton("Abrir Prontuário");
        buttonPanel.add(btnAgendar);
        buttonPanel.add(btnExcluir);
        buttonPanel.add(btnAbrirProntuario);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnConfirmar = new JButton("Confirmar Consulta");
        JButton btnCancelar = new JButton("Cancelar Consulta");
        actionPanel.add(btnConfirmar);
        actionPanel.add(btnCancelar);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.NORTH);
        southPanel.add(actionPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Paciente", "Médico", "Data/Hora", "Status"}, 0);
        table = new JTable(tableModel);

        setLayout(new BorderLayout(10, 10));
        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        carregarPacientes();
        carregarMedicos();

        btnAgendar.addActionListener(e -> agendarConsulta());
        btnExcluir.addActionListener(e -> excluirConsulta());
        btnAbrirProntuario.addActionListener(e -> abrirProntuario());
        btnConfirmar.addActionListener(e -> alterarStatus("CONFIRMADA"));
        btnCancelar.addActionListener(e -> alterarStatus("CANCELADA"));

        atualizarTabela();
    }

    private void abrirProntuario() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma consulta na tabela para ver o prontuário.", "Nenhuma Consulta Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Consulta consultaSelecionada = listaConsultasAtual.get(selectedRow);

        ProntuarioView prontuarioView = new ProntuarioView(consultaSelecionada);

        JDesktopPane desktopPane = getDesktopPane();
        desktopPane.add(prontuarioView);
        prontuarioView.setVisible(true);
    }

    private void carregarMedicos() {
        cbMedico.removeAllItems();
        (new SwingWorker<List<Usuario>, Void>() {
            @Override protected List<Usuario> doInBackground() { return new UsuarioDAO().listarMedicos(); }
            @Override protected void done() {
                try { get().forEach(cbMedico::addItem); } catch (Exception e) { e.printStackTrace(); }
            }
        }).execute();
    }

    private void agendarConsulta() {
        try {
            Consulta consulta = new Consulta();
            consulta.setPaciente((Paciente) cbPaciente.getSelectedItem());
            consulta.setMedico((Usuario) cbMedico.getSelectedItem());

            Date data = dateChooser.getDate();
            Date hora = (Date) timeSpinner.getValue();
            if (data == null || hora == null) {
                JOptionPane.showMessageDialog(this, "Data e hora devem ser preenchidas.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            LocalDate localDate = data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime localTime = hora.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            consulta.setDataHora(LocalDateTime.of(localDate, localTime));

            consulta.setStatus("AGENDADA");
            consulta.setStatusChamada(Consulta.StatusChamada.AGUARDANDO);
            consultaDAO.salvar(consulta);
            JOptionPane.showMessageDialog(this, "Consulta agendada com sucesso!");
            atualizarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao agendar consulta: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarTabela() {
        (new SwingWorker<List<Consulta>, Void>() {
            @Override
            protected List<Consulta> doInBackground() {
                return consultaDAO.listarTodas();
            }

            @Override
            protected void done() {
                try {
                    listaConsultasAtual = get();
                    tableModel.setRowCount(0);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                    for (Consulta c : listaConsultasAtual) {
                        tableModel.addRow(new Object[]{
                                c.getId(),
                                c.getPaciente().getNome(),
                                c.getMedico().getNome(),
                                c.getDataHora().format(formatter),
                                c.getStatus()
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(ConsultaView.this,
                            "Erro ao atualizar a tabela de consultas: " + e.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }).execute();
    }


    private void carregarPacientes() {
        cbPaciente.removeAllItems();

        SwingWorker<List<Paciente>, Void> worker = new SwingWorker<>() {

            @Override
            protected List<Paciente> doInBackground() throws Exception {
                return new PacienteDAO().listarTodos();
            }

            @Override
            protected void done() {
                try {
                    List<Paciente> pacientes = get();
                    for (Paciente paciente : pacientes) {
                        cbPaciente.addItem(paciente);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(
                            ConsultaView.this,
                            "Erro ao carregar a lista de pacientes: " + e.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };
        worker.execute();
    }
    private void excluirConsulta() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma consulta para excluir.", "Nenhuma Consulta Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir a consulta selecionada?\nEsta ação não pode ser desfeita.", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String consultaId = (String) tableModel.getValueAt(selectedRow, 0);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                consultaDAO.excluir(consultaId);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(ConsultaView.this, "Consulta excluída com sucesso!");
                    atualizarTabela();
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(ConsultaView.this, "Erro ao excluir consulta: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void alterarStatus(String novoStatus) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma consulta para alterar o status.", "Nenhuma Consulta Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente alterar o status desta consulta para '" + novoStatus + "'?", "Confirmar Alteração de Status", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String consultaId = (String) tableModel.getValueAt(selectedRow, 0);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                consultaDAO.atualizarStatusConsulta(consultaId, novoStatus);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(ConsultaView.this, "Status da consulta atualizado com sucesso!");
                    atualizarTabela();
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(ConsultaView.this, "Erro ao atualizar status: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
}