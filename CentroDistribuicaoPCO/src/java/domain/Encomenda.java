package java.domain;

import java.util.ArrayList;
import java.util.HashMap;

public class Encomenda {
    private  String estado,referencia;
    private Boolean prioridade;
    private final Loja loja;
    private ArrayList<LinhaEncomenda> linhasencomenda; 
    public Encomenda(String estado,String referencia,Boolean prioridade, Loja loja ){
        this.estado= estado;
        this.referencia= referencia;
        this.prioridade=  prioridade;
        this.loja= loja;  

    }
    public String getestado(){return estado;}
    public void setestado(String  estado){this.estado = estado; }
    public String getreferencia(){return referencia;}
    public void setreferencia(String  referencia){this.referencia = referencia; }
    public String getloja(){return loja.toString();} // nao se vai mudar  a loja na encomenda ne ?? 

    public String toString() {
        return String.format("estado:%s prioridade: %s",estado,prioridade,loja);
    } 
    
}