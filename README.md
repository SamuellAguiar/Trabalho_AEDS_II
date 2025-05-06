# Sistema de Adoção de Gatos

Este projeto é um sistema desenvolvido em Java para gerenciar a adoção de gatos. Ele permite o cadastro, listagem, busca e adoção de gatos, além de gerar bases de dados ordenadas ou desordenadas para fins de teste e análise de desempenho.

## Funcionalidades

1. **Cadastro de Gatos**:
   - Cadastro manual de gatos, onde o usuário insere os dados como nome, raça, idade e sexo.
   - Cadastro automático de gatos com dados gerados aleatoriamente.

2. **Listagem de Gatos**:
   - Listar todos os gatos cadastrados no sistema.
   - Listar apenas os gatos disponíveis para adoção.

3. **Adoção de Gatos**:
   - Registrar a adoção de um gato, atualizando seu status no sistema e salvando os dados do adotante.

4. **Busca de Gatos**:
   - Busca sequencial por ID, verificando cada registro até encontrar o gato desejado.
   - Busca binária por ID, mais eficiente, mas requer que o arquivo esteja ordenado.
   - Busca simples por ID para visualização.

5. **Geração de Bases de Dados**:
   - Gerar uma base de dados desordenada com IDs aleatórios.
   - Gerar ou ordenar uma base de dados com IDs em ordem crescente.

6. **Resumo de Adoções**:
   - Exibir um resumo com o total de gatos adotados e disponíveis para adoção.

## Estrutura do Projeto

- **`src/`**: Contém os arquivos Java que implementam as funcionalidades do sistema.
  - `MenuPrincipal.java`: Classe principal que exibe o menu e gerencia as interações do usuário.
  - `CadastroGato.java`: Gerencia o cadastro de gatos.
  - `AdocaoGato.java`: Gerencia o processo de adoção de gatos.
  - `OperacoesArquivo.java`: Realiza operações como listagem e contagem de gatos.
  - `Buscas.java`: Implementa as buscas sequencial e binária.
  - `GeradorDeGatosOrdenados.java` e `GeradorDeGatosDesordenados.java`: Geram bases de dados para testes.
  - `Gato.java`: Representa a entidade "Gato".
  - `Adocao.java`: Representa a entidade "Adoção".

- **Arquivos de Dados**:
  - `gatos.txt`: Armazena os dados dos gatos cadastrados.
  - `adocoes.txt`: Armazena os registros de adoções realizadas.
  - `log_busca_binaria.txt` e `log_busca_sequencial.txt`: Armazenam os logs das buscas realizadas.

## Como Executar

1. Compile os arquivos Java no diretório `src/`.
2. Execute a classe `MenuPrincipal` para iniciar o sistema.
3. Siga as instruções exibidas no menu para interagir com o sistema.

## Objetivo do Trabalho

Este projeto foi desenvolvido como parte de um trabalho acadêmico para a disciplina de Algoritmos e Estruturas de Dados II. Ele visa aplicar conceitos de manipulação de arquivos, ordenação, busca e organização de dados em um contexto prático.

## Tecnologias Utilizadas

- Linguagem: Java
- Manipulação de arquivos: `BufferedReader`, `BufferedWriter`, `FileReader`, `FileWriter`, `RandomAccessFile`
- Estruturas de dados: Listas, ordenação e busca

## Autor

- **Samuell**: Desenvolvedor do sistema e responsável pela implementação das funcionalidades.