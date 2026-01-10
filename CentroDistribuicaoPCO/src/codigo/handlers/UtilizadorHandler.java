package codigo.handlers;

import codigo.domain.Utilizador;
import codigo.domain.enums.Cargo;
import codigo.repositories.UtilizadorRepository;
import java.util.*;

public class UtilizadorHandler {
    private final List<Utilizador> utilizadores = new ArrayList<>();

    private UtilizadorRepository repo;

    public UtilizadorHandler(UtilizadorRepository repo) {
        this.repo = repo;
    }

    
    
    public void indicarCriacaoUtilizador() {
        // aqui poderias iniciar algum estado interno se precisares    
    }

    public Utilizador dadosUtilizador(String nome, String email, String password,Cargo cargo) {
        if(password==null || nome== null ||email==null || cargo==null){
            throw new IllegalArgumentException("falta de  informação");
        }
        Utilizador p = new Utilizador(nome,email,password,cargo);
        utilizadores.add(p);
        return p;
    }
    
    public List<Utilizador> listarUtilizadores() {
        return new ArrayList<>(utilizadores);
    }
}
