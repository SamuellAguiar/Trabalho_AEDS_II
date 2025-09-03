# 🐾 Sistema de Adoção de Gatos  

Este projeto consiste no desenvolvimento de um sistema em **Java** para gerenciamento de gatos e processos de adoção, com ênfase no uso de **manipulação de arquivos** como meio de persistência de dados.  

O sistema foi projetado para fins **acadêmicos**, visando aplicar conceitos de **algoritmos de ordenação interna e externa**, **estruturas de busca**, **tabela hash** e operações de **entrada/saída em arquivos** (`.txt` e `.dat`).  

---

## ✅ Funcionalidades Implementadas  

### 📋 Cadastro de Gatos  
- **Cadastro manual**: Inserção de dados pelo usuário (ID, nome, raça, idade, sexo e status – disponível/adotado).  
- **Cadastro automático**: Geração aleatória de registros para testes e experimentação.  

### 🔍 Operações de Busca  
- **Busca sequencial**: Percorre sequencialmente os registros até localizar o gato pelo identificador.  
- **Busca binária**: Executada sobre arquivos previamente ordenados por ID, proporcionando maior eficiência.  
- **Busca direta**: Visualização rápida dos dados de um gato a partir do ID informado.  

### 📃 Listagem  
- Exibição de todos os gatos cadastrados.  
- Exibição apenas dos gatos disponíveis para adoção.  

### 🐱 Adoção de Gatos  
- Registro do processo de adoção.  
- Atualização automática do status do gato para “adotado”.  
- Armazenamento dos dados do adotante vinculados ao registro de adoção.  

### 🧪 Geração e Gerenciamento de Bases  
- Criação automática de `gatos.txt`.  
- Métodos de ordenação implementados:  
  - **Interna**: BubbleSort.  
  - **Externa**: Seleção Natural com Intercalação por Árvore de Vencedores.  
- Utilização de **Tabela Hash** para organização e acesso eficiente aos registros.  

### 📊 Estatísticas do Sistema  
- Número total de gatos cadastrados.  
- Quantidade de gatos disponíveis.  
- Quantidade de gatos já adotados.  

### 📝 Geração de Logs  
- `log_busca_binaria.txt`: Detalhes de comparações e tempo de execução da busca binária.  
- `log_busca_sequencial.txt`: Detalhes de comparações e tempo de execução da busca sequencial.  
- `Log_Selecao_Natural.txt`: Informações sobre partições geradas e desempenho da ordenação externa.  
- `Log_Arvore_Vencedores.txt`: Estatísticas da etapa de intercalação.  

---

## 🗂 Estrutura do Projeto  

```
src/
├── MenuPrincipal.java # Interface principal de interação (menu)
├── CadastroGato.java # Cadastro manual e automático de gatos
├── AdocaoGato.java # Controle de processos de adoção
├── Adocao.java # Classe modelo: entidade Adoção
├── Gato.java # Classe modelo: entidade Gato
├── OperacoesArquivo.java # Rotinas de manipulação de arquivos
├── Buscas.java # Implementações de busca (sequencial e binária)
├── GerenciarBases.java # Rotinas de ordenação externa
├── Hash.java # Estrutura de Tabela Hash
├── SelecaoNaturalArquivo.java # Ordenação externa: Seleção Natural
├── ArvoreDeVencedores.java # Ordenação externa: Intercalação por Árvore de Vencedores
```

---

## 📁 Arquivos Gerados  

- **Bases principais**  
  - `gatos.txt`: Lista de gatos cadastrados.  
  - `adocoes.txt`: Registro de adoções realizadas.  
  - `hash.dat`: Arquivo de armazenamento da Tabela Hash.  

- **Arquivos ordenados**  
  - `gatos_ordenado.txt`: Lista de gatos ordenada por ID.  
  - `gatos_BubbleSort.txt`: Ordenação interna de gatos via BubbleSort.  
  - `adocoes_BubbleSort.txt`: Ordenação interna de adoções via BubbleSort.  

- **Logs de execução**  
  - `log_busca_binaria.txt`  
  - `log_busca_sequencial.txt`  
  - `Log_Selecao_Natural.txt`  
  - `Log_Arvore_Vencedores.txt`  

---

## 🎯 Objetivo Acadêmico  

Este projeto foi desenvolvido como requisito da disciplina **Algoritmos e Estruturas de Dados II**, com os seguintes objetivos:  

- Aplicar técnicas de **manipulação de arquivos** em Java (uso de `BufferedReader`, `BufferedWriter`, `RandomAccessFile`, entre outros).  
- Implementar **estruturas de busca** (sequencial e binária).  
- Explorar **algoritmos de ordenação** internos e externos.  
- Simular um sistema real de gerenciamento, garantindo **persistência de dados** sem dependência de banco de dados.  
- Aplicar estruturas avançadas, como **Tabela Hash** e **Árvore de Vencedores**, em cenários práticos.  

---

## 👥 Autores  

- **Samuell Aguiar** — [samuell.aguiar@aluno.ufop.edu.br](mailto:samuell.aguiar@aluno.ufop.edu.br)  
- **Gabriel Roberto** — [gabriel.candido@aluno.ufop.edu.br](mailto:gabriel.candido@aluno.ufop.edu.br)  

