import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

                    if (partes.length == 6 && partes[5].equalsIgnoreCase("false")) {
                         System.out.println("ID: " + partes[0]);
                         System.out.println("Nome: " + partes[1]);
                         System.out.println("Idade: " + partes[2] + " meses");
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
          final String ARQUIVO_GATOS = "gatos.txt";

          System.out.println("=====================================================");
          System.out.println("           A D O Ç Õ E S   R E G I S T R A D A S   ");
          System.out.println("=====================================================\n");

          try (BufferedReader adocaoReader = new BufferedReader(new FileReader(ARQUIVO_ADOCOES))) {
               String linhaAdocao;
               boolean vazio = true;

               while ((linhaAdocao = adocaoReader.readLine()) != null) {
                    Adocao adocao = Adocao.fromCSV(linhaAdocao);

                    Gato gato = buscarGatoPorId(adocao.getIdGato(), ARQUIVO_GATOS);

                    if (gato == null) {
                         System.out.printf("• Adoção para o gato ID %d não pôde ser exibida (gato não encontrado).\n\n",
                                   adocao.getIdGato());
                         continue;
                    }

                    System.out.println("---------------------------------------------------");
                    System.out.printf("Adoção do dia %s\n", adocao.getDataAdocao());
                    System.out.printf("Adotante : %s\n", adocao.getNomeAdotante());
                    System.out.println("-----------------------------------------------------");
                    System.out.printf("Gato     : %s (ID %d)\n", gato.getNome(), gato.getId());
                    System.out.printf("Raça     : %s\n", gato.getRaca());
                    System.out.printf("Idade    : %d meses\n", gato.getIdade());
                    System.out.printf("Sexo     : %s\n", gato.getSexo());
                    System.out.printf("Status   : %s\n", gato.isAdotado() ? "Adotado" : "Disponível");
                    System.out.println("---------------------------------------------------\n");

                    vazio = false;
               }

               if (vazio) {
                    System.out.println("Nenhuma adoção registrada.\n");
               }

               System.out.println("=====================================================");
          } catch (IOException e) {
               System.out.println("Erro ao ler o arquivo: " + e.getMessage());
               System.out.println("=====================================================");
          }
     }

     private static Gato buscarGatoPorId(int idGato, String arquivoGatos) {
          try (BufferedReader gatoReader = new BufferedReader(new FileReader(arquivoGatos))) {
               String linhaGato;
               while ((linhaGato = gatoReader.readLine()) != null) {
                    Gato g = Gato.fromCSV(linhaGato);
                    if (g.getId() == idGato) {
                         return g;
                    }
               }
          } catch (IOException ex) {
               System.out.println("[Aviso] Não foi possível ler o arquivo de gatos: " + ex.getMessage());
          }
          return null;
     }

     private static final String ARQ_GATOS = "gatos.txt";
     private static final String ARQ_ADOCOES = "adocoes.txt";

     public static void editarAdocao() {
          Scanner sc = new Scanner(System.in);
          System.out.print("Informe o ID do gato para editar a adoção: ");
          int id = sc.nextInt();
          sc.nextLine();

          boolean encontrado = false;

          File arquivoOriginal = new File(ARQ_ADOCOES);
          File arquivoTemp = new File("adocoes_temp.txt");

          try (
                    BufferedReader br = new BufferedReader(new FileReader(arquivoOriginal));
                    BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoTemp))) {
               String linha;

               while ((linha = br.readLine()) != null) {
                    Adocao a = Adocao.fromCSV(linha);

                    if (a.getIdGato() == id) {
                         System.out.println("Registro atual: " + a);

                         System.out.print("Novo nome do adotante (Enter = manter): ");
                         String novoNome = sc.nextLine();
                         if (!novoNome.isBlank()) {
                              a.setNomeAdotante(novoNome);
                         }

                         System.out.print("Nova data (aaaa‑MM‑dd, Enter = manter): ");
                         String novaData = sc.nextLine();
                         if (!novaData.isBlank()) {
                              a.setDataAdocao(novaData);
                         }

                         encontrado = true;
                    }

                    bw.write(a.toCSV());
                    bw.newLine();
               }

          } catch (IOException e) {
               System.out.println("Erro: " + e.getMessage());
               return;
          }

          if (!encontrado) {
               System.out.println("ID não encontrado.");
               arquivoTemp.delete();
               return;
          }

          // Substitui o arquivo original
          arquivoOriginal.delete();
          arquivoTemp.renameTo(arquivoOriginal);

          System.out.println("Adoção atualizada com sucesso!");
     }

     public static void excluirAdocao() {
          Scanner sc = new Scanner(System.in);
          System.out.print("Informe o ID do gato cuja adoção será excluída: ");
          int id = sc.nextInt();

          boolean removido = false;

          // === Atualiza o arquivo de adoções ===
          File arquivoOriginal = new File(ARQ_ADOCOES);
          File arquivoTemp = new File("adocoes_temp.txt");

          try (
                    BufferedReader br = new BufferedReader(new FileReader(arquivoOriginal));
                    BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoTemp))) {
               String linha;
               while ((linha = br.readLine()) != null) {
                    Adocao a = Adocao.fromCSV(linha);
                    if (a.getIdGato() == id) {
                         removido = true;
                         continue; // Pula a escrita (exclui)
                    }
                    bw.write(a.toCSV());
                    bw.newLine();
               }
          } catch (IOException e) {
               System.out.println("Erro ao processar adoções: " + e.getMessage());
               return;
          }

          if (!removido) {
               System.out.println("Adoção não encontrada.");
               arquivoTemp.delete();
               return;
          }

          arquivoOriginal.delete();
          arquivoTemp.renameTo(arquivoOriginal);

          // === Atualiza o arquivo de gatos (marca como disponível) ===
          File gatosOriginal = new File(ARQ_GATOS);
          File gatosTemp = new File("gatos_temp.txt");

          try (
                    BufferedReader br = new BufferedReader(new FileReader(gatosOriginal));
                    BufferedWriter bw = new BufferedWriter(new FileWriter(gatosTemp))) {
               String linha;
               while ((linha = br.readLine()) != null) {
                    Gato g = Gato.fromCSV(linha);
                    if (g.getId() == id) {
                         g.setAdotado(false); // Marca como disponível
                    }
                    bw.write(g.toCSV());
                    bw.newLine();
               }
          } catch (IOException e) {
               System.out.println("Erro ao atualizar status do gato: " + e.getMessage());
               return;
          }

          gatosOriginal.delete();
          gatosTemp.renameTo(gatosOriginal);

          System.out.println("Adoção excluída e gato marcado como disponível!");
     }

}
