package codigo.handlers;

import codigo.domain.Utilizador;
import java.util.*;
import codigo.domain.Role;
public class UtilizadorHandler {
    private final List<Utilizador> utilizadores = new ArrayList<>();

    public void indicarCriacaoUtilizador() {
        // aqui poderias iniciar algum estado interno se precisares
    
    
    }

    public Utilizador dadosUtilizador(String nome, String email, String password, ArrayList<String> permissoes,Role cargo) {
        if(password==null || nome== null ||email==null || cargo==null){
            throw new IllegalArgumentException("falta de  informação");
        }
        Utilizador p = new Utilizador(nome,email,password,permissoes,cargo);
        utilizadores.add(p);
        return p;
    
    }

    public void AdicionarPermissao(String permissao,Utilizador utilizador){
        if(permissao== null|| utilizador.equals(null)){
            throw new IllegalArgumentException("tentaste adicionar uma permissao vazia ou nao expecificaste o utilizador que"+
            " querias adicionar  uma permissao");
        }
        if(!utilizadores.contains(utilizador)){
        throw new  IllegalArgumentException("esse utilizador nao existe");
        }
        utilizadores.get(utilizadores.indexOf(utilizador)).AdicionarPermissao(permissao);
     }
     
    

    public List<Utilizador> listarUtilizadores() {
        return new ArrayList<>(utilizadores);
    }
}
