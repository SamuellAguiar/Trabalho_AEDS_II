
# 🐾 Sistema de Adoção de Gatos

Este projeto é um sistema em Java que simula um processo completo de gerenciamento e adoção de gatos, utilizando manipulação de arquivos. Ele é voltado para fins educacionais, aplicando conceitos de algoritmos, estruturas de dados e operações de entrada/saída com arquivos `.txt`.

---

## ✅ Funcionalidades

### 📋 Cadastro de Gatos
- **Manual**: Entrada de dados como ID, nome, raça, idade, sexo e status (disponível ou adotado).
- **Automático**: Geração aleatória de gatos para testes, com preenchimento automático de dados.

### 🔍 Busca de Gatos
- **Busca sequencial**: Lê todos os registros até encontrar o gato pelo ID.
- **Busca binária**: Exige arquivo ordenado por ID para realizar uma busca mais eficiente.
- **Busca simples**: Visualiza rapidamente os dados de um gato com base no ID.

### 📃 Listagem de Gatos
- Lista completa de todos os gatos cadastrados.
- Lista apenas dos gatos disponíveis para adoção.

### 🐱 Adoção de Gatos
- Registra a adoção de um gato.
- Atualiza o status do gato para "adotado".
- Solicita e armazena dados do adotante.

### 🧪 Geração e Gerenciamento de Bases
- Geração automática de `gatos.txt`.
- Ordenação por ID com:
  - BubbleSort (interno).
  - Seleção Natural + Intercalação com Árvore de Vencedores (externo).


### 📊 Estatísticas
- Total de gatos cadastrados.
- Quantos estão disponíveis.
- Quantos já foram adotados.

### 📝 Logs de Execução
- `log_busca_binaria.txt`: Comparações e tempo da busca binária.
- `log_busca_sequencial.txt`: Comparações e tempo da busca sequencial.
- `Log_Selecao_Natural.txt`: Partições e tempo da ordenação externa.
- `Log_Arvore_Vencedores.txt`: Tempo de intercalação final.

---

## 🗂 Estrutura do Projeto

```
src/
├── MenuPrincipal.java # Menu interativo principal
├── CadastroGato.java # Cadastro manual e automático
├── AdocaoGato.java # Registro e controle de adoções
├── OperacoesArquivo.java # Manipulação geral dos arquivos
├── Buscas.java # Implementações de busca
├── GerenciarBases.java # Geração e ordenação externa de bases
├── Gato.java # Classe modelo Gato
├── Adocao.java # Classe modelo Adoção

```

## 📁 Arquivos Gerados

- `gatos.txt`: Lista de gatos cadastrados.
- `adocoes.txt`: Registros de adoções.
- Logs: `log_busca_binaria.txt`, `log_busca_sequencial.txt`, `Log_Selecao_Natural.txt`, `Log_Arvore_Vencedores.txt`

---

## ▶️ Como Executar

1. Compile todos os arquivos Java dentro do diretório `src/`:

   ```bash
   javac src/*.java
   ```

2. Execute a classe principal:

   ```bash
   java -cp src MenuPrincipal
   ```

3. Utilize o menu interativo exibido no terminal para utilizar o sistema.

---

## 🎯 Objetivo Acadêmico
Projeto desenvolvido para a disciplina Algoritmos e Estruturas de Dados II com foco em:

Manipulação de arquivos (BufferedReader, RandomAccessFile, etc.).

Estruturas de busca (sequencial e binária).

Ordenação interna e externa.

Simulação de um sistema real com persistência.

---

## 👥 Autores

- **Samuell Aguiar** — [samuell.aguiar@aluno.ufop.edu.br](mailto:samuell.aguiar@aluno.ufop.edu.br)

- **Gabriel Roberto** — [gabriel.candido@aluno.ufop.edu.br](mailto:gabriel.candido@aluno.ufop.edu.br)
