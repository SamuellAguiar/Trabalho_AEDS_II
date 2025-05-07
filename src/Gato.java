public class Gato {
     private int id;
     private String nome;
     private String raca;
     private int idade;
     private String sexo;
     private boolean adotado;

     public Gato(int id, String nome, String raca, int idade, String sexo, boolean adotado) {
          this.id = id;
          this.nome = nome;
          this.raca = raca;
          this.idade = idade;
          this.sexo = sexo;
          this.adotado = adotado;
     }

     public int getId() {
          return id;
     }

     public boolean isAdotado() {
          return adotado;
     }

     public void setAdotado(boolean adotado) {
          this.adotado = adotado;
     }

     public String toCSV() {
          return id + "; " + nome + "; " + raca + "; " + idade + "; " + sexo + "; " + adotado;
     }

     public static Gato fromCSV(String linha) {
          String[] partes = linha.split(";");
          for (int i = 0; i < partes.length; i++) {
               partes[i] = partes[i].trim();
          }
          return new Gato(
                    Integer.parseInt(partes[0]),
                    partes[1],
                    partes[2],
                    Integer.parseInt(partes[3]),
                    partes[4],
                    Boolean.parseBoolean(partes[5]));
     }

     @Override
     public String toString() {
          return "ID: " + id + " | Nome: " + nome + " | Raça: " + raca + " | Idade: " + idade +
                    " | Sexo: " + sexo + " | Adotado: " + (adotado ? "Sim" : "Não");
     }
}
