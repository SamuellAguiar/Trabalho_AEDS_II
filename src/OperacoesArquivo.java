import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class OperacoesArquivo {

     private static final String ARQUIVO_GATOS = "gatos.txt";

     public static void listarGatos() {
          System.out.println("===============================================");
          System.out.println("Lista de Gatos Cadastrados:");
          System.out.println("===============================================");

          try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_GATOS))) {
               String linha;
               boolean vazio = true;

               while ((linha = reader.readLine()) != null) {
                    Gato g = Gato.fromCSV(linha);
                    System.out.println(g.toString());
                    vazio = false;
               }

               if (vazio) {
                    System.out.println("Nenhum gato cadastrado.");
               }
               System.out.println("===============================================");
          } catch (IOException e) {
               System.out.println("Erro ao ler o arquivo: " + e.getMessage());
               System.out.println("===============================================");
          }
     }

     public static void listarGatosDisponiveisParaAdocao() {
          System.out.println("===============================================");
          System.out.println("       GATOS DISPONÍVEIS PARA ADOÇÃO");
          System.out.println("===============================================");

          try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_GATOS))) {
               String linha;
               boolean encontrou = false;

               while ((linha = reader.readLine()) != null) {
                    String[] partes = linha.split(";");
                    for (int i = 0; i < partes.length; i++) {
                         partes[i] = partes[i].trim();
                    }

                    // Verifica se a linha está no formato correto e se o gato não foi adotado
                    if (partes.length == 6 && partes[5].equalsIgnoreCase("false")) {
                         System.out.println("ID: " + partes[0]);
                         System.out.println("Nome: " + partes[1]);
                         System.out.println("Idade: " + partes[2] + " anos");
                         System.out.println("Raça: " + partes[3]);
                         System.out.println("Cor: " + partes[4]);
                         System.out.println("-----------------------------------------------");
                         encontrou = true;
                    }
               }

               if (!encontrou) {
                    System.out.println("Nenhum gato disponível para adoção no momento.");
               }

               System.out.println("===============================================");

          } catch (IOException e) {
               System.out.println("Erro ao ler o arquivo: " + e.getMessage());
               System.out.println("===============================================");
          }
     }

     public static void contarResumoDeAdocoes() {
          int adotados = 0;
          int disponiveis = 0;

          try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_GATOS))) {
               String linha;

               while ((linha = reader.readLine()) != null) {
                    String[] partes = linha.split(";");
                    for (int i = 0; i < partes.length; i++) {
                         partes[i] = partes[i].trim();
                    }
                    if (partes.length == 6) {
                         boolean adotado = Boolean.parseBoolean(partes[5]);
                         if (adotado) {
                              adotados++;
                         } else {
                              disponiveis++;
                         }
                    }
               }

               System.out.println("===============================================");
               System.out.println("Resumo de Adoções:");
               System.out.println("===============================================");
               System.out.println("Total de gatos cadastrados: " + (adotados + disponiveis));
               System.out.println("Total de gatos adotados: " + adotados);
               System.out.println("Total de gatos disponíveis: " + disponiveis);
          } catch (IOException e) {
               System.out.println("Erro ao ler o arquivo: " + e.getMessage());
               System.out.println("===============================================");
          }
     }

     public static void listarAdocoes() {
          final String ARQUIVO_ADOCOES = "adocoes.txt";
          System.out.println("===============================================");
          System.out.println("Lista de Adoções Registradas:");
          System.out.println("===============================================");

          try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_ADOCOES))) {
               String linha;
               boolean vazio = true;

               while ((linha = reader.readLine()) != null) {
                    Adocao a = Adocao.fromCSV(linha);
                    System.out.println(a.toString());
                    vazio = false;
               }

               if (vazio) {
                    System.out.println("Nenhuma adoção registrada.");
               }

               System.out.println("===============================================");
          } catch (IOException e) {
               System.out.println("Erro ao ler o arquivo de adoções: " + e.getMessage());
               System.out.println("===============================================");
          }
     }
}
