package codigo.domain;
import java.time.LocalDate;

class StockItem {

    Produto produto;
    String lote;
    Localizacao localizacao;
    int quantidade;
    private String  estado; // Disponivel, Quarentena, Reservado
    // LocalDate validade;.... 

    public StockItem(Produto produto, int quantidade,String lote, Localizacao localizacao){
        this.produto= produto;
        this.quantidade= quantidade;
        this.lote= lote;
        this.localizacao=localizacao; 
    }

    // getters|setters
    public int getQuantidade(){return quantidade;}
    public  void setQuantidade(int quantidade){ this.quantidade= quantidade;}
    public Localizacao getLocalizacao(){return localizacao;}
    }

