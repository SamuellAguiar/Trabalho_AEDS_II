import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPrincipal {

     public static void main(String[] args) {
          Scanner scanner = new Scanner(System.in);
          int opcao = -1;

          do {
               limparTela(); // Limpa o terminal antes de mostrar o menu
               exibirMenu();

               try {
                    System.out.print("Digite sua opção: ");
                    opcao = scanner.nextInt();
                    scanner.nextLine(); // Limpar o buffer

                    System.out.println("\n-------------------------------------");

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
                              AdocaoGato.adotarGato();
                              break;
                         case 6:
                              Buscas.buscarSequencial();
                              break;
                         case 7:
                              Buscas.buscarBinaria();
                              break;
                         case 8:
                              OperacoesArquivo.buscarGatoPorId();
                              break;
                         case 9:
                              GeradorDeGatosDesordenados.gerarBaseDeDados();
                              break;
                         case 10:
                              GeradorDeGatosOrdenados.gerarOuOrdenarBase();
                              break;
                         case 11:
                              OperacoesArquivo.contarResumoDeAdocoes();
                              break;
                         case 0:
                              System.out.println("\n===============================================");
                              System.out.println(" Obrigado por usar o Sistema de Adoção de Gatos!");
                              System.out.println(" Até logo.");
                              System.out.println("===============================================");
                              break;

                         default:
                              System.out.println("Opção inválida. Por favor, escolha uma opção do menu.");
                    }

               } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida. Digite apenas números correspondentes ao menu.");
                    scanner.nextLine(); // Limpar entrada inválida
               } catch (IOException e) {
                    System.out.println("Erro ao acessar arquivo: " + e.getMessage());
               }

               System.out.println("\nPressione Enter para continuar...");
               scanner.nextLine(); // Espera confirmação do usuário
          } while (opcao != 0);

          scanner.close();
     }

     private static void exibirMenu() {
          System.out.println("===============================================");
          System.out.println("          SISTEMA DE ADOÇÃO DE GATOS");
          System.out.println("===============================================");
          System.out.println(" 1.  Cadastrar gato manualmente");
          System.out.println(" 2.  Cadastrar gato aleatoriamente");
          System.out.println(" 3.  Listar todos os gatos");
          System.out.println(" 4.  Listar gatos disponíveis para adoção");
          System.out.println(" 5.  Registrar adoção de um gato");
          System.out.println("-----------------------------------------------");
          System.out.println(" 6.  Buscar gato por ID (sequencial)");
          System.out.println(" 7.  Buscar gato por ID (binária)");
          System.out.println(" 8.  Buscar gato por ID (visualização)");
          System.out.println("-----------------------------------------------");
          System.out.println(" 9.  Gerar base de dados aleatória");
          System.out.println(" 10.  Gerar base de dados ordenada");
          System.out.println("-----------------------------------------------");
          System.out.println("11.  Exibir resumo de adoções");
          System.out.println(" 0.  Sair do sistema");
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
               // Caso falhe, apenas imprime várias linhas em branco
               for (int i = 0; i < 50; i++) {
                    System.out.println();
               }
          }
     }
}
