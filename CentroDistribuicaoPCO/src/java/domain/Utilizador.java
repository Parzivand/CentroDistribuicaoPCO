package java.domain;

public class Utilizador{

    // Atributos

    private String nome, email, password, permissoes;

    // Construtor
    public Utilizador(String nome, String email, String password, String permissoes) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.permissoes = permissoes;
    }

    // Gets e Setters

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getPermissoes() { return permissoes; }
    public void setPermissoes(String permissoes) { this.permissoes = permissoes; }

}