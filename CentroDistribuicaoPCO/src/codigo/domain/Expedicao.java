package codigo.domain;

import java.util.HashMap;

public class Expedicao{

    // Atributos

    private String estado, Localizacao;

    // Construtor

    public Expedicao(String estado, String localizacao) {
        this.estado = estado;
        Localizacao = localizacao;
    }

    // Gets e Setters
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getLocalizacao() { return Localizacao; }
    public void setLocalizacao(String localizacao) { Localizacao = localizacao; }

    
}