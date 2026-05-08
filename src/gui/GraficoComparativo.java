package gui;

import simulator.ResultadoSimulacao;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraficoComparativo extends JPanel {
    private static final Color[] CORES = {
            new Color(37, 99, 235),
            new Color(22, 163, 74),
            new Color(234, 88, 12),
            new Color(220, 38, 38)
    };

    private List<ResultadoSimulacao> resultados = Collections.emptyList();

    public GraficoComparativo() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(620, 320));
        setMinimumSize(new Dimension(420, 260));
    }

    public void setResultados(List<ResultadoSimulacao> resultados) {
        this.resultados = resultados == null
                ? Collections.emptyList()
                : new ArrayList<>(resultados);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (resultados.isEmpty()) {
                desenharEstadoVazio(g2);
                return;
            }

            desenharGrafico(g2);
        } finally {
            g2.dispose();
        }
    }

    private void desenharEstadoVazio(Graphics2D g2) {
        String texto = "Execute uma simulação para visualizar o comparativo";
        g2.setFont(new Font("SansSerif", Font.PLAIN, 15));
        g2.setColor(new Color(100, 116, 139));
        FontMetrics metrics = g2.getFontMetrics();
        int x = (getWidth() - metrics.stringWidth(texto)) / 2;
        int y = getHeight() / 2;
        g2.drawString(texto, Math.max(16, x), y);
    }

    private void desenharGrafico(Graphics2D g2) {
        int largura = getWidth();
        int altura = getHeight();
        int margemEsquerda = 54;
        int margemDireita = 28;
        int margemSuperior = 34;
        int margemInferior = 68;
        int baseY = altura - margemInferior;
        int areaAltura = Math.max(40, baseY - margemSuperior);
        int maxFaltas = obterMaiorValor();

        desenharEixos(g2, margemEsquerda, margemSuperior, largura - margemDireita, baseY);

        int quantidade = resultados.size();
        int areaLargura = largura - margemEsquerda - margemDireita;
        int espacoPorBarra = Math.max(1, areaLargura / quantidade);
        int larguraBarra = Math.min(82, Math.max(36, espacoPorBarra / 2));

        for (int i = 0; i < quantidade; i++) {
            ResultadoSimulacao resultado = resultados.get(i);
            int valor = resultado.getFaltasDePagina();
            int alturaBarra = (int) Math.round((valor / (double) maxFaltas) * areaAltura);
            int x = margemEsquerda + i * espacoPorBarra + (espacoPorBarra - larguraBarra) / 2;
            int y = baseY - alturaBarra;

            Color cor = CORES[i % CORES.length];
            g2.setColor(cor);
            g2.fillRoundRect(x, y, larguraBarra, alturaBarra, 8, 8);

            g2.setColor(cor.darker());
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(x, y, larguraBarra, alturaBarra, 8, 8);

            desenharValor(g2, String.valueOf(valor), x, y, larguraBarra);
            desenharRotulo(g2, resultado, x, baseY, larguraBarra);
        }
    }

    private void desenharEixos(Graphics2D g2, int esquerda, int topo, int direita, int baseY) {
        g2.setColor(new Color(226, 232, 240));
        g2.setStroke(new BasicStroke(1.1f));

        int linhas = 4;
        for (int i = 0; i <= linhas; i++) {
            int y = topo + ((baseY - topo) * i / linhas);
            g2.drawLine(esquerda, y, direita, y);
        }

        g2.setColor(new Color(71, 85, 105));
        g2.drawLine(esquerda, topo, esquerda, baseY);
        g2.drawLine(esquerda, baseY, direita, baseY);
    }

    private void desenharValor(Graphics2D g2, String texto, int x, int y, int larguraBarra) {
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.setColor(new Color(15, 23, 42));
        FontMetrics metrics = g2.getFontMetrics();
        int textoX = x + (larguraBarra - metrics.stringWidth(texto)) / 2;
        g2.drawString(texto, textoX, Math.max(18, y - 8));
    }

    private void desenharRotulo(Graphics2D g2, ResultadoSimulacao resultado, int x, int baseY, int larguraBarra) {
        g2.setColor(new Color(30, 41, 59));
        g2.setFont(new Font("SansSerif", Font.BOLD, 13));
        centralizarTexto(g2, resultado.getNomeAlgoritmo(), x + larguraBarra / 2, baseY + 24);

        g2.setColor(new Color(100, 116, 139));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        centralizarTexto(g2, resultado.getFaltasDePagina() + " faltas", x + larguraBarra / 2, baseY + 43);
    }

    private void centralizarTexto(Graphics2D g2, String texto, int centroX, int y) {
        FontMetrics metrics = g2.getFontMetrics();
        g2.drawString(texto, centroX - metrics.stringWidth(texto) / 2, y);
    }

    private int obterMaiorValor() {
        int maior = 1;
        for (ResultadoSimulacao resultado : resultados) {
            maior = Math.max(maior, resultado.getFaltasDePagina());
        }
        return maior;
    }
}
