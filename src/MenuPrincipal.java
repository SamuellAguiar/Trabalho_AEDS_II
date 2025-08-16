import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPrincipal {

     public static void main(String[] args) throws IOException {
          Scanner scanner = new Scanner(System.in);
          ArvoreBPlus bpt = new ArvoreBPlus("arvore.txt", "gatos.txt");
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
                              inserirGatoNaArvore(bpt, scanner);
                              break;
                         case 19:
                              removerGatoDaArvore(bpt, scanner);
                              break;
                         case 20:
                              buscarGatoNaArvore(bpt, scanner);
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

     private static void inserirGatoNaArvore(ArvoreBPlus bpt, Scanner scanner) throws IOException {
          System.out.print("Digite o ID do gato a ser indexado na Árvore B+: ");
          int id = scanner.nextInt();
          scanner.nextLine();

          // Lógica para encontrar a posição do gato no arquivo "gatos.txt"
          long pos = encontrarPosicaoDoGato("gatos.txt", id);

          if (pos != -1) {
               bpt.inserir(id, pos);
               System.out.println("Gato de ID " + id + " inserido na Árvore B+ com sucesso.");
          } else {
               System.out.println("ERRO: Gato com ID " + id + " não encontrado na base de dados principal.");
          }
     }

     private static void removerGatoDaArvore(ArvoreBPlus bpt, Scanner scanner) throws IOException {
          System.out.print("Digite o ID do gato a ser removido da Árvore B+: ");
          int id = scanner.nextInt();
          scanner.nextLine();

          bpt.remover(id); // O método de remoção já foi corrigido anteriormente
          System.out.println("Tentativa de remoção do gato com ID " + id + " da árvore concluída.");
     }

     private static void buscarGatoNaArvore(ArvoreBPlus bpt, Scanner scanner) throws IOException {
          System.out.print("Digite o ID do gato a ser buscado na Árvore B+: ");
          int id = scanner.nextInt();
          scanner.nextLine();

          Gato gatoEncontrado = bpt.buscar(id);

          if (gatoEncontrado != null) {
               System.out.println("Gato encontrado:");
               System.out.println(gatoEncontrado.toString());
          } else {
               System.out.println("Gato com ID " + id + " não encontrado na Árvore B+.");
          }
     }

     // Função utilitária para encontrar a posição de um gato no arquivo principal
     private static long encontrarPosicaoDoGato(String nomeArquivo, int idBusca) throws IOException {
          try (RandomAccessFile raf = new RandomAccessFile(nomeArquivo, "r")) {
               long pos = raf.getFilePointer();
               String linha;
               while ((linha = raf.readLine()) != null) {
                    String[] parts = linha.split(";");
                    int idAtual = Integer.parseInt(parts[0]);
                    if (idAtual == idBusca) {
                         return pos; // Retorna a posição do início da linha
                    }
                    pos = raf.getFilePointer();
               }
          }
          return -1; // Retorna -1 se não encontrar
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
          System.out.println(" Árvore B+");
          System.out.println(" 18. Inserir gato na árvore B+");
          System.out.println(" 19. Remover gato da árvore B+");
          System.out.println(" 20. Buscar gato na árvore B+");
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
}
