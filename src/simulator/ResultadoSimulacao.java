package simulator;

public class ResultadoSimulacao {
    private final String nomeAlgoritmo;
    private final int faltasDePagina;

    public ResultadoSimulacao(String nomeAlgoritmo, int faltasDePagina) {
        this.nomeAlgoritmo = nomeAlgoritmo;
        this.faltasDePagina = faltasDePagina;
    }

    public String getNomeAlgoritmo() {
        return nomeAlgoritmo;
    }

    public int getFaltasDePagina() {
        return faltasDePagina;
    }
}
