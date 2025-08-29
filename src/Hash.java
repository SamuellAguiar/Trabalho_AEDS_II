import java.io.*;
import java.util.*;

public class Hash {
     private static final String ARQUIVO_GATOS = "gatos.txt";
     private int BUCKETS_POR_ARQUIVO = 10; // buckets por arquivo
     private int M; // total de buckets (dinâmico)
     private int numArquivos; // total de arquivos hash

     public Hash(int tamanhoBase) throws IOException {
          inicializarArquivos(tamanhoBase);
     }

     // Inicializa arquivos hash de acordo com o tamanho da base
     private void inicializarArquivos(int tamanhoBase) throws IOException {
          if (tamanhoBase <= 10) {
               M = 10;
          } else if (tamanhoBase <= 100) {
               M = 20; // ajuste para base média
          } else {
               M = 100; // base grande
          }

          numArquivos = (int) Math.ceil((double) M / BUCKETS_POR_ARQUIVO);

          System.out.println("Inicializando tabela hash:");
          System.out.println("- Tamanho da base: " + tamanhoBase);
          System.out.println("- Número de buckets: " + M);
          System.out.println("- Buckets por arquivo: " + BUCKETS_POR_ARQUIVO);
          System.out.println("- Número de arquivos hash: " + numArquivos);

          // Criar arquivos hash
          for (int i = 0; i < numArquivos; i++) {
               File f = new File("tabelaHash_" + i + ".dat");
               if (!f.exists()) {
                    try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(f))) {
                         int bucketsNoArquivo = BUCKETS_POR_ARQUIVO;
                         if (i == numArquivos - 1 && M % BUCKETS_POR_ARQUIVO != 0) {
                              bucketsNoArquivo = M % BUCKETS_POR_ARQUIVO; // último arquivo pode ter menos
                         }
                         for (int j = 0; j < bucketsNoArquivo; j++) {
                              dos.writeLong(-1); // inicializa bucket vazio
                         }
                    }
               }
          }
     }

     // Função hash
     private int hash(int id) {
          return id % M;
     }

     // Retorna nome do arquivo que contém o bucket
     private String getNomeArquivo(int bucket) {
          int indiceArquivo = bucket / BUCKETS_POR_ARQUIVO;
          return "tabelaHash_" + indiceArquivo + ".dat";
     }

     // Retorna posição do bucket dentro do arquivo
     private int getPosicaoNoArquivo(int bucket) {
          return bucket % BUCKETS_POR_ARQUIVO;
     }

     // Ler todos os gatos do arquivo
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

     // Escrever todos os gatos no arquivo
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

          // Buscar posição do gato na base
          int posGato = -1;
          for (int i = 0; i < linhas.size(); i++) {
               Gato g = Gato.fromCSV(linhas.get(i));
               if (g.getId() == novoGato.getId()) {
                    posGato = i;
                    break;
               }
          }

          if (posGato == -1) {
               System.out.println("Erro: Gato não encontrado na base. Cadastre-o antes de inserir na hash.");
               return;
          }

          // Calcular bucket
          int bucket = hash(novoGato.getId());
          String nomeArquivo = getNomeArquivo(bucket);
          int posNoArquivo = getPosicaoNoArquivo(bucket);

          try (RandomAccessFile raf = new RandomAccessFile(nomeArquivo, "rw")) {
               raf.seek(posNoArquivo * 8L);
               long ponteiroBucket = raf.readLong();

               if (ponteiroBucket == -1) {
                    // Bucket vazio → aponta direto para o gato
                    raf.seek(posNoArquivo * 8L);
                    raf.writeLong(posGato);
               } else {
                    // Colisão → percorrer lista encadeada
                    long anterior = -1;
                    long cursor = ponteiroBucket;
                    while (cursor != -1) {
                         Gato gAtual = Gato.fromCSV(linhas.get((int) cursor));
                         if (gAtual.getId() == novoGato.getId()) {
                              System.out.println("Erro: ID " + novoGato.getId() + " já está na hash!");
                              return;
                         }
                         anterior = cursor;
                         cursor = gAtual.getProximo();
                    }
                    // Atualizar último nó da lista encadeada
                    Gato gUltimo = Gato.fromCSV(linhas.get((int) anterior));
                    gUltimo.setProximo(posGato);
                    linhas.set((int) anterior, gUltimo.toCSV());
                    escreverGatos(linhas);
               }
          }

          System.out.println("Gato " + novoGato.getId() + " inserido com sucesso no bucket " + bucket);
     }

     // Buscar gato por ID
     public Gato buscar(int id) throws IOException {
          List<String> linhas = lerGatos();
          int bucket = hash(id);
          String nomeArquivo = getNomeArquivo(bucket);
          int posNoArquivo = getPosicaoNoArquivo(bucket);

          try (RandomAccessFile raf = new RandomAccessFile(nomeArquivo, "r")) {
               raf.seek(posNoArquivo * 8L);
               long ponteiro = raf.readLong();

               if (ponteiro == -1) {
                    System.out.println("Bucket vazio. Registro não encontrado.");
                    return null;
               }

               long cursor = ponteiro;
               while (cursor != -1) {
                    Gato g = Gato.fromCSV(linhas.get((int) cursor));
                    if (g.getId() == id) {
                         return g;
                    }
                    cursor = g.getProximo();
               }
          }

          System.out.println("Registro com ID " + id + " não encontrado na hash.");
          return null;
     }

     // Remover gato por ID
     public boolean remover(int id) throws IOException {
          List<String> linhas = lerGatos(); // 1. Carrega todos os gatos do arquivo "gatos.txt"
          int bucket = hash(id); // 2. Descobre o bucket do gato
          String nomeArquivo = getNomeArquivo(bucket);
          int posNoArquivo = getPosicaoNoArquivo(bucket);

          try (RandomAccessFile raf = new RandomAccessFile(nomeArquivo, "rw")) {
               raf.seek(posNoArquivo * 8L); // 3. Vai até o bucket no arquivo de índice
               long ponteiro = raf.readLong();// 4. Lê o ponteiro do bucket

               // ----- Caso 1: Bucket vazio -----
               if (ponteiro == -1) {
                    System.out.println("Bucket vazio. Registro não encontrado.");
                    return false;
               }

               long anterior = -1; // 5. Guarda o nó anterior (inicialmente nenhum)
               long cursor = ponteiro; // 6. Começa a busca pelo primeiro gato do bucket

               while (cursor != -1) { // 7. Percorre a lista encadeada
                    Gato g = Gato.fromCSV(linhas.get((int) cursor));

                    // ----- Caso 2: Gato encontrado -----
                    if (g.getId() == id) {
                         // Subcaso A: Gato está no início da lista
                         if (anterior == -1) {
                              raf.seek(posNoArquivo * 8L);
                              raf.writeLong(g.getProximo());
                         } else {
                              // Subcaso B: Gato está no meio ou fim
                              Gato gAnterior = Gato.fromCSV(linhas.get((int) anterior));
                              gAnterior.setProximo(g.getProximo());
                              linhas.set((int) anterior, gAnterior.toCSV());
                         }

                         // Marca o nó removido como "desligado" da lista
                         g.setProximo(-1);
                         linhas.set((int) cursor, g.toCSV());
                         escreverGatos(linhas);

                         System.out.println("Registro com ID " + id + " removido com sucesso.");
                         return true;
                    }

                    // ----- Caso 3: Não é o gato ainda → segue para o próximo -----
                    anterior = cursor;
                    cursor = g.getProximo();
               }
          }

          // ----- Caso 4: Chegou ao fim da lista e não achou -----
          System.out.println("Registro com ID " + id + " não encontrado na hash.");
          return false;
     }

     // Imprimir tabela hash completa
     public void imprimirTabela() throws IOException {
          List<String> linhas = lerGatos();

          System.out.println("\n--- Conteúdo da Tabela Hash ---");
          for (int i = 0; i < numArquivos; i++) {
               String nomeArquivo = "tabelaHash_" + i + ".dat";
               try (RandomAccessFile raf = new RandomAccessFile(nomeArquivo, "r")) {
                    System.out.println("Arquivo: " + nomeArquivo);
                    for (int j = 0; j < BUCKETS_POR_ARQUIVO; j++) {
                         int bucket = i * BUCKETS_POR_ARQUIVO + j;
                         if (bucket >= M)
                              break;

                         raf.seek(j * 8L);
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
                    System.out.println("-----------------------------");
               }
          }
     }
}
