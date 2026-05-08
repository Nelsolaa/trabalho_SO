package simulator.algoritmos;

import java.util.LinkedList;

public class LRU implements AlgoritmoSubstituicao {
    @Override
    public int executar(int[] cadeiaReferencia, int numQuadros) {
        LinkedList<Integer> memoria = new LinkedList<>();
        int faltasDePagina = 0;

        for (int pagina : cadeiaReferencia) {
            if (memoria.contains(pagina)) {
                memoria.remove(Integer.valueOf(pagina));
                memoria.addLast(pagina);
                continue;
            }

            faltasDePagina++;

            if (memoria.size() == numQuadros) {
                memoria.removeFirst();
            }

            memoria.addLast(pagina);
        }

        return faltasDePagina;
    }

    @Override
    public String getNome() {
        return "LRU";
    }
}
