package codigo.domain;

import java.util.ArrayList;
import java.util.List;

import codigo.domain.enums.Cargo;

public class Utilizador{

    // Atributos

    private String nome;
    private String email;
    private String password;
    private List<String>  permissoes;// lista de funcoes que o utilizador pode fazer ex: ADMINISTRADOR(criar produto, criar  fornecedor,...)
    private Cargo cargo;// Ex: ADMINISTRADOR, OPERDADOR DE  SELECAO , GESTOR LOGISTICO , ETC...  
    /*  faz sentido ser  uma lista de permissoes porque  podes ter varias
                                       permissoes nao sei se quiseres mudar tas a vontade se te fizer mais
                                        sentido*/ 

    // Construtor
    public Utilizador(String nome, String email, String password, List<String> permissoes,Cargo cargo) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.permissoes = permissoes;
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
    
    public List<String> getPermissoes() { return new ArrayList<>(permissoes);}
    
    public void AdicionarPermissao(String permissao ){
        if (permissoes.contains(permissao.toLowerCase())){
            IO.println("Essa jase encontra nesse utilizador!");
        }else{
            permissoes.add(permissao.toLowerCase());
        }
    }
    

}