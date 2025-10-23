package ui;

import model.historicoCagada;
import model.registroCagada;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraficoUi extends JFrame {

    private final GraficoPanel graficoPanel;

    public GraficoUi(historicoCagada historico) {
        setTitle("📊 Gráfico de Cagadas — Pedro & Gabi");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        graficoPanel = new GraficoPanel();
        graficoPanel.setRegistros(historico.listar());
        graficoPanel.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(graficoPanel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        JLabel legenda = new JLabel("💙 Pedro vs 💖 Gabi", SwingConstants.CENTER);
        legenda.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        legenda.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(legenda, BorderLayout.NORTH);

        setVisible(true);
    }

    private static class GraficoPanel extends JPanel {
        private List<registroCagada> registros;

        public void setRegistros(List<registroCagada> registros) {
            this.registros = registros;
            revalidate();
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            int altura = (registros != null ? registros.size() * 35 + 80 : 200);
            return new Dimension(800, altura);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (registros == null || registros.isEmpty()) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));

            int larguraPainel = getWidth();
            int meio = larguraPainel / 2;
            int y = 60;
            int alturaBarra = 20;
            int margemEsquerda = 120;
            int larguraTotal = larguraPainel - margemEsquerda * 2;

            g2.setColor(Color.BLACK);
            g2.drawString("💙 Pedro", meio - 100, 25);
            g2.drawString("💖 Gabi", meio + 50, 25);

            for (registroCagada r : registros) {
                int pedro = r.getPedro();
                int gabi = r.getGabriela();
                int max = Math.max(pedro, gabi);
                if (max == 0) max = 1;

                int larguraMetade = larguraTotal / 2;
                int larguraPedro = (int) ((double) pedro / max * larguraMetade);
                int larguraGabi = (int) ((double) gabi / max * larguraMetade);

                // Data à esquerda
                g2.setColor(Color.DARK_GRAY);
                g2.drawString(r.getData(), margemEsquerda - 60, y + 15);

                // Barras
                g2.setColor(new Color(100, 149, 237)); // azul
                g2.fillRoundRect(meio - larguraPedro - 10, y, larguraPedro, alturaBarra, 10, 10);

                g2.setColor(new Color(255, 105, 180)); // rosa
                g2.fillRoundRect(meio + 10, y, larguraGabi, alturaBarra, 10, 10);

                y += 35;
            }
        }
    }
}
