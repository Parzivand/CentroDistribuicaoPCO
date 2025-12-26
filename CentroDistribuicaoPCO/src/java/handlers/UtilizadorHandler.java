import java.domain.Produto;
import java.domain.Utilizador;
import java.util.*;
public class UtilizadorHandler {
    private final List<Utilizador> utilizadores = new ArrayList<>();

    public void indicarCriacaoUtilizador() {
        // aqui poderias iniciar algum estado interno se precisares
    
    
    }

    public Utilizador dadosUtilizador(String nome, String email, String password, ArrayList<String> permissoes) {
        // validações mínimas aqui
        Utilizador p = new Utilizador(nome,email,password,permissoes);
        utilizadores.add(p);
        return p;
    }

    public List<Utilizador> listarUtilizadores() {
        return new ArrayList<>(utilizadores);
    }
}
