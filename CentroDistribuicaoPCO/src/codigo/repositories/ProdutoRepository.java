package codigo.repositories;

import codigo.domain.Produto;
import java.util.*;

public class ProdutoRepository {

    private final Map<String, Produto> produtos = new HashMap<>();

    public void save(Produto produto) {
        produtos.put(produto.getSKU(), produto);
    }

    public Optional<Produto> findBySku(String sku) {
        return Optional.ofNullable(produtos.get(sku));
    }

    public Collection<Produto> findAll() {
        return produtos.values();
    }

    public boolean existsBySku(String sku) {
        return produtos.containsKey(sku);
    }
}