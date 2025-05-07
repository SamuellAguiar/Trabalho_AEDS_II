import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GeradorDeGatosDesordenados {
     private static final String ARQUIVO_GATOS = "gatos.txt";

     private static final String[] NOMES = { "Luna", "Mingau", "Tom", "Mimi", "Felix", "Bola", "Pantera", "Garfield",
               "Nina", "Tigrão" };
     private static final String[] RACOES = { "Persa", "Siamês", "Angorá", "SRD", "Maine Coon", "Siberiano", "Bengal" };
     private static final String[] SEXOS = { "M", "F" };

     public static void gerarBaseDeDados() {
          Scanner scanner = new Scanner(System.in);
          System.out.println("===============================================");
          System.out.println("Gerando base de dados desordenada...");
          System.out.println("===============================================");
          System.out.println("Escolha a quantidade de gatos a serem gerados: ");
          System.out.println("10, 100 ou 1000?");
          System.out.println("===============================================");
          System.out.println("Digite a quantidade desejada: ");
          int quantidade = scanner.nextInt();

          if (quantidade != 10 && quantidade != 100 && quantidade != 1000) {
               System.out.println("===============================================");
               System.out.println("Quantidade inválida. Escolha 10, 100 ou 1000.");
               System.out.println("===============================================");
               System.out.println("Operação cancelada.");
               System.out.println("===============================================");
               return;
          }

          try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_GATOS, false))) {
               Random random = new Random();
               List<Gato> gatos = new ArrayList<>();

               // Criar gatos com IDs em ordem
               for (int i = 1; i <= quantidade; i++) {
                    String nome = NOMES[random.nextInt(NOMES.length)];
                    String raca = RACOES[random.nextInt(RACOES.length)];
                    int idade = random.nextInt(20) + 1;
                    String sexo = SEXOS[random.nextInt(SEXOS.length)];

                    Gato gato = new Gato(i, nome, raca, idade, sexo, false); // Adotado = false
                    gatos.add(gato);
               }

               // Embaralhar a lista para desordenar por ID
               Collections.shuffle(gatos);

               // Escrever os gatos embaralhados no arquivo
               for (Gato gato : gatos) {
                    writer.write(gato.toCSV());
                    writer.newLine();
               }

               System.out.println("===============================================");
               System.out.println("Base de dados desordenada gerada com sucesso!");
               System.out.println("===============================================");
          } catch (IOException e) {
               System.out.println("Erro ao gerar base de dados: " + e.getMessage());
               System.out.println("===============================================");
          }
     }
}
