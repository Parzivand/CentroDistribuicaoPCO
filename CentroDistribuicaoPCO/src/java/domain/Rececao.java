package java.domain;

public class Rececao {

    // Atributos

    private Produto produto;
    private Fornecedor fornecedor;
    // Talvez adicionas quantidade

    // Construtor
    public Rececao(Produto produto, Fornecedor fornecedor) {
        this.produto = produto;
        this.fornecedor = fornecedor;
    }
    
    // Gets e Setters

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public Fornecedor getFornecedor() { return fornecedor; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }
    
}