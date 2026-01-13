package codigo.domain;

import codigo.domain.enums.Cargo;

public class Utilizador{

    // Atributos

    private String nome;
    private String email;
    private String password;
    private Cargo cargo;// Ex: ADMINISTRADOR, OPERDADOR DE  SELECAO , GESTOR LOGISTICO , ETC...  


    // Construtor
    public Utilizador(String nome, String email, String password,Cargo cargo) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.cargo = cargo; 
    }

    // Gets e Setters

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEmail() { return email;}
    public void setEmail(String email) { this.email = email;}

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Cargo getcargo() { return cargo; }
    public void setPassword(Cargo cargo) { this.cargo = cargo; }
    
}