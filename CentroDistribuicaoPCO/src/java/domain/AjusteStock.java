package java.domain;

public class AjusteStock {

    //Atributos

    private String estado, produto_codigo, id, localizacao;
    private boolean aprovacao;
    private int quantidade;

    //Construtor

    public AjusteStock(String estado, String produto_codigo, String id, String localizacao, boolean aprovacao, int quantidade) {
        this.estado = estado;
        this.produto_codigo = produto_codigo;
        this.id = id;
        this.localizacao = localizacao;
        this.aprovacao = aprovacao;
        this.quantidade = quantidade;
    }

    //Gets e Setters

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getProduto_codigo() { return produto_codigo; }
    public void setProduto_codigo(String produto_codigo) { this.produto_codigo = produto_codigo; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public boolean isAprovacao() { return aprovacao; }
    public void setAprovacao(boolean aprovacao) { this.aprovacao = aprovacao; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    
}