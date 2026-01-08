package codigo.domain;

public class LinhaEncomenda {
    private final String produtoNome;
    private int quantidade;
    private String referenciaEncomenda;
    private int quantidadePendente=0;

    public LinhaEncomenda(String nome, int quantidade) {
        if (nome == null || quantidade <= 0) {
            throw new IllegalArgumentException("Produto não pode ser null e quantidade deve ser > 0");
        }
        this.produtoNome = nome;
        this.quantidade = quantidade;
    }

    // Getters (convenção camelCase)
    public String getProduto() { return produtoNome; }
    public int getQuantidade() { return quantidade; }
    public String getreferenciaencomenda(){return referenciaEncomenda;}
    // acho que é util para a encomenda para o registo da encomenda
    public int getquantidadependente(){return quantidadePendente;}
    
    // Setter corrigido (recebe parâmetro!)
    public void setQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser > 0");
        }
        this.quantidade = quantidade;
    }
    public void setquantidadePendente(int quantidadePendente){this.quantidadePendente=quantidadePendente;}
    
    @Override
    public String toString() {
        return String.format("LinhaEncomenda{produto=%s, quantidade=%d}",
                produtoNome, quantidade);
    }
}
