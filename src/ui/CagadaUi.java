package ui;

import model.historicoCagada;
import model.registroCagada;
import service.ExportadorCSV;
import service.Leitor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;

public class CagadaUi extends JFrame {

    private final DefaultTableModel tableModel;
    private final JLabel totalLabel;
    private historicoCagada historico;
    private boolean escuroAtivo = false;

    public CagadaUi() {
        setTitle("💩 Contador de Cagadas — Pedro & Gabi");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(new Color(250, 249, 246));

        // Top panel (title)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(250, 249, 246));
        topPanel.setBorder(new EmptyBorder(8, 16, 8, 16));

        JLabel title = new JLabel("🧻 Relatório Mensal de Cagadas", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        title.setForeground(new Color(75, 55, 45));

        // Botão de tema (claro/escuro)
        JButton btnTema = new JButton("🌙");
        btnTema.setFocusPainted(false);
        btnTema.setBorderPainted(false);
        btnTema.setBackground(new Color(250, 249, 246));
        btnTema.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnTema.setToolTipText("Alternar modo claro/escuro");
        btnTema.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnTema.addActionListener(e -> {
            escuroAtivo = !escuroAtivo;
            atualizarTema(escuroAtivo);
            btnTema.setText(escuroAtivo ? "☀️" : "🌙");
        });

        topPanel.add(title, BorderLayout.CENTER);
        topPanel.add(btnTema, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center: tabela
        String[] colunas = {"Data", "Pedro (💩)", "Obs.: Pedro", "Gabi (💩)", "Obs.: Gabi"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        table.setRowHeight(36);
        table.setShowGrid(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 210, 200)));
        add(scroll, BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout(8, 8));
        right.setPreferredSize(new Dimension(260, 0));
        right.setBackground(new Color(250, 249, 246));
        right.setBorder(new EmptyBorder(12, 12, 12, 12));

        totalLabel = new JLabel("", SwingConstants.CENTER);
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLabel.setForeground(new Color(90, 60, 45));
        updateTotals(0, 0);
        right.add(totalLabel, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(0, 1, 8, 8));
        buttons.setBackground(new Color(250, 249, 246));

        JButton btnImport = new JButton("📂 Importar descrição (.txt)");
        JButton btnPaste = new JButton("📋 Colar descrição (CTRL+V)");
        JButton btnReset = new JButton("♻️ Limpar");
        JButton btnExport = new JButton("💾 Exportar CSV");
        JButton btnGraph = new JButton("📊 Ver Gráfico");
        btnExport.setEnabled(false);

        styleButton(btnImport);
        styleButton(btnPaste);
        styleButton(btnReset);
        styleButton(btnExport);
        styleButton(btnGraph);

        buttons.add(btnImport);
        buttons.add(btnPaste);
        buttons.add(btnReset);
        buttons.add(btnExport);
        buttons.add(btnGraph);

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

        btnGraph.addActionListener(e -> {
            if (historico == null || historico.estaVazio()) {
                JOptionPane.showMessageDialog(this, "Nada para exibir!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            new GraficoUi(historico);
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        if (historico == null || historico.estaVazio()) {
            updateTotals(0, 0);
            return;
        }

        for (registroCagada r : historico.listar()) {
            tableModel.addRow(new Object[]{
                    r.getData(),
                    r.getPedro(),
                    r.getObsPedro(),
                    r.getGabriela(),
                    r.getObsGabi()
            });
        }

        updateTotals(historico.getTotalPedro(), historico.getTotalGabriela());
    }

    private void updateTotals(int pedroTotal, int gabiTotal) {
        double mediaPedro = historico != null ? historico.getMediaPedro() : 0;
        double mediaGabi = historico != null ? historico.getMediaGabi() : 0;

        totalLabel.setText(String.format(
                "<html><div style='text-align:center;'>"
                        + "<b>Total Mensal</b><br>"
                        + "Pedro: %d 💩 (média %.2f/dia)<br>"
                        + "Gabi: %d 💩 (média %.2f/dia)"
                        + "</div></html>",
                pedroTotal, mediaPedro, gabiTotal, mediaGabi
        ));
    }

    private void styleButton(JButton b) {
        b.setBackground(new Color(230, 220, 210));
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
    }

    private void atualizarTema(boolean escuro) {
        Color fundo, texto, painel, borda, tabelaBg, tabelaTexto;
        this.escuroAtivo = escuro;

        if (escuro) {
            fundo = new Color(40, 40, 45);
            painel = new Color(55, 55, 60);
            texto = new Color(235, 235, 235);
            tabelaBg = new Color(50, 50, 55);
            tabelaTexto = new Color(240, 240, 240);
            borda = new Color(90, 90, 90);
            UIManager.put("Table.gridColor", new Color(65, 65, 70));
            UIManager.put("ScrollBar.thumb", new Color(80, 80, 85));
            UIManager.put("ScrollBar.track", new Color(45, 45, 50));
        } else {
            fundo = new Color(250, 249, 246);
            painel = new Color(255, 255, 255);
            texto = new Color(60, 50, 45);
            tabelaBg = Color.WHITE;
            tabelaTexto = Color.BLACK;
            borda = new Color(220, 210, 200);
            UIManager.put("Table.gridColor", new Color(230, 220, 210));
        }

        getContentPane().setBackground(fundo);
        atualizarComponentes(getContentPane(), fundo, texto, painel, borda, tabelaBg, tabelaTexto);
        repaint();
    }

    private void atualizarComponentes(Container container, Color fundo, Color texto, Color painel, Color borda, Color tabelaBg, Color tabelaTexto) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel panel) {
                panel.setBackground(painel);
                atualizarComponentes(panel, fundo, texto, painel, borda, tabelaBg, tabelaTexto);
            } else if (comp instanceof JScrollPane scroll) {
                scroll.getViewport().setBackground(tabelaBg);
                scroll.setBorder(BorderFactory.createLineBorder(borda));

                JScrollBar vertical = scroll.getVerticalScrollBar();
                JScrollBar horizontal = scroll.getHorizontalScrollBar();

                if (escuroAtivo) {
                    vertical.setBackground(new Color(45, 45, 50));
                    vertical.setForeground(new Color(100, 100, 110));
                    horizontal.setBackground(new Color(45, 45, 50));
                    horizontal.setForeground(new Color(100, 100, 110));
                    vertical.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
                        @Override
                        protected void configureScrollBarColors() {
                            this.thumbColor = new Color(100, 100, 110);
                            this.trackColor = new Color(45, 45, 50);
                        }
                    });
                    horizontal.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
                        @Override
                        protected void configureScrollBarColors() {
                            this.thumbColor = new Color(100, 100, 110);
                            this.trackColor = new Color(45, 45, 50);
                        }
                    });
                } else {
                    vertical.setBackground(Color.WHITE);
                    vertical.setForeground(new Color(200, 200, 200));
                    horizontal.setBackground(Color.WHITE);
                    horizontal.setForeground(new Color(200, 200, 200));
                    vertical.setUI(new javax.swing.plaf.basic.BasicScrollBarUI());
                    horizontal.setUI(new javax.swing.plaf.basic.BasicScrollBarUI());
                }
                Component view = scroll.getViewport().getView();
                if (view instanceof JTable tabela) {
                    tabela.setBackground(tabelaBg);
                    tabela.setForeground(tabelaTexto);
                    tabela.getTableHeader().setBackground(escuroAtivo ? new Color(60, 60, 65) : new Color(240, 240, 240));
                    tabela.getTableHeader().setForeground(escuroAtivo ? Color.WHITE : Color.DARK_GRAY);
                    tabela.setGridColor(escuroAtivo ? new Color(80, 80, 85) : new Color(210, 210, 210));

                    JTableHeader header = tabela.getTableHeader();
                    header.setBackground(escuroAtivo ? new Color(60, 60, 65) : new Color(240, 240, 240));
                    header.setForeground(Color.BLACK);
                    header.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
                }
            } else if (comp instanceof JButton botao) {
                botao.setBackground(escuroAtivo ? new Color(70, 70, 80) : new Color(230, 220, 210));
                botao.setForeground(texto);
                botao.setBorder(BorderFactory.createLineBorder(borda));
            } else if (comp instanceof JLabel label) {
                label.setForeground(texto);
            } else if (comp instanceof JSplitPane split) {
                split.setBackground(fundo);
                atualizarComponentes(split.getLeftComponent() instanceof Container c1 ? c1 : null, fundo, texto, painel, borda, tabelaBg, tabelaTexto);
                atualizarComponentes(split.getRightComponent() instanceof Container c2 ? c2 : null, fundo, texto, painel, borda, tabelaBg, tabelaTexto);
            } else if (comp instanceof JCheckBox box) {
                box.setBackground(fundo);
                box.setForeground(texto);
            }
        }
    }
}