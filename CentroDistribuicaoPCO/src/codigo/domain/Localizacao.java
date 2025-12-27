
import java.util.HashMap;
public  class Localizacao {

    // Atributos

    private String tipo, suporta_resticoes;
    private int capacidade_max; // Determinamos um tamanho e depois torna-lo final
    private HashMap<Integer,Rececao> Stock__de_produtos;
    // Algo que mostre a quantidade atual?
    
    // Construtor

    public Localizacao(String tipo, String suporta_resticoes, int capacidade_max) {
        this.tipo = tipo;
        this.suporta_resticoes = suporta_resticoes;
        this.capacidade_max = capacidade_max;
    }

    //  mostra quantos produtos tem atualmente no armazem 
    public String capacidade_atual(){
     int contador=0;
     int index=0;  
     for(Rececao  rececao:Stock__de_produtos.values()){
        contador+=rececao.getlinhasrececao().get(index).getquantidade();
        index++;
        if(contador==capacidade_max){
         return"capacidade maxima atingida"; 
    }
    }   
     return"capacidade atual: "+contador;
    }

    // Gets e Setters

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getSuporta_resticoes() { return suporta_resticoes; }
    public void setSuporta_resticoes(String suporta_resticoes) { this.suporta_resticoes = suporta_resticoes; }

    public int getCapacidade_max() { return capacidade_max; }
    public void setCapacidade_max(int capacidade_max) { this.capacidade_max = capacidade_max; }


}