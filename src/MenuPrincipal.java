import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPrincipal {

     public static void main(String[] args) {
          Scanner scanner = new Scanner(System.in);
          int opcao = -1;

          do {
               exibirMenu();

               try {
                    System.out.print("Digite sua opção: ");
                    opcao = scanner.nextInt();
                    scanner.nextLine(); // Limpar o buffer

                    System.out.println(); // Linha em branco para separar visualmente

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
                              GeradorDeGatosDesordenados.gerarBaseDeDados();
                              break;
                         case 9:
                              GeradorDeGatosOrdenados.gerarOuOrdenarBase();
                              break;
                         case 10:
                              OperacoesArquivo.buscarGatoPorId();
                              break;
                         case 11:
                              OperacoesArquivo.contarResumoDeAdocoes();
                              break;
                         case 0:
                              System.out.println("Encerrando o sistema. Até logo!");
                              break;
                         default:
                              System.out.println("Opção inválida. Por favor, tente novamente.");
                    }

               } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida. Digite um número correspondente ao menu.");
                    scanner.nextLine(); // Limpar entrada inválida
               } catch (IOException e) {
                    System.out.println("Erro ao acessar arquivo: " + e.getMessage());
               }

               System.out.println(); // Espaço entre interações

          } while (opcao != 0);

          scanner.close();
     }

     private static void exibirMenu() {
          System.out.println("=====================================");
          System.out.println("    SISTEMA DE ADOÇÃO DE GATOS");
          System.out.println("=====================================");
          System.out.println("1.  Cadastrar gato manualmente");
          System.out.println("2.  Cadastrar gato aleatoriamente");
          System.out.println("3.  Listar todos os gatos");
          System.out.println("4.  Listar gatos disponíveis para adoção");
          System.out.println("5.  Registrar adoção de um gato");
          System.out.println("-------------------------------------");
          System.out.println("6.  Buscar gato por ID (sequencial)");
          System.out.println("7.  Buscar gato por ID (binária)");
          System.out.println("10. Buscar gato por ID (visualização)");
          System.out.println("-------------------------------------");
          System.out.println("8.  Gerar base de dados aleatória");
          System.out.println("9.  Gerar base de dados ordenada");
          System.out.println("-------------------------------------");
          System.out.println("11. Exibir resumo de adoções");
          System.out.println("0.  Sair do sistema");
          System.out.println("=====================================");
     }
}
