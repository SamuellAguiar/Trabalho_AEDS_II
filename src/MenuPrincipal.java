import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPrincipal {

     public static void main(String[] args) {
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
                              GerarBasesDesordenados.gerarBaseDeDados();
                              break;
                         case 13:
                              GerarBasesOrdenadas.gerarOuOrdenarBases();
                              break;
                         case 14:
                              Buscas.buscarSequencial();
                              break;
                         case 15:
                              Buscas.buscarBinaria();
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
          System.out.println(" 8.  Listar Adoções ");
          System.out.println(" 9.  Editar uma adoção ");
          System.out.println(" 10.  Excluir uma adoção ");
          System.out.println(" 11.  Exibir resumo de adoções ");
          System.out.println("-----------------------------------------------");
          System.out.println(" Bases de dados ");
          System.out.println(" 12.  Gerar base de dados aleatória ");
          System.out.println(" 13.  Gerar base de dados ordenada ");
          System.out.println("-----------------------------------------------");
          System.out.println(" Buscas ");
          System.out.println(" 14.  Buscar gato por ID (sequencial) ");
          System.out.println(" 15.  Buscar gato por ID (binária) ");
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
