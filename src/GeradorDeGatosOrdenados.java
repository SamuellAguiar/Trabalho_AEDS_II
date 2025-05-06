import java.io.*;
import java.util.*;

public class GeradorDeGatosOrdenados {
    private static final String ARQUIVO_GATOS = "gatos.txt";

    private static final String[] NOMES = { "Luna", "Mingau", "Tom", "Mimi", "Felix", "Bola", "Pantera", "Garfield",
            "Nina", "Tigrão" };
    private static final String[] RACOES = { "Persa", "Siamês", "Angorá", "SRD", "Maine Coon", "Siberiano", "Bengal" };
    private static final String[] SEXOS = { "M", "F" };

    public static void gerarOuOrdenarBase() {
        File arquivo = new File(ARQUIVO_GATOS);

        if (arquivo.exists()) {
            System.out.println("Arquivo já existe. Ordenando os dados por ID...");
            ordenarArquivoExistente();
        } else {
            System.out.println("Arquivo não encontrado. Gerando base ordenada por ID...");
            gerarNovoArquivoOrdenado();
        }
    }

    private static void ordenarArquivoExistente() {
        List<Gato> gatos = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_GATOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Gato gato = Gato.fromCSV(linha);
                gatos.add(gato);
            }

            gatos.sort(Comparator.comparingInt(Gato::getId));

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_GATOS, false))) {
                for (Gato gato : gatos) {
                    writer.write(gato.toCSV());
                    writer.newLine();
                }
            }

            System.out.println("Arquivo ordenado com sucesso!");

        } catch (IOException e) {
            System.out.println("Erro ao ordenar arquivo: " + e.getMessage());
        }
    }

    private static void gerarNovoArquivoOrdenado() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Quantos gatos deseja gerar? (10, 100 ou 1000): ");
        int quantidade = scanner.nextInt();

        if (quantidade != 10 && quantidade != 100 && quantidade != 1000) {
            System.out.println("Quantidade inválida. Escolha 10, 100 ou 1000.");
            return;
        }

        Random random = new Random();
        List<Gato> gatos = new ArrayList<>();

        for (int i = 1; i <= quantidade; i++) {
            String nome = NOMES[random.nextInt(NOMES.length)];
            String raca = RACOES[random.nextInt(RACOES.length)];
            int idade = random.nextInt(20) + 1;
            String sexo = SEXOS[random.nextInt(SEXOS.length)];

            Gato gato = new Gato(i, nome, raca, idade, sexo, false);
            gatos.add(gato);
        }

        gatos.sort(Comparator.comparingInt(Gato::getId));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_GATOS, false))) {
            for (Gato gato : gatos) {
                writer.write(gato.toCSV());
                writer.newLine();
            }

            System.out.println("Base de dados criada e ordenada com sucesso!");

        } catch (IOException e) {
            System.out.println("Erro ao criar arquivo: " + e.getMessage());
        }
    }
}
