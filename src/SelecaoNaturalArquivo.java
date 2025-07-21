import java.io.*;

public class SelecaoNaturalArquivo {

    public static void gerarParticoes() {
        gerarParticoesPara("gatos.txt", "gato", "id");
        gerarParticoesPara("adocoes.txt", "adocao", "idAdocao");
    }

    private static void gerarParticoesPara(String arquivoEntrada, String prefixo, String campoChave) {
        int tamParticao = 5;
        int contadorParticao = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivoEntrada))) {
            String linha;
            int count = 0;

            BufferedWriter writer = new BufferedWriter(new FileWriter(prefixo + "_part" + contadorParticao + ".txt"));

            while ((linha = reader.readLine()) != null) {
                writer.write(linha);
                writer.newLine();
                count++;

                if (count == tamParticao) {
                    writer.close();
                    ordenarArquivo(prefixo + "_part" + contadorParticao + ".txt", campoChave);
                    contadorParticao++;
                    count = 0;
                    writer = new BufferedWriter(new FileWriter(prefixo + "_part" + contadorParticao + ".txt"));
                }
            }

            writer.close();
            ordenarArquivo(prefixo + "_part" + contadorParticao + ".txt", campoChave);

        } catch (IOException e) {
            System.out.println("Erro ao gerar partições para " + prefixo + ": " + e.getMessage());
        }
    }

    private static void ordenarArquivo(String nomeArquivo, String campoChave) throws IOException {
        File input = new File(nomeArquivo);
        File temp = new File("temp_" + nomeArquivo);

        BufferedReader reader = new BufferedReader(new FileReader(input));
        PrintWriter writer = new PrintWriter(new FileWriter(temp));

        String[] linhas = new String[1000];
        int i = 0, posCampo = 0;

        while ((linhas[i] = reader.readLine()) != null)
            i++;

        // Ordena com base no campo 0 (id ou idAdocao)
        for (int j = 0; j < i - 1; j++) {
            for (int k = j + 1; k < i; k++) {
                int id1 = Integer.parseInt(linhas[j].split(";")[0]);
                int id2 = Integer.parseInt(linhas[k].split(";")[0]);
                if (id1 > id2) {
                    String tmp = linhas[j];
                    linhas[j] = linhas[k];
                    linhas[k] = tmp;
                }
            }
        }

        for (int j = 0; j < i; j++) {
            writer.println(linhas[j]);
        }

        reader.close();
        writer.close();

        input.delete();
        temp.renameTo(input);
    }
}
