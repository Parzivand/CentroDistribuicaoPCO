package java.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Produto{

    // Atributos

    private String nome, SKU, unidadeMedida;
    private ArrayList<String> restricoes; // restricoes mudadas 
    private Date  validade;
    private boolean vencido;
    private HashSet<NaoConformidade> naoConformidade; // validade mudada para uma data 

    // Construtor

    public Produto(String nome, String SKU, String unidadeMedida,ArrayList restricoes, Date validade,
        HashSet naoConformidade) {
        this.nome = nome;
        this.SKU = SKU;
        this.unidadeMedida = unidadeMedida;
        this.restricoes = restricoes;
        this.validade = validade;
        this.vencido= false;
        this.naoConformidade= naoConformidade;
    
    }
    public Produto(String nome, String SKU, String unidadeMedida,ArrayList restricoes,
        HashSet naoconformidades
    ) {
        this.nome = nome;
        this.SKU = SKU;
        this.unidadeMedida = unidadeMedida;
        this.restricoes = restricoes;
        this.validade = null;
        this.naoConformidade=naoconformidades; 
    }
    public Produto(String nome, String SKU, String unidadeMedida,ArrayList restricoes, Date validade) {
        this.nome = nome;
        this.SKU = SKU;
        this.unidadeMedida = unidadeMedida;
        this.restricoes = restricoes;
        this.validade = validade;
        this.vencido= false;
        this.naoConformidade= new HashSet<>() ;
    
    }
    public Produto(String nome, String SKU, String unidadeMedida,ArrayList restricoes) {
        this.nome = nome;
        this.SKU = SKU;
        this.unidadeMedida = unidadeMedida;
        this.restricoes = restricoes;
        this.naoConformidade= new HashSet<>() ;
    
    }

    // Gets e Setters

    public String getSKU() { return SKU;} 
    public void setSKU(String SKU) { this.SKU = SKU; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getUnidadeMedida() { return unidadeMedida; }
    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; }

    public String getRestricoes() { return restricoes.toString(); }
    
    public Date isValidade() { return validade; }
    public void setValidade(Date validade) { this.validade = validade; }

    public String toString(){
        if(validade==null){return"SKU: %s  nome: %s unidade de medida: %s restricoes: %s ".format(SKU,nome,
            unidadeMedida,restricoes.toString());
        }
        else{return"SKU: %s  nome: %s unidade de medida: %s restricoes: %s  validade:%s".format(SKU,nome,
                 unidadeMedida,restricoes.toString(),validade); }
    }

}