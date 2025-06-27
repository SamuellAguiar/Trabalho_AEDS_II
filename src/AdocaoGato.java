import java.io.*;
import java.util.Scanner;

public class AdocaoGato {

     private static final String ARQUIVO_GATOS = "gatos.txt";
     private static final String ARQUIVO_ADOCOES = "adocoes.txt";

     public static void adotarGato() {
          Scanner scanner = new Scanner(System.in);
          System.out.println("===============================================");
          System.out.println("Adoção de Gatos");
          System.out.println("===============================================");
          System.out.print("Digite o ID do gato desejado para adoção: ");
          int idGato = scanner.nextInt();
          scanner.nextLine();

          Gato gatoParaAdotar = buscarGatoPorId(idGato);
          if (gatoParaAdotar == null) {
               System.out.println("===============================================");
               System.out.println("Gato com ID " + idGato + " não existe.");
               System.out.println("===============================================");
               return;
          }

          if (gatoParaAdotar.isAdotado()) {
               System.out.println("===============================================");
               System.out.println("Gato com ID " + idGato + " já foi adotado.");
               System.out.println("===============================================");
               return;
          }

          System.out.print("Digite o nome do adotante: ");
          String nomeAdotante = scanner.nextLine();

          System.out.print("Digite a data da adoção (dd-mm-aaaa): ");
          String dataAdocao = scanner.nextLine();

          Adocao adocao = new Adocao(idGato, nomeAdotante, dataAdocao);
          salvarAdocao(adocao);
          atualizarStatusGatoEmDisco(idGato);

          System.out.println("Adoção registrada e status do gato atualizado com sucesso!");
          System.out.println("===============================================");
     }

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

     // 💡 100% DISCO: usa arquivo temporário para alterar uma linha
     private static void atualizarStatusGatoEmDisco(int idGato) {
          File arquivoOriginal = new File(ARQUIVO_GATOS);
          File arquivoTemp = new File("gatos_temp.txt");

          try (
                    BufferedReader reader = new BufferedReader(new FileReader(arquivoOriginal));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoTemp))) {
               String linha;
               while ((linha = reader.readLine()) != null) {
                    Gato g = Gato.fromCSV(linha);
                    if (g.getId() == idGato) {
                         g.setAdotado(true);
                    }
                    writer.write(g.toCSV());
                    writer.newLine();
               }
          } catch (IOException e) {
               System.out.println("Erro ao atualizar o status do gato: " + e.getMessage());
               return;
          }

          // Substitui o original
          if (!arquivoOriginal.delete()) {
               System.out.println("Erro ao deletar o arquivo original.");
               return;
          }
          if (!arquivoTemp.renameTo(arquivoOriginal)) {
               System.out.println("Erro ao renomear o arquivo temporário.");
          }
     }
}
