import java.io.IOException;
import java.util.Scanner;

public class MenuPrincipal {

     public static void main(String[] args) {
          Scanner scanner = new Scanner(System.in);
          int opcao;

          do {
               System.out.println("=== SISTEMA DE ADOÇÃO DE GATOS ===");
               System.out.println("1. Cadastrar gato manualmente");
               System.out.println("2. Cadastrar gato aleatoriamente");
               System.out.println("3. Listar todos os gatos");
               System.out.println("4. Listar gatos disponíveis para adoção");
               System.out.println("5. Registrar adoção de um gato");
               System.out.println("6. Buscar gato por ID (busca sequencial)");
               System.out.println("7. Buscar gato por ID (busca binária)");
               System.out.println("8. Gerar base de dados de gatos aleatórios");
               System.out.println("9. Gerar base de dados de gatos ordenados");
               System.out.println("10. Buscar gato por ID (visualização)");
               System.out.println("11. Exibir resumo de adoções");
               System.out.println("0. Sair");
               System.out.print("Escolha uma opção: ");
               opcao = scanner.nextInt();
               scanner.nextLine(); // limpa o buffer

               try {
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
                              System.out.println("Encerrando...");
                              break;
                         default:
                              System.out.println("Opção inválida. Tente novamente.");
                    }
               } catch (IOException e) {
                    System.out.println("Erro de arquivo: " + e.getMessage());
               }

               System.out.println();

          } while (opcao != 0);

          scanner.close();
     }
}
