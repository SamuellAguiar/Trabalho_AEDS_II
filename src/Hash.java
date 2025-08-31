import java.io.*;
import java.util.*;

public class Hash {
     private static final String ARQUIVO_GATOS = "gatos.txt";
     private static final String ARQUIVO_HASH = "hash.dat";
     private int M; // total de buckets

     public Hash(int tamanhoBase) throws IOException {
          inicializarArquivo(tamanhoBase);
     }

     // Inicializa arquivo hash com M buckets
     private void inicializarArquivo(int tamanhoBase) throws IOException {
          if (tamanhoBase <= 10) {
               M = 10;
          } else if (tamanhoBase <= 100) {
               M = 20;
          } else {
               M = 100;
          }

          System.out.println("Inicializando tabela hash:");
          System.out.println("- Tamanho da base: " + tamanhoBase);
          System.out.println("- Número de buckets: " + M);

          File f = new File(ARQUIVO_HASH);
          if (!f.exists()) {
               try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(f))) {
                    for (int j = 0; j < M; j++) {
                         dos.writeLong(-1); // bucket vazio
                    }
               }
          }
     }

     // Função hash
     private int hash(int id) {
          return id % M;
     }

     // Lê todos os gatos do arquivo
     private List<String> lerGatos() throws IOException {
          List<String> linhas = new ArrayList<>();
          try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_GATOS))) {
               String l;
               while ((l = br.readLine()) != null) {
                    linhas.add(l);
               }
          }
          return linhas;
     }

     // Reescreve todos os gatos no arquivo
     private void escreverGatos(List<String> linhas) throws IOException {
          try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_GATOS))) {
               for (String l : linhas) {
                    bw.write(l);
                    bw.newLine();
               }
          }
     }

     // Inserir gato
     public void inserirGato(Gato novoGato) throws IOException {
          List<String> linhas = lerGatos();

          int posGato = -1;
          for (int i = 0; i < linhas.size(); i++) {
               Gato g = Gato.fromCSV(linhas.get(i));
               if (g.getId() == novoGato.getId()) {
                    posGato = i;
                    break;
               }
          }

          if (posGato == -1) {
               System.out.println("Erro: Gato não encontrado na base.");
               return;
          }

          int bucket = hash(novoGato.getId());

          try (RandomAccessFile raf = new RandomAccessFile(ARQUIVO_HASH, "rw")) {
               long pos = bucket * 8L;
               raf.seek(pos);
               long ponteiroBucket = raf.readLong();

               if (ponteiroBucket == -1) {
                    raf.seek(pos);
                    raf.writeLong(posGato);
               } else {
                    long anterior = -1;
                    long cursor = ponteiroBucket;
                    while (cursor != -1) {
                         Gato gAtual = Gato.fromCSV(linhas.get((int) cursor));
                         if (gAtual.getId() == novoGato.getId()) {
                              System.out.println("Erro: ID já existe na hash!");
                              return;
                         }
                         anterior = cursor;
                         cursor = gAtual.getProximo();
                    }
                    Gato gUltimo = Gato.fromCSV(linhas.get((int) anterior));
                    gUltimo.setProximo(posGato);
                    linhas.set((int) anterior, gUltimo.toCSV());
                    escreverGatos(linhas);
               }
          }

          System.out.println("Gato " + novoGato.getId() + " inserido no bucket " + bucket);
     }

     // Buscar gato
     public Gato buscar(int id) throws IOException {
          List<String> linhas = lerGatos();
          int bucket = hash(id);

          try (RandomAccessFile raf = new RandomAccessFile(ARQUIVO_HASH, "r")) {
               long pos = bucket * 8L;
               raf.seek(pos);
               long ponteiro = raf.readLong();

               long cursor = ponteiro;
               while (cursor != -1) {
                    Gato g = Gato.fromCSV(linhas.get((int) cursor));
                    if (g.getId() == id) {
                         return g;
                    }
                    cursor = g.getProximo();
               }
          }

          System.out.println("Registro não encontrado.");
          return null;
     }

     // Remover gato
     public boolean remover(int id) throws IOException {
          List<String> linhas = lerGatos();
          int bucket = hash(id);

          try (RandomAccessFile raf = new RandomAccessFile(ARQUIVO_HASH, "rw")) {
               long pos = bucket * 8L;
               raf.seek(pos);
               long ponteiro = raf.readLong();

               if (ponteiro == -1) {
                    System.out.println("Bucket vazio.");
                    return false;
               }

               long anterior = -1;
               long cursor = ponteiro;

               while (cursor != -1) {
                    Gato g = Gato.fromCSV(linhas.get((int) cursor));

                    if (g.getId() == id) {
                         if (anterior == -1) {
                              raf.seek(pos);
                              raf.writeLong(g.getProximo());
                         } else {
                              Gato gAnterior = Gato.fromCSV(linhas.get((int) anterior));
                              gAnterior.setProximo(g.getProximo());
                              linhas.set((int) anterior, gAnterior.toCSV());
                         }

                         g.setProximo(-1);
                         linhas.set((int) cursor, g.toCSV());
                         escreverGatos(linhas);

                         System.out.println("Registro removido.");
                         return true;
                    }

                    anterior = cursor;
                    cursor = g.getProximo();
               }
          }
          return false;
     }

     // Imprimir tabela hash
     public void imprimirTabela() throws IOException {
          List<String> linhas = lerGatos();

          System.out.println("\n--- Conteúdo da Hash ---");
          try (RandomAccessFile raf = new RandomAccessFile(ARQUIVO_HASH, "r")) {
               for (int bucket = 0; bucket < M; bucket++) {
                    long pos = bucket * 8L;
                    raf.seek(pos);
                    long ponteiro = raf.readLong();

                    System.out.print("Bucket " + bucket + ": ");
                    if (ponteiro == -1) {
                         System.out.println("[vazio]");
                    } else {
                         long atual = ponteiro;
                         while (atual != -1) {
                              Gato g = Gato.fromCSV(linhas.get((int) atual));
                              System.out.print(" -> " + g.getId());
                              atual = g.getProximo();
                         }
                         System.out.println();
                    }
               }
          }
     }
}
