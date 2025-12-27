package codigo.domain;

import java.util.HashMap;

public class Expedicao{

    // Atributos

    private String estado, Localizacao;
    private HashMap<String,Encomenda> encomendas;
    // Construtor

    public Expedicao(String estado, String localizacao) {
        this.estado = estado;
        Localizacao = localizacao;
        this.encomendas= new HashMap<>();
    }
    public HashMap getencomendas(){
        return encomendas;
    }
    public String verencomendas(){return encomendas.toString();}

    // Gets e Setters
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getLocalizacao() { return Localizacao; }
    public void setLocalizacao(String localizacao) { Localizacao = localizacao; }
    
    
}