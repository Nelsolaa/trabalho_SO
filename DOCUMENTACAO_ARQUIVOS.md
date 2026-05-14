# Documentação Detalhada dos Arquivos do Projeto

Este documento explica todos os arquivos principais do projeto de forma detalhada, com trechos de código e explicações em linguagem simples.

O objetivo é que uma pessoa sem experiência em programação consiga entender:

- para que serve cada arquivo;
- como os arquivos se conectam;
- o que cada classe Java faz;
- como os algoritmos funcionam;
- por que existem arquivos compilados, arquivos da IDE e documentos auxiliares.

## 1. O que é este projeto?

Este projeto é um simulador de algoritmos de substituição de páginas.

Em sistemas operacionais, quando um programa precisa usar uma página de memória que não está carregada na memória principal, ocorre uma falta de página. Quando a memória está cheia, o sistema precisa escolher uma página antiga para remover e abrir espaço para a nova.

Esse projeto compara quatro formas diferentes de fazer essa escolha:

- FIFO;
- LRU;
- NFU;
- Ótimo.

O projeto foi feito em Java e pode ser executado de duas formas:

- pelo terminal, usando modo CLI;
- por uma interface gráfica feita com Swing.

## 2. Estrutura geral do projeto

A estrutura principal é esta:

```text
SO/
├── .gitignore
├── README.md
├── DOCUMENTACAO_ARQUIVOS.md
├── MDs/
├── src/
│   ├── Main.java
│   ├── gui/
│   └── simulator/
├── out/
└── .idea/
```

Cada pasta tem uma função:

- `src/`: contém o código-fonte, ou seja, o código escrito pelo programador.
- `out/`: contém o código compilado, gerado automaticamente pelo Java.
- `MDs/`: contém documentos de apoio do trabalho.
- `.idea/`: contém configurações do IntelliJ IDEA.
- `.git/`: contém arquivos internos do Git, usados para versionamento.

## 3. Arquivos da raiz do projeto

## 3.1 `.gitignore`

### Para que serve?

O arquivo `.gitignore` diz ao Git quais arquivos ou pastas devem ser ignorados.

O Git é a ferramenta que controla versões do projeto. Nem sempre queremos enviar todos os arquivos para o repositório, principalmente arquivos temporários, gerados automaticamente ou documentos locais.

### Conteúdo do arquivo

```gitignore
MDs/
```

### Explicação

Essa linha informa que a pasta `MDs/` deve ser ignorada pelo Git.

Isso significa que, se novos arquivos forem criados dentro da pasta `MDs/`, o Git não vai tentar adicioná-los automaticamente ao controle de versão.

## 3.2 `README.md`

### Para que serve?

O `README.md` é a primeira documentação que alguém normalmente lê ao abrir o projeto.

Ele explica:

- o nome do projeto;
- o objetivo;
- os requisitos;
- como compilar;
- como executar;
- um caso de teste esperado.

### Trecho importante

```markdown
# Simulador de Algoritmos de Substituição de Páginas

Simulador em Java puro para comparar faltas de página nos algoritmos FIFO, LRU, NFU e Ótimo.
```

### Explicação

Esse trecho apresenta o projeto. Ele diz que o programa compara quantas faltas de página acontecem em cada algoritmo.

