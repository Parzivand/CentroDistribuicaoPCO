package codigo.domain;

import java.util.ArrayList;
import java.util.List;

public class Encomenda {
    
    private String referencia;
    private Loja loja;
    private int prioridade;      // 1 (baixa) a 5 (alta)
    private String estado;       // POR_PREPARAR, PREPARADA, POR_EXPEDIR, EXPEDIDA
    private final List<LinhaEncomenda> linhas = new ArrayList<>();

    public Encomenda(String referencia, Loja loja, int prioridade) {
        if (prioridade < 1 || prioridade > 5) {
            throw new IllegalArgumentException("Prioridade deve ser entre 1 (baixa) e 5 (alta)");
        }
        
        this.referencia = referencia;
        this.loja = loja;
        this.prioridade = prioridade;
        this.estado = "POR_PREPARAR";
    }

    // Adicionar linha à encomenda
    public void adicionarLinha(Produto produto, int quantidade) {
        linhas.add(new LinhaEncomenda(produto, quantidade));
    }

    // Getters
    public String getReferencia() { return referencia; }
    public Loja getLoja() { return loja; }
    public int getPrioridade() { return prioridade; }
    public String getEstado() { return estado; }
    public List<LinhaEncomenda> getLinhas() { return new ArrayList<>(linhas); }

    // Setters
    public void setEstado(String estado) { this.estado = estado; }
    
    // Métodos úteis para prioridade
    public boolean isAltaPrioridade() {
        return prioridade >= 4; // 4 e 5 = alta prioridade
    }

    public String getPrioridadeTexto() {
        return switch (prioridade) {
            case 1 -> "★☆☆☆☆ Baixa";
            case 2 -> "★★☆☆☆";
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
}
