import java.io.*;
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
          long inicioTempo = System.nanoTime();

          try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_GATOS))) {
               String linha;
               while ((linha = reader.readLine()) != null) {
                    comparacoes++;
                    String[] partes = linha.split(";");
                    int id = Integer.parseInt(partes[0]);

                    if (id == idBusca) {
                         System.out.println("===============================================");
                         System.out.println("Gato encontrado:");
                         System.out.println("===============================================");
                         System.out.println(linha);
                         encontrado = true;
                         break;
                    }
               }
          } catch (IOException e) {
               System.out.println("===============================================");
               System.out.println("Erro ao ler o arquivo: " + e.getMessage());
          }

          long fimTempo = System.nanoTime();
          long duracao = fimTempo - inicioTempo;

          if (!encontrado) {
               System.out.println("===============================================");
               System.out.println("Gato com ID " + idBusca + " não encontrado.");
          }

          salvarLog("Sequencial", "log_busca_sequencial.txt", idBusca, encontrado, comparacoes, duracao);
     }

     public static void buscarBinaria() {
          Scanner scanner = new Scanner(System.in);
          System.out.println("===============================================");
          System.out.println("Busca Binária de Gatos (arquivo deve estar ordenado por ID)");
          System.out.println("===============================================");
          System.out.println("A busca binária requer que o arquivo esteja ordenado por ID.");
          System.out.println("===============================================");
          System.out.print("Digite o ID do gato que deseja buscar: ");
          int idBusca = scanner.nextInt();

          int comparacoes = 0;
          boolean encontrado = false;
          long inicioTempo = System.nanoTime();

          try (RandomAccessFile raf = new RandomAccessFile(ARQUIVO_GATOS, "r")) {
               long inicio = 0;
               long fim = raf.length();

               while (inicio <= fim) {
                    long meio = (inicio + fim) / 2;

                    raf.seek(meio);
                    if (meio != 0)
                         raf.readLine();

                    long posicao = raf.getFilePointer();
                    String linha = raf.readLine();

                    if (linha == null)
                         break;

                    comparacoes++;
                    String[] partes = linha.split(";");
                    int id = Integer.parseInt(partes[0]);

                    if (id == idBusca) {
                         System.out.println("===============================================");
                         System.out.println("Gato encontrado: ");
                         System.out.println(linha);
                         System.out.println("===============================================");
                         encontrado = true;
                         break;
                    } else if (id < idBusca) {
                         inicio = raf.getFilePointer();
                    } else {
                         fim = posicao - 1;
                    }
               }
          } catch (IOException e) {
               System.out.println("===============================================");
               System.out.println("Erro ao acessar o arquivo: " + e.getMessage());
               System.out.println("===============================================");
          }

          long fimTempo = System.nanoTime();
          long duracao = fimTempo - inicioTempo;

          if (!encontrado) {
               System.out.println("===============================================");
               System.out.println(
                         "Gato com ID " + idBusca + " não encontrado (ou arquivo não está ordenado corretamente).");
          }

          salvarLog("Binária", "log_busca_binaria.txt", idBusca, encontrado, comparacoes, duracao);
     }

     private static void salvarLog(String tipoBusca, String nomeArquivo, int id, boolean encontrado, int comparacoes,
               long tempoNano) {
          try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeArquivo, false))) {
               bw.write("=== Busca " + tipoBusca + " ===\n");
               bw.write("ID buscado: " + id + "\n");
               bw.write("Encontrado: " + (encontrado ? "Sim" : "Não") + "\n");
               bw.write("Comparações: " + comparacoes + "\n");
               bw.write("Tempo (ns): " + tempoNano + "\n");
               bw.write("Tempo (ms): " + (tempoNano / 1_000_000.0) + "\n");
               bw.write("-----------------------------\n");
          } catch (IOException e) {
               System.out.println("Erro ao escrever no log: " + e.getMessage());
               System.out.println("===============================================");
          }
     }

}
