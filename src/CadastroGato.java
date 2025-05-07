import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class CadastroGato {
     private static final String ARQUIVO_GATOS = "gatos.txt";
     private static int contadorId = 1;

     private static int obterProximoId() {
          int maiorId = 0;
          try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_GATOS))) {
               String linha;
               while ((linha = reader.readLine()) != null) {
                    String[] campos = linha.split(";");
                    int id = Integer.parseInt(campos[0]);
                    if (id > maiorId) {
                         maiorId = id;
                    }
               }
          } catch (IOException e) {
               System.out.println("Erro ao ler o arquivo: " + e.getMessage());
               System.out.println("===============================================");
          }
          return maiorId + 1;
     }

     public static void cadastrarGatoManual() throws IOException {

          Scanner scanner = new Scanner(System.in);

          System.out.print("Nome do gato: \n");
          String nome = scanner.nextLine();

          System.out.print("Raça: \n");
          String raca = scanner.nextLine();

          System.out.print("Idade (em anos): \n");
          int idade = scanner.nextInt();
          scanner.nextLine();

          System.out.print("Sexo (M/F): \n");
          String sexo = scanner.nextLine();

          int id = obterProximoId();

          Gato gato = new Gato(id, nome, raca, idade, sexo, false);
          salvarGatoNoArquivo(gato);
          System.out.println("Gato cadastrado com sucesso!");
     }

     public static void cadastrarGatoAleatorio() throws IOException {
          String[] nomes = { "Mimi", "Luna", "Thor", "Simba", "Tom", "Nina", "Bella" };
          String[] racas = { "Siames", "Persa", "SRD", "Angora", "Maine Coon" };
          String[] sexos = { "M", "F" };

          Random random = new Random();

          String nome = nomes[random.nextInt(nomes.length)];
          String raca = racas[random.nextInt(racas.length)];
          int idade = random.nextInt(15) + 1;
          String sexo = sexos[random.nextInt(sexos.length)];

          int id = obterProximoId();

          Gato gato = new Gato(id, nome, raca, idade, sexo, false);
          salvarGatoNoArquivo(gato);
          System.out.println("===============================================");
          System.out.println("Gato aleatório cadastrado com sucesso!");
     }

     private static void salvarGatoNoArquivo(Gato gato) throws IOException {
          BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_GATOS, true));
          writer.write(gato.toCSV());
          writer.newLine();
          writer.close();
     }
}
