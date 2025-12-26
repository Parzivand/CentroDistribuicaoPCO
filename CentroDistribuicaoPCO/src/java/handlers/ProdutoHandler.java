
package java.handlers;

import java.domain.Produto;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProdutoHandler {
    private final HashMap<String,Produto> produtos = new HashMap<>();

    public void indicarCriacaoProduto() {
        // aqui poderias iniciar algum estado interno se precisares
    }

    public Produto dadosProduto(String sku, String nome,
                                String unidadeMedida, Date validade,
                                ArrayList restricoes) {
        // validações mínimas aqui
        Produto p = new Produto(sku, nome, unidadeMedida, restricoes, validade);
        produtos.put(sku, p);
        return p;
    }

    public HashMap<String,Produto> listarProdutos() {
        return new HashMap<>(produtos);
    }
}
