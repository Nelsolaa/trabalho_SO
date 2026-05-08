
package simulator.algoritmos;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class FIFO implements AlgoritmoSubstituicao {
    @Override
    public int executar(int[] cadeiaReferencia, int numQuadros) {
        Queue<Integer> memoria = new LinkedList<>();
        Set<Integer> paginasNaMemoria = new HashSet<>();
        int faltasDePagina = 0;

        for (int pagina : cadeiaReferencia) {
            if (!paginasNaMemoria.contains(pagina)) {
                faltasDePagina++;

                if (memoria.size() == numQuadros) {
                    Integer paginaRemovida = memoria.poll();
                    paginasNaMemoria.remove(paginaRemovida);
                }

                memoria.offer(pagina);
                paginasNaMemoria.add(pagina);
            }
        }

        return faltasDePagina;
    }

    @Override
    public String getNome() {
        return "FIFO";
    }
}
