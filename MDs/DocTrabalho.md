Título: Simulador de Algoritmos de Substituição de Páginas

Resumo:
Este trabalho propõe o desenvolvimento de um simulador para avaliar o desempenho de diferentes algoritmos de substituição de páginas em sistemas de gerenciamento de memória virtual. Os algoritmos estudados incluem FIFO, LRU, do Relógio, Ótimo, NFU e de Envelhecimento.

Introdução:
O gerenciamento eficiente da memória virtual é crucial para o desempenho dos sistemas operacionais. A alocação e substituição de páginas são tarefas complexas que afetam diretamente a experiência do usuário. Neste contexto, os algoritmos de substituição de páginas desempenham um papel fundamental na otimização do uso da memória.
Sabemos e vimos em sala os principais algoritmos de substituição de páginas:

1. Algoritmo FIFO (First In, First Out):
   Nesse algoritmo, a página mais antiga na memória é a primeira a ser substituída quando ocorre uma falta de página.
   Implementação: Mantenha uma fila (ou lista) das páginas na ordem em que foram carregadas. Quando uma nova página precisa ser trazida para a memória, remova a página no início da fila.
   É simples de entender e implementar, mas pode não ser eficiente em cenários reais.

2. Algoritmo LRU (Least Recently Used):
   Esse algoritmo substitui a página que foi menos recentemente usada.
   Implementação: Mantenha uma estrutura (como uma lista ligada) para rastrear a ordem de uso das páginas. Quando ocorre uma falta de página, remova a página que foi usada menos recentemente.
   É mais eficiente do que o FIFO, mas requer mais overhead para manter o registro de uso.

3. Algoritmo do Relógio (Clock):
   Também conhecido como “segunda chance”, esse algoritmo simula um relógio circular.
   Implementação: Crie uma lista circular de páginas. Quando ocorre uma falta de página, verifique se a página apontada pelo ponteiro do relógio foi usada recentemente. Se sim, avance o ponteiro; caso contrário, substitua essa página.
   É uma abordagem intermediária entre FIFO e LRU.

4. Algoritmo Ótimo:
   Esse é o algoritmo ideal, mas não é prático na implementação real.
   Implementação: Substitui a página que será usada mais tarde no futuro (ótima previsão).
   Serve como referência para avaliar o desempenho dos outros algoritmos.

5. Algoritmo NFU (Not Frequently Used):
   Baseado na frequência de uso das páginas.
   Implementação: Atribui um contador a cada página e incrementa o contador sempre que a página é referenciada. A página com o menor contador é substituída.
   Pode ser implementado em software.

6. Algoritmo de Envelhecimento (Aging):
   Uma variação do LRU em software.
   Implementação: Usa bits de envelhecimento para rastrear o tempo desde a última referência a cada página. As páginas com menor tempo de referência são substituídas.
   Também pode ser implementado em software.

Objetivos:
Sabendo que temos essas implementações, vamos criar um SIMULADOR para os principais algoritmos de substituição de páginas, com o objetivo de verificar as faltas de páginas no gerenciamento da memória virtual.

Atenção aos detalhes do simulador:

1. O programa deve iniciar com uma cadeia de números (int) que irá indicar nossa página a ser utilizada.

2. No programa devem haver 4 métodos (escolha entre os 6 listados anteriormente) onde serão implementados os 4 algoritmos de substituição.

3. A saída do programa (cada método)  deve indicar como ficaram as faltas de página por cada método, ou seja como no exemplo abaixo:
- Método 1 - X faltas de página
- Método 2 - X faltas de página
- Método 3 - X faltas de página
- Método 4 - X faltas de página

4. Uma opção EXTRA (+1 ponto) ao trabalho seria usar uma interface gráfica para utilização do programa e uma indicação com os gráficos como comparativo.
   Ver o site como referência: https://sdpm-simulator.netlify.app

DICA *** Usem:
- Swing para o próprio JAVA - https://www.devmedia.com.br/introducao-a-interface-gui-no-java/25646
- Primefaces para implementação com JSF (web) - https://www.primefaces.org/

Metodologia:

O simulador será desenvolvido em linguagem de programação Java. Ele receberá como entrada uma sequência de números inteiros representando as páginas a serem utilizadas. Em seguida, serão implementados quatro métodos correspondentes aos algoritmos de substituição. O programa calculará e apresentará o número de faltas de página para cada método.

Resultados Esperados:
Espera-se que o simulador forneça insights sobre o desempenho relativo dos diferentes algoritmos de substituição de páginas. Com base nos resultados obtidos, poderemos avaliar quais algoritmos são mais adequados para diferentes cenários e cargas de trabalho.
A entrega no AVA deve ser um arquivo no formato PDF contendo o relatório e um LINK para o README e feito de acordo com o modelo anexo, com as informações relativas a realização do trabalho com os gráficos resultantes do comparativo. O código deve ser adicionado aos anexos com um LINK para o GITHUB do projeto. (Não esqueçam de colocar no arquivo README como funciona a execução, isso irá facilitar na correção do trabalho).

Os trabalhos devem ser enviados até dia 08/05 e apresentados na aula de LAB seguinte (mediante escolha do professor).

O trabalho será feito em DUPLA
A nota será de até 2 pontos.
Modelo-Artigo.docx Modelo-Artigo.docx