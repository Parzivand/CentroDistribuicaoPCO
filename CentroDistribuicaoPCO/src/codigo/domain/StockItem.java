package codigo.domain;
import java.time.LocalDate;

import codigo.domain.enums.estadoStock;

public class StockItem {

    Produto produto;
    String lote;
    String localizacao;
    int quantidade;
    private estadoStock  estado; // Disponivel, Quarentena, Reservado
    // LocalDate validade;.... tambem acho que nao 

    public StockItem(Produto produto, int quantidade,String lote,String  localizacao){
        this.produto= produto;
        this.quantidade= quantidade;
        this.lote= lote;
         this.localizacao=localizacao; 
    }

    // getters|setters
    public int getQuantidade(){return quantidade;}
    public  void setQuantidade(int quantidade){ this.quantidade= quantidade;}
    public String getLocalizacao(){return localizacao;}
    public void setLocalizacao(String novaLocalizacao){localizacao=novaLocalizacao;}
    public estadoStock getEstado(){return estado;}
    public void setEstado(estadoStock estado){this.estado=estado;}
    public Produto geProduto(){return produto;}
    }

