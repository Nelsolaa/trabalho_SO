# Simulador de Algoritmos de Substituição de Páginas

Este projeto é um simulador desenvolvido em *Java* para avaliar o desempenho de diferentes algoritmos de substituição de páginas em sistemas de gerenciamento de memória virtual. O objetivo principal é calcular e comparar a quantidade de *faltas de página (Page Faults)* geradas por cada método de substituição a partir de uma cadeia de referência fornecida.

## ⚙️ Algoritmos Implementados

Para esta simulação, foram escolhidos os 4 algoritmos mais consolidados e com estruturas de dados nativas e eficientes no Java:

1. *FIFO (First In, First Out)*
2. *LRU (Least Recently Used)*
3. *NFU (Not Frequently Used)*
4. *Ótimo (Optimal)*

## 🛠️ Plano de Implementação (Java)

O coração do simulador baseia-se em varrer um array de inteiros (a cadeia de páginas) e gerenciar uma estrutura de dados que simula a memória física (os quadros/frames). Abaixo está o plano lógico para cada algoritmo:

### 1. FIFO
- *Estrutura de Dados:* Queue<Integer> (instanciada como um LinkedList).
- *Lógica:* A fila mantém a ordem exata de entrada. Quando ocorre uma falta de página e a memória está cheia, utiliza-se o método poll() para remover o elemento da cabeça da fila (o mais antigo) e offer() para inserir a nova página no final da fila.

### 2. LRU
- *Estrutura de Dados:* LinkedList<Integer> ou ArrayList<Integer>.
- *Lógica:* Representa a memória ordenando do menos recentemente usado (início) para o mais recentemente usado (final).
- Toda vez que uma página é acessada (mesmo que já esteja na memória), ela é removida de sua posição atual e reinserida no final da lista.
- Quando ocorre uma falta de página, remove-se o elemento de índice 0 (o menos recentemente usado) e adiciona-se o novo no final.

### 3. NFU
- *Estrutura de Dados:* HashMap<Integer, Integer> (para contagem) + List<Integer> (para a memória atual).
- *Lógica:* O mapa guarda <NumeroDaPagina, FrequenciaDeAcesso>. Toda vez que uma página aparece na cadeia, sua contagem no mapa é incrementada.
- Na ocorrência de uma falta de página, o algoritmo itera sobre os elementos atualmente na memória, consulta a frequência de cada um no HashMap, identifica o de menor valor e o substitui.

### 4. Ótimo
- *Estrutura de Dados:* List<Integer> para a memória.
- *Lógica:* Como a cadeia de referência é conhecida previamente pelo simulador, o algoritmo "olha para o futuro".
- Em uma falta de página com a memória cheia, ele percorre a lista de páginas que estão na memória e verifica, no restante da cadeia de entrada (a partir do índice atual), qual delas vai demorar mais para ser referenciada novamente. A página com a ocorrência mais distante (ou que não aparecerá mais) é a escolhida para substituição.

---

## 🚀 Como Executar o Projeto

*Pré-requisitos:*
* [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) versão 11 ou superior.
* IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code) ou terminal.

*Passo a passo (Via Terminal):*

1. Clone este repositório:
   ```bash
   git clone [https://github.com/SEU-USUARIO/nome-do-repositorio.git](https://github.com/SEU-USUARIO/nome-do-repositorio.git)