package codigo.repositories;

import codigo.domain.Fornecedor;
import java.util.*;

public class FornecedorRepository {

    private final Map<String, Fornecedor> fornecedores = new HashMap<>();

    public void save(Fornecedor f) {
        fornecedores.put(f.getNome(), f);
    }

    public Optional<Fornecedor> findByNome(String nome) {
        return Optional.ofNullable(fornecedores.get(nome));
    }

    public Collection<Fornecedor> findAll() {
        return fornecedores.values();
    }
}
