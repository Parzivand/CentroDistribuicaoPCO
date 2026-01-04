package codigo.domain;

import java.util.Objects;

public class Loja {
    
    //Atributos

    private String morada, nome;
    private String codigo; //Tem tamanho?

    //Construtor

    public Loja(String morada,String nome, String codigo) {
        this.morada = morada;
        this.nome = nome;
        this.codigo = codigo;
    }


    //Gets e Setters

    public String getMorada() { return morada; }
    public void setMorada(String morada) { this.morada = morada; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo;}
    
    
    @Override 
    public boolean equals(Object o){
        if(this==o )return true;
        if(!(o instanceof Loja))return false;
        Loja loja = (Loja) o;
        return Objects.equals(codigo, loja.codigo);
    }
    @Override 
        public int hashCode(){
            return Objects.hash(codigo);
        }
    
    @Override
        public String toString(){
            return String.format("morada: %s nome: %s",morada,nome);
        }  
    
    }