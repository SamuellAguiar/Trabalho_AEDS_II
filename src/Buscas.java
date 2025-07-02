import java.io.*;
import java.util.Scanner;

public class Buscas {
     private static final String ARQUIVO_GATOS = "gatos.txt";
     private static final String ARQUIVO_INDICE = "indice.txt";

     public static void gerarIndiceOrdenado() {
          try (RandomAccessFile raf = new RandomAccessFile(ARQUIVO_GATOS, "r");
                    BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_INDICE, false))) {
               long posicao = 0;
               String linha;
               while ((linha = raf.readLine()) != null) {
                    String[] partes = linha.split(";", 2);
                    writer.write(partes[0] + ";" + posicao);
                    writer.newLine();
                    posicao = raf.getFilePointer();
               }
          } catch (IOException e) {
               System.out.println("Erro ao gerar índice: " + e.getMessage());
               return;
          }
          try {
               GerenciarBases.bubbleSortEmDisco(ARQUIVO_INDICE);
          } catch (IOException e) {
               System.out.println("Erro ao ordenar índice: " + e.getMessage());
          }
     }

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
          System.out.println("Busca Binária de Gatos (com índice em disco)");
          System.out.println("===============================================");
          System.out.print("Digite o ID do gato que deseja buscar: ");
          int idBusca = scanner.nextInt();

          gerarIndiceOrdenado();

          int comparacoes = 0;
          boolean encontrado = false;
          long posicaoGato = -1;
          long inicio = System.nanoTime();

          try {
               int totalLinhas = countLines(ARQUIVO_INDICE);
               int left = 0, right = totalLinhas - 1;

               while (left <= right) {
                    int mid = (left + right) / 2;
                    String linhaIndice = readLineAt(ARQUIVO_INDICE, mid);
                    if (linhaIndice == null)
                         break;
                    comparacoes++;
                    String[] partes = linhaIndice.split(";");
                    int idAtual = Integer.parseInt(partes[0]);

                    if (idAtual == idBusca) {
                         posicaoGato = Long.parseLong(partes[1]);
                         encontrado = true;
                         break;
                    } else if (idAtual < idBusca) {
                         left = mid + 1;
                    } else {
                         right = mid - 1;
                    }
               }

               if (encontrado) {
                    try (RandomAccessFile raf = new RandomAccessFile(ARQUIVO_GATOS, "r")) {
                         raf.seek(posicaoGato);
                         String linhaGato = raf.readLine();
                         System.out.println("Gato encontrado:");
                         exibirGatoFormatado(linhaGato.split(";"));
                    }
               } else {
                    System.out.println("Gato com ID " + idBusca + " não encontrado.");
               }

          } catch (IOException e) {
               System.out.println("Erro na busca binária: " + e.getMessage());
          }

          long fim = System.nanoTime();
          salvarLog("Binária", "log_busca_binaria.txt", idBusca, encontrado, comparacoes, fim - inicio);
     }

     private static int countLines(String file) throws IOException {
          try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
               int count = 0;
               while (reader.readLine() != null)
                    count++;
               return count;
          }
     }

     private static String readLineAt(String file, int lineNumber) throws IOException {
          try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
               String line;
               int index = 0;
               while ((line = reader.readLine()) != null) {
                    if (index++ == lineNumber)
                         return line;
               }
               return null;
          }
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

}
