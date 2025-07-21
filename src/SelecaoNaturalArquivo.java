import java.io.*;

public class SelecaoNaturalArquivo {

    public static void gerarParticoes() {
        try {
            gerarParticoesPara("gatos.txt", "gato");
            gerarParticoesPara("adocoes.txt", "adocao");
        } catch (IOException e) {
            System.out.println("Erro ao gerar partições: " + e.getMessage());
        }
    }

    private static void gerarParticoesPara(String arquivoEntrada, String prefixo) throws IOException {
        int M = 5; // Tamanho da memória (M registros)

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivoEntrada))) {
            selecaoNaturalCompleta(reader, arquivoEntrada, prefixo, M);
        }
    }

    private static void selecaoNaturalCompleta(BufferedReader reader, String arquivoEntrada, String prefixo, int M)
            throws IOException {
        String[] memoria = new String[M];
        String arquivoReservatorio = prefixo + "_reservatorio.tmp";
        int contadorParticao = 1;

        // Passo 1: Ler M registros iniciais para a memória
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

        while (registrosNaMemoria > 0 || new java.io.File(arquivoReservatorio).exists()) {
            String arquivoParticao = prefixo + "_part" + contadorParticao + ".txt";
            BufferedWriter writerParticao = new BufferedWriter(new FileWriter(arquivoParticao));

            while (true) {
                // Passo 2: Selecionar registro com menor chave válida
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

                if (indiceMenor == -1) {
                    // Não há mais registros válidos na memória para esta partição
                    break;
                }

                // Passo 3: Gravar o registro na partição de saída
                writerParticao.write(memoria[indiceMenor]);
                writerParticao.newLine();
                ultimaChaveGravada = menorChave;

                // Passo 4: Substituir pelo próximo registro
                String proximoRegistro = null;
                if (!arquivoTerminado) {
                    proximoRegistro = reader.readLine();
                    if (proximoRegistro == null) {
                        arquivoTerminado = true;
                    }
                }

                if (proximoRegistro != null) {
                    int proximaChave = parseKey(proximoRegistro);

                    // Passo 5: Verificar se a chave é menor que a recém gravada
                    if (proximaChave < ultimaChaveGravada) {
                        // Gravar no reservatório (arquivo em disco)
                        gravarNoReservatorio(proximoRegistro, arquivoReservatorio);
                        memoria[indiceMenor] = null;
                    } else {
                        // Substituir na memória
                        memoria[indiceMenor] = proximoRegistro;
                    }
                } else {
                    // Não há mais registros no arquivo principal
                    memoria[indiceMenor] = null;
                }

                // Contar registros válidos na memória
                registrosNaMemoria = 0;
                for (int i = 0; i < M; i++) {
                    if (memoria[i] != null)
                        registrosNaMemoria++;
                }
            }

            writerParticao.close();

            // Passo 6 e 7: Se não há registros válidos, usar o reservatório
            if (registrosNaMemoria == 0 && new java.io.File(arquivoReservatorio).exists()) {
                // Copiar registros do reservatório para a memória
                registrosNaMemoria = carregarDoReservatorio(memoria, arquivoReservatorio, M);
                ultimaChaveGravada = -1; // Resetar para nova partição
                contadorParticao++;
            } else if (registrosNaMemoria == 0) {
                // Fim do processamento
                break;
            }
        }

        // Limpar arquivo temporário do reservatório
        new java.io.File(arquivoReservatorio).delete();
    }

    private static void gravarNoReservatorio(String registro, String arquivoReservatorio) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoReservatorio, true))) {
            writer.write(registro);
            writer.newLine();
        }
    }

    private static int carregarDoReservatorio(String[] memoria, String arquivoReservatorio, int M) throws IOException {
        java.io.File reservatorio = new java.io.File(arquivoReservatorio);
        if (!reservatorio.exists())
            return 0;

        // Ler todos os registros do reservatório
        java.util.List<String> registrosReservatorio = new java.util.ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(reservatorio))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                registrosReservatorio.add(linha);
            }
        }

        // Limpar o array de memória
        for (int i = 0; i < M; i++) {
            memoria[i] = null;
        }

        // Carregar até M registros na memória
        int carregados = 0;
        for (int i = 0; i < Math.min(M, registrosReservatorio.size()); i++) {
            memoria[i] = registrosReservatorio.get(i);
            carregados++;
        }

        // Reescrever o reservatório com os registros restantes
        reservatorio.delete();
        if (registrosReservatorio.size() > M) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoReservatorio))) {
                for (int i = M; i < registrosReservatorio.size(); i++) {
                    writer.write(registrosReservatorio.get(i));
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
}
