package codigo.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import codigo.domain.enums.Estadoencomenda;

public class Encomenda {
    
    private String referencia;
    private Loja loja;
    private int prioridade;      // 1 (baixa) a 5 (alta)
    private Estadoencomenda estado;       // POR_PREPARAR, PREPARADA, POR_EXPEDIR, EXPEDIDA
    private final List<LinhaEncomenda> linhas = new ArrayList<>();

    public Encomenda(String referencia, Loja loja, int prioridade) {
        if (prioridade < 1 || prioridade > 5) {
            throw new IllegalArgumentException("Prioridade deve ser entre 1 (baixa) e 5 (alta)");
        }
        
        this.referencia = referencia;
        this.loja = loja;
        this.prioridade = prioridade;
        this.estado = Estadoencomenda.POR_PREPARAR;
    }

    // Adicionar linha à encomenda
    public void adicionarLinha(String nome, int quantidade){
        linhas.add(new LinhaEncomenda(nome, quantidade));
    } 

    // Getters
    public String getReferencia() { return referencia; }
    public Loja getLoja() { return loja; }
    public int getPrioridade() { return prioridade; }
    public Estadoencomenda getEstado() { return estado; }
    public List<LinhaEncomenda> getLinhas() { return new ArrayList<>(linhas); }

    // Setters
    public void setEstado(Estadoencomenda estado) { this.estado = estado; }
    
    // Métodos úteis para prioridade
    public boolean isAltaPrioridade() {
        return prioridade >= 4; // 4 e 5 = alta prioridade
    }
    
    public String getPrioridadeTexto() {
        return switch (prioridade) {
            case 1 -> "★☆☆☆☆ Baixa";
            case 2 -> "★★☆☆☆ Baixa" ;
            case 3 -> "★★★☆☆ Média";
            case 4 -> "★★★★☆ Alta";
            case 5 -> "★★★★★ Alta";
            default -> "Inválida";
        };
    }

    @Override
    public String toString() {
        return String.format("Encomenda{%s | Loja: %s | Prioridade: %s | Linhas: %d}",
                referencia, loja.getNome(), getPrioridadeTexto(), linhas.size());
    }
   
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Expedicao)) return false;
        Encomenda encomenda = (Encomenda) obj;
        return Objects.equals(referencia, encomenda.getReferencia());

    }
    @Override 
    public int hashCode() {
        // TODO Auto-generated method stub
        return Objects.hashCode(referencia);
    }
}
