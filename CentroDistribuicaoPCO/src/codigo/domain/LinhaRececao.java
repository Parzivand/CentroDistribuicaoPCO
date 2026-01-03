package codigo.domain ;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;


public class LinhaRececao {

    private Produto produto;
    private String lote;
    // private LocalDate validade; 
    //pq a linha tem validade se podes usar a validade do  produto ???   
    private int quantidadeRecebida;
    private String estado;
    private ArrayList naoconformidades = new ArrayList();

    public LinhaRececao(Produto produto, String lote, int quantidadeRecebida) {
        this.produto = produto;
        this.lote = lote;
        //this.validade = validade;
        this.quantidadeRecebida = quantidadeRecebida;
    }




    // getters/setters
    
    
    public ArrayList getnaoconformidades(){return new ArrayList<>(naoconformidades);}
    public void setnaoconformidades(String tipo, String descricao){
    naoconformidades.add(new NaoConformidade(tipo, descricao));
    }
    public Produto getProduto() { return produto;}
    public String getLote() { return lote;}
    public Date getValidade() { return produto.getValidade(); }
    public int getQuantidadeRecebida() { return quantidadeRecebida; }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
}