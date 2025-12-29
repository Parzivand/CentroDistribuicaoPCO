package codigo.domain;
import java.util.Objects;
public class Tarefa {

    private String id;           // EXP-0001-T1
    private String descricao;    // "Selecionar 10 litro(s) (LAC-0000001) de A1"
    private boolean concluida = false;


    public Tarefa(String id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }


        public void concluir() { 
        this.concluida = true; 
    }
    
    public boolean isConcluida() { 
        return concluida; 
    }


    // getters/setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tarefa tarefa = (Tarefa) o;

        return Objects.equals(id, tarefa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String status = concluida ? "✅" : "⏳";
        return String.format("%s [%s] %s", status, id, descricao);
    }
}