Aqui está o prompt otimizado para o agente de implementação:

---

**Prompt:**

> Você é um agente de implementação Java. Neste workspace existem 3 arquivos de contexto:
>
> - DocTrabalho.md — enunciado do trabalho acadêmico
> - NossaEscolha.md — decisões de design (algoritmos escolhidos e estruturas de dados)
> - Planning.md — **planejamento técnico detalhado com pseudocódigo, estrutura de pacotes, caso de teste e ordem de implementação**
>
> **Leia o arquivo Planning.md por completo antes de iniciar qualquer implementação.** Ele contém toda a especificação que você precisa: pseudocódigo de cada algoritmo, estruturas de dados, armadilhas a evitar, caso de teste com resultados esperados e a arquitetura de classes.
>
> **Sua tarefa é implementar a Fase 1 (CLI) e a Fase 2 (Swing GUI) do simulador, seguindo rigorosamente a tabela de ordem de implementação do Planning.md (etapas 1 a 10).**
>
> Regras:
> 1. Crie a estrutura de pastas e arquivos Java conforme definido no Planning.md (`src/Main.java`, `src/simulator/`, `src/simulator/algoritmos/`, `src/gui/`).
> 2. Implemente cada algoritmo como classe separada implementando a interface `AlgoritmoSubstituicao`.
> 3. Após implementar cada algoritmo, compile e execute o caso de teste de validação definido no Planning.md (cadeia `7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1`, 3 quadros) e confirme que FIFO=15, LRU=12, Ótimo=9 faltas de página. Só prossiga se os valores baterem.
> 4. Não use bibliotecas externas — apenas Java puro (Collections API + Swing).
> 5. Codifique em UTF-8.
> 6. O `Main.java` deve suportar dois modos: sem argumentos abre a GUI Swing; com `--cli` executa no terminal.
> 7. Na GUI, implemente o gráfico de barras comparativo com `Graphics2D` conforme descrito no Planning.md.
> 8. Ao finalizar, execute a compilação e o teste CLI completo para garantir que tudo funciona.

---

Esse prompt é autocontido — o agente vai encontrar tudo que precisa no Planning.md que já está no workspace. Ele sabe a ordem, os critérios de validação e as restrições técnicas.