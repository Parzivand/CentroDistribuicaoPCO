package java.domain;

import java.util.Date;

public class LinhaRececao {
    private String lote;
    private int quantidade_recebida;
    private final Produto produto;
    public LinhaRececao(String lote, int quantidade_recebida,Produto produto){
        this.lote= lote;
        this.quantidade_recebida= quantidade_recebida;
        this.produto= produto;
    }
    
    public String getLote(){
        return lote;
    }
    public int getquantidade(){
        return quantidade_recebida;
    }
    public String getValidade(){return produto.toString();}
    public void setquantidade(int quantidade){this.quantidade_recebida=quantidade;}
    public void setlote(String lote){this.lote=lote;}
}
