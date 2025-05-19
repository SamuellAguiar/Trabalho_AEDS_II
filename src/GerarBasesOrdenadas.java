import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GerarBasesOrdenadas {
    private static final String ARQ_GATOS = "gatos.txt";
    private static final String ARQ_ADOCOES = "adocoes.txt";

    private static final String[] NOMES = { "Luna", "Mingau", "Tom", "Mimi", "Felix", "Bola", "Pantera", "Garfield",
            "Nina", "Tigrao" };
    private static final String[] RACAS = { "Persa", "Siames", "Angora", "SRD", "Maine Coon", "Siberiano", "Bengal" };
    private static final String[] SEXOS = { "M", "F" };
    private static final String[] ADOTANTES = { "João", "Maria", "Lucas", "Ana", "Gabriel", "Paula", "Rafaela",
            "Bruno" };

    public static void gerarOuOrdenarBases() {
        File fGatos = new File(ARQ_GATOS);
        File fAdocoes = new File(ARQ_ADOCOES);

        if (fGatos.exists() && fAdocoes.exists()) {
            System.out.println("Arquivos encontrados. Ordenando ambos por ID...");
            ordenarArquivosExistentes();
        } else {
            System.out.println("Arquivos não encontrados. Gerando bases ordenadas por ID...");
            gerarNovasBasesOrdenadas();
        }
    }

    /* ===== 1) Ordenar arquivos já existentes ===== */
    private static void ordenarArquivosExistentes() {
        List<Gato> gatos = new ArrayList<>();
        List<Adocao> adocoes = new ArrayList<>();

        // lê gatos
        try (BufferedReader br = new BufferedReader(new FileReader(ARQ_GATOS))) {
            String linha;
            while ((linha = br.readLine()) != null)
                gatos.add(Gato.fromCSV(linha));
        } catch (IOException e) {
            System.out.println("Erro lendo gatos: " + e.getMessage());
            return;
        }

        // lê adoções
        try (BufferedReader br = new BufferedReader(new FileReader(ARQ_ADOCOES))) {
            String linha;
            while ((linha = br.readLine()) != null)
                adocoes.add(Adocao.fromCSV(linha));
        } catch (IOException e) {
            System.out.println("Erro lendo adoções: " + e.getMessage());
            return;
        }

        // ordena ambas
        gatos.sort(Comparator.comparingInt(Gato::getId));
        adocoes.sort(Comparator.comparingInt(Adocao::getIdGato));

        // regrava gatos
        try (BufferedWriter wg = new BufferedWriter(new FileWriter(ARQ_GATOS, false))) {
            for (Gato g : gatos) {
                wg.write(g.toCSV());
                wg.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro gravando gatos: " + e.getMessage());
        }

        // regrava adoções
        try (BufferedWriter wa = new BufferedWriter(new FileWriter(ARQ_ADOCOES, false))) {
            for (Adocao a : adocoes) {
                wa.write(a.toCSV());
                wa.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro gravando adoções: " + e.getMessage());
        }

        System.out.println("Arquivos ordenados com sucesso!");
    }

    /* ===== 2) Gerar novas bases ordenadas ===== */
    private static void gerarNovasBasesOrdenadas() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Escolha quantidade (10, 100 ou 1000): ");
        int qtd = sc.nextInt();
        if (qtd != 10 && qtd != 100 && qtd != 1000) {
            System.out.println("Quantidade inválida. Operação cancelada.");
            return;
        }

        Random rnd = new Random();
        List<Gato> gatos = new ArrayList<>();
        List<Adocao> adocoes = new ArrayList<>();

        for (int id = 1; id <= qtd; id++) {
            String nome = NOMES[rnd.nextInt(NOMES.length)];
            String raca = RACAS[rnd.nextInt(RACAS.length)];
            int idade = rnd.nextInt(20) + 1;
            String sexo = SEXOS[rnd.nextInt(SEXOS.length)];

            boolean adotado = rnd.nextBoolean(); // 50 %
            gatos.add(new Gato(id, nome, raca, idade, sexo, adotado));

            if (adotado) {
                String adotante = ADOTANTES[rnd.nextInt(ADOTANTES.length)];
                String data = LocalDate.now()
                        .minusDays(rnd.nextInt(365))
                        .format(DateTimeFormatter.ISO_DATE);
                adocoes.add(new Adocao(id, adotante, data));
            }
        }

        // já estão em ordem pois id crescente foi usado, mas garanta:
        gatos.sort(Comparator.comparingInt(Gato::getId));
        adocoes.sort(Comparator.comparingInt(Adocao::getIdGato));

        try (BufferedWriter wg = new BufferedWriter(new FileWriter(ARQ_GATOS, false));
                BufferedWriter wa = new BufferedWriter(new FileWriter(ARQ_ADOCOES, false))) {

            for (Gato g : gatos) {
                wg.write(g.toCSV());
                wg.newLine();
            }
            for (Adocao a : adocoes) {
                wa.write(a.toCSV());
                wa.newLine();
            }

            System.out.println("Bases criadas e ordenadas com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro criando arquivos: " + e.getMessage());
        }
    }
}
