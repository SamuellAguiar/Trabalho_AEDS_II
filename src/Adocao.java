public class Adocao {
     private int idGato;
     private String nomeAdotante;
     private String dataAdocao;

     public Adocao(int idGato, String nomeAdotante, String dataAdocao) {
          this.idGato = idGato;
          this.nomeAdotante = nomeAdotante;
          this.dataAdocao = dataAdocao;
     }

     public String getNomeAdotante() {
          return nomeAdotante;
     }

     public String getDataAdocao() {
          return dataAdocao;
     }
     
     public int getIdGato() {
          return idGato;
     }

     public String toCSV() {
          return idGato + ";" + nomeAdotante + ";" + dataAdocao;
     }

     public static Adocao fromCSV(String linha) {
          String[] partes = linha.split(";");
          return new Adocao(
                    Integer.parseInt(partes[0]),
                    partes[1],
                    partes[2]);
     }

     @Override
     public String toString() {
          return "ID Gato: " + idGato + " | Adotante: " + nomeAdotante + " | Data: " + dataAdocao;
     }
}
