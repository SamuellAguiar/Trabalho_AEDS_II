import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class OperacoesArquivo {

     private static final String ARQUIVO_GATOS = "gatos.txt";

     public static void listarGatos() {
          System.out.println("\n--- Lista de Todos os Gatos ---");
          try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_GATOS))) {
               String linha;
               boolean vazio = true;

               while ((linha = reader.readLine()) != null) {
                    System.out.println(linha);
                    vazio = false;
               }

               if (vazio) {
                    System.out.println("Nenhum gato cadastrado.");
               }
          } catch (IOException e) {
               System.out.println("Erro ao ler o arquivo: " + e.getMessage());
          }
     }

     public static void listarGatosDisponiveisParaAdocao() {
          System.out.println("\n--- Gatos Disponíveis para Adoção ---");
          try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_GATOS))) {
               String linha;
               boolean encontrou = false;

               while ((linha = reader.readLine()) != null) {
                    String[] partes = linha.split(";");
                    if (partes.length == 6 && partes[5].equalsIgnoreCase("false")) {
                         System.out.println(linha);
                         encontrou = true;
                    }
               }

               if (!encontrou) {
                    System.out.println("Nenhum gato disponível para adoção no momento.");
               }

          } catch (IOException e) {
               System.out.println("Erro ao ler o arquivo: " + e.getMessage());
          }
     }

     public static void buscarGatoPorId() {
          Scanner scanner = new Scanner(System.in);
          System.out.print("\nDigite o ID do gato que deseja buscar: ");
          int idBuscado = scanner.nextInt();

          try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_GATOS))) {
               String linha;
               boolean encontrado = false;

               while ((linha = reader.readLine()) != null) {
                    String[] partes = linha.split(";");
                    if (partes.length >= 1 && Integer.parseInt(partes[0]) == idBuscado) {
                         System.out.println("Gato encontrado: " + linha);
                         encontrado = true;
                         break;
                    }
               }

               if (!encontrado) {
                    System.out.println("Nenhum gato encontrado com o ID informado.");
               }
          } catch (IOException e) {
               System.out.println("Erro ao ler o arquivo: " + e.getMessage());
          }
     }

     public static void contarResumoDeAdocoes() {
          int adotados = 0;
          int disponiveis = 0;

          try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_GATOS))) {
               String linha;

               while ((linha = reader.readLine()) != null) {
                    String[] partes = linha.split(";");
                    if (partes.length == 6) {
                         boolean adotado = Boolean.parseBoolean(partes[5]);
                         if (adotado) {
                              adotados++;
                         } else {
                              disponiveis++;
                         }
                    }
               }

               System.out.println("\n--- Resumo de Adoções ---");
               System.out.println("Total de gatos adotados: " + adotados);
               System.out.println("Total de gatos disponíveis: " + disponiveis);

          } catch (IOException e) {
               System.out.println("Erro ao ler o arquivo: " + e.getMessage());
          }
     }
}
