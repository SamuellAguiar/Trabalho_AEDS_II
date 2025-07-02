import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;

public class GerenciarBases {
    private static final String ARQ_GATOS = "gatos.txt";
    private static final String ARQ_ADOCOES = "adocoes.txt";

    private static final String[] NOMES = {
        "Luna","Mingau","Tom","Mimi","Felix","Bola","Pantera","Garfield","Nina","Tigrao"
    };
    private static final String[] RACAS = {
        "Persa","Siames","Angora","SRD","Maine Coon","Siberiano","Bengal"
    };
    private static final String[] SEXOS = { "M","F" };
    private static final String[] ADOTANTES = {
        "Joao","Maria","Lucas","Ana","Gabriel","Paula","Rafaela","Bruno"
    };

    public static void gerarBaseOrdenadaEmDisco(int qtd) throws IOException {
        if (qtd != 10 && qtd != 100 && qtd != 1000) {
            throw new IllegalArgumentException("Quantidade inválida: " + qtd);
        }
        try (
            BufferedWriter gat = new BufferedWriter(new FileWriter(ARQ_GATOS));
            BufferedWriter ado = new BufferedWriter(new FileWriter(ARQ_ADOCOES))
        ) {
            Random rnd = new Random();
            for (int id = 1; id <= qtd; id++) {
                String nome = NOMES[rnd.nextInt(NOMES.length)];
                String raca = RACAS[rnd.nextInt(RACAS.length)];
                int idade = rnd.nextInt(20)+1;
                String sexo = SEXOS[rnd.nextInt(SEXOS.length)];
                boolean adotado = rnd.nextBoolean();

                String linhaGato = String.join(";", 
                    String.valueOf(id), nome, raca, String.valueOf(idade), sexo, String.valueOf(adotado));
                gat.write(linhaGato); gat.newLine();

                if (adotado) {
                    String adotante = ADOTANTES[rnd.nextInt(ADOTANTES.length)];
                    String data = LocalDate.now()
                        .minusDays(rnd.nextInt(365))
                        .format(DateTimeFormatter.ISO_DATE);
                    String linhaAdo = String.join(";",
                        String.valueOf(id), adotante, data);
                    ado.write(linhaAdo); ado.newLine();
                }
            }
        }
    }

    public static void bubbleSortEmDisco(String arquivo) throws IOException {
        File orig = new File(arquivo);
        boolean trocou;
        do {
            trocou = false;
            File tmp = new File(arquivo + ".tmp");
            try (
                BufferedReader r = new BufferedReader(new FileReader(orig));
                BufferedWriter w = new BufferedWriter(new FileWriter(tmp))
            ) {
                String prev = r.readLine();
                if (prev == null) break;
                while (true) {
                    String curr = r.readLine();
                    if (curr == null) {
                        w.write(prev); w.newLine();
                        break;
                    }
                    int k1 = parseKey(prev), k2 = parseKey(curr);
                    if (k1 > k2) {
                        w.write(curr); w.newLine();
                        w.write(prev); w.newLine();
                        trocou = true;
                        prev = r.readLine();
                        if (prev == null) break;
                    } else {
                        w.write(prev); w.newLine();
                        prev = curr;
                    }
                }
            }
            if (!orig.delete()) throw new IOException("Falha ao deletar " + arquivo);
            if (!tmp.renameTo(orig)) throw new IOException("Falha ao renomear " + tmp);
        } while (trocou);
    }

    private static int parseKey(String linha) {
        try { return Integer.parseInt(linha.split(";",2)[0].trim()); }
        catch(Exception e){ return Integer.MAX_VALUE; }
    }

    public static void desordenarArquivo(String arquivo) throws IOException {
        int total = contarLinhas(arquivo);
        Random rnd = new Random();
        for (int i = total-1; i>0; i--) {
            int j = rnd.nextInt(i+1);
            if (i!=j) swapLinhasEmDisco(arquivo, i, j);
        }
    }

    private static int contarLinhas(String arquivo) throws IOException {
        try (BufferedReader r = new BufferedReader(new FileReader(arquivo))) {
            int c=0; while(r.readLine()!=null) c++;
            return c;
        }
    }

    private static void swapLinhasEmDisco(String arquivo, int pos1, int pos2)
            throws IOException {
        if (pos1>pos2) { int t=pos1; pos1=pos2; pos2=t; }

        String l1=null, l2=null;
        try (BufferedReader r = new BufferedReader(new FileReader(arquivo))) {
            String line; int idx=0;
            while((line=r.readLine())!=null) {
                if (idx==pos1) l1=line;
                if (idx==pos2) { l2=line; break; }
                idx++;
            }
        }
        if (l1==null||l2==null) return;

        File orig = new File(arquivo);
        File tmp  = new File(arquivo+".swap");
        try (
            BufferedReader r = new BufferedReader(new FileReader(orig));
            BufferedWriter w = new BufferedWriter(new FileWriter(tmp))
        ) {
            String line; int idx=0;
            while((line=r.readLine())!=null) {
                if (idx==pos1) w.write(l2);
                else if(idx==pos2) w.write(l1);
                else w.write(line);
                w.newLine(); idx++;
            }
        }
        if (!orig.delete()) throw new IOException("Erro ao deletar original");
        if (!tmp.renameTo(orig)) throw new IOException("Erro ao renomear swap");
    }

    public static void verificarOuGerarOuDesordenar() throws IOException {
        File fG = new File(ARQ_GATOS), fA = new File(ARQ_ADOCOES);
        Scanner sc = new Scanner(System.in);
        if (!fG.exists()||!fA.exists()) {
            System.out.println("Não existem arquivos. Quantos registros gerar? (10/100/1000)");
            int qtd = sc.nextInt();
            gerarBaseOrdenadaEmDisco(qtd);
            System.out.println("Base gerada e ordenada.");
        }
        else {
            if (estaOrdenadoPorId(ARQ_GATOS)) {
                System.out.print("Já está ordenado. Desordenar? (s/n): ");
                if (sc.nextLine().equalsIgnoreCase("s")) {
                    desordenarArquivo(ARQ_GATOS);
                    desordenarArquivo(ARQ_ADOCOES);
                    System.out.println("Arquivos desordenados.");
                }
            } else {
                System.out.print("Está desordenado. Ordenar? (s/n): ");
                if (sc.nextLine().equalsIgnoreCase("s")) {
                    bubbleSortEmDisco(ARQ_GATOS);
                    bubbleSortEmDisco(ARQ_ADOCOES);
                    System.out.println("Arquivos ordenados.");
                }
            }
        }
    }

    private static boolean estaOrdenadoPorId(String arquivo) throws IOException {
        try (BufferedReader r = new BufferedReader(new FileReader(arquivo))) {
            String prev = null, curr;
            while ((curr=r.readLine())!=null) {
                if (prev!=null && parseKey(prev)>parseKey(curr)) return false;
                prev=curr;
            }
            return true;
        }
    }
}
