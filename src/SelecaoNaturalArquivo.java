import java.io.*;

public class SelecaoNaturalArquivo {

    public static void gerarParticoes() {
        try {
            gerarParticoesPara("gatos.txt", "gato");
        } catch (IOException e) {
            System.out.println("Erro ao gerar partições: " + e.getMessage());
        }
    }

    private static void gerarParticoesPara(String arquivoEntrada, String prefixo) throws IOException {
        int M = 5;
        long inicio = System.nanoTime();
        int totalParticoes = 0;
        int totalRegistros = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivoEntrada))) {
            totalParticoes = selecaoNaturalCompleta(reader, prefixo, M);
        }

        long fim = System.nanoTime();
        salvarLogGeral("SelecaoNatural", prefixo + "_log.txt", totalParticoes, fim - inicio);
    }

    private static int selecaoNaturalCompleta(BufferedReader reader, String prefixo, int M) throws IOException {
        String[] memoria = new String[M];
        String arquivoReservatorio = prefixo + "_reservatorio.tmp";
        int contadorParticao = 1;

        int registrosNaMemoria = 0;
        for (int i = 0; i < M; i++) {
            String linha = reader.readLine();
            if (linha != null) {
                memoria[i] = linha;
                registrosNaMemoria++;
            } else {
                break;
            }
        }

        boolean arquivoTerminado = false;
        int ultimaChaveGravada = -1;
 while (registrosNaMemoria > 0 || new File(arquivoReservatorio).exists()) {
       
            String arquivoParticao = prefixo + "_part" + contadorParticao + ".txt";
            BufferedWriter writerParticao = new BufferedWriter(new FileWriter(arquivoParticao));

            while (true) {
                int indiceMenor = -1;
                int menorChave = Integer.MAX_VALUE;

                for (int i = 0; i < M; i++) {
                    if (memoria[i] != null) {
                        int chave = parseKey(memoria[i]);
                        if (chave >= ultimaChaveGravada && chave < menorChave) {
                            menorChave = chave;
                            indiceMenor = i;
                        }
                    }
                }

                if (indiceMenor == -1)
                    break;

                writerParticao.write(memoria[indiceMenor]);
                writerParticao.newLine();
                ultimaChaveGravada = menorChave;

                String proximoRegistro = null;
                if (!arquivoTerminado) {
                    proximoRegistro = reader.readLine();
                    if (proximoRegistro == null)
                        arquivoTerminado = true;
                }

                if (proximoRegistro != null) {
                    int proximaChave = parseKey(proximoRegistro);
                    if (proximaChave < ultimaChaveGravada) {
                        gravarNoReservatorio(proximoRegistro, arquivoReservatorio);
                        memoria[indiceMenor] = null;
                    } else {
                        memoria[indiceMenor] = proximoRegistro;
                    }
                } else {
                    memoria[indiceMenor] = null;
                }

                registrosNaMemoria = 0;
                for (int i = 0; i < M; i++)
                    if (memoria[i] != null)
                        registrosNaMemoria++;
            }

            writerParticao.close();

            if (registrosNaMemoria == 0 && new File(arquivoReservatorio).exists()) {
                registrosNaMemoria = carregarDoReservatorio(memoria, arquivoReservatorio, M);
                ultimaChaveGravada = -1;
                contadorParticao++;
            } else if (registrosNaMemoria == 0) {
                break;
            }
        }

        new File(arquivoReservatorio).delete();
        return contadorParticao;
    }

    private static void gravarNoReservatorio(String registro, String arquivoReservatorio) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoReservatorio, true))) {
            writer.write(registro);
            writer.newLine();
        }
    }

    private static int carregarDoReservatorio(String[] memoria, String arquivoReservatorio, int M) throws IOException {
        File reservatorio = new File(arquivoReservatorio);
        if (!reservatorio.exists())
            return 0;

        java.util.List<String> registros = new java.util.ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(reservatorio))) {
            String linha;
            while ((linha = reader.readLine()) != null)
                registros.add(linha);
        }

        for (int i = 0; i < M; i++)
            memoria[i] = null;

        int carregados = 0;
        for (int i = 0; i < Math.min(M, registros.size()); i++) {
            memoria[i] = registros.get(i);
            carregados++;
        }

        reservatorio.delete();
        if (registros.size() > M) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoReservatorio))) {
                for (int i = M; i < registros.size(); i++) {
                    writer.write(registros.get(i));
                    writer.newLine();
                }
            }
        }

        return carregados;
    }

    private static int parseKey(String linha) {
        try {
            return Integer.parseInt(linha.split(";")[0].trim());
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    private static void salvarLogGeral(String tipo, String arquivoLog, int particoes, long tempoNano) {
        double tempoMs = tempoNano / 1_000_000.0;
        double tempoSec = tempoNano / 1_000_000_000.0;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Log_Selecao_Natural.txt", false))) {
            bw.write("=== Log de Selecao Natural ===\n");
            bw.write("Particoes geradas: " + particoes + "\n");
            bw.write(String.format("Tempo total: %d ns\n", tempoNano));
            bw.write(String.format("Tempo total: %.3f ms\n", tempoMs));
            bw.write(String.format("Tempo total: %.3f s\n", tempoSec));
        } catch (IOException e) {
            System.out.println("Erro ao salvar log: " + e.getMessage());
        }
    }
}