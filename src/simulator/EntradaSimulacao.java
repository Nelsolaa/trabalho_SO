package simulator;

public final class EntradaSimulacao {
    private EntradaSimulacao() {
    }

    public static int[] parsearCadeiaReferencia(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe a cadeia de referência.");
        }

        String[] partes = texto.trim().split("\\s+");
        int[] cadeia = new int[partes.length];

        for (int i = 0; i < partes.length; i++) {
            try {
                cadeia[i] = Integer.parseInt(partes[i]);
            } catch (NumberFormatException erro) {
                throw new IllegalArgumentException("A cadeia deve conter apenas números inteiros separados por espaço.");
            }
        }

        return cadeia;
    }

    public static int parsearQuadros(String texto) {
        try {
            int quadros = Integer.parseInt(texto.trim());
            if (quadros < 1) {
                throw new IllegalArgumentException("O número de quadros deve ser maior que zero.");
            }
            return quadros;
        } catch (NumberFormatException erro) {
            throw new IllegalArgumentException("Informe um número inteiro válido para os quadros.");
        }
    }
}
