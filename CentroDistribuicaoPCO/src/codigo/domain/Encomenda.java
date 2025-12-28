package codigo.domain;

import java.util.ArrayList;
import java.util.List;

public class Encomenda {

    private String referencia;
    private Loja loja;
    private boolean prioridade;
    private String estado; // por preparar, preparada, expedida...
    private final List<LinhaEncomenda> linhas = new ArrayList<>();

    public Encomenda(String referencia, Loja loja, boolean prioridade) {
        this.referencia = referencia;
        this.loja = loja;
        this.prioridade = prioridade;
        this.estado = "POR_PREPARAR";
    }

    public void adicionarLinha(Produto produto, int quantidade) {
        linhas.add(new LinhaEncomenda(produto, quantidade));
    }

    public List<LinhaEncomenda> getLinhas() {
        return new ArrayList<>(linhas);
    }

    // getters/setters restantes
}
