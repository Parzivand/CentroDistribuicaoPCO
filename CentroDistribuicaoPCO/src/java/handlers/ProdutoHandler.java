package java.handlers;

import java.domain.Produto;
import java.util.ArrayList;
import java.util.List;

public class ProdutoHandler {
    private final List<Produto> produtos = new ArrayList<>();

    public void indicarCriacaoProduto() {
        // aqui poderias iniciar algum estado interno se precisares
    }

    public Produto dadosProduto(String sku, String nome,
                                String unidadeMedida, boolean validade,
                                String restricoes) {
        // validações mínimas aqui
        Produto p = new Produto(sku, nome, unidadeMedida, restricoes, validade);
        produtos.add(p);
        return p;
    }

    public List<Produto> listarProdutos() {
        return new ArrayList<>(produtos);
    }
}