### Trecho de compilação

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java -Path src).FullName
```

### Explicação

Esse comando compila todos os arquivos `.java` que estão dentro da pasta `src/` e coloca os arquivos compilados dentro da pasta `out/`.

Explicando por partes:

- `javac`: compilador do Java;
- `-encoding UTF-8`: informa que os arquivos usam codificação UTF-8;
- `-d out`: manda o resultado da compilação para a pasta `out`;
- `(Get-ChildItem ...)`: comando do PowerShell que encontra todos os arquivos `.java`.

### Trecho de execução

```bash
java -cp out Main
```

Esse comando executa a versão gráfica do programa.

```bash
java -cp out Main --cli
```

Esse comando executa a versão de terminal.

## 3.3 `DOCUMENTACAO_ARQUIVOS.md`

### Para que serve?

Este é o arquivo que você está lendo.

Ele serve como uma explicação detalhada do projeto, arquivo por arquivo.

## 4. Pasta `src/`

A pasta `src/` é a pasta mais importante do projeto, porque contém o código-fonte Java.

Código-fonte é o código escrito por humanos antes de ser compilado.

## 4.1 `src/Main.java`

### Para que serve?

Esse arquivo é o ponto de entrada do programa.

Em Java, o programa começa a executar a partir do método `main`.

### Trecho principal

```java
public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && "--cli".equalsIgnoreCase(args[0])) {
            executarCli();
            return;
        }

        SwingUtilities.invokeLater(() -> new SimuladorGUI().setVisible(true));
    }
}
```

### Explicação linha por linha

```java
public class Main {
```

Cria uma classe chamada `Main`. Em Java, quase tudo fica dentro de classes.

```java
public static void main(String[] args) {
```

Esse é o método inicial do programa. Quando você executa `java Main`, o Java procura esse método.

```java
if (args.length > 0 && "--cli".equalsIgnoreCase(args[0])) {
```

Verifica se o usuário passou algum argumento ao executar o programa.

Se o primeiro argumento for `--cli`, o programa entra no modo terminal.

```java
executarCli();
return;
```

Chama o método que executa a versão de terminal e depois encerra o método `main`.

```java
SwingUtilities.invokeLater(() -> new SimuladorGUI().setVisible(true));
```

Se o usuário não passou `--cli`, o programa abre a interface gráfica.

`SwingUtilities.invokeLater` é uma forma correta de iniciar telas Swing em Java.

### Modo terminal

```java
private static void executarCli() {
    Scanner scanner = new Scanner(System.in, "UTF-8");
```

Esse método é responsável pelo modo CLI.

CLI significa Command Line Interface, ou seja, interface por linha de comando.

`Scanner` é usado para ler o que o usuário digita no terminal.

### Leitura dos dados

```java
System.out.println("Digite a cadeia de referência separada por espaços:");
String cadeiaTexto = scanner.nextLine();

System.out.println("Digite o número de quadros de memória:");
String quadrosTexto = scanner.nextLine();
```

Aqui o programa pede duas informações:

- a cadeia de referência;
- o número de quadros de memória.

Exemplo de cadeia:

```text
7 0 1 2 0 3 0 4
```

Cada número representa uma página que está sendo acessada.

### Conversão dos dados

```java
int[] cadeiaReferencia = EntradaSimulacao.parsearCadeiaReferencia(cadeiaTexto);
int numQuadros = EntradaSimulacao.parsearQuadros(quadrosTexto);
```

O usuário digita texto, mas os algoritmos precisam de números.

Essas duas linhas convertem texto em números:

- `cadeiaTexto` vira um vetor de inteiros;
- `quadrosTexto` vira um número inteiro.

### Execução da simulação

```java
List<ResultadoSimulacao> resultados = new SimuladorSubstituicao().executar(cadeiaReferencia, numQuadros);
imprimirResultados(resultados);
```

Essa parte cria o simulador, executa os algoritmos e imprime os resultados.

## 4.2 `src/simulator/EntradaSimulacao.java`

### Para que serve?

Esse arquivo cuida da entrada do usuário.

Ele transforma textos digitados pelo usuário em valores que o programa consegue usar.

Também valida se os dados estão corretos.

### Classe utilitária

```java
public final class EntradaSimulacao {
    private EntradaSimulacao() {
    }
}
```

### Explicação

```java
public final class EntradaSimulacao
```

Cria uma classe que não deve ser herdada por outras classes.

```java
private EntradaSimulacao() {
}
```

Esse construtor privado impede que alguém crie um objeto dessa classe.

Isso acontece porque a classe só possui métodos estáticos. Ela funciona como uma caixa de ferramentas.

### Conversão da cadeia de referência

```java
public static int[] parsearCadeiaReferencia(String texto) {
    if (texto == null || texto.trim().isEmpty()) {
        throw new IllegalArgumentException("Informe a cadeia de referência.");
    }

    String[] partes = texto.trim().split("\\s+");
    int[] cadeia = new int[partes.length];
```

### Explicação

```java
if (texto == null || texto.trim().isEmpty())
```

Verifica se o usuário não digitou nada.

`trim()` remove espaços no começo e no fim.

```java
throw new IllegalArgumentException(...)
```

Se a entrada estiver errada, o programa lança um erro com mensagem explicativa.

```java
String[] partes = texto.trim().split("\\s+");
```

Divide o texto em pedaços separados por espaço.

Por exemplo:

```text
"7 0 1 2"
```

vira:

```text
["7", "0", "1", "2"]
```

```java
int[] cadeia = new int[partes.length];
```

Cria um vetor de inteiros com o mesmo tamanho da quantidade de números digitados.

### Conversão de cada número

```java
for (int i = 0; i < partes.length; i++) {
    try {
        cadeia[i] = Integer.parseInt(partes[i]);
    } catch (NumberFormatException erro) {
        throw new IllegalArgumentException("A cadeia deve conter apenas números inteiros separados por espaço.");
    }
}
```

### Explicação

O `for` percorre cada pedaço do texto.

```java
Integer.parseInt(partes[i])
```

Converte texto em número inteiro.

Por exemplo:

```text
"7"
```

vira:

```text
7
```

Se o usuário digitar algo inválido, como:

```text
7 A 2
```

o programa mostra uma mensagem de erro.

### Conversão do número de quadros

```java
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
```

### Explicação

Esse método converte o número de quadros digitado pelo usuário.

Quadros representam quantos espaços existem na memória.

Se o usuário digitar `3`, significa que cabem 3 páginas na memória ao mesmo tempo.

O método também impede valores inválidos como:

- `0`;
- `-1`;
- texto;
- entrada vazia.

## 4.3 `src/simulator/ResultadoSimulacao.java`

### Para que serve?

Esse arquivo representa o resultado de um algoritmo.

Ele guarda duas informações:

- o nome do algoritmo;
- a quantidade de faltas de página.

### Código principal

```java
public class ResultadoSimulacao {
    private final String nomeAlgoritmo;
    private final int faltasDePagina;

    public ResultadoSimulacao(String nomeAlgoritmo, int faltasDePagina) {
        this.nomeAlgoritmo = nomeAlgoritmo;
        this.faltasDePagina = faltasDePagina;
    }
}
```

### Explicação

```java
private final String nomeAlgoritmo;
```

Guarda o nome do algoritmo, como `FIFO` ou `LRU`.

`String` significa texto.

```java
private final int faltasDePagina;
```

Guarda a quantidade de faltas de página.

`int` significa número inteiro.

```java
final
```

Significa que o valor não será alterado depois de criado.

### Getters

```java
public String getNomeAlgoritmo() {
    return nomeAlgoritmo;
}

public int getFaltasDePagina() {
    return faltasDePagina;
}
```

### Explicação

Esses métodos permitem acessar os valores guardados dentro do objeto.

Como os atributos são `private`, outras classes não conseguem acessá-los diretamente. Por isso existem os getters.

## 4.4 `src/simulator/SimuladorSubstituicao.java`

### Para que serve?

Esse arquivo é o organizador da simulação.

Ele não implementa diretamente os algoritmos. Ele chama cada algoritmo e junta os resultados.

### Lista de algoritmos

```java
private final List<AlgoritmoSubstituicao> algoritmos;

public SimuladorSubstituicao() {
    this.algoritmos = Arrays.asList(
            new FIFO(),
            new LRU(),
            new NFU(),
            new Otimo()
    );
}
```

### Explicação

```java
List<AlgoritmoSubstituicao>
```

Cria uma lista de algoritmos.

Todos os algoritmos seguem a mesma interface, então podem ficar na mesma lista.

```java
new FIFO()
new LRU()
new NFU()
new Otimo()
```

Cria um objeto de cada algoritmo.

### Execução dos algoritmos

```java
public List<ResultadoSimulacao> executar(int[] cadeiaReferencia, int numQuadros) {
    validarEntrada(cadeiaReferencia, numQuadros);

    List<ResultadoSimulacao> resultados = new ArrayList<>();
    for (AlgoritmoSubstituicao algoritmo : algoritmos) {
        int faltas = algoritmo.executar(cadeiaReferencia, numQuadros);
        resultados.add(new ResultadoSimulacao(algoritmo.getNome(), faltas));
    }

    return resultados;
}
```

### Explicação

```java
validarEntrada(cadeiaReferencia, numQuadros);
```

Antes de executar, o programa verifica se os dados fazem sentido.

```java
List<ResultadoSimulacao> resultados = new ArrayList<>();
```

Cria uma lista vazia para guardar os resultados.

```java
for (AlgoritmoSubstituicao algoritmo : algoritmos)
```

Percorre cada algoritmo da lista.

```java
int faltas = algoritmo.executar(cadeiaReferencia, numQuadros);
```

Executa o algoritmo atual.

```java
resultados.add(new ResultadoSimulacao(algoritmo.getNome(), faltas));
```

Guarda o nome do algoritmo e o número de faltas.

### Validação

```java
private void validarEntrada(int[] cadeiaReferencia, int numQuadros) {
    if (cadeiaReferencia == null || cadeiaReferencia.length == 0) {
        throw new IllegalArgumentException("A cadeia de referência deve ter pelo menos uma página.");
    }

    if (numQuadros < 1) {
        throw new IllegalArgumentException("O número de quadros deve ser maior que zero.");
    }
}
```

### Explicação

Esse método impede que o simulador rode com dados inválidos.

Ele verifica:

- se a cadeia existe;
- se a cadeia tem pelo menos uma página;
- se o número de quadros é maior que zero.

## 5. Pasta `src/simulator/algoritmos/`

Essa pasta contém os algoritmos de substituição de páginas.

## 5.1 `AlgoritmoSubstituicao.java`

### Para que serve?

Esse arquivo define uma interface.

Uma interface funciona como um contrato. Ela diz quais métodos uma classe precisa ter.

### Código

```java
public interface AlgoritmoSubstituicao {
    int executar(int[] cadeiaReferencia, int numQuadros);

    String getNome();
}
```

### Explicação

Todo algoritmo precisa ter:

```java
int executar(int[] cadeiaReferencia, int numQuadros);
```

Esse método recebe:

- a sequência de páginas;
- a quantidade de quadros.

E retorna:

- o número de faltas de página.

Também precisa ter:

```java
String getNome();
```

Esse método retorna o nome do algoritmo.

Exemplo:

```text
FIFO
```

## 5.2 `FIFO.java`

### Para que serve?

Implementa o algoritmo FIFO.

FIFO significa First In, First Out.

Em português: o primeiro que entra é o primeiro que sai.

### Ideia simples

Imagine uma fila de pessoas.

A pessoa que chegou primeiro será atendida primeiro.

No FIFO, a página que entrou primeiro na memória será removida primeiro quando precisar liberar espaço.

### Estruturas usadas

```java
Queue<Integer> memoria = new LinkedList<>();
Set<Integer> paginasNaMemoria = new HashSet<>();
int faltasDePagina = 0;
```

### Explicação

```java
Queue<Integer> memoria
```

Representa a memória como uma fila.

```java
Set<Integer> paginasNaMemoria
```

Guarda quais páginas estão na memória.

O `Set` ajuda a verificar rapidamente se uma página já está carregada.

```java
int faltasDePagina = 0;
```

Começa o contador de faltas em zero.

### Trecho principal

```java
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
```

### Explicação passo a passo

```java
for (int pagina : cadeiaReferencia)
```

Percorre cada página da cadeia.

```java
if (!paginasNaMemoria.contains(pagina))
```

Verifica se a página não está na memória.

Se não está, ocorreu uma falta de página.

```java
faltasDePagina++;
```

Aumenta o contador de faltas.

```java
if (memoria.size() == numQuadros)
```

Verifica se a memória está cheia.

```java
Integer paginaRemovida = memoria.poll();
```

Remove a página mais antiga da fila.

```java
memoria.offer(pagina);
```

Adiciona a nova página no fim da fila.

## 5.3 `LRU.java`

### Para que serve?

Implementa o algoritmo LRU.

LRU significa Least Recently Used.

Em português: menos recentemente usado.

### Ideia simples

O algoritmo remove a página que está há mais tempo sem ser acessada.

### Estrutura usada

```java
LinkedList<Integer> memoria = new LinkedList<>();
int faltasDePagina = 0;
```

### Explicação

A lista guarda as páginas em ordem de uso:

- início da lista: página menos recente;
- fim da lista: página mais recente.

### Trecho principal

```java
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
```

### Explicação passo a passo

```java
if (memoria.contains(pagina))
```

Verifica se a página já está carregada.

Se estiver, não ocorre falta de página.

```java
memoria.remove(Integer.valueOf(pagina));
memoria.addLast(pagina);
```

Remove a página da posição atual e coloca no fim.

Isso marca a página como usada recentemente.

```java
continue;
```

Pula para a próxima página da cadeia.

```java
faltasDePagina++;
```

Se a página não estava na memória, conta uma falta.

```java
memoria.removeFirst();
```

Remove a página menos recentemente usada.

```java
memoria.addLast(pagina);
```

Adiciona a nova página como a mais recente.

## 5.4 `NFU.java`

### Para que serve?

Implementa o algoritmo NFU.

NFU significa Not Frequently Used.

Em português: não frequentemente usado.

### Ideia simples

Cada página tem um contador.

Toda vez que uma página aparece na cadeia, seu contador aumenta.

Quando precisa remover uma página, o algoritmo escolhe a página com menor contador.

### Estruturas usadas

```java
List<Integer> memoria = new ArrayList<>();
Map<Integer, Integer> contadores = new HashMap<>();
int faltasDePagina = 0;
```

### Explicação

```java
List<Integer> memoria
```

Guarda as páginas que estão atualmente na memória.

```java
Map<Integer, Integer> contadores
```

Guarda quantas vezes cada página foi usada.

Exemplo:

```text
Página 7 apareceu 3 vezes
Página 0 apareceu 5 vezes
Página 1 apareceu 2 vezes
```

### Incremento do contador

```java
contadores.put(pagina, contadores.getOrDefault(pagina, 0) + 1);
```

### Explicação

Essa linha aumenta o contador da página atual.

```java
contadores.getOrDefault(pagina, 0)
```

Busca o contador atual da página.

Se a página ainda não existe no mapa, considera o valor zero.

Depois soma `1`.

### Trecho principal

```java
if (memoria.contains(pagina)) {
    continue;
}

faltasDePagina++;

if (memoria.size() == numQuadros) {
    Integer paginaVitima = escolherPaginaVitima(memoria, contadores);
    memoria.remove(Integer.valueOf(paginaVitima));
}

memoria.add(pagina);
```

### Explicação

Se a página já está na memória, o programa só segue para a próxima.

Se a página não está na memória, ocorre falta de página.

Se a memória está cheia, o programa chama:

```java
escolherPaginaVitima(memoria, contadores)
```

Esse método escolhe qual página será removida.

### Escolha da vítima

```java
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
```

### Explicação

```java
int menorContador = Integer.MAX_VALUE;
```

Começa com um valor muito alto para conseguir encontrar o menor contador real.

```java
for (Integer pagina : memoria)
```

Percorre apenas as páginas que estão na memória.

```java
if (contador < menorContador)
```

Se encontrou uma página com contador menor, ela vira a candidata a ser removida.

## 5.5 `Otimo.java`

### Para que serve?

Implementa o algoritmo Ótimo.

Esse algoritmo é chamado de ótimo porque, teoricamente, ele faz a melhor escolha possível.

### Ideia simples

Quando precisa remover uma página, ele olha para o futuro.

Ele remove:

- a página que nunca mais será usada;
- ou a página que vai demorar mais para ser usada novamente.

Na vida real, o sistema operacional não sabe o futuro. Mas em uma simulação, a cadeia completa já é conhecida.

### Estrutura usada

```java
List<Integer> memoria = new ArrayList<>();
int faltasDePagina = 0;
```

### Trecho principal

```java
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
```

### Explicação

```java
for (int i = 0; i < cadeiaReferencia.length; i++)
```

Percorre a cadeia usando índice.

Esse índice é importante porque o algoritmo precisa saber onde está agora para olhar o futuro a partir dali.

```java
int pagina = cadeiaReferencia[i];
```

Pega a página atual.

```java
if (memoria.contains(pagina))
```

Se a página já está na memória, não acontece falta.

```java
Integer paginaVitima = escolherPaginaVitima(memoria, cadeiaReferencia, i + 1);
```

Escolhe a página que será removida olhando para as próximas posições da cadeia.

### Escolha da vítima

```java
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
```

### Explicação

```java
int proximoUso = encontrarProximoUso(...)
```

Procura quando aquela página será usada novamente.

```java
if (proximoUso == -1)
```

Se o próximo uso for `-1`, significa que a página não aparece mais no futuro.

Nesse caso, ela é a melhor página para remover.

```java
if (proximoUso > indiceMaisDistante)
```

Se a página vai demorar mais para aparecer do que as outras, ela vira candidata a remoção.

### Busca pelo próximo uso

```java
private int encontrarProximoUso(int pagina, int[] cadeiaReferencia, int inicioBusca) {
    for (int i = inicioBusca; i < cadeiaReferencia.length; i++) {
        if (cadeiaReferencia[i] == pagina) {
            return i;
        }
    }

    return -1;
}
```

### Explicação

Esse método percorre o restante da cadeia procurando a próxima aparição da página.

Se encontrar, retorna o índice.

Se não encontrar, retorna `-1`.

## 6. Pasta `src/gui/`

Essa pasta contém a interface gráfica do projeto.

Interface gráfica é a janela com campos, botões, tabela e gráfico.

## 6.1 `SimuladorGUI.java`

### Para que serve?

Esse arquivo cria a janela principal do programa.

Ele permite que o usuário use o simulador sem precisar digitar comandos no terminal.

### Declaração da classe

```java
public class SimuladorGUI extends JFrame {
```

### Explicação

`JFrame` é a classe do Java Swing que representa uma janela.

Quando `SimuladorGUI` herda de `JFrame`, ela passa a ser uma janela.

### Campos principais

```java
private static final String CADEIA_PADRAO = "7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1";

private final JTextField campoCadeia;
private final JSpinner spinnerQuadros;
private final DefaultTableModel modeloTabela;
private final GraficoComparativo graficoComparativo;
private final SimuladorSubstituicao simulador;
```

### Explicação

```java
CADEIA_PADRAO
```

É uma cadeia já preenchida para facilitar testes.

```java
JTextField campoCadeia
```

Campo onde o usuário digita a sequência de páginas.

```java
JSpinner spinnerQuadros
```

Componente usado para escolher o número de quadros.

```java
DefaultTableModel modeloTabela
```

Modelo que controla os dados exibidos na tabela.

```java
GraficoComparativo graficoComparativo
```

Painel que desenha o gráfico de barras.

```java
SimuladorSubstituicao simulador
```

Objeto que executa os algoritmos.

### Construtor da tela

```java
public SimuladorGUI() {
    super("Simulador de Substituição de Páginas");
    this.simulador = new SimuladorSubstituicao();
    this.campoCadeia = new JTextField(CADEIA_PADRAO);
    this.spinnerQuadros = new JSpinner(new SpinnerNumberModel(3, 1, 999, 1));
    this.modeloTabela = criarModeloTabela();
    this.graficoComparativo = new GraficoComparativo();

    configurarJanela();
    montarInterface();
}
```

### Explicação

```java
super("Simulador de Substituição de Páginas");
```

Define o título da janela.

```java
new JTextField(CADEIA_PADRAO)
```

Cria o campo de texto já preenchido com uma cadeia de teste.

```java
new SpinnerNumberModel(3, 1, 999, 1)
```

Cria um seletor numérico:

- valor inicial: `3`;
- valor mínimo: `1`;
- valor máximo: `999`;
- passo: `1`.

### Configuração da janela

```java
private void configurarJanela() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setMinimumSize(new Dimension(820, 620));
}
```

### Explicação

```java
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
```

Faz o programa encerrar quando o usuário fecha a janela.

```java
setMinimumSize(new Dimension(820, 620));
```

Define o tamanho mínimo da janela.

### Botão de simulação

```java
JButton botaoSimular = new JButton("Simular");
botaoSimular.addActionListener(evento -> executarSimulacao());
```

### Explicação

Cria um botão chamado `Simular`.

Quando o usuário clica nele, o método `executarSimulacao()` é chamado.

### Execução pela GUI

```java
private void executarSimulacao() {
    try {
        int[] cadeiaReferencia = EntradaSimulacao.parsearCadeiaReferencia(campoCadeia.getText());
        int numQuadros = (Integer) spinnerQuadros.getValue();

        List<ResultadoSimulacao> resultados = simulador.executar(cadeiaReferencia, numQuadros);
        atualizarResultados(resultados);
    } catch (IllegalArgumentException erro) {
        JOptionPane.showMessageDialog(
                this,
                erro.getMessage(),
                "Entrada inválida",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
```

### Explicação

```java
campoCadeia.getText()
```

Pega o texto digitado no campo da cadeia.

```java
spinnerQuadros.getValue()
```

Pega o número escolhido no seletor.

```java
simulador.executar(...)
```

Executa os algoritmos.

```java
atualizarResultados(resultados)
```

Atualiza a tabela e o gráfico.

```java
catch (IllegalArgumentException erro)
```

Se a entrada estiver errada, mostra uma janela de aviso.

### Atualização da tabela e do gráfico

```java
private void atualizarResultados(List<ResultadoSimulacao> resultados) {
    modeloTabela.setRowCount(0);
    for (ResultadoSimulacao resultado : resultados) {
        modeloTabela.addRow(new Object[]{
                resultado.getNomeAlgoritmo(),
                resultado.getFaltasDePagina()
        });
    }
    graficoComparativo.setResultados(resultados);
}
```

### Explicação

```java
modeloTabela.setRowCount(0);
```

Limpa a tabela antes de colocar novos resultados.

```java
modeloTabela.addRow(...)
```

Adiciona uma linha para cada algoritmo.

```java
graficoComparativo.setResultados(resultados);
```

Envia os resultados para o gráfico.

## 6.2 `GraficoComparativo.java`

### Para que serve?

Esse arquivo desenha o gráfico de barras da interface.

Ele não usa biblioteca externa. O gráfico é desenhado manualmente com `Graphics2D`.

### Declaração da classe

```java
public class GraficoComparativo extends JPanel {
```

### Explicação

`JPanel` é um painel visual do Swing.

Ao herdar de `JPanel`, essa classe pode desenhar dentro de uma área da janela.

### Cores das barras

```java
private static final Color[] CORES = {
        new Color(37, 99, 235),
        new Color(22, 163, 74),
        new Color(234, 88, 12),
        new Color(220, 38, 38)
};
```

### Explicação

Esse vetor define as cores usadas no gráfico.

Cada algoritmo recebe uma cor diferente.

### Lista de resultados

```java
private List<ResultadoSimulacao> resultados = Collections.emptyList();
```

### Explicação

Guarda os resultados que serão desenhados.

No começo, a lista é vazia porque nenhuma simulação foi executada ainda.

### Recebendo resultados

```java
public void setResultados(List<ResultadoSimulacao> resultados) {
    this.resultados = resultados == null
            ? Collections.emptyList()
            : new ArrayList<>(resultados);
    repaint();
}
```

### Explicação

Esse método recebe os resultados da simulação.

```java
repaint();
```

Pede para o Java redesenhar o painel.

### Método de desenho

```java
@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g.create();

    try {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (resultados.isEmpty()) {
            desenharEstadoVazio(g2);
            return;
        }

        desenharGrafico(g2);
    } finally {
        g2.dispose();
    }
}
```

### Explicação

Esse método é chamado automaticamente pelo Swing quando o painel precisa ser desenhado.

```java
super.paintComponent(g);
```

Limpa o painel antes de desenhar novamente.

```java
Graphics2D g2 = (Graphics2D) g.create();
```

Cria uma ferramenta de desenho mais avançada.

```java
setRenderingHint(...)
```

Ativa suavização para deixar o desenho mais bonito.

```java
if (resultados.isEmpty())
```

Se ainda não existe resultado, desenha uma mensagem inicial.

```java
desenharGrafico(g2);
```

Se existem resultados, desenha o gráfico.

### Desenho do estado vazio

```java
private void desenharEstadoVazio(Graphics2D g2) {
    String texto = "Execute uma simulação para visualizar o comparativo";
    g2.setFont(new Font("SansSerif", Font.PLAIN, 15));
    g2.setColor(new Color(100, 116, 139));
    FontMetrics metrics = g2.getFontMetrics();
    int x = (getWidth() - metrics.stringWidth(texto)) / 2;
    int y = getHeight() / 2;
    g2.drawString(texto, Math.max(16, x), y);
}
```

### Explicação

Antes de executar uma simulação, o gráfico não tem dados.

Então ele mostra uma mensagem no centro do painel.

### Desenho das barras

```java
int alturaBarra = (int) Math.round((valor / (double) maxFaltas) * areaAltura);
int x = margemEsquerda + i * espacoPorBarra + (espacoPorBarra - larguraBarra) / 2;
int y = baseY - alturaBarra;

g2.fillRoundRect(x, y, larguraBarra, alturaBarra, 8, 8);
```

### Explicação

Esse trecho calcula:

- a altura da barra;
- a posição horizontal;
- a posição vertical.

Quanto maior o número de faltas de página, maior a barra.

```java
fillRoundRect(...)
```

Desenha um retângulo arredondado preenchido.

## 7. Pasta `MDs/`

Essa pasta guarda documentos de apoio do trabalho.

Mesmo não sendo código executável, esses arquivos explicam decisões, planejamento e contexto.

## 7.1 `MDs/DocTrabalho.md`

### Para que serve?

É o enunciado do trabalho.

### O que contém?

Contém:

- título do trabalho;
- resumo;
- introdução;
- explicação dos algoritmos de substituição;
- objetivos;
- requisitos;
- metodologia;
- resultados esperados;
- detalhes de entrega.

### Importância

Esse arquivo mostra o que o professor pediu e quais regras o projeto deveria seguir.

## 7.2 `MDs/NossaEscolha.md`

### Para que serve?

Registra as escolhas feitas para implementar o projeto.

### O que contém?

Explica que foram escolhidos:

- FIFO;
- LRU;
- NFU;
- Ótimo.

Também descreve quais estruturas de dados seriam usadas em cada algoritmo.

Exemplo:

```text
FIFO usa Queue<Integer>
LRU usa LinkedList<Integer>
NFU usa HashMap<Integer, Integer>
Ótimo usa List<Integer>
```

### Importância

Esse documento justifica as decisões técnicas do projeto.

## 7.3 `MDs/Planning.md`

### Para que serve?

É o planejamento técnico detalhado.

### O que contém?

Contém:

- estrutura de pastas;
- nomes das classes;
- pseudocódigo dos algoritmos;
- ordem de implementação;
- caso de teste;
- plano da interface gráfica.

### Importância

Esse arquivo funciona como um roteiro de desenvolvimento.

Ele mostra como o projeto deveria ser construído passo a passo.

## 7.4 `MDs/prompt.md`

### Para que serve?

É um prompt preparado para orientar um agente de implementação.

### O que contém?

Contém instruções para:

- ler o planejamento;
- criar a estrutura do projeto;
- implementar os algoritmos;
- validar os resultados;
- criar a interface gráfica;
- compilar e testar.

## 8. Pasta `.idea/`

Essa pasta é criada pelo IntelliJ IDEA.

Ela não contém a lógica do simulador, mas contém informações para a IDE abrir e configurar o projeto corretamente.

## 8.1 `.idea/.gitignore`

### Para que serve?

Define quais arquivos internos da IDE devem ser ignorados pelo Git.

### Exemplos

```gitignore
/shelf/
/workspace.xml
/httpRequests/
```

### Explicação

Esses arquivos costumam guardar informações locais do computador do desenvolvedor.

Por isso, normalmente não precisam ser enviados ao repositório.

## 8.2 `.idea/misc.xml`

### Para que serve?

Guarda configurações gerais do projeto na IDE.

### Trecho

```xml
<component name="ProjectRootManager" languageLevel="JDK_26" project-jdk-name="openjdk-26">
    <output url="file://$PROJECT_DIR$/out" />
</component>
```

### Explicação

Esse trecho informa:

- qual versão do Java a IDE está usando;
- que a pasta de saída da compilação é `out/`.

## 8.3 `.idea/modules.xml`

### Para que serve?

Informa quais módulos existem no projeto.

### Trecho

```xml
<module fileurl="file://$PROJECT_DIR$/.idea/SO.iml" filepath="$PROJECT_DIR$/.idea/SO.iml" />
```

### Explicação

Diz ao IntelliJ que o projeto possui um módulo definido pelo arquivo `SO.iml`.

## 8.4 `.idea/SO.iml`

### Para que serve?

Define o módulo Java do projeto.

### Explicação simples

Um módulo é uma unidade de organização dentro da IDE.

Nesse projeto, o módulo representa o próprio projeto Java.

## 8.5 `.idea/vcs.xml`

### Para que serve?

Configura o controle de versão na IDE.

### Trecho

```xml
<mapping directory="" vcs="Git" />
```

### Explicação

Esse trecho diz ao IntelliJ que o projeto usa Git.

## 8.6 `.idea/workspace.xml`

### Para que serve?

Guarda informações locais da sessão do IntelliJ.

### O que pode conter?

Pode conter:

- arquivos abertos recentemente;
- configurações de janela;
- changelists;
- preferências locais;
- estado da interface da IDE.

### Importância

Esse arquivo não afeta a lógica do programa.

Ele apenas ajuda a IDE a lembrar o estado do ambiente de trabalho.

## 9. Pasta `out/`

A pasta `out/` contém os arquivos compilados.

Arquivos `.java` são escritos por humanos.

Arquivos `.class` são gerados pelo compilador Java.

O computador executa os arquivos `.class`, não os arquivos `.java` diretamente.

## 9.1 `out/Main.class`

### Para que serve?

É a versão compilada de `src/Main.java`.

### O que faz?

Contém o ponto de entrada do programa em formato que a JVM consegue executar.

JVM significa Java Virtual Machine, ou Máquina Virtual Java.

## 9.2 `out/gui/SimuladorGUI.class`

### Para que serve?

É a versão compilada de `src/gui/SimuladorGUI.java`.

### O que faz?

Contém a janela principal já transformada em bytecode.

Bytecode é o formato intermediário que a JVM entende.

## 9.3 `out/gui/SimuladorGUI$1.class`

### Para que serve?

É uma classe gerada automaticamente pelo compilador.

### Por que ela existe?

Ela aparece porque o código cria uma classe anônima ao montar a tabela:

```java
return new DefaultTableModel(new Object[]{"Algoritmo", "Faltas de página"}, 0) {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
};
```

### Explicação

Esse trecho cria um `DefaultTableModel` personalizado.

O método `isCellEditable` retorna `false`, impedindo que o usuário edite a tabela.

Como essa classe não tem nome próprio, o Java gera o arquivo `SimuladorGUI$1.class`.

## 9.4 `out/gui/GraficoComparativo.class`

### Para que serve?

É a versão compilada de `GraficoComparativo.java`.

### O que faz?

Contém o código do gráfico de barras já compilado.

## 9.5 `out/simulator/EntradaSimulacao.class`

### Para que serve?

É a versão compilada de `EntradaSimulacao.java`.

### O que faz?

Contém os métodos que validam e convertem a entrada do usuário.

## 9.6 `out/simulator/ResultadoSimulacao.class`

### Para que serve?

É a versão compilada de `ResultadoSimulacao.java`.

### O que faz?

Representa os resultados de cada algoritmo em formato executável pela JVM.

## 9.7 `out/simulator/SimuladorSubstituicao.class`

### Para que serve?

É a versão compilada de `SimuladorSubstituicao.java`.

### O que faz?

Executa os algoritmos e organiza os resultados em tempo de execução.

## 9.8 `out/simulator/algoritmos/AlgoritmoSubstituicao.class`

### Para que serve?

É a versão compilada da interface `AlgoritmoSubstituicao`.

### O que faz?

Define o contrato comum que os algoritmos seguem.

## 9.9 `out/simulator/algoritmos/FIFO.class`

### Para que serve?

É a versão compilada do algoritmo FIFO.

### O que faz?

Executa a lógica de remover a página mais antiga da memória.

## 9.10 `out/simulator/algoritmos/LRU.class`

### Para que serve?

É a versão compilada do algoritmo LRU.

### O que faz?

Executa a lógica de remover a página menos recentemente usada.

## 9.11 `out/simulator/algoritmos/NFU.class`

### Para que serve?

É a versão compilada do algoritmo NFU.

### O que faz?

Executa a lógica de remover a página menos frequente.

## 9.12 `out/simulator/algoritmos/Otimo.class`

### Para que serve?

É a versão compilada do algoritmo Ótimo.

### O que faz?

Executa a lógica de olhar para o futuro e remover a melhor página possível.

## 10. Pasta `.git/`

A pasta `.git/` é criada automaticamente quando o projeto usa Git.

Ela guarda:

- histórico de commits;
- configurações do repositório;
- ponteiros para branches;
- objetos internos;
- logs;
- informações de versionamento.

### Importante

Essa pasta não deve ser editada manualmente.

Ela não faz parte da lógica do simulador, mas é essencial para o controle de versão.

## 11. Como os arquivos trabalham juntos?

O fluxo do programa é este:

```text
Usuário executa o programa
        ↓
Main.java decide entre GUI e CLI
        ↓
EntradaSimulacao valida os dados
        ↓
SimuladorSubstituicao chama os algoritmos
        ↓
FIFO, LRU, NFU e Ótimo calculam as faltas
        ↓
ResultadoSimulacao guarda os resultados
        ↓
CLI imprime ou GUI mostra tabela e gráfico
```

## 12. Exemplo prático do funcionamento

Entrada:

```text
Cadeia: 7 0 1 2
Quadros: 3
```

Significa:

- o programa vai acessar as páginas 7, 0, 1 e 2;
- a memória só comporta 3 páginas ao mesmo tempo.

No começo, a memória está vazia.

Quando acessa `7`, falta página.

Quando acessa `0`, falta página.

Quando acessa `1`, falta página.

Agora a memória está cheia:

```text
[7, 0, 1]
```

Quando acessa `2`, falta página de novo.

Como só cabem 3 páginas, algum algoritmo precisa escolher qual página remover.

Cada algoritmo escolhe de um jeito diferente.

Por isso os resultados podem ser diferentes.

## 13. Diferença entre os principais tipos de arquivo

### `.java`

Arquivo de código-fonte Java.

É escrito pelo programador.

Exemplo:

```text
src/Main.java
```

### `.class`

Arquivo compilado.

É gerado automaticamente pelo compilador.

Exemplo:

```text
out/Main.class
```

### `.md`

Arquivo Markdown.

É usado para documentação em texto.

Exemplo:

```text
README.md
```

### `.xml`

Arquivo de configuração estruturada.

No projeto, aparece principalmente dentro da pasta `.idea/`.

Exemplo:

```text
.idea/misc.xml
```

## 14. Resumo final

O projeto está dividido de forma organizada:

- `Main.java` inicia o programa.
- `EntradaSimulacao.java` valida a entrada.
- `SimuladorSubstituicao.java` coordena a execução.
- `ResultadoSimulacao.java` guarda resultados.
- `AlgoritmoSubstituicao.java` define o contrato dos algoritmos.
- `FIFO.java`, `LRU.java`, `NFU.java` e `Otimo.java` fazem os cálculos.
- `SimuladorGUI.java` cria a janela.
- `GraficoComparativo.java` desenha o gráfico.
- `README.md` explica como usar.
- `MDs/` guarda documentos do trabalho.
- `.idea/` guarda configuração da IDE.
- `out/` guarda arquivos compilados.
- `.git/` guarda histórico e controle de versão.
