
package simulator.algoritmos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NFU implements AlgoritmoSubstituicao {
    @Override
    public int executar(int[] cadeiaReferencia, int numQuadros) {
        List<Integer> memoria = new ArrayList<>();
        Map<Integer, Integer> contadores = new HashMap<>();
        int faltasDePagina = 0;

        for (int pagina : cadeiaReferencia) {
            contadores.put(pagina, contadores.getOrDefault(pagina, 0) + 1);

            if (memoria.contains(pagina)) {
                continue;
            }

            faltasDePagina++;

            if (memoria.size() == numQuadros) {
                Integer paginaVitima = escolherPaginaVitima(memoria, contadores);
                memoria.remove(Integer.valueOf(paginaVitima));
            }

            memoria.add(pagina);
        }

        return faltasDePagina;
    }

    private Integer escolherPaginaVitima(List<Integer> memoria, Map<Integer, Integer> contadores) {
        Integer paginaVitima = null;
        int menorContador = Integer.MAX_VALUE;

        for (Integer pagina : memoria) {
            int contador = contadores.getOrDefault(pagina, 0);
            if (contador < menorContador) {
                menorContador = contador;
                paginaVitima = pagina;
            }
        }

        return paginaVitima;
    }

    @Override
    public String getNome() {
        return "NFU";
    }
}
