import java.io.*;
import java.util.*;

public class AdocaoGato {

     private static final String ARQUIVO_GATOS = "gatos.txt";
     private static final String ARQUIVO_ADOCOES = "adocoes.txt";

     public static void adotarGato() {
          Scanner scanner = new Scanner(System.in);

          System.out.print("Digite o ID do gato a ser adotado: ");
          int idGato = scanner.nextInt();
          scanner.nextLine(); // Limpa buffer

          Gato gatoParaAdotar = buscarGatoPorId(idGato);
          if (gatoParaAdotar == null) {
               System.out.println("Gato com ID " + idGato + " não existe.");
               return;
          }

          if (gatoParaAdotar.isAdotado()) {
               System.out.println("Este gato já foi adotado.");
               return;
          }

          System.out.print("Digite o nome do adotante: ");
          String nomeAdotante = scanner.nextLine();

          System.out.print("Digite a data da adoção (dd/mm/aaaa): ");
          String dataAdocao = scanner.nextLine();

          Adocao adocao = new Adocao(idGato, nomeAdotante, dataAdocao);
          salvarAdocao(adocao);

          gatoParaAdotar.setAdotado(true);
          atualizarStatusGato(gatoParaAdotar);

          System.out.println("Adoção registrada e status do gato atualizado com sucesso!");
     }

     /* 
     private static boolean existeGato(int idGato) {
          try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_GATOS))) {
               String linha;
               while ((linha = reader.readLine()) != null) {
                    Gato g = Gato.fromCSV(linha);
                    if (g.getId() == idGato) {
                         return true;
                    }
               }
          } catch (IOException e) {
               System.out.println("Erro ao verificar gato: " + e.getMessage());
          }
          return false;
     }

     private static boolean gatoJaAdotado(int idGato) {
          try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_ADOCOES))) {
               String linha;
               while ((linha = reader.readLine()) != null) {
                    Adocao a = Adocao.fromCSV(linha);
                    if (a.getIdGato() == idGato) {
                         return true;
                    }
               }
          } catch (FileNotFoundException e) {
               // Se o arquivo ainda não existe, nenhum gato foi adotado.
               return false;
          } catch (IOException e) {
               System.out.println("Erro ao verificar adoção: " + e.getMessage());
          }
          return false;
     }
*/

     private static void salvarAdocao(Adocao adocao) {
          try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_ADOCOES, true))) {
               writer.write(adocao.toCSV());
               writer.newLine();
          } catch (IOException e) {
               System.out.println("Erro ao salvar adoção: " + e.getMessage());
          }
     }

     private static Gato buscarGatoPorId(int idGato) {
          try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_GATOS))) {
               String linha;
               while ((linha = reader.readLine()) != null) {
                    Gato g = Gato.fromCSV(linha);
                    if (g.getId() == idGato) {
                         return g;
                    }
               }
          } catch (IOException e) {
               System.out.println("Erro ao buscar gato: " + e.getMessage());
          }
          return null;
     }

     private static void atualizarStatusGato(Gato gatoAtualizado) {
          List<Gato> gatos = new ArrayList<>();

          // Lê todos os gatos
          try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_GATOS))) {
               String linha;
               while ((linha = reader.readLine()) != null) {
                    Gato g = Gato.fromCSV(linha);
                    if (g.getId() == gatoAtualizado.getId()) {
                         g.setAdotado(true);
                    }
                    gatos.add(g);
               }
          } catch (IOException e) {
               System.out.println("Erro ao ler gatos para atualizar: " + e.getMessage());
               return;
          }

          // Reescreve o arquivo com os dados atualizados
          try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_GATOS))) {
               for (Gato g : gatos) {
                    writer.write(g.toCSV());
                    writer.newLine();
               }
          } catch (IOException e) {
               System.out.println("Erro ao atualizar status do gato: " + e.getMessage());
          }
     }

}
