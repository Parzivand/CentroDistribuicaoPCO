package codigo.repositories;

import codigo.domain.Utilizador;
import java.util.*;

public class UtilizadorRepository {

    private final Map<String, Utilizador> utilizadores = new HashMap<>();

    public void save(Utilizador u) {
        utilizadores.put(u.getEmail(), u);
    }

    public Optional<Utilizador> findByEmail(String email) {
        return Optional.ofNullable(utilizadores.get(email));
    }

    public Collection<Utilizador> findAll() {
        return utilizadores.values();
    }
}
