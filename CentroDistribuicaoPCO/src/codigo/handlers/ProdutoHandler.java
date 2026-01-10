package codigo.handlers;

import codigo.domain.Produto;
import codigo.domain.enums.TipoRestricoes;
import codigo.repositories.ProdutoRepository;
import java.util.*;

public class ProdutoHandler {

    // SKU -> Produto (mantém só para gerar SKU/contadores e para paginação ordenada)
    private final Map<String, Produto> produtos = new TreeMap<>();
    private final Map<String, Integer> contadoresPorCategoria = new HashMap<>();

    private InventarioHandler inventarioHandler;
    private final ProdutoRepository repo;

    public ProdutoHandler(InventarioHandler inventarioHandler, ProdutoRepository repo) {
        this.inventarioHandler = inventarioHandler;
        if (repo == null) throw new IllegalArgumentException("ProdutoRepository não pode ser null");
        this.repo = repo;

        // “hidrata” cache local com o que já estiver no repo (ex.: vindo do JSON)
        for (Produto p : repo.findAll()) {
            produtos.put(p.getSKU(), p);
            // atualiza contador por categoria para evitar repetir SKUs
            String cat = normalizarCategoria(p.getCategoria());
            int num = extrairNumeroSku(p.getSKU());
            contadoresPorCategoria.put(cat, Math.max(contadoresPorCategoria.getOrDefault(cat, 0), num));
        }
    }

    private int extrairNumeroSku(String sku) {
        // Ex: ALI-0000010 -> 10
        try {
            String[] parts = sku.split("-");
            return Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return 0;
        }
    }

    private String normalizarCategoria(String categoria) {
        if (categoria == null) throw new IllegalArgumentException("Categoria não pode ser null.");
        String cat = categoria.trim().toUpperCase();
        if (!cat.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Categoria inválida (esperado 3 letras A-Z). Valor recebido: " + cat);
        }
        return cat;
    }

    private String proximoSkuParaCategoria(String categoria) {
        String cat = normalizarCategoria(categoria);
        int atual = contadoresPorCategoria.getOrDefault(cat, 0) + 1;
        contadoresPorCategoria.put(cat, atual);
        return String.format("%s-%07d", cat, atual);
    }

    public Produto criarProduto(String nome, String categoria, String unidadeMedida,
                               List<TipoRestricoes> restricoes, Date validade) {

        if (nome == null || categoria == null || unidadeMedida == null || restricoes == null) {
            throw new IllegalArgumentException("Tem alguma informação em falta!");
        }

        String skuGerado = proximoSkuParaCategoria(categoria);

        // garante unicidade também no repo
        if (repo.existsBySku(skuGerado) || produtos.containsKey(skuGerado)) {
            throw new IllegalStateException("SKU já existente: " + skuGerado);
        }

        String catNormalizada = normalizarCategoria(categoria);
        Produto p = new Produto(nome, skuGerado, unidadeMedida, restricoes, validade, catNormalizada);

        // salva em ambos (repo + cache ordenada)
        repo.save(p);
        produtos.put(p.getSKU(), p);

        return p;
    }

    public Produto criarProduto(String nome, String categoria, String unidadeMedida, List<TipoRestricoes> restricoes) {
        return criarProduto(nome, categoria, unidadeMedida, restricoes, null);
    }

    public void removerProduto(String sku) {
        Produto p = produtos.get(sku);
        if (p == null) throw new IllegalArgumentException("Produto não existe");

        if (inventarioHandler != null && !inventarioHandler.podeRemoverProduto(p)) {
            throw new IllegalStateException("Produto não pode ser removido — possui stock ou reservas.");
        }

        produtos.remove(sku);
        // (opcional) remover do repo também: seria ideal criar repo.deleteBySku(sku)
        // por agora só remove da cache para não aparecer no menu
    }

    public void setInventarioHandler(InventarioHandler inv) {
        this.inventarioHandler = inv;
    }

    public Produto procurarPorSku(String sku) {
        Produto p = produtos.get(sku);
        if (p != null) return p;
        return repo.findBySku(sku).orElse(null);
    }

    public boolean existeSku(String sku) {
        return produtos.containsKey(sku) || repo.existsBySku(sku);
    }

    public ArrayList<Produto> getProdutos(int pageSize, int pageNumber) {
        ArrayList<Produto> todos = new ArrayList<>(produtos.values());
        
        int start = pageSize * pageNumber;
        if (start >= todos.size()) {
            return new ArrayList<>();
        }
        
        int end = Math.min(start + pageSize, todos.size());
        return new ArrayList<>(todos.subList(start, end));
    }

    // Mantém compatibilidade com o teu MenuPrincipal atual (se chama getProdutos(10))
    public ArrayList<Produto> getProdutos(int valor) {
        return getProdutos(valor, 0);  // primeira página
    }
}
