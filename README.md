# Simulador de Algoritmos de Substituição de Páginas

Simulador em Java puro para comparar faltas de página nos algoritmos FIFO, LRU, NFU e Ótimo. O projeto possui modo terminal e interface gráfica Swing com gráfico comparativo.

## Requisitos

- JDK 11 ou superior

## Compilar

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
```

No PowerShell:

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java -Path src).FullName
```

## Executar GUI

```bash
java -cp out Main
```

## Executar CLI

```bash
java -cp out Main --cli
```

Caso de validação:

```text
Cadeia: 7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1
Quadros: 3
```

Resultado esperado:

```text
FIFO  - 15 faltas de página
LRU   - 12 faltas de página
NFU   - 13 faltas de página
Ótimo - 9 faltas de página
```
