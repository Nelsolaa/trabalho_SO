import gui.SimuladorGUI;
import simulator.EntradaSimulacao;
import simulator.ResultadoSimulacao;
import simulator.SimuladorSubstituicao;

import javax.swing.SwingUtilities;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && "--cli".equalsIgnoreCase(args[0])) {
            executarCli();
            return;
        }

        SwingUtilities.invokeLater(() -> new SimuladorGUI().setVisible(true));
    }

    private static void executarCli() {
        Scanner scanner = new Scanner(System.in, "UTF-8");

        try {
            System.out.println("Digite a cadeia de referência separada por espaços:");
            String cadeiaTexto = scanner.nextLine();

            System.out.println("Digite o número de quadros de memória:");
            String quadrosTexto = scanner.nextLine();

            int[] cadeiaReferencia = EntradaSimulacao.parsearCadeiaReferencia(cadeiaTexto);
            int numQuadros = EntradaSimulacao.parsearQuadros(quadrosTexto);

            List<ResultadoSimulacao> resultados = new SimuladorSubstituicao().executar(cadeiaReferencia, numQuadros);
            imprimirResultados(resultados);
        } catch (IllegalArgumentException erro) {
            System.err.println("Erro: " + erro.getMessage());
        }
    }

    private static void imprimirResultados(List<ResultadoSimulacao> resultados) {
        for (int i = 0; i < resultados.size(); i++) {
            ResultadoSimulacao resultado = resultados.get(i);
            System.out.printf(
                    "Método %d (%-5s) - %d faltas de página%n",
                    i + 1,
                    resultado.getNomeAlgoritmo(),
                    resultado.getFaltasDePagina()
            );
        }
    }
}
