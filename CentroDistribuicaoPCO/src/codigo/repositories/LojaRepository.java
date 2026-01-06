package codigo.repositories;

import codigo.domain.Loja;
import java.util.*;

public class LojaRepository {

    private final Map<String, Loja> lojas = new HashMap<>();

    public void save(Loja l) {
        lojas.put(l.getNome(), l);
    }

    public Optional<Loja> findByNome(String nome) {
        return Optional.ofNullable(lojas.get(nome));
    }

    public Collection<Loja> findAll() {
        return lojas.values();
    }
}
