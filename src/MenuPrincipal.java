import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPrincipal {
     public static void main(String[] args) throws IOException {
          Scanner scanner = new Scanner(System.in);
          int opcao = -1;

          do {
               limparTela();
               exibirMenu();

               try {
                    System.out.print("Digite sua opção: ");
                    opcao = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("-------------------------------------");

                    switch (opcao) {
                         case 1:
                              CadastroGato.cadastrarGatoManual();
                              break;
                         case 2:
                              CadastroGato.cadastrarGatoAleatorio();
                              break;
                         case 3:
                              OperacoesArquivo.listarGatos();
                              break;
                         case 4:
                              OperacoesArquivo.listarGatosDisponiveisParaAdocao();
                              break;
                         case 5:
                              CadastroGato.editarGato();
                              break;
                         case 6:
                              CadastroGato.excluirGato();
                              break;
                         case 7:
                              AdocaoGato.adotarGato();
                              break;
                         case 8:
                              OperacoesArquivo.listarAdocoes();
                              break;
                         case 9:
                              OperacoesArquivo.editarAdocao();
                              break;
                         case 10:
                              OperacoesArquivo.excluirAdocao();
                              break;
                         case 11:
                              OperacoesArquivo.contarResumoDeAdocoes();
                              break;
                         case 12:
                              GerenciarBases.escolha();
                              break;
                         case 13:
                              GerenciarBases.verificarOuGerarOuDesordenar();
                              break;
                         case 14:
                              Buscas.buscarSequencial();
                              break;
                         case 15:
                              Buscas.buscarBinaria();
                              break;
                         case 16:
                              SelecaoNaturalArquivo.gerarParticoes();
                              break;
                         case 17:
                              ArvoreDeVencedores.intercalar();
                              break;
                         case 18:
                              inicializarHash();
                              inserirNaHash(scanner);
                              break;
                         case 19:
                              buscarNaHash(scanner);
                              break;
                         case 20:
                              removerDaHash(scanner);
                              break;
                         case 21:
                              imprimirHash();
                              break;
                         case 0:
                              System.out.println(" Obrigado por usar o Sistema de Adoção de Gatos!");
                              System.out.println(" Até logo.");
                              break;
                         default:
                              System.out.println("===============================================");
                              System.out.println("Opção inválida. Por favor, escolha uma opção do menu.");
                              System.out.println("===============================================");
                    }

               } catch (InputMismatchException e) {
                    System.out.println("===============================================");
                    System.out.println("Entrada inválida. Digite apenas números correspondentes ao menu.");
                    System.out.println("===============================================");
                    scanner.nextLine();
               } catch (IOException e) {
                    System.out.println("===============================================");
                    System.out.println("Erro ao acessar arquivo: " + e.getMessage());
                    System.out.println("===============================================");
               }

               System.out.println("-------------------------------------");
               System.out.println("Pressione Enter para continuar...");
               System.out.println("-------------------------------------");
               scanner.nextLine();
          } while (opcao != 0);
          scanner.close();
     }

     private static void exibirMenu() {
          System.out.println("===============================================");
          System.out.println("          SISTEMA DE ADOÇÃO DE GATOS");
          System.out.println("===============================================");
          System.out.println(" Operações disponíveis ");
          System.out.println(" 1.  Cadastrar gato manualmente ");
          System.out.println(" 2.  Cadastrar gato aleatoriamente ");
          System.out.println(" 3.  Listar todos os gatos ");
          System.out.println(" 4.  Listar gatos disponíveis para adoção ");
          System.out.println(" 5.  Editar um gato");
          System.out.println(" 6.  Excluir um gato ");
          System.out.println(" 7.  Registrar adoção de um gato ");
          System.out.println(" 8.  Listar adoções ");
          System.out.println(" 9.  Editar uma adoção ");
          System.out.println(" 10. Excluir uma adoção ");
          System.out.println(" 11. Exibir resumo de adoções ");
          System.out.println("-----------------------------------------------");
          System.out.println(" Bases de dados ");
          System.out.println(" 12. Verificar/Gerar base de dados ");
          System.out.println(" 13. Ordenar/Desordenar base de dados ");
          System.out.println("-----------------------------------------------");
          System.out.println(" Buscas ");
          System.out.println(" 14. Buscar gato por ID (sequencial) ");
          System.out.println(" 15. Buscar gato por ID (binária) ");
          System.out.println("-----------------------------------------------");
          System.out.println(" Ordenação e Intercalação");
          System.out.println(" 16. Selecao Natural ");
          System.out.println(" 17. Arvore de vencedores ");
          System.out.println("-----------------------------------------------");
          System.out.println(" Hashing ");
          System.out.println(" 18. Inserir gato na hash ");
          System.out.println(" 19. Buscar gato na hash ");
          System.out.println(" 20. Remover gato da hash ");
          System.out.println(" 21. Imprimir tabela hash ");
          System.out.println("-----------------------------------------------");
          System.out.println(" 0.  Sair do sistema ");
          System.out.println("===============================================");
     }

     private static void limparTela() {
          try {
               String os = System.getProperty("os.name");
               if (os.contains("Windows")) {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
               } else {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
               }
          } catch (Exception e) {
               for (int i = 0; i < 50; i++) {
                    System.out.println();
               }
          }
     }

     // ===============================
     // Funções auxiliares para a Hash
     // ===============================

     private static Hash hash; // instância única da hash

     // Inicializar hash com base no tamanho do arquivo gatos.txt
     private static void inicializarHash() throws IOException {
          if (hash != null)
               return; // já inicializada

          // Lê o número de linhas no arquivo gatos.txt
          int tamanhoBase = 0;
          try (BufferedReader br = new BufferedReader(new FileReader("gatos.txt"))) {
               while (br.readLine() != null) {
                    tamanhoBase++;
               }
          }

          if (tamanhoBase == 0) {
               System.out.println("Arquivo gatos.txt está vazio. Não é possível inicializar a hash.");
               return;
          }

          // Cria a hash com base no tamanho do arquivo
          hash = new Hash(tamanhoBase);
          System.out.println("Hash inicializada automaticamente para base de tamanho " + tamanhoBase);
     }

     private static void inserirNaHash(Scanner sc) throws IOException {
          if (hash == null) {
               inicializarHash(); // inicializa automaticamente se ainda não estiver
          }
          if (hash == null)
               return; // segurança caso o arquivo esteja vazio

          System.out.print("Digite o ID do gato para inserir na hash: ");
          int id = sc.nextInt();
          sc.nextLine();

          Gato gato = OperacoesArquivo.buscarGatoPorId(id);
          if (gato != null) {
               hash.inserirGato(gato);
          } else {
               System.out.println("Gato não encontrado no arquivo base (gatos.txt).");
          }
     }

     private static void buscarNaHash(Scanner sc) throws IOException {
          if (hash == null) {
               inicializarHash();
          }
          if (hash == null)
               return;

          System.out.print("Digite o ID do gato para buscar na hash: ");
          int id = sc.nextInt();
          sc.nextLine();

          Gato resultado = hash.buscar(id);
          if (resultado != null) {
               System.out.println("Gato encontrado: " + resultado);
          } else {
               System.out.println("Gato não encontrado na hash.");
          }
     }

     private static void removerDaHash(Scanner sc) throws IOException {
          if (hash == null) {
               inicializarHash();
          }
          if (hash == null)
               return;

          System.out.print("Digite o ID do gato para remover da hash: ");
          int id = sc.nextInt();
          sc.nextLine();

          boolean removido = hash.remover(id);
          if (removido) {
               System.out.println("Gato removido da hash com sucesso.");
          } else {
               System.out.println("Gato não encontrado na hash.");
          }
     }

     private static void imprimirHash() throws IOException {
          if (hash == null) {
               inicializarHash();
          }
          if (hash == null)
               return;

          hash.imprimirTabela();
     }
}