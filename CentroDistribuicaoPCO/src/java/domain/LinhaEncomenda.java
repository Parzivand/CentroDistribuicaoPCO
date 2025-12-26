package java.domain;

public class LinhaEncomenda {
    private final Produto produto;
    private int quantidade;
    public LinhaEncomenda(Produto produto, int quantidade){
        this.produto= produto;
        this.quantidade= quantidade;
    } 
    public Produto getproduto(){return produto;}
    
    public int getquantidade(){return quantidade;}
    public void setquantidade(){this.quantidade=quantidade;}

    public String toString(){
        return String.format(" sku: nome:%s %s",produto.getSKU(),produto.getNome(),
        produto.);
    }
}
