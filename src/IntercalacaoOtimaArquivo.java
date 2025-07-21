import java.io.*;
import java.util.*;

public class IntercalacaoOtimaArquivo {

    public static void intercalar() {
        intercalarPara("gato", "gatos_ordenado.txt");
        intercalarPara("adocao", "adocoes_ordenado.txt");
    }

    private static void intercalarPara(String prefixo, String saidaFinal) {
        List<String> arquivos = obterParticoes(prefixo);
        System.out.println("\n[DEBUG] Arquivos de partição encontrados para '" + prefixo + "': " + arquivos);

        imprimirConteudoArquivos(arquivos); // <-- debug visual dos arquivos de entrada

        int fase = 1;

        while (arquivos.size() > 1) {
            List<String> novas = new ArrayList<>();
            System.out.println("[INFO] Fase " + fase + " da intercalação para '" + prefixo + "'");

            for (int i = 0; i < arquivos.size(); i += 3) {
                String out = prefixo + "_saida_" + fase + "_" + (i / 3 + 1) + ".txt";
                try {
                    System.out.println("[INFO] Intercalando arquivos: " +
                            safeGet(arquivos, i) + ", " +
                            safeGet(arquivos, i + 1) + ", " +
                            safeGet(arquivos, i + 2) +
                            " -> " + out);

                    intercalar3Arquivos(
                            safeGet(arquivos, i),
                            safeGet(arquivos, i + 1),
                            safeGet(arquivos, i + 2),
                            out);
                    novas.add(out);
                } catch (IOException e) {
                    System.out.println("[ERRO] Falha ao intercalar arquivos na fase " + fase + ": " + e.getMessage());
                }
            }

            arquivos = novas;
            fase++;
        }

        if (!arquivos.isEmpty()) {
            File destino = new File(saidaFinal);
            if (destino.exists()) {
                destino.delete(); // remove destino antigo, se existir
            }

            boolean sucesso = new File(arquivos.get(0)).renameTo(destino);
            if (sucesso) {
                System.out.println("[SUCESSO] Arquivo final de '" + prefixo + "' gerado: " + saidaFinal);
            } else {
                System.out.println("[ERRO] Falha ao renomear " + arquivos.get(0) + " para " + saidaFinal);
            }
        } else {
            System.out.println("[AVISO] Nenhum arquivo de saída foi gerado para '" + prefixo + "'");
        }

        apagarParticoes(prefixo); // limpa arquivos "_part"
    }

    private static String safeGet(List<String> lista, int index) {
        return (index < lista.size()) ? lista.get(index) : "null";
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

    private static void intercalar3Arquivos(String a, String b, String c, String saida) throws IOException {
        BufferedReader ra = a != null && !a.equals("null") ? new BufferedReader(new FileReader(a)) : null;
        BufferedReader rb = b != null && !b.equals("null") ? new BufferedReader(new FileReader(b)) : null;
        BufferedReader rc = c != null && !c.equals("null") ? new BufferedReader(new FileReader(c)) : null;
        BufferedWriter w = new BufferedWriter(new FileWriter(saida));

        String la = ra != null ? ra.readLine() : null;
        String lb = rb != null ? rb.readLine() : null;
        String lc = rc != null ? rc.readLine() : null;

        while (la != null || lb != null || lc != null) {
            String menor = menorLinha(la, lb, lc);
            if (menor != null) {
                w.write(menor);
                w.newLine();
                if (la != null && menor.equals(la)) {
                    la = ra.readLine();
                } else if (lb != null && menor.equals(lb)) {
                    lb = rb.readLine();
                } else if (lc != null && menor.equals(lc)) {
                    lc = rc.readLine();
                }
            }
        }

        if (ra != null)
            ra.close();
        if (rb != null)
            rb.close();
        if (rc != null)
            rc.close();
        w.close();
    }

    private static String menorLinha(String a, String b, String c) {
        String menor = null;
        for (String linha : new String[] { a, b, c }) {
            if (linha == null)
                continue;
            if (menor == null || Integer.parseInt(linha.split(";")[0]) < Integer.parseInt(menor.split(";")[0])) {
                menor = linha;
            }
        }
        return menor;
    }

    private static void apagarParticoes(String prefixo) {
        File dir = new File(".");
        for (File f : dir.listFiles()) {
            if (f.getName().startsWith(prefixo + "_part") && f.getName().endsWith(".txt")) {
                boolean deletado = f.delete();
                if (deletado) {
                    System.out.println("[INFO] Arquivo de partição apagado: " + f.getName());
                } else {
                    System.out.println("[ERRO] Falha ao apagar partição: " + f.getName());
                }
            }
        }
    }

    // Utilitário extra para depuração: mostra conteúdo dos arquivos de entrada
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
}
