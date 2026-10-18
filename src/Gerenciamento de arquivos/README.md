# Simulador de gerenciamento de arquivos

Este projeto demonstra o uso de uma árvore genérica para representar um sistema
de arquivos em memória. Diretórios são nós que podem conter outros diretórios e
arquivos; cada arquivo armazena seu tamanho em bytes.

O programa é executado no terminal e permite criar, consultar, remover e
visualizar a árvore. Também é possível exportar a estrutura para um arquivo
Markdown.

## Estrutura

- `Arvore.java`: implementação da árvore genérica, com busca, remoção, altura,
	soma de valores e impressão.
- `No.java`: nó genérico com referência para o pai e para os filhos.
- `Item.java`: representa um diretório ou arquivo e formata tamanhos.
- `Simulador.java`: menu interativo e operações do gerenciador.

## Compilação e execução

A partir da raiz do repositório, execute:

```bash
mkdir -p bin
javac -d bin "src/Gerenciamento de arquivos"/*.java
java -cp bin arvore_generica.Simulador
```

O diretório `bin` contém os arquivos compilados e pode ser removido depois da
execução.

## Operações disponíveis

| Opção | Operação |
| ---: | --- |
| 1 | Criar um diretório no diretório atual. |
| 2 | Criar um arquivo, informando nome, destino e tamanho em bytes. |
| 3 | Selecionar o diretório atual. |
| 4 | Remover um arquivo pelo nome ou caminho. |
| 5 | Remover um diretório e toda a sua subárvore, mediante confirmação quando ele não estiver vazio. |
| 6 | Listar o conteúdo do diretório atual. |
| 7 | Exibir a árvore completa e sua altura. |
| 8 | Localizar todos os arquivos com determinado nome e mostrar seus caminhos e tamanhos. |
| 9 | Mostrar o caminho completo de todos os arquivos com determinado nome. |
| 10 | Calcular o tamanho total de um diretório, incluindo seus arquivos descendentes. |
| 11 | Exportar a árvore para um arquivo `.md`. |
| 0 | Encerrar o simulador. |

## Caminhos

Os caminhos podem ser informados de forma absoluta ou relativa:

- `/`: diretório raiz;
- `/documentos/trabalho`: caminho absoluto;
- `documentos/trabalho`: caminho relativo ao diretório atual;
- `..`: diretório pai;
- `.`: diretório atual;
- entrada vazia: mantém ou usa o diretório atual, conforme a operação.

Não é permitido criar nomes vazios, `.` ou `..`, nem nomes que contenham `/`.
Não podem existir dois itens com o mesmo nome dentro do mesmo diretório. A raiz
não pode ser removida.

## Exemplo de uso

Uma sequência possível no menu é:

1. Criar o diretório `documentos`.
2. Selecionar `/documentos`.
3. Criar `relatorio.pdf` com tamanho `2048` bytes.
4. Usar a opção 7 para visualizar a árvore e sua altura.
5. Usar a opção 11 para gerar `arvore.md`.

O arquivo exportado contém a árvore em uma lista Markdown indentada, por
exemplo:

```markdown
- `/`
	- `documentos/`
		- `relatorio.pdf (2.00 KB)`
```
