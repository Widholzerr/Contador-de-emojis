package ui;

import model.historicoCagada;
import model.registroCagada;
import service.ExportadorCSV;
import service.Leitor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

/**
 * UI moderna com Swing puro (Nimbus L&F se disponível).
 * - importar arquivo .txt
 * - visualizar tabela diária
 * - mostrar totais
 * - exportar CSV
 */
public class CagadaUi extends JFrame {

    private final DefaultTableModel tableModel;
    private final JLabel totalLabel;
    private historicoCagada historico;

    public CagadaUi() {
        setTitle("💩 Contador de Cagadas — Pedro & Gabi");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(new Color(250, 249, 246));

        // Top panel (title)
        JLabel title = new JLabel("📅 Relatório Mensal de Cagadas", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(75, 55, 45));
        title.setBorder(new EmptyBorder(12, 12, 12, 12));
        add(title, BorderLayout.NORTH);

        // Center: tabela
        String[] colunas = {"Data", "Pedro (💩)", "Gabi (💩)"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFont(new Font("Consolas", Font.PLAIN, 14));
        table.setFillsViewportHeight(true);
        table.setShowGrid(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 210, 200)));
        add(scroll, BorderLayout.CENTER);

        // Right panel: ações e totais
        JPanel right = new JPanel();
        right.setPreferredSize(new Dimension(260, 0));
        right.setBackground(new Color(250, 249, 246));
        right.setLayout(new BorderLayout(8, 8));
        right.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Totais
        totalLabel = new JLabel("", SwingConstants.CENTER);
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLabel.setForeground(new Color(90, 60, 45));
        updateTotals(0, 0);
        right.add(totalLabel, BorderLayout.NORTH);

        // Buttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(0, 1, 8, 8));
        buttons.setBackground(new Color(250, 249, 246));

        JButton btnImport = new JButton("📂 Importar descrição (.txt)");
        JButton btnPaste = new JButton("📋 Colar descrição (CTRL+V)");
        JButton btnReset = new JButton("♻️ Limpar");
        JButton btnExport = new JButton("💾 Exportar CSV");
        btnExport.setEnabled(false);

        styleButton(btnImport);
        styleButton(btnPaste);
        styleButton(btnReset);
        styleButton(btnExport);

        buttons.add(btnImport);
        buttons.add(btnPaste);
        buttons.add(btnReset);
        buttons.add(btnExport);

        right.add(buttons, BorderLayout.CENTER);
        add(right, BorderLayout.EAST);

        // Bottom: info
        JLabel info = new JLabel("Cole a descrição do grupo ou importe o .txt exportado do WhatsApp.", SwingConstants.CENTER);
        info.setFont(new Font("SansSerif", Font.PLAIN, 12));
        info.setForeground(new Color(120, 95, 80));
        info.setBorder(new EmptyBorder(6, 6, 6, 6));
        add(info, BorderLayout.SOUTH);

        // Actions
        btnImport.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int op = chooser.showOpenDialog(this);
            if (op == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    historico = Leitor.lerArquivo(file);
                    refreshTable();
                    btnExport.setEnabled(!historico.estaVazio());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao ler arquivo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnPaste.addActionListener(e -> {
            try {
                String pasted = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(java.awt.datatransfer.DataFlavor.stringFlavor);
                // salvar temporariamente e ler com o leitor para garantir o mesmo parser
                File temp = File.createTempFile("desc_grupo", ".txt");
                java.nio.file.Files.writeString(temp.toPath(), pasted);
                historico = Leitor.lerArquivo(temp);
                temp.delete();
                refreshTable();
                btnExport.setEnabled(!historico.estaVazio());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao colar/ler texto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnReset.addActionListener(e -> {
            historico = new historicoCagada();
            tableModel.setRowCount(0);
            updateTotals(0, 0);
            btnExport.setEnabled(false);
        });

        btnExport.addActionListener(e -> {
            if (historico == null || historico.estaVazio()) {
                JOptionPane.showMessageDialog(this, "Nada para exportar!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File("relatorio_cagadas.csv"));
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File out = chooser.getSelectedFile();
                try {
                    ExportadorCSV.exportar(out, historico);
                    JOptionPane.showMessageDialog(this, "Relatório exportado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao exportar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        if (historico == null) {
            updateTotals(0, 0);
            return;
        }
        for (registroCagada reg : historicoCagada.listar()) {
            tableModel.addRow(new Object[]{reg.getData(), reg.getPedro(), reg.getGabriela()});
        }
        updateTotals(historicoCagada.getTotalPedro(), historico.getTotalGabriela());
    }

    private void updateTotals(int pedroTotal, int gabiTotal) {
        totalLabel.setText(String.format("<html><div style='text-align:center;'>Total — Pedro: <b>%d</b> 💩<br>Gabi: <b>%d</b> 💩</div></html>", pedroTotal, gabiTotal));
    }

    private void styleButton(JButton b) {
        b.setBackground(new Color(230, 220, 210));
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
    }
}