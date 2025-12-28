package codigo.domain;

<<<<<<< Updated upstream
import java.util.HashMap;

import codigo.domain.Produto;

public  class Localizacao {

    // Atributos

    private String tipo, suporta_resticoes;
    private int capacidade_max; // Determinamos um tamanho e depois torna-lo final
    private HashMap<Integer,Rececao> Stock__de_produtos;
    // Algo que mostre a quantidade atual?
    
=======
public class Localizacao {

    // Atributos

    private String codigo;
    private String tipo; // estante, solo, frigorifico, doca...
    private int capacidadeMaxima;
    private String restricoesSuportadas; // frio, perigoso...

>>>>>>> Stashed changes
    // Construtor
    public Localizacao(String codigo, String tipo, int capacidade_max, String suporta_resticoes) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.capacidadeMaxima = capacidade_max;
        this.restricoesSuportadas = suporta_resticoes;
    }
    //  mostra quantos produtos tem atualmente no armazem 
    public int capacidade_atual(){
     int contador=0;
     int index=0;  
     for(Rececao  rececao:Stock__de_produtos.values()){
        contador+=rececao.getlinhasrececao().get(index).getquantidade();
        index++;
        if(contador>=capacidade_max){
         
     }   
    }

    // Gets e Setters

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getCapacidadeMaxima() { return capacidadeMaxima; }
    public void setCapacidadeMaxima(int capacidadeMaxima) { this.capacidadeMaxima = capacidadeMaxima; }

    public String getRestricoesSuportadas() { return restricoesSuportadas; }
    public void setRestricoesSuportadas(String restricoesSuportadas) { this.restricoesSuportadas = restricoesSuportadas; }

    
}