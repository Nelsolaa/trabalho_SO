# Guia de Apresentação — Simulador de Substituição de Páginas

**Universidade de Fortaleza — Sistemas Operacionais**
**Autores:** Luiz Carlos Filho (2410410) e Nelson (2417210)

---

## 1. O que é o projeto (resposta rápida de 30 segundos)

> "Desenvolvemos um simulador em Java que compara quatro algoritmos clássicos de substituição de páginas — FIFO, LRU, NFU e Ótimo — usando a mesma cadeia de referências. O usuário informa a sequência de páginas e o número de quadros de memória disponíveis, e o programa mostra quantas faltas de página cada algoritmo gerou, tanto pelo terminal quanto por uma interface gráfica com gráfico de barras."

---

## 2. Contexto teórico — o que saber explicar

### O que é memória virtual?

O sistema operacional divide a memória em **páginas** (blocos de tamanho fixo). Quando um processo precisa de uma página que não está na RAM, ocorre uma **falta de página (page fault)** — o SO precisa buscá-la no disco, o que é muito lento.

### O que é um quadro de memória?

Um **quadro (frame)** é um espaço físico na RAM que comporta uma página. Se há 3 quadros disponíveis, no máximo 3 páginas podem estar na memória ao mesmo tempo.

### Por que isso importa?

Cada falta de página provoca uma operação de disco (E/S), que é ordens de grandeza mais lenta do que a RAM. Reduzir faltas de página = melhorar desempenho do sistema.

### O que é a cadeia de referências?

É a sequência de páginas que o processo vai acessar. Exemplo: `7 0 1 2 0 3 0 4` significa que o processo acessou as páginas 7, depois 0, depois 1, e assim por diante.

---

## 3. Os quatro algoritmos — como explicar cada um

### 3.1 FIFO — First In, First Out

**Ideia:** A página que chegou primeiro na memória é a primeira a sair.

**Analogia:** Uma fila de banco. Quem chegou primeiro sai primeiro.

**Estrutura de dados usada:** `Queue<Integer>` (fila) + `Set<Integer>` para busca rápida.

**Código-chave:**
```java
// Remove o mais antigo quando a memória está cheia
Integer paginaRemovida = memoria.poll();  // tira da frente da fila
memoria.offer(pagina);                    // adiciona no fim
```

**Ponto fraco:** Ignora completamente o padrão de uso. Uma página muito usada pode ser removida só porque chegou primeiro. Sofre da **Anomalia de Belády**: aumentar os quadros pode, paradoxalmente, aumentar as faltas.

**Resultado no caso clássico:** 15 faltas (pior dos quatro).

---

### 3.2 LRU — Least Recently Used

**Ideia:** Remove a página que faz mais tempo que não é acessada.

**Analogia:** Guarda-roupa — você guarda as roupas que usa pouco e mantém à mão as que usa sempre.

**Estrutura de dados usada:** `LinkedList<Integer>` onde o início = menos recente, o fim = mais recente.

**Código-chave:**
```java
if (memoria.contains(pagina)) {
    memoria.remove(Integer.valueOf(pagina));  // tira do lugar atual
    memoria.addLast(pagina);                 // move para o fim (mais recente)
    continue;                                // não houve falta
}
// falta: remove o menos recente (início da lista)
memoria.removeFirst();
memoria.addLast(pagina);
```

**Vantagem:** Aproveita a **localidade temporal** — páginas usadas recentemente tendem a ser usadas novamente em breve.

**Resultado no caso clássico:** 12 faltas (segundo melhor).

---

### 3.3 NFU — Not Frequently Used

**Ideia:** Cada página tem um contador de quantas vezes foi referenciada. Remove a página com menor contador.

**Analogia:** Popularidade — quem é menos popular sai primeiro.

**Estrutura de dados usada:** `List<Integer>` (memória) + `Map<Integer, Integer>` (contadores).

**Código-chave:**
```java
// Atualiza o contador de toda referência, esteja na memória ou não
contadores.put(pagina, contadores.getOrDefault(pagina, 0) + 1);

// Escolha da vítima: menor contador entre as páginas na memória
for (Integer p : memoria) {
    int contador = contadores.getOrDefault(p, 0);
    if (contador < menorContador) { paginaVitima = p; }
}
```

**Ponto fraco:** "Acumula histórico" — páginas muito usadas no passado distante permanecem com contador alto e ficam na memória mesmo que não sejam mais necessárias.

**Resultado no caso clássico:** 13 faltas.

---

### 3.4 Ótimo (Algoritmo de Bélády)

**Ideia:** Remove a página cuja próxima referência está mais distante no futuro. Se uma página não vai mais aparecer, ela é a escolhida.

**Por que é impossível na prática?** O SO não conhece o futuro. Só é possível em simulação, onde a cadeia completa já é conhecida.

**Para que serve?** É o **limite inferior teórico** — nenhum algoritmo real consegue fazer menos faltas para uma dada cadeia. É o _baseline_ de comparação.

**Código-chave:**
```java
// Para cada página na memória, encontra o índice do próximo uso
int proximoUso = encontrarProximoUso(pagina, cadeiaReferencia, i + 1);
if (proximoUso == -1) return pagina;         // nunca mais usada → remove já
if (proximoUso > indiceMaisDistante) {       // mais distante → candidata
    paginaVitima = pagina;
}
```

**Resultado no caso clássico:** 9 faltas (melhor possível).

---

## 4. Arquitetura do projeto — como explicar o código

```
src/
├── Main.java                        ← ponto de entrada (CLI ou GUI)
├── gui/
│   ├── SimuladorGUI.java            ← janela Swing (inputs, tabela, gráfico)
│   └── GraficoComparativo.java      ← gráfico de barras desenhado com Graphics2D
└── simulator/
    ├── EntradaSimulacao.java        ← parsing e validação da entrada do usuário
    ├── ResultadoSimulacao.java      ← POJO: nome do algoritmo + número de faltas
    ├── SimuladorSubstituicao.java   ← orquestrador: chama os 4 algoritmos
    └── algoritmos/
        ├── AlgoritmoSubstituicao.java  ← interface comum (contrato)
        ├── FIFO.java
        ├── LRU.java
        ├── NFU.java
        └── Otimo.java
```

### Fluxo de execução

```
Usuário → Main.java
              ↓ (decide: CLI ou GUI)
         EntradaSimulacao.java (valida entrada)
              ↓
         SimuladorSubstituicao.java (chama os 4 algoritmos)
              ↓
         FIFO / LRU / NFU / Otimo (calculam as faltas)
              ↓
         ResultadoSimulacao (guarda nome + faltas)
              ↓
         CLI imprime / GUI atualiza tabela e gráfico
```

### Por que usar uma interface (`AlgoritmoSubstituicao`)?

A interface define um **contrato**: todo algoritmo deve ter `executar(int[], int)` e `getNome()`. Isso permite que o `SimuladorSubstituicao` chame qualquer algoritmo da mesma forma, sem precisar saber qual é:

```java
for (AlgoritmoSubstituicao algoritmo : algoritmos) {
    int faltas = algoritmo.executar(cadeiaReferencia, numQuadros);
    resultados.add(new ResultadoSimulacao(algoritmo.getNome(), faltas));
}
```

Isso é o **Princípio de Substituição de Liskov** na prática — podemos adicionar um 5º algoritmo sem mudar nada no `SimuladorSubstituicao`.

---

## 5. Resultados e análise — o que falar sobre os números

### Caso clássico (Tanenbaum, cap. de memória virtual)

| Algoritmo | Faltas | Posição |
|:---------:|:------:|:-------:|
| Ótimo     | 9      | 1º (melhor possível) |
| LRU       | 12     | 2º |
| NFU       | 13     | 3º |
| FIFO      | 15     | 4º (pior) |

**Cadeia:** `7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1`  
**Quadros:** 3

### Como analisar os resultados

- **Ótimo → 9 faltas:** limite inferior. Serve para medir o quanto os outros "perdem".
- **LRU → 12 faltas:** apenas 3 faltas a mais que o Ótimo. Funciona bem porque a cadeia tem boa **localidade temporal** (páginas 0, 1, 2 aparecem várias vezes seguidas).
- **NFU → 13 faltas:** perde do LRU por acumular histórico. A página 7, acessada no início, tem contador alto e "resiste" à remoção mesmo quando não é mais útil.
- **FIFO → 15 faltas:** o pior. Remove a página mais antiga, não importa se ela é muito usada.

### A diferença importa?

Sim. Entre FIFO (15) e Ótimo (9) há uma diferença de **6 faltas — redução de 40%**. Em sistemas com milhões de acessos por segundo, isso se traduz em diferença de desempenho real.

---

## 6. Interface gráfica — como demonstrar

### Como abrir

```bash
java -cp out Main
```

A janela já abre com a cadeia clássica preenchida e 3 quadros. Basta clicar em **Simular**.

### O que mostrar

1. Clicar em **Simular** com os valores padrão → mostrar tabela e gráfico.
2. Alterar o número de quadros (ex.: de 3 para 4) → simular novamente e mostrar como os números mudam.
3. Digitar uma cadeia diferente para demonstrar que funciona com qualquer entrada.

### Como abrir no modo terminal (CLI)

```bash
java -cp out Main --cli
```

O programa pede a cadeia e o número de quadros pelo terminal e imprime:

```
Método 1 (FIFO ) - 15 faltas de página
Método 2 (LRU  ) - 12 faltas de página
Método 3 (NFU  ) - 13 faltas de página
Método 4 (Ótimo) -  9 faltas de página
```

---

## 7. Escolhas técnicas — justificativas para perguntas do professor

### Por que Java?

Java é multiplataforma (JVM roda em qualquer SO), tem Swing nativo para GUI sem dependências externas, e as Collections API oferecem exatamente as estruturas necessárias.

### Por que sem bibliotecas externas?

O enunciado exigia Java puro. O gráfico de barras foi desenhado manualmente com `Graphics2D` em vez de usar uma biblioteca como JFreeChart.

### Por que usar `Queue` no FIFO e `LinkedList` no LRU?

- `Queue` é semanticamente correta para FIFO: `poll()` remove o mais antigo, `offer()` insere o mais novo.
- `LinkedList` no LRU funciona como lista ordenada por tempo de uso: `removeFirst()` pega o mais antigo, `addLast()` coloca o mais recente.

### Por que o Ótimo usa índice numérico no `for`?

```java
for (int i = 0; i < cadeiaReferencia.length; i++) {
```
Porque ele precisa saber a **posição atual** para procurar o próximo uso apenas nas posições **futuras** (`i + 1` em diante). Um `for-each` não fornece o índice.

### Por que o NFU mantém contadores mesmo para páginas fora da memória?

Porque o NFU conta referências globais, não apenas quando a página está carregada. Quando a página volta para a memória, seu histórico de uso é preservado — isso é ao mesmo tempo uma vantagem (mais informação) e uma desvantagem (histórico "envelhece" mal).

---

## 8. Perguntas prováveis do professor (e respostas)

**"O que é anomalia de Belády?"**
> É um fenômeno do FIFO onde aumentar o número de quadros pode aumentar o número de faltas de página. Não acontece com LRU ou Ótimo porque eles são algoritmos de "pilha" (stack algorithms).

**"O algoritmo Ótimo pode ser usado em sistemas reais?"**
> Não. Ele exige conhecer a cadeia de referências futura, o que é impossível em tempo real. Serve apenas como referência teórica para medir a eficiência dos algoritmos práticos.

**"Qual a diferença entre LRU e NFU?"**
> LRU rastreia *quando* a página foi usada pela última vez (ordem temporal). NFU conta *quantas vezes* a página foi referenciada (frequência). LRU "esquece" o passado ao mover páginas na lista; NFU acumula todo o histórico no contador.

**"Por que o NFU ficou entre LRU e FIFO e não entre LRU e Ótimo?"**
> Porque o contador global do NFU penaliza páginas novas e beneficia páginas com histórico longo, mesmo que elas não sejam mais relevantes. O LRU descarta esse histórico e foca apenas no uso recente, que é mais preditivo.

**"Como vocês validaram que a implementação está correta?"**
> Usamos o caso clássico do livro Tanenbaum (Sistemas Operacionais Modernos, 4ª ed.). Os valores esperados são amplamente documentados — FIFO 15, LRU 12, Ótimo 9 — e nosso simulador reproduziu exatamente esses resultados.

**"O que é a interface `AlgoritmoSubstituicao`?"**
> É um contrato em Java que obriga cada algoritmo a implementar dois métodos: `executar()` (retorna o número de faltas) e `getNome()` (retorna o nome). Isso permite que o orquestrador `SimuladorSubstituicao` trate todos os algoritmos de forma uniforme.

**"Por que o gráfico não usa nenhuma biblioteca?"**
> O enunciado pedia Java puro. Implementamos o gráfico com `Graphics2D` do próprio Swing, sobrescrevendo `paintComponent()` em um `JPanel`. As barras são retângulos (`fillRoundRect`) cujas alturas são proporcionais ao número de faltas.

---

## 9. Roteiro sugerido de apresentação (5–10 minutos)

1. **Contextualização (1 min):** Expliquem o problema — o que é falta de página, por que o SO precisa de um algoritmo de substituição.
2. **Os algoritmos (2 min):** Expliquem brevemente cada um com a analogia. Destaquem o Ótimo como limite teórico.
3. **Arquitetura do código (1 min):** Mostre o diagrama de fluxo. Expliquem a interface e o padrão.
4. **Demonstração ao vivo (3 min):** Abra a GUI, clique em Simular, mostre a tabela e o gráfico. Mude o número de quadros e simule de novo.
5. **Análise dos resultados (1–2 min):** Explique por que Ótimo < LRU < NFU < FIFO e o que isso significa.
6. **Conclusão (30 seg):** Diferença de 40% entre FIFO e Ótimo — impacto real em sistemas operacionais.

---

## 10. Comandos rápidos para a apresentação

### Compilar (caso necessário)

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java -Path src).FullName
```

### Executar interface gráfica

```bash
java -cp out Main
```

### Executar no terminal

```bash
java -cp out Main --cli
```

### Entrada para demonstração rápida

```
Cadeia:  7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1
Quadros: 3
```

---

## 11. Referência bibliográfica utilizada

TANENBAUM, A. S.; BOS, H. **Sistemas Operacionais Modernos.** 4. ed. São Paulo: Pearson, 2016.
