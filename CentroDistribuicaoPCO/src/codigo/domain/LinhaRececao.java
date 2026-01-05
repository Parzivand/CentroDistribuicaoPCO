package codigo.domain ;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;


public class LinhaRececao {

    private Produto produto;
    private String lote;
  
    private int quantidadeRecebida;
    private String estado;
    private ArrayList naoconformidades = new ArrayList();

    public LinhaRececao(Produto produto, String lote, int quantidadeRecebida) {
        this.produto = produto;
        this.lote = lote;
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
    @Override 
    public String toString() {
       
        return String.format("lote:%s produto: %s  quantidade recebida: %d \n", lote,produto,quantidadeRecebida);
    }
}