package simulator.algoritmos;

import java.util.ArrayList;
import java.util.List;

public class Otimo implements AlgoritmoSubstituicao {
    @Override
    public int executar(int[] cadeiaReferencia, int numQuadros) {
        List<Integer> memoria = new ArrayList<>();
        int faltasDePagina = 0;

        for (int i = 0; i < cadeiaReferencia.length; i++) {
            int pagina = cadeiaReferencia[i];

            if (memoria.contains(pagina)) {
                continue;
            }

            faltasDePagina++;

            if (memoria.size() == numQuadros) {
                Integer paginaVitima = escolherPaginaVitima(memoria, cadeiaReferencia, i + 1);
                memoria.remove(Integer.valueOf(paginaVitima));
            }

            memoria.add(pagina);
        }

        return faltasDePagina;
    }

    private Integer escolherPaginaVitima(List<Integer> memoria, int[] cadeiaReferencia, int inicioBusca) {
        Integer paginaVitima = null;
        int indiceMaisDistante = -1;

        for (Integer pagina : memoria) {
            int proximoUso = encontrarProximoUso(pagina, cadeiaReferencia, inicioBusca);

            if (proximoUso == -1) {
                return pagina;
            }

            if (proximoUso > indiceMaisDistante) {
                indiceMaisDistante = proximoUso;
                paginaVitima = pagina;
            }
        }

        return paginaVitima;
    }

    private int encontrarProximoUso(int pagina, int[] cadeiaReferencia, int inicioBusca) {
        for (int i = inicioBusca; i < cadeiaReferencia.length; i++) {
            if (cadeiaReferencia[i] == pagina) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public String getNome() {
        return "Ótimo";
    }
}
