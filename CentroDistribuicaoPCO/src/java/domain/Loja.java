package java.domain;

public class Loja {
    
    //Atributos

    private String morada, nome;
    private int codigo;

    //Construtor

    public Loja(String morada, String nome, int codigo) {
        this.morada = morada;
        this.nome = nome;
        this.codigo = codigo;
    }

    //Gets e Setters

    public String getMorada() { return morada; }
    public void setMorada(String morada) { this.morada = morada; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }
    
}