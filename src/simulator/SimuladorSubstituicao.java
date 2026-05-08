package simulator;

import simulator.algoritmos.AlgoritmoSubstituicao;
import simulator.algoritmos.FIFO;
import simulator.algoritmos.LRU;
import simulator.algoritmos.NFU;
import simulator.algoritmos.Otimo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimuladorSubstituicao {
    private final List<AlgoritmoSubstituicao> algoritmos;

    public SimuladorSubstituicao() {
        this.algoritmos = Arrays.asList(
                new FIFO(),
                new LRU(),
                new NFU(),
                new Otimo()
        );
    }

    public List<ResultadoSimulacao> executar(int[] cadeiaReferencia, int numQuadros) {
        validarEntrada(cadeiaReferencia, numQuadros);

        List<ResultadoSimulacao> resultados = new ArrayList<>();
        for (AlgoritmoSubstituicao algoritmo : algoritmos) {
            int faltas = algoritmo.executar(cadeiaReferencia, numQuadros);
            resultados.add(new ResultadoSimulacao(algoritmo.getNome(), faltas));
        }

        return resultados;
    }

    public List<AlgoritmoSubstituicao> getAlgoritmos() {
        return Collections.unmodifiableList(algoritmos);
    }

    private void validarEntrada(int[] cadeiaReferencia, int numQuadros) {
        if (cadeiaReferencia == null || cadeiaReferencia.length == 0) {
            throw new IllegalArgumentException("A cadeia de referência deve ter pelo menos uma página.");
        }

        if (numQuadros < 1) {
            throw new IllegalArgumentException("O número de quadros deve ser maior que zero.");
        }
    }
}
