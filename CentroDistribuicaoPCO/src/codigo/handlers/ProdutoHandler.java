package codigo.handlers;

import codigo.domain.Produto;
import codigo.domain.enums.TipoRestricoes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.lang.reflect.Array;
import java.time.LocalDate;
import javax.management.InvalidAttributeValueException;

public class ProdutoHandler {

    // Mapa SKU -> Produto
    private final Map<String, Produto> produtos = new TreeMap<>();

    // Mapa categoria -> último número usado
    private final Map<String, Integer> contadoresPorCategoria = new HashMap<>();

    private InventarioHandler inventarioHandler;


    public ProdutoHandler (InventarioHandler inventarioHandler){
        this.inventarioHandler = inventarioHandler;

    }


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
                                List<TipoRestricoes> restricoes,
                                Date validade) {

        String skuGerado = proximoSkuParaCategoria(categoria);

        // segurança extra: garante unicidade
        if (produtos.containsKey(skuGerado)) {
            throw new IllegalStateException("SKU já existente: " + skuGerado);
        }

        // verifica se tem alguma informacao  em falta        
        if(nome==null || categoria==null || unidadeMedida==null|| restricoes==null){
            throw new IllegalArgumentException("tem alguma informacão em falta!"); 
        }

        // categoria já vem normalizada dentro do SKU, mas podes guardar cat normalizado também
        String catNormalizada = normalizarCategoria(categoria);

        Produto p = new Produto(nome, skuGerado, unidadeMedida, restricoes, validade, catNormalizada);
        produtos.put(p.getSKU(), p);
        return p;
    }

    

    public void removerProduto(String sku) {

        Produto p = produtos.get(sku);
        if (p == null)
            throw new IllegalArgumentException("Produto não existe");

        if (!inventarioHandler.podeRemoverProduto(p))
            throw new IllegalStateException(
                "Produto não pode ser removido — possui stock ou reservas."
            );

        produtos.remove(sku);
    }
    
    public void setInventarioHandler(InventarioHandler inv) {
        this.inventarioHandler = inv;
    }

    /**
     * Cria e regista um novo produto sem validade (validade = null).
     */
    public Produto criarProduto(String nome,
                                String categoria,
                                String unidadeMedida,
                                List<TipoRestricoes> restricoes) {
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
    
    public ArrayList<Produto>  getProdutos(int valor) { 
   // criei uma lista com todos os valores do Map para que se possa  mostrar todos os valores de 10 em 10 caso
   // seja solicitado
   ArrayList<Produto> mostrar_produtos = new ArrayList<>();
   mostrar_produtos.addAll(produtos.values());
   if(valor>mostrar_produtos.size()-1){
    IO.println(mostrar_produtos);
    throw new IndexOutOfBoundsException("demasiado  grande o valor\n");
   }
        return new ArrayList(mostrar_produtos.subList(0, valor)); 
    }
}
    

