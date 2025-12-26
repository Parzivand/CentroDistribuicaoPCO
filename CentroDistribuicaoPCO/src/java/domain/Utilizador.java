package java.domain;

import java.util.ArrayList;
import java.util.List;

public class Utilizador{

    // Atributos

    private String nome, email, password;
    private List<String>  permissoes; /*  faz sentido ser  uma lista de permissoes porque  podes ter varias
                                       permissoes nao sei se quiseres mudar tas a vontade se te fizer mais
                                        sentido*/ 

    // Construtor
    public Utilizador(String nome, String email, String password, ArrayList<String> permissoes) {
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
    
    public String getPermissoes() { return permissoes.toString();}
    
    public void AdicionarPermissao(String permissao ){
        if (permissoes.contains(permissao.toLowerCase())){
            IO.println("Essa nao se encontra nesse utilizador!");
        }else{
            permissoes.add(permissao.toLowerCase());
        }
    }
    

}