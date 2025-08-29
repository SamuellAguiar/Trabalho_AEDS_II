public class Gato {
     private int id;
     private String nome;
     private String raca;
     private int idade;
     private String sexo;
     private boolean adotado;
     private long proximo;

     public Gato(int id, String nome, String raca, int idade, String sexo, boolean adotado) {
          this.id = id;
          this.nome = nome;
          this.raca = raca;
          this.idade = idade;
          this.sexo = sexo;
          this.adotado = adotado;
          this.proximo = -1;
     }

     public int getId() {
          return id;
     }

     public boolean isAdotado() {
          return adotado;
     }

     public void setId(int id) {
          this.id = id;
     }

     public String getNome() {
          return nome;
     }

     public void setNome(String nome) {
          this.nome = nome;
     }

     public String getRaca() {
          return raca;
     }

     public void setRaca(String raca) {
          this.raca = raca;
     }

     public int getIdade() {
          return idade;
     }

     public void setIdade(int idade) {
          this.idade = idade;
     }

     public String getSexo() {
          return sexo;
     }

     public void setSexo(String sexo) {
          this.sexo = sexo;
     }

     public void setAdotado(boolean adotado) {
          this.adotado = adotado;
     }

     public String toCSV() {
          return id + "; " + nome + "; " + raca + "; " + idade + "; " + sexo + "; " + adotado + "; " + proximo;
     }

     public long getProximo() {
          return proximo;
     }

     public void setProximo(long proximo) {
          this.proximo = proximo;
     }

     public static Gato fromCSV(String linha) {
          String[] partes = linha.split(";");
          for (int i = 0; i < partes.length; i++) {
               partes[i] = partes[i].trim();
          }

          int id = Integer.parseInt(partes[0]);
          String nome = partes[1];
          String raca = partes[2];
          int idade = Integer.parseInt(partes[3]);
          String sexo = partes[4];
          boolean adotado = Boolean.parseBoolean(partes[5]);

          long prox = -1; // valor padrão
          if (partes.length >= 7) { // só pega se existir
               prox = Long.parseLong(partes[6]);
          }

          Gato g = new Gato(id, nome, raca, idade, sexo, adotado);
          g.setProximo(prox);
          return g;
     }

     public String toString() {
          return "ID: " + id + " | Nome: " + nome + " | Raça: " + raca + " | Idade: " + idade +
                    " | Sexo: " + sexo + " | Adotado: " + (adotado ? "Sim" : "Não") +
                    " | Próximo: " + proximo;
     }
}