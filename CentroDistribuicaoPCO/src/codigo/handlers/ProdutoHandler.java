package codigo.handlers;

import codigo.domain.Produto;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProdutoHandler {

    // Mapa SKU -> Produto
    private final Map<String, Produto> produtos = new HashMap<>();

    // Mapa categoria -> último número usado
    private final Map<String, Integer> contadoresPorCategoria = new HashMap<>();

    /**
     * Normaliza e valida a categoria.
     * - trim
     * - maiúsculas
     * - exatamente 3 letras A-Z
     */
    private String normalizarCategoria(String categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria não pode ser null.");
        }

        String cat = categoria.trim().toUpperCase();

        // garante exatamente 3 letras maiúsculas
        if (!cat.matches("[A-Z]{3}")) { // 3 letras A-Z
            throw new IllegalArgumentException(
                    "Categoria inválida (esperado 3 letras A-Z). Valor recebido: " + cat
            );
        }

        return cat;
    }

    /**
     * Gera o próximo SKU para uma categoria no formato CAT-0000000.
     */
    private String proximoSkuParaCategoria(String categoria) {
        String cat = normalizarCategoria(categoria);

        int atual = contadoresPorCategoria.getOrDefault(cat, 0) + 1;
        contadoresPorCategoria.put(cat, atual);

        return String.format("%s-%07d", cat, atual);
    }

    /**
     * Cria e regista um novo produto com validade.
     * SKU é gerado automaticamente a partir da categoria.
     */
    public Produto criarProduto(String nome,
                                String categoria,
                                String unidadeMedida,
                                List<String> restricoes,
                                Date validade) {

        String skuGerado = proximoSkuParaCategoria(categoria);

        // segurança extra: garante unicidade
        if (produtos.containsKey(skuGerado)) {
            throw new IllegalStateException("SKU já existente: " + skuGerado);
        }

        // categoria já vem normalizada dentro do SKU, mas podes guardar cat normalizado também
        String catNormalizada = normalizarCategoria(categoria);

        Produto p = new Produto(nome, skuGerado, unidadeMedida, restricoes, validade, catNormalizada);
        produtos.put(p.getSKU(), p);
        return p;
    }

    /**
     * Cria e regista um novo produto sem validade (validade = null).
     */
    public Produto criarProduto(String nome,
                                String categoria,
                                String unidadeMedida,
                                List<String> restricoes) {
        return criarProduto(nome, categoria, unidadeMedida, restricoes, null);
    }

    /**
     * Obtém um produto pelo SKU.
     */
    public Produto procurarPorSku(String sku) {
        return produtos.get(sku);
    }

    /**
     * Verifica se já existe um produto com o SKU dado.
     */
    public boolean existeSku(String sku) {
        return produtos.containsKey(sku);
    }

    /**
     * Devolve uma cópia do mapa de produtos.
     */
    public Map<String, Produto> getProdutos() {
        return new HashMap<>(produtos);
    }
}
