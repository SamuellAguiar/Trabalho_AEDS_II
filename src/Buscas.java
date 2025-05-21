import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Buscas {
     private static final String ARQUIVO_GATOS = "gatos.txt";
     private static final String ARQUIVO_INDICE = "indice.txt";

     public static void gerarIndice() {
          try (RandomAccessFile raf = new RandomAccessFile(ARQUIVO_GATOS, "r");
                    BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_INDICE))) {

               String linha;
               long posicao = 0;

               while ((linha = raf.readLine()) != null) {
                    String[] partes = linha.split(";");
                    if (partes.length > 0) {
                         writer.write(partes[0] + ";" + posicao + "\n");
                    }
                    posicao = raf.getFilePointer();
               }

          } catch (IOException e) {
               System.out.println("Erro ao gerar o índice: " + e.getMessage());
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
          System.out.println("Busca Binária de Gatos (com índice em disco)");
          System.out.println("===============================================");
          System.out.print("Digite o ID do gato que deseja buscar: ");
          int idBusca = scanner.nextInt();

          gerarIndice(); 

          int comparacoes = 0;
          boolean encontrado = false;
          long inicioTempo = System.nanoTime();
          long posicaoEncontrada = -1;

          try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_INDICE))) {
               List<String> linhasIndice = new ArrayList<>();
               String linha;
               while ((linha = reader.readLine()) != null) {
                    linhasIndice.add(linha);
               }

               linhasIndice.sort(Comparator.comparingInt(l -> Integer.parseInt(l.split(";")[0])));

               int inicio = 0;
               int fim = linhasIndice.size() - 1;

               while (inicio <= fim) {
                    int meio = (inicio + fim) / 2;
                    comparacoes++;

                    String[] partes = linhasIndice.get(meio).split(";");
                    int id = Integer.parseInt(partes[0]);

                    if (id == idBusca) {
                         posicaoEncontrada = Long.parseLong(partes[1]);
                         encontrado = true;
                         break;
                    } else if (id < idBusca) {
                         inicio = meio + 1;
                    } else {
                         fim = meio - 1;
                    }
               }

               if (encontrado) {
                    try (RandomAccessFile raf = new RandomAccessFile(ARQUIVO_GATOS, "r")) {
                         raf.seek(posicaoEncontrada);
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
          long duracao = fimTempo - inicioTempo;

          salvarLog("Binária", "log_busca_binaria.txt", idBusca, encontrado, comparacoes, duracao);
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
               System.out.println("===============================================");
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
