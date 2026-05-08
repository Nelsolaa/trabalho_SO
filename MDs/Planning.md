# Planning — Simulador de Algoritmos de Substituição de Páginas

## Contexto do Projeto

- **Linguagem:** Java (JDK 11+)
- **Algoritmos escolhidos (4 de 6):** FIFO, LRU, NFU e Ótimo
- **Entrada do programa:** uma cadeia de números inteiros (referências de páginas) + número de quadros (frames) de memória física
- **Saída obrigatória:** quantidade de faltas de página por algoritmo
- **Extra (+1 ponto):** interface gráfica com Swing + gráfico comparativo
- **Estratégia de desenvolvimento:** primeiro CLI funcional, depois migrar para Swing

---

## Fase 1 — Implementação CLI

### 1.1 Estrutura de Pacotes e Classes

```
src/
├── Main.java                        // Ponto de entrada CLI
├── simulator/
│   ├── SimuladorSubstituicao.java   // Classe orquestradora
│   ├── ResultadoSimulacao.java      // POJO com resultado de cada algoritmo
│   ├── algoritmos/
│   │   ├── AlgoritmoSubstituicao.java   // Interface comum
│   │   ├── FIFO.java
│   │   ├── LRU.java
│   │   ├── NFU.java
│   │   └── Otimo.java
```

### 1.2 Interface Comum — `AlgoritmoSubstituicao`

Todos os 4 algoritmos devem implementar uma interface comum para facilitar a orquestração e futura reutilização na GUI.

```java
package simulator.algoritmos;

public interface AlgoritmoSubstituicao {
    /**
     * Executa a simulação do algoritmo.
     * @param cadeiaReferencia array de inteiros representando as páginas referenciadas
     * @param numQuadros número de quadros (frames) disponíveis na memória física
     * @return número de faltas de página (page faults)
     */
    int executar(int[] cadeiaReferencia, int numQuadros);

    /**
     * @return nome legível do algoritmo (ex: "FIFO", "LRU")
     */
    String getNome();
}
```

### 1.3 POJO de Resultado — `ResultadoSimulacao`

```java
package simulator;

public class ResultadoSimulacao {
    private String nomeAlgoritmo;
    private int faltasDePagina;
    // construtor, getters
}
```

### 1.4 Classe Orquestradora — `SimuladorSubstituicao`

Responsável por:
1. Receber a cadeia de referência e o número de quadros.
2. Instanciar os 4 algoritmos.
3. Executar cada um passando os mesmos parâmetros.
4. Coletar e retornar uma `List<ResultadoSimulacao>`.

Isso permite que tanto a CLI quanto a GUI usem a mesma classe para executar a simulação.

### 1.5 Classe `Main` (CLI)

Fluxo:
1. Solicitar ao usuário (via `Scanner`) a cadeia de páginas separada por espaços (ex: `7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1`).
2. Solicitar ao usuário o número de quadros de memória (ex: `3`).
3. Parsear a entrada para `int[]`.
4. Chamar `SimuladorSubstituicao` e obter a lista de resultados.
5. Imprimir no formato exigido:
```
Método 1 (FIFO)   - X faltas de página
Método 2 (LRU)    - X faltas de página
Método 3 (NFU)    - X faltas de página
Método 4 (Ótimo)  - X faltas de página
```

---

## Detalhamento de Cada Algoritmo

### Algoritmo 1: FIFO (First In, First Out)

**Conceito:** A página que está há mais tempo na memória é removida primeiro.

**Estruturas de dados:**
- `Queue<Integer> memoria` — instanciar como `new LinkedList<>()`. Mantém a ordem de chegada.
- `Set<Integer> paginasNaMemoria` — `new HashSet<>()`. Para verificação O(1) se a página já está carregada.

**Pseudocódigo detalhado:**
```
faltasDePagina = 0

para cada pagina em cadeiaReferencia:
    se pagina NÃO está em paginasNaMemoria:
        faltasDePagina++
        se memoria.size() == numQuadros:
            paginaRemovida = memoria.poll()    // remove a mais antiga (cabeça da fila)
            paginasNaMemoria.remove(paginaRemovida)
        memoria.offer(pagina)                  // insere no final da fila
        paginasNaMemoria.add(pagina)
    // se a página JÁ está na memória, nada acontece (sem reordenação)

retornar faltasDePagina
```

**Ponto de atenção:** No FIFO, quando uma página já carregada é referenciada novamente, NÃO se altera sua posição na fila. Isso o diferencia do LRU.

---

### Algoritmo 2: LRU (Least Recently Used)

**Conceito:** Remove a página que foi usada menos recentemente.

**Estruturas de dados:**
- `LinkedList<Integer> memoria` — funciona como lista ordenada por uso. Índice 0 = menos recente, último índice = mais recente.

