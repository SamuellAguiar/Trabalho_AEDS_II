import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class CadastroGato {
     private static final String ARQUIVO_GATOS = "gatos.txt";
     private static int contadorId = 1;

     public static void cadastrarGatoManual() throws IOException {
          Scanner scanner = new Scanner(System.in);

          System.out.print("Nome do gato: ");
          String nome = scanner.nextLine();

          System.out.print("Raça: ");
          String raca = scanner.nextLine();

          System.out.print("Idade (em anos): ");
          int idade = scanner.nextInt();
          scanner.nextLine(); // limpar buffer

          System.out.print("Sexo (M/F): ");
          String sexo = scanner.nextLine();

          Gato gato = new Gato(contadorId++, nome, raca, idade, sexo, false); // novo campo "adotado" = false
          salvarGatoNoArquivo(gato);
          System.out.println("Gato cadastrado com sucesso!");
     }

     public static void cadastrarGatoAleatorio() throws IOException {
          String[] nomes = { "Mimi", "Luna", "Thor", "Simba", "Tom", "Nina", "Bella" };
          String[] racas = { "Siamês", "Persa", "SRD", "Angorá", "Maine Coon" };
          String[] sexos = { "M", "F" };

          Random random = new Random();

          String nome = nomes[random.nextInt(nomes.length)];
          String raca = racas[random.nextInt(racas.length)];
          int idade = random.nextInt(15) + 1; // entre 1 e 15 anos
          String sexo = sexos[random.nextInt(sexos.length)];

          Gato gato = new Gato(contadorId++, nome, raca, idade, sexo, false); // novo campo "adotado" = false
          salvarGatoNoArquivo(gato);
          System.out.println("Gato aleatório cadastrado com sucesso!");
     }

     private static void salvarGatoNoArquivo(Gato gato) throws IOException {
          BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_GATOS, true));
          writer.write(gato.toCSV()); // escreve o gato no formato CSV
          writer.newLine();
          writer.close();
     }
}
