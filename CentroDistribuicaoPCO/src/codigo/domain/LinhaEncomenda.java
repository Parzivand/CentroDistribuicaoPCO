package java.domain;

public class LinhaEncomenda {
    private final Produto produto;
    private int quantidade;

    public LinhaEncomenda(Produto produto, int quantidade) {
        if (produto == null || quantidade <= 0) {
            throw new IllegalArgumentException("Produto não pode ser null e quantidade deve ser > 0");
        }
        this.produto = produto;
        this.quantidade = quantidade;
    }

    // Getters (convenção camelCase)
    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }

    // Setter corrigido (recebe parâmetro!)
    public void setQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser > 0");
        }
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return String.format("LinhaEncomenda{produto=%s, quantidade=%d}",
                produto.getSKU() + " - " + produto.getNome(), quantidade);
    }
}
