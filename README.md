
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
- Geração de arquivo `gatos.txt` com registros aleatórios e desordenados.
- Ordenação dos gatos por ID para permitir busca binária.
- Criação de índice auxiliar com os IDs e posições dos gatos para otimização.

### 📊 Resumo Estatístico
- Exibe contagem de gatos:
  - Totais cadastrados.
  - Quantos estão disponíveis para adoção.
  - Quantos já foram adotados.

### 📝 Logs de Buscas
- Geração de arquivos:
  - `log_busca_binaria.txt`
  - `log_busca_sequencial.txt`
- Esses logs guardam a quantidade de comparações e o tempo de execução das buscas.

---

## 🗂 Estrutura do Projeto

```
src/
├── MenuPrincipal.java          # Menu principal com todas as opções do sistema
├── CadastroGato.java           # Cadastro manual e automático de gatos
├── AdocaoGato.java             # Registro de adoções e atualização do status
├── OperacoesArquivo.java      # Listagens, contagens e manipulação de arquivos
├── Buscas.java                 # Implementação de busca sequencial e binária
├── GerenciarBases.java        # Geração e ordenação de base de dados
├── Gato.java                   # Classe modelo representando um gato
├── Adocao.java                 # Classe modelo representando uma adoção
```

### 📁 Arquivos Criados
- `gatos.txt` → Gatos cadastrados.
- `adocoes.txt` → Registros de adoções.
- `indice.txt` → Índice auxiliar para buscas.
- `log_busca_binaria.txt` / `log_busca_sequencial.txt` → Log de desempenho das buscas.

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

Este projeto foi desenvolvido como parte da disciplina **Algoritmos e Estruturas de Dados II**, com foco em:

- Manipulação de arquivos (`BufferedReader`, `BufferedWriter`, `FileReader`, `FileWriter`, `RandomAccessFile`)
- Estruturas de busca (sequencial e binária)
- Ordenação externa
- Simulação prática de um sistema real baseado em persistência de dados

---

## 👥 Autores

- **Samuell Aguiar**
- **Gabriel Roberto**
