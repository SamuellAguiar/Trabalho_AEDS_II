import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GerarBasesDesordenados {
    private static final String ARQ_GATOS = "gatos.txt";
    private static final String ARQ_ADOCOES = "adocoes.txt";

    private static final String[] NOMES = { "Luna", "Mingau", "Tom", "Mimi", "Felix", "Bola", "Pantera", "Garfield",
            "Nina", "Tigrao" };
    private static final String[] RACAS = { "Persa", "Siames", "Angora", "SRD", "Maine Coon", "Siberiano", "Bengal" };
    private static final String[] SEXOS = { "M", "F" };
    private static final String[] ADOTANTES = { "João", "Maria", "Lucas", "Ana", "Gabriel", "Paula", "Rafaela",
            "Bruno" };

    public static void gerarBaseDeDados() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("===============================================");
        System.out.println("Gerando base de dados desordenada...");
        System.out.println("===============================================");
        System.out.println("Escolha a quantidade de gatos a serem gerados (10, 100 ou 1000):");
        int quantidade = scanner.nextInt();

        if (quantidade != 10 && quantidade != 100 && quantidade != 1000) {
            System.out.println("Quantidade inválida. Operação cancelada.");
            return;
        }

        Random random = new Random();
        List<Gato> gatos = new ArrayList<>();

        // 1. Cria a lista de gatos (aqui nada muda)
        for (int i = 1; i <= quantidade; i++) {
            String nome = NOMES[random.nextInt(NOMES.length)];
            String raca = RACAS[random.nextInt(RACAS.length)];
            int idade = random.nextInt(20) + 1;
            String sexo = SEXOS[random.nextInt(SEXOS.length)];

            // decide depois se será adotado
            gatos.add(new Gato(i, nome, raca, idade, sexo, false));
        }

        // 2. Embaralha como antes
        Collections.shuffle(gatos);

        // 3. Escreve em dois arquivos ao mesmo tempo
        try (BufferedWriter wg = new BufferedWriter(new FileWriter(ARQ_GATOS, false));
                BufferedWriter wa = new BufferedWriter(new FileWriter(ARQ_ADOCOES, false))) {

            for (Gato gato : gatos) {
                // 50 % de chance de adoção (ajuste se quiser)
                boolean seraAdotado = random.nextBoolean();
                gato.setAdotado(seraAdotado);

                // grava linha do gato
                wg.write(gato.toCSV());
                wg.newLine();

                // se adotado, grava linha da adoção no segundo arquivo
                if (seraAdotado) {
                    String adotante = ADOTANTES[random.nextInt(ADOTANTES.length)];
                    String data = LocalDate.now()
                            .minusDays(random.nextInt(365))
                            .format(DateTimeFormatter.ISO_DATE);
                    Adocao ad = new Adocao(gato.getId(), adotante, data);
                    wa.write(ad.toCSV());
                    wa.newLine();
                }
            }
            System.out.println("Bases desordenadas geradas em gatos.txt e adocoes.txt!");
        } catch (IOException e) {
            System.out.println("Erro ao gerar base de dados: " + e.getMessage());
        }
    }
}