**Pseudocódigo detalhado:**
```
faltasDePagina = 0

para cada pagina em cadeiaReferencia:
    se memoria.contains(pagina):
        // página já está na memória: ATUALIZAR posição de uso
        memoria.remove(Integer.valueOf(pagina))   // remove da posição atual
        memoria.addLast(pagina)                   // reinsere no final (mais recente)
    senão:
        // FALTA DE PÁGINA
        faltasDePagina++
        se memoria.size() == numQuadros:
            memoria.removeFirst()                 // remove o índice 0 (menos recente)
        memoria.addLast(pagina)                   // insere no final (mais recente)

retornar faltasDePagina
```

**Ponto de atenção:** A diferença central do LRU para o FIFO é que a cada acesso (hit) a uma página existente, ela é movida para o final da lista, atualizando seu "tempo de uso". Usar `memoria.remove(Integer.valueOf(pagina))` e não `memoria.remove(indice)` — o primeiro remove por valor, o segundo por índice.

---

### Algoritmo 3: NFU (Not Frequently Used)

**Conceito:** Cada página tem um contador de frequência. Na substituição, a página com menor contador entre as que estão na memória é removida.

**Estruturas de dados:**
- `List<Integer> memoria` — lista das páginas atualmente nos quadros.
- `Map<Integer, Integer> contadores` — `new HashMap<>()`. Mapeia `pagina → frequênciaDeAcesso`. O contador é **global** (persiste mesmo se a página sair da memória e voltar).

**Pseudocódigo detalhado:**
```
faltasDePagina = 0

para cada pagina em cadeiaReferencia:
    // SEMPRE incrementar o contador da página referenciada
    contadores.put(pagina, contadores.getOrDefault(pagina, 0) + 1)

    se memoria.contains(pagina):
        // HIT: página já está na memória, nada mais a fazer
        continuar
    senão:
        // FALTA DE PÁGINA
        faltasDePagina++
        se memoria.size() == numQuadros:
            // encontrar a página NA MEMÓRIA com o MENOR contador
            paginaVitima = null
            menorContador = Integer.MAX_VALUE
            para cada p em memoria:
                se contadores.get(p) < menorContador:
                    menorContador = contadores.get(p)
                    paginaVitima = p
            memoria.remove(Integer.valueOf(paginaVitima))
        memoria.add(pagina)

retornar faltasDePagina
```

**Pontos de atenção:**
- O contador é incrementado para TODA referência, incluindo hits.
- Na escolha da vítima, só se comparam os contadores das páginas que estão **atualmente na memória**, não de todas as páginas já vistas.
- Em caso de empate no contador, remover a primeira encontrada na iteração (ordem de inserção na `List`).

---

### Algoritmo 4: Ótimo (Optimal / Bélády)

**Conceito:** Remove a página que vai demorar mais para ser usada novamente no futuro. É o algoritmo teórico ideal, possível apenas porque o simulador conhece toda a cadeia de referência antecipadamente.

**Estruturas de dados:**
- `List<Integer> memoria` — lista das páginas atualmente nos quadros.

**Pseudocódigo detalhado:**
```
faltasDePagina = 0

para cada i de 0 até cadeiaReferencia.length - 1:
    pagina = cadeiaReferencia[i]

    se memoria.contains(pagina):
        // HIT: nada a fazer
        continuar
    senão:
        // FALTA DE PÁGINA
        faltasDePagina++
        se memoria.size() == numQuadros:
            // Para cada página na memória, encontrar o próximo uso futuro
            indiceMaisDistante = -1
            paginaVitima = null

            para cada p em memoria:
                proximoUso = -1   // -1 significa "não será mais usada"
                para cada j de (i + 1) até cadeiaReferencia.length - 1:
                    se cadeiaReferencia[j] == p:
                        proximoUso = j
                        parar  // encontrou o próximo uso, não precisa continuar
                
                se proximoUso == -1:
                    // esta página não será mais usada: vítima perfeita
                    paginaVitima = p
                    parar   // não precisa verificar outras
                senão se proximoUso > indiceMaisDistante:
                    indiceMaisDistante = proximoUso
                    paginaVitima = p

            memoria.remove(Integer.valueOf(paginaVitima))
        memoria.add(pagina)

retornar faltasDePagina
```

**Pontos de atenção:**
- Se uma página na memória não aparece mais no restante da cadeia, ela é a vítima imediata (melhor caso).
- Se todas aparecem novamente, a que aparece mais distante é a vítima.
- A busca pelo "próximo uso" sempre começa em `i + 1` (o índice seguinte ao atual na cadeia).

---

## Caso de Teste para Validação

Usar a cadeia clássica para validar os algoritmos antes de prosseguir para a GUI:

```
Cadeia de referência: 7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1
Número de quadros: 3
```

