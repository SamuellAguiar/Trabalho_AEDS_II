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

    private static List<String> estadoOriginalGatos;
    private static List<String> estadoOriginalAdocoes;

    private static List<String> estadoOrdenadoGatos;
    private static List<String> estadoOrdenadoAdocoes;

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

    private static void ordenarArquivosExistentes() {
        List<Gato> gatos = new ArrayList<>();
        List<Adocao> adocoes = new ArrayList<>();

        estadoOriginalGatos = new ArrayList<>();
        estadoOriginalAdocoes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQ_GATOS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                estadoOriginalGatos.add(linha);
                gatos.add(Gato.fromCSV(linha));
            }
        } catch (IOException e) {
            System.out.println("Erro lendo gatos: " + e.getMessage());
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ARQ_ADOCOES))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                estadoOriginalAdocoes.add(linha);
                adocoes.add(Adocao.fromCSV(linha));
            }
        } catch (IOException e) {
            System.out.println("Erro lendo adoções: " + e.getMessage());
            return;
        }

        gatos.sort(Comparator.comparingInt(Gato::getId));
        adocoes.sort(Comparator.comparingInt(Adocao::getIdGato));

        estadoOrdenadoGatos = new ArrayList<>();
        estadoOrdenadoAdocoes = new ArrayList<>();

        try (BufferedWriter wg = new BufferedWriter(new FileWriter(ARQ_GATOS, false))) {
            for (Gato g : gatos) {
                String linha = g.toCSV();
                estadoOrdenadoGatos.add(linha);
                wg.write(linha);
                wg.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro gravando gatos: " + e.getMessage());
        }

        try (BufferedWriter wa = new BufferedWriter(new FileWriter(ARQ_ADOCOES, false))) {
            for (Adocao a : adocoes) {
                String linha = a.toCSV();
                estadoOrdenadoAdocoes.add(linha);
                wa.write(linha);
                wa.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro gravando adoções: " + e.getMessage());
        }

        System.out.println("Arquivos ordenados com sucesso!");
    }

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

            boolean adotado = rnd.nextBoolean();
            gatos.add(new Gato(id, nome, raca, idade, sexo, adotado));

            if (adotado) {
                String adotante = ADOTANTES[rnd.nextInt(ADOTANTES.length)];
                String data = LocalDate.now()
                        .minusDays(rnd.nextInt(365))
                        .format(DateTimeFormatter.ISO_DATE);
                adocoes.add(new Adocao(id, adotante, data));
            }
        }

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

    public static void restaurarEstadoOriginal() {
        if (estadoOriginalGatos == null || estadoOriginalAdocoes == null) {
            System.out.println("Nenhum estado original armazenado para restaurar.");
            return;
        }

        try (BufferedWriter wg = new BufferedWriter(new FileWriter(ARQ_GATOS, false))) {
            for (String linha : estadoOriginalGatos) {
                wg.write(linha);
                wg.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro restaurando gatos: " + e.getMessage());
            return;
        }

        try (BufferedWriter wa = new BufferedWriter(new FileWriter(ARQ_ADOCOES, false))) {
            for (String linha : estadoOriginalAdocoes) {
                wa.write(linha);
                wa.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro restaurando adoções: " + e.getMessage());
            return;
        }

        System.out.println("Estado original restaurado com sucesso!");
    }

}
