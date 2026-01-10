package codigo.handlers;

import codigo.domain.Utilizador;
import codigo.domain.enums.Cargo;
import codigo.repositories.UtilizadorRepository;
import java.util.ArrayList;
import java.util.List;

public class UtilizadorHandler {

    private final UtilizadorRepository repo;

    public UtilizadorHandler(UtilizadorRepository repo) {
        if (repo == null) throw new IllegalArgumentException("repo não pode ser null");
        this.repo = repo;
    }

    public void indicarCriacaoUtilizador() {
        // opcional
    }

    public Utilizador dadosUtilizador(String nome, String email, String password, Cargo cargo) {
        if (password == null || nome == null || email == null || cargo == null) {
            throw new IllegalArgumentException("falta de informação");
        }

        Utilizador u = new Utilizador(nome.trim(), email.trim(), password, cargo);

        // guarda no repositório (para o login ver)
        repo.save(u);

        return u;
    }

    public List<Utilizador> listarUtilizadores() {
        // lê SEMPRE do repositório (inclui dados.json + novos registos)
        return new ArrayList<>(repo.findAll());
    }
}
