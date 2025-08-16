import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

// A classe BPlusNode permanece a mesma.
class BPlusNode {
     int nChaves;
     int[] chaves;
     long[] filhos;
     boolean folha;
     long proxFolha;

     public BPlusNode(int ordem, boolean folha) {
          this.nChaves = 0;
          this.chaves = new int[2 * ordem];
          this.filhos = new long[2 * ordem + 1];
          this.folha = folha;
          this.proxFolha = -1;
     }

     public String toLine(int ordem) {
          StringBuilder sb = new StringBuilder();
          sb.append(folha).append(";");
          sb.append(nChaves).append(";");
          for (int i = 0; i < 2 * ordem; i++)
               sb.append(chaves[i]).append(",");
          sb.append(";");
          for (int i = 0; i < 2 * ordem + 1; i++)
               sb.append(filhos[i]).append(",");
          sb.append(";");
          sb.append(proxFolha);
          return sb.toString();
     }

     public static BPlusNode fromLine(String line, int ordem) {
          String[] parts = line.split(";");
          boolean isFolha = Boolean.parseBoolean(parts[0]);
          BPlusNode n = new BPlusNode(ordem, isFolha);
          n.nChaves = Integer.parseInt(parts[1]);
          String[] ch = parts[2].split(",");
          for (int i = 0; i < ch.length && i < 2 * ordem; i++)
               n.chaves[i] = Integer.parseInt(ch[i]);
          String[] fl = parts[3].split(",");
          for (int i = 0; i < fl.length && i < 2 * ordem + 1; i++)
               n.filhos[i] = Long.parseLong(fl[i]);
          n.proxFolha = Long.parseLong(parts[4]);
          return n;
     }
}

public class ArvoreBPlus {
     private int ordem = 2;
     private RandomAccessFile arvoreFile;
     private RandomAccessFile dadosFile;
     private long raiz;

     public ArvoreBPlus(String arvFileName, String dadosFileName) throws IOException {
          arvoreFile = new RandomAccessFile(arvFileName, "rw");
          dadosFile = new RandomAccessFile(dadosFileName, "r");
          if (arvoreFile.length() == 0) {
               BPlusNode root = new BPlusNode(ordem, true);
               raiz = escreverNo(root, 0); // A raiz sempre começa na posição 0
          } else {
               raiz = 0; // Assume que a raiz sempre está na posição 0
          }
     }

     // --- MÉTODO DE ESCRITA CORRIGIDO ---
     private long escreverNo(BPlusNode n, long pos) throws IOException {
          arvoreFile.seek(pos);
          String linhaNo = n.toLine(ordem);
          // Padroniza a linha para ter um tamanho fixo para sobrescrever corretamente
          linhaNo = String.format("%-100s", linhaNo);
          arvoreFile.writeBytes(linhaNo + "\n");
          return pos;
     }

     // Novo método para encontrar um espaço livre ou alocar no final
     private long encontrarEspacoLivre() throws IOException {
          // Lógica simplificada: sempre escreve no final.
          // Um sistema real teria uma lista de espaços livres.
          return arvoreFile.length();
     }

     private BPlusNode lerNo(long pos) throws IOException {
          arvoreFile.seek(pos);
          String line = arvoreFile.readLine();
          if (line == null)
               return null;
          return BPlusNode.fromLine(line.trim(), ordem);
     }

     public Gato buscar(int id) throws IOException {
          long posFolha = buscarPosFolha(raiz, id);
          if (posFolha == -1)
               return null;

          BPlusNode folha = lerNo(posFolha);
          if (folha == null)
               return null;

          for (int i = 0; i < folha.nChaves; i++) {
               if (folha.chaves[i] == id) {
                    dadosFile.seek(folha.filhos[i]);
                    String linha = dadosFile.readLine();
                    return Gato.fromCSV(linha);
               }
          }
          return null;
     }

     private long buscarPosFolha(long posNode, int id) throws IOException {
          if (posNode == -1)
               return -1;

          BPlusNode node = lerNo(posNode);
          if (node == null || node.folha)
               return posNode;

          int i = 0;
          while (i < node.nChaves && id >= node.chaves[i]) {
               i++;
          }
          return buscarPosFolha(node.filhos[i], id);
     }

     public void inserir(int id, long posDado) throws IOException {
          if (buscar(id) != null) {
               System.out.println("AVISO: Gato com ID " + id + " já existe. Inserção ignorada.");
               return;
          }

          long posFolha = buscarPosFolha(raiz, id);
          BPlusNode folha = lerNo(posFolha);

          if (folha.nChaves < 2 * ordem) {
               int i = 0;
               while (i < folha.nChaves && id > folha.chaves[i])
                    i++;
               for (int j = folha.nChaves; j > i; j--) {
                    folha.chaves[j] = folha.chaves[j - 1];
                    folha.filhos[j] = folha.filhos[j - 1];
               }
               folha.chaves[i] = id;
               folha.filhos[i] = posDado;
               folha.nChaves++;
               escreverNo(folha, posFolha);
          } else {
               System.out.println("AVISO: Inserção falhou. Nó cheio e split não implementado.");
          }
     }

     // --- REMOÇÃO COM ATUALIZAÇÃO CORRETA ---
     public void remover(int id) throws IOException {
          removerRec(raiz, -1, -1, id);
     }

     private void removerRec(long posNode, long posParent, int parentIndex, int id) throws IOException {
          if (posNode == -1)
               return;

          BPlusNode node = lerNo(posNode);
          int i = 0;
          while (i < node.nChaves && id > node.chaves[i]) {
               i++;
          }

          if (node.folha) {
               if (i < node.nChaves && node.chaves[i] == id) {
                    for (int j = i; j < node.nChaves - 1; j++) {
                         node.chaves[j] = node.chaves[j + 1];
                         node.filhos[j] = node.filhos[j + 1];
                    }
                    node.nChaves--;
                    node.chaves[node.nChaves] = 0;
                    node.filhos[node.nChaves] = 0;

                    // A correção chave: SEMPRE sobrescreve a posição original do nó
                    escreverNo(node, posNode);

                    if (posNode != raiz && node.nChaves < ordem) {
                         handleUnderflow(posNode, posParent, parentIndex);
                    }
               }
          } else {
               long posFilho = (i < node.nChaves && id == node.chaves[i]) ? node.filhos[i + 1] : node.filhos[i];
               removerRec(posFilho, posNode, i, id);
          }
     }

     // ... (O resto dos métodos handleUnderflow, redistribuir, concatenar permanecem
     // os mesmos)
     private void handleUnderflow(long posNode, long posParent, int parentIndex) throws IOException {
          BPlusNode parent = lerNo(posParent);
          if (parentIndex > 0) {
               long posIrmaoEsq = parent.filhos[parentIndex - 1];
               BPlusNode irmaoEsq = lerNo(posIrmaoEsq);
               if (irmaoEsq.nChaves > ordem) {
                    redistribuir(posIrmaoEsq, posNode, posParent, parentIndex - 1, true);
                    return;
               }
          }
          if (parentIndex < parent.nChaves) {
               long posIrmaoDir = parent.filhos[parentIndex + 1];
               BPlusNode irmaoDir = lerNo(posIrmaoDir);
               if (irmaoDir.nChaves > ordem) {
                    redistribuir(posNode, posIrmaoDir, posParent, parentIndex, false);
                    return;
               }
          }
          if (parentIndex > 0) {
               concatenar(parent.filhos[parentIndex - 1], posNode, posParent, parentIndex - 1);
          } else {
               concatenar(posNode, parent.filhos[parentIndex + 1], posParent, parentIndex);
          }
     }

     private void redistribuir(long posEsq, long posDir, long posParent, int parentKeyIndex, boolean isFromLeft)
               throws IOException {
          BPlusNode esq = lerNo(posEsq);
          BPlusNode dir = lerNo(posDir);
          BPlusNode parent = lerNo(posParent);
          if (isFromLeft) {
               for (int i = dir.nChaves; i > 0; i--) {
                    dir.chaves[i] = dir.chaves[i - 1];
                    dir.filhos[i] = dir.filhos[i - 1];
               }
               dir.chaves[0] = esq.chaves[esq.nChaves - 1];
               dir.filhos[0] = esq.filhos[esq.nChaves - 1];
               dir.nChaves++;
               esq.nChaves--;
               parent.chaves[parentKeyIndex] = dir.chaves[0];
          } else {
               esq.chaves[esq.nChaves] = dir.chaves[0];
               esq.filhos[esq.nChaves] = dir.filhos[0];
               esq.nChaves++;
               for (int i = 0; i < dir.nChaves - 1; i++) {
                    dir.chaves[i] = dir.chaves[i + 1];
                    dir.filhos[i] = dir.filhos[i + 1];
               }
               dir.nChaves--;
               parent.chaves[parentKeyIndex] = dir.chaves[0];
          }
          escreverNo(esq, posEsq);
          escreverNo(dir, posDir);
          escreverNo(parent, posParent);
     }

     private void concatenar(long posEsq, long posDir, long posParent, int parentKeyIndex) throws IOException {
          BPlusNode esq = lerNo(posEsq);
          BPlusNode dir = lerNo(posDir);
          BPlusNode parent = lerNo(posParent);
          for (int i = 0; i < dir.nChaves; i++) {
               esq.chaves[esq.nChaves] = dir.chaves[i];
               esq.filhos[esq.nChaves] = dir.filhos[i];
               esq.nChaves++;
          }
          if (esq.folha) {
               esq.proxFolha = dir.proxFolha;
          }
          for (int i = parentKeyIndex; i < parent.nChaves - 1; i++) {
               parent.chaves[i] = parent.chaves[i + 1];
          }
          for (int i = parentKeyIndex + 1; i < parent.nChaves; i++) {
               parent.filhos[i] = parent.filhos[i + 1];
          }
          parent.nChaves--;
          escreverNo(esq, posEsq);
          escreverNo(parent, posParent);
          if (posParent != raiz && parent.nChaves < ordem) {
               System.out.println("AVISO: Underflow no nó pai. Propagação não implementada.");
          }
          if (posParent == raiz && parent.nChaves == 0) {
               raiz = posEsq;
          }
     }
}