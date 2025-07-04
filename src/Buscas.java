import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Buscas {
     private static final String ARQUIVO_GATOS = "gatos.txt";

     public static void buscarSequencial() {
          Scanner scanner = new Scanner(System.in);
          System.out.println("===============================================");
          System.out.println("Busca Sequencial de Gatos");
          System.out.println("===============================================");
          System.out.print("Digite o ID do gato que deseja buscar: ");
          int idBusca = scanner.nextInt();

          int comparacoes = 0;
          boolean encontrado = false;
          long inicio = System.nanoTime();

          try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_GATOS))) {
               String linha;
               while ((linha = reader.readLine()) != null) {
                    comparacoes++;
                    String[] partes = linha.split(";");
                    if (Integer.parseInt(partes[0]) == idBusca) {
                         System.out.println("Gato encontrado:");
                         exibirGatoFormatado(partes);
                         encontrado = true;
                         break;
                    }
               }
          } catch (IOException e) {
               System.out.println("Erro na busca sequencial: " + e.getMessage());
          }

          long fim = System.nanoTime();
          salvarLog("Sequencial", "log_busca_sequencial.txt", idBusca, encontrado, comparacoes, fim - inicio);
     }

     public static void buscarBinaria() {
          Scanner scanner = new Scanner(System.in);
          System.out.println("===============================================");
          System.out.println("Busca Binária de Gatos (sem índice)");
          System.out.println("===============================================");
          System.out.print("Digite o ID do gato que deseja buscar: ");
          int idBusca = scanner.nextInt();

          int comparacoes = 0;
          boolean encontrado = false;
          long inicio = System.nanoTime();

          try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_GATOS))) {
               // Carregar todos os gatos em memória
               List<Gato> gatos = new ArrayList<>();
               String linha;
               while ((linha = reader.readLine()) != null) {
                    Gato g = Gato.fromCSV(linha);
                    gatos.add(g);
               }

               // Ordenar os gatos por ID
               gatos.sort((g1, g2) -> Integer.compare(g1.getId(), g2.getId()));

               // Busca binária na lista
               int left = 0, right = gatos.size() - 1;
               while (left <= right) {
                    int mid = (left + right) / 2;
                    comparacoes++;
                    Gato g = gatos.get(mid);

                    if (g.getId() == idBusca) {
                         System.out.println("Gato encontrado:");
                         System.out.println(g);
                         encontrado = true;
                         break;
                    } else if (g.getId() < idBusca) {
                         left = mid + 1;
                    } else {
                         right = mid - 1;
                    }
               }

               if (!encontrado) {
                    System.out.println("Gato com ID " + idBusca + " não encontrado.");
               }

          } catch (IOException e) {
               System.out.println("Erro na busca binária: " + e.getMessage());
          }

          long fim = System.nanoTime();
          salvarLog("Binária", "log_busca_binaria.txt", idBusca, encontrado, comparacoes, fim - inicio);
     }

     private static void salvarLog(String tipo, String arquivoLog, int id, boolean found, int comps, long tempoNano) {
          double tempoMs = tempoNano / 1_000_000.0;
          double tempoSec = tempoNano / 1_000_000_000.0;
          try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoLog, false))) {
               bw.write("=== Busca " + tipo + " ===\n");
               bw.write("ID: " + id + "\n");
               bw.write("Encontrado: " + (found ? "Sim" : "Não") + "\n");
               bw.write("Comparações: " + comps + "\n");
               bw.write(String.format("Tempo: %d ns\n", tempoNano));
               bw.write(String.format("Tempo: %.3f ms\n", tempoMs));
               bw.write(String.format("Tempo: %.3f s\n", tempoSec));
          } catch (IOException e) {
               System.out.println("Erro ao escrever log: " + e.getMessage());
          }
     }

     private static void exibirGatoFormatado(String[] p) {
          System.out.println("ID: " + p[0]);
          System.out.println("Nome: " + p[1]);
          System.out.println("Raça: " + p[2]);
          System.out.println("Idade: " + p[3] + " meses");
          System.out.println("Sexo: " + p[4]);
          System.out.println("Adotado: " + ("true".equalsIgnoreCase(p[5]) ? "Sim" : "Não"));
          System.out.println("-----------------------------------------------");
     }

     public static Gato buscarSequencialPorId(int idBusca) {
          try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_GATOS))) {
               String linha;
               while ((linha = reader.readLine()) != null) {
                    Gato g = Gato.fromCSV(linha);
                    if (g.getId() == idBusca) {
                         return g;
                    }
               }
          } catch (IOException e) {
               System.out.println("Erro na busca sequencial: " + e.getMessage());
          }
          return null;
     }

}