Resultados esperados (referência teórica):
- **FIFO:** 15 faltas de página
- **LRU:** 12 faltas de página
- **Ótimo:** 9 faltas de página
- **NFU:** o resultado varia conforme a implementação de desempate, mas deve ser um valor entre o LRU e o FIFO (~12-15)

> O agente implementador DEVE rodar este caso de teste e comparar os resultados de FIFO, LRU e Ótimo com os valores esperados acima. São valores clássicos da literatura e servem como prova de corretude.

---

## Fase 2 — Interface Gráfica com Swing

Só iniciar esta fase após a Fase 1 estar 100% funcional e validada.

### 2.1 Estrutura Adicional

```
src/
├── gui/
│   ├── SimuladorGUI.java        // JFrame principal
│   └── GraficoComparativo.java  // Painel com gráfico de barras
```

### 2.2 Tela Principal — `SimuladorGUI`

Componentes do `JFrame`:
1. **JTextField** para a cadeia de referência (o usuário digita os números separados por espaço).
2. **JTextField** ou **JSpinner** para o número de quadros.
3. **JButton "Simular"** — ao clicar, parseia os inputs, chama `SimuladorSubstituicao`, obtém os resultados.
4. **JTextArea** ou **JTable** — exibe os resultados textuais:
   ```
   FIFO   - 15 faltas de página
   LRU    - 12 faltas de página
   NFU    - 13 faltas de página
   Ótimo  -  9 faltas de página
   ```
5. **Painel do gráfico** — um `JPanel` customizado (`GraficoComparativo`) que desenha um gráfico de barras comparativo.

### 2.3 Gráfico de Barras — `GraficoComparativo`

- Estender `JPanel` e sobrescrever `paintComponent(Graphics g)`.
- Desenhar 4 barras verticais, uma para cada algoritmo.
- Cada barra tem altura proporcional ao número de faltas de página.
- Usar cores distintas para cada algoritmo (ex: azul, verde, laranja, vermelho).
- Labels abaixo de cada barra com o nome do algoritmo e o número de faltas.
- Não é necessário usar bibliotecas externas (JFreeChart, etc.) — desenho manual com `Graphics2D` é suficiente e evita dependências.

### 2.4 Fluxo da GUI

1. Usuário preenche a cadeia e o número de quadros.
2. Clica em "Simular".
3. O ActionListener valida os inputs (números válidos, quadros > 0).
4. Chama `SimuladorSubstituicao.executar()`.
5. Atualiza a área de resultados textuais.
6. Passa os resultados para `GraficoComparativo` e chama `repaint()`.

### 2.5 Ponto de Entrada da GUI

No `Main.java`, adicionar um argumento ou simplesmente mudar o ponto de entrada:
- Se executado sem argumentos → abre a GUI.
- Se executado com `--cli` → executa no modo CLI (para testes e validação rápida).

---

## Resumo da Ordem de Implementação

| Etapa | O que fazer | Critério de conclusão |
|-------|-------------|----------------------|
| 1 | Criar a interface `AlgoritmoSubstituicao` e o POJO `ResultadoSimulacao` | Compilar sem erros |
| 2 | Implementar `FIFO.java` | Passar no caso de teste (15 faltas) |
| 3 | Implementar `LRU.java` | Passar no caso de teste (12 faltas) |
| 4 | Implementar `NFU.java` | Produzir resultado coerente (entre 10-15) |
| 5 | Implementar `Otimo.java` | Passar no caso de teste (9 faltas) |
| 6 | Criar `SimuladorSubstituicao` e `Main` (CLI) | Exibir os 4 resultados formatados |
| 7 | Testar CLI com múltiplas cadeias e números de quadros | Todos os algoritmos funcionando |
| 8 | Criar `SimuladorGUI.java` (Swing) | Tela abre, inputs funcionam, resultados aparecem |
| 9 | Criar `GraficoComparativo.java` | Gráfico de barras renderiza corretamente |
| 10 | Ajustar `Main.java` para suportar modo CLI e GUI | Ambos os modos funcionam |

---

## Observações para o Agente Implementador

1. **Não usar bibliotecas externas.** Tudo com Java puro (Collections API + Swing).
2. **Codificar em UTF-8** para suportar caracteres como "Ótimo" corretamente.
3. **Cada algoritmo é uma classe separada** que implementa a interface `AlgoritmoSubstituicao`. Isso mantém o código organizado e facilita testes individuais.
4. **A classe `SimuladorSubstituicao` é o ponto central** — tanto CLI quanto GUI chamam ela. Nunca duplicar lógica de simulação.
5. **Validação de entrada:** na CLI e na GUI, validar que a cadeia contém apenas inteiros e que o número de quadros é >= 1.
6. **O caso de teste clássico (cadeia `7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1`, 3 quadros)** é obrigatório para validação antes de seguir para a GUI.
