import java.io.*;
import java.util.Scanner;

public class Buscas {
     private static final String ARQUIVO_GATOS = "gatos.txt";
     private static final String ARQUIVO_INDICE = "indice.txt";

     public static void gerarIndiceOrdenado() {
          try (
                    BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_GATOS));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_INDICE))) {
               RandomAccessFile raf = new RandomAccessFile(ARQUIVO_GATOS, "r");
               long posicao = 0;
               String linha;

               File temp = new File("indice_temp.txt");
               BufferedWriter tempWriter = new BufferedWriter(new FileWriter(temp));

               while ((linha = raf.readLine()) != null) {
                    String[] partes = linha.split(";");
                    if (partes.length > 0) {
                         tempWriter.write(partes[0] + ";" + posicao + "\n");
                    }
                    posicao = raf.getFilePointer();
               }

               tempWriter.close();

               // Agora ordenamos o arquivo temporário e salvamos no final
               ordenarArquivoIndice(temp, new File(ARQUIVO_INDICE));
               temp.delete();

          } catch (IOException e) {
               System.out.println("Erro ao gerar índice: " + e.getMessage());
          }
     }

     // Ordena o arquivo índice em disco (por ID crescente)
     private static void ordenarArquivoIndice(File origem, File destino) throws IOException {
          // Lê as linhas
          BufferedReader reader = new BufferedReader(new FileReader(origem));
          PrintWriter writer = new PrintWriter(new FileWriter(destino));
          String[] linhas = reader.lines().toArray(String[]::new);
          reader.close();

          // Ordena por ID
          java.util.Arrays.sort(linhas, (a, b) -> {
               int idA = Integer.parseInt(a.split(";")[0]);
               int idB = Integer.parseInt(b.split(";")[0]);
               return Integer.compare(idA, idB);
          });

          // Escreve no destino
          for (String linha : linhas) {
               writer.println(linha);
          }
          writer.close();
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
                         exibirGatoFormatado(partes);
                         encontrado = true;
                         break;
                    }
               }
          } catch (IOException e) {
               System.out.println("Erro ao ler o arquivo: " + e.getMessage());
          }

          long fimTempo = System.nanoTime();
          salvarLog("Sequencial", "log_busca_sequencial.txt", idBusca, encontrado, comparacoes, fimTempo - inicioTempo);
     }

     public static void buscarBinaria() {
          Scanner scanner = new Scanner(System.in);
          System.out.println("===============================================");
          System.out.println("Busca Binária de Gatos (com índice em disco)");
          System.out.println("===============================================");
          System.out.print("Digite o ID do gato que deseja buscar: ");
          int idBusca = scanner.nextInt();

          gerarIndiceOrdenado(); // Cria o índice ordenado

          int comparacoes = 0;
          boolean encontrado = false;
          long posicao = -1;

          long inicioTempo = System.nanoTime();

          try (RandomAccessFile indice = new RandomAccessFile(ARQUIVO_INDICE, "r")) {
               long tamanho = indice.length();
               long inicio = 0;
               long fim = tamanho;

               while (inicio <= fim) {
                    long meio = (inicio + fim) / 2;

                    // Ajustar ponteiro para o início de uma linha
                    indice.seek(meio);
                    if (meio != 0)
                         indice.readLine(); // descarta linha incompleta

                    long posAtual = indice.getFilePointer();
                    String linha = indice.readLine();

                    if (linha == null)
                         break;

                    comparacoes++;

                    String[] partes = linha.split(";");
                    int idAtual = Integer.parseInt(partes[0]);

                    if (idAtual == idBusca) {
                         posicao = Long.parseLong(partes[1]);
                         encontrado = true;
                         break;
                    } else if (idBusca < idAtual) {
                         fim = posAtual - 1;
                    } else {
                         inicio = indice.getFilePointer();
                    }
               }

               if (encontrado) {
                    try (RandomAccessFile raf = new RandomAccessFile(ARQUIVO_GATOS, "r")) {
                         raf.seek(posicao);
                         String linhaGato = raf.readLine();
                         System.out.println("===============================================");
                         System.out.println("Gato encontrado:");
                         System.out.println("===============================================");
                         exibirGatoFormatado(linhaGato.split(";"));
                    }
               } else {
                    System.out.println("===============================================");
                    System.out.println("Gato com ID " + idBusca + " não encontrado.");
               }

          } catch (IOException e) {
               System.out.println("Erro na busca binária: " + e.getMessage());
          }

          long fimTempo = System.nanoTime();
          salvarLog("Binária", "log_busca_binaria.txt", idBusca, encontrado, comparacoes, fimTempo - inicioTempo);
     }

     private static void salvarLog(String tipoBusca, String nomeArquivo, int id, boolean encontrado, int comparacoes,
               long tempoNano) {
          try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeArquivo, false))) {
               bw.write("-----------------------------\n");
               bw.write("=== Busca " + tipoBusca + " ===\n");
               bw.write("ID buscado: " + id + "\n");
               bw.write("Encontrado: " + (encontrado ? "Sim" : "Não") + "\n");
               bw.write("Comparações: " + comparacoes + "\n");
               bw.write("Tempo (ns): " + tempoNano + "\n");
               bw.write("Tempo (ms): " + (tempoNano / 1_000_000.0) + "\n");
               bw.write("-----------------------------\n");
          } catch (IOException e) {
               System.out.println("Erro ao escrever no log: " + e.getMessage());
          }
     }

     private static void exibirGatoFormatado(String[] partes) {
          System.out.println("ID: " + partes[0]);
          System.out.println("Nome: " + partes[1]);
          System.out.println("Raça: " + partes[2]);
          System.out.println("Idade: " + partes[3] + " anos");
          System.out.println("Sexo: " + partes[4]);
          System.out.println("Adotado: " + (partes[5].equalsIgnoreCase("true") ? "Sim" : "Não"));
          System.out.println("-----------------------------------------------");
     }
}
