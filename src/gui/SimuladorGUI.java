package gui;

import simulator.EntradaSimulacao;
import simulator.ResultadoSimulacao;
import simulator.SimuladorSubstituicao;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

public class SimuladorGUI extends JFrame {
    private static final String CADEIA_PADRAO = "7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1";

    private final JTextField campoCadeia;
    private final JSpinner spinnerQuadros;
    private final DefaultTableModel modeloTabela;
    private final GraficoComparativo graficoComparativo;
    private final SimuladorSubstituicao simulador;

    public SimuladorGUI() {
        super("Simulador de Substituição de Páginas");
        this.simulador = new SimuladorSubstituicao();
        this.campoCadeia = new JTextField(CADEIA_PADRAO);
        this.spinnerQuadros = new JSpinner(new SpinnerNumberModel(3, 1, 999, 1));
        this.modeloTabela = criarModeloTabela();
        this.graficoComparativo = new GraficoComparativo();

        configurarJanela();
        montarInterface();
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(820, 620));
    }

    private void montarInterface() {
        JPanel conteudo = new JPanel(new BorderLayout(18, 18));
        conteudo.setBorder(new EmptyBorder(18, 20, 20, 20));
        conteudo.setBackground(new Color(248, 250, 252));

        conteudo.add(criarCabecalho(), BorderLayout.NORTH);
        conteudo.add(criarCentro(), BorderLayout.CENTER);

        setContentPane(conteudo);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel criarCabecalho() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        JLabel titulo = new JLabel("Simulador de Algoritmos de Substituição de Páginas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(new Color(15, 23, 42));
        painel.add(titulo, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        painel.add(criarPainelEntrada(), gbc);

        return painel;
    }

    private JPanel criarPainelEntrada() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(14, 14, 14, 14)
        ));

        campoCadeia.setFont(new Font("SansSerif", Font.PLAIN, 14));
        spinnerQuadros.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton botaoSimular = new JButton("Simular");
        botaoSimular.setFont(new Font("SansSerif", Font.BOLD, 14));
        botaoSimular.setFocusPainted(false);
        botaoSimular.addActionListener(evento -> executarSimulacao());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        painel.add(criarLabel("Cadeia de referência"), gbc);

        gbc.gridx = 1;
        painel.add(criarLabel("Quadros"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(campoCadeia, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        spinnerQuadros.setPreferredSize(new Dimension(90, 30));
        painel.add(spinnerQuadros, gbc);

        gbc.gridx = 2;
        botaoSimular.setPreferredSize(new Dimension(110, 31));
        painel.add(botaoSimular, gbc);

        return painel;
    }

    private JPanel criarCentro() {
        JPanel painel = new JPanel(new BorderLayout(18, 18));
        painel.setOpaque(false);
        painel.add(criarPainelResultados(), BorderLayout.NORTH);
        painel.add(criarPainelGrafico(), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelResultados() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JTable tabela = new JTable(modeloTabela);
        tabela.setRowHeight(30);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(new Color(241, 245, 249));
        tabela.setGridColor(new Color(226, 232, 240));

        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);
        tabela.getColumnModel().getColumn(1).setCellRenderer(centralizado);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setPreferredSize(new Dimension(760, 152));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));

        painel.add(scroll, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelGrafico() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel titulo = criarLabel("Comparativo de faltas de página");
        titulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        painel.add(titulo, BorderLayout.NORTH);
        painel.add(graficoComparativo, BorderLayout.CENTER);

        return painel;
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(new Color(51, 65, 85));
        return label;
    }

    private DefaultTableModel criarModeloTabela() {
        return new DefaultTableModel(new Object[]{"Algoritmo", "Faltas de página"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void executarSimulacao() {
        try {
            int[] cadeiaReferencia = EntradaSimulacao.parsearCadeiaReferencia(campoCadeia.getText());
            int numQuadros = (Integer) spinnerQuadros.getValue();

            List<ResultadoSimulacao> resultados = simulador.executar(cadeiaReferencia, numQuadros);
            atualizarResultados(resultados);
        } catch (IllegalArgumentException erro) {
            JOptionPane.showMessageDialog(
                    this,
                    erro.getMessage(),
                    "Entrada inválida",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void atualizarResultados(List<ResultadoSimulacao> resultados) {
        modeloTabela.setRowCount(0);
        for (ResultadoSimulacao resultado : resultados) {
            modeloTabela.addRow(new Object[]{
                    resultado.getNomeAlgoritmo(),
                    resultado.getFaltasDePagina()
            });
        }
        graficoComparativo.setResultados(resultados);
    }
}
