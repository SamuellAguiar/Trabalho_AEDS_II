
import java.io.*;
import java.util.*;

public class ArvoreDeVencedores {

    public static void intercalar() {
        intercalarComArvoreDeVencedores("gato", "gatos_ordenado.txt");
    }

    private static void intercalarComArvoreDeVencedores(String prefixo, String saidaFinal) {
        List<String> arquivos = obterParticoes(prefixo);
        System.out.println("\n[DEBUG] Arquivos de partição encontrados para '" + prefixo + "': " + arquivos);
        imprimirConteudoArquivos(arquivos);

        int fase = 1;
        long inicio = System.nanoTime();

        while (arquivos.size() > 1) {
            List<String> novas = new ArrayList<>();
            System.out.println(
                    "[INFO] Fase " + fase + " da intercalação com árvore de vencedores para '" + prefixo + "'");

            for (int i = 0; i < arquivos.size(); i += 3) {
                List<String> grupo = new ArrayList<>();
                for (int j = 0; j < 3 && i + j < arquivos.size(); j++) {
                    grupo.add(arquivos.get(i + j));
                }

                String out = prefixo + "_saida_" + fase + "_" + (i / 3 + 1) + ".txt";
                try {
                    intercalarGrupoComArvoreDeVencedores(grupo, out);
                    novas.add(out);
                } catch (IOException e) {
                    System.out.println("[ERRO] Falha na intercalação com árvore: " + e.getMessage());
                }
            }

            arquivos = novas;
            fase++;
        }

        long fim = System.nanoTime();
        salvarLogGeral("IntercalacaoArvore", prefixo + "_intercalacao_log.txt", fase - 1, fim - inicio);

        if (!arquivos.isEmpty()) {
            File destino = new File(saidaFinal);
            if (destino.exists())
                destino.delete();
            boolean sucesso = new File(arquivos.get(0)).renameTo(destino);
            if (sucesso) {
                System.out.println("[SUCESSO] Arquivo final de '" + prefixo + "' gerado: " + saidaFinal);
            } else {
                System.out.println("[ERRO] Falha ao renomear " + arquivos.get(0) + " para " + saidaFinal);
            }
        }

        apagarArquivosTemporarios(prefixo);
    }

    private static void intercalarGrupoComArvoreDeVencedores(List<String> arquivosEntrada, String saida)
            throws IOException {
        int n = arquivosEntrada.size();
        BufferedReader[] leitores = new BufferedReader[n];
        String[] linhas = new String[n];

        for (int i = 0; i < n; i++) {
            leitores[i] = new BufferedReader(new FileReader(arquivosEntrada.get(i)));
            linhas[i] = leitores[i].readLine();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(saida));

        while (true) {
            int indiceMenor = -1;
            for (int i = 0; i < n; i++) {
                if (linhas[i] == null)
                    continue;
                if (indiceMenor == -1 || compararLinhasPorId(linhas[i], linhas[indiceMenor]) < 0) {
                    indiceMenor = i;
                }
            }
            if (indiceMenor == -1)
                break;

            writer.write(linhas[indiceMenor]);
            writer.newLine();
            linhas[indiceMenor] = leitores[indiceMenor].readLine();
        }

        for (BufferedReader r : leitores)
            if (r != null)
                r.close();
        writer.close();
    }

    private static int compararLinhasPorId(String a, String b) {
        int ka = Integer.parseInt(a.split(";")[0]);
        int kb = Integer.parseInt(b.split(";")[0]);
        return Integer.compare(ka, kb);
    }

    private static List<String> obterParticoes(String prefixo) {
        List<String> arquivos = new ArrayList<>();
        File dir = new File(".");
        for (File f : dir.listFiles()) {
            if (f.getName().startsWith(prefixo + "_part") && f.getName().endsWith(".txt")) {
                arquivos.add(f.getName());
            }
        }
        Collections.sort(arquivos);
        return arquivos;
    }

    private static void apagarArquivosTemporarios(String prefixo) {
        File dir = new File(".");
        for (File f : dir.listFiles()) {
            String nome = f.getName();
            if ((nome.startsWith(prefixo + "_part") || nome.startsWith(prefixo + "_saida")) && nome.endsWith(".txt")) {
                if (f.delete()) {
                    System.out.println("[INFO] Arquivo temporário apagado: " + f.getName());
                } else {
                    System.out.println("[ERRO] Falha ao apagar arquivo temporário: " + f.getName());
                }
            }
        }
    }

    private static void imprimirConteudoArquivos(List<String> arquivos) {
        for (String nomeArquivo : arquivos) {
            System.out.println("\n------ Conteúdo de " + nomeArquivo + " ------");
            try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    System.out.println(linha);
                }
            } catch (IOException e) {
                System.out.println("[ERRO] Não foi possível ler " + nomeArquivo + ": " + e.getMessage());
            }
        }
    }

    private static void salvarLogGeral(String tipo, String arquivoLog, int fases, long tempoNano) {
        double tempoMs = tempoNano / 1_000_000.0;
        double tempoSec = tempoNano / 1_000_000_000.0;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Log_Arvore_Vencedores.txt", false))) {
            bw.write("=== Log de Arvore de Vencedores ===\n");
            bw.write("Fases de intercalacao: " + fases + "\n");
            bw.write(String.format("Tempo total: %d ns\n", tempoNano));
            bw.write(String.format("Tempo total: %.3f ms\n", tempoMs));
            bw.write(String.format("Tempo total: %.3f s\n", tempoSec));
        } catch (IOException e) {
            System.out.println("Erro ao salvar log: " + e.getMessage());
        }
    }
}
