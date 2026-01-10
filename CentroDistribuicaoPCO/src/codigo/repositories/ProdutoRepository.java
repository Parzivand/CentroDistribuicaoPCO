package codigo.repositories;

import codigo.domain.Produto;
import codigo.domain.enums.TipoRestricoes;
import codigo.dto.ProdutoJsonDTO;
import codigo.handlers.ProdutoHandler;
import codigo.resources.JsonService;
import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class ProdutoRepository {

    private final Map<String, Produto> produtos = new HashMap<>();
    private final JsonService jsonService = new JsonService();

    // Carrega do JSON no boot
    public ProdutoRepository(ProdutoHandler produtoHandler) {
        loadFromJson(produtoHandler);
        System.out.println("✅ Carregados " + produtos.size() + " produtos");
    }

    // Caso queiras continuar a permitir "repo vazio"
    public ProdutoRepository() {
        System.out.println("✅ ProdutoRepository inicializado vazio (sem JSON)");
    }

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

    private void loadFromJson(ProdutoHandler produtoHandler) {
        if (produtoHandler == null) {
            System.err.println("⚠️ ProdutoHandler null: não dá para carregar produtos (precisa gerar SKU).");
            return;
        }

        String[] paths = {"../dados.json", "dados.json"};

        for (String path : paths) {
            File file = new File(path);
            System.out.println("🔍 " + path + " → " + file.getAbsolutePath() + " (exists: " + file.exists() + ")");
            if (!file.exists()) continue;

            try {
                // debug preview
                String rawContent = new String(Files.readAllBytes(file.toPath()));
                System.out.println("📄 JSON preview: " +
                        rawContent.substring(0, Math.min(200, rawContent.length())) + "...");

                List<ProdutoJsonDTO> list =
                        jsonService.readListFieldFromFile(path, "produtos", ProdutoJsonDTO.class);

                System.out.println("📦 Produtos lidos: " + (list == null ? "null" : list.size()));
                if (list == null || list.isEmpty()) return;

                int count = 0;
                for (ProdutoJsonDTO p : list) {
                    if (p == null) continue;

                    String nome = p.nome != null ? p.nome : "Unknown";
                    String categoria = p.categoria != null ? p.categoria : "";
                    String unidade = p.unidadeMedida != null ? p.unidadeMedida : "";

                    List<TipoRestricoes> restr = new ArrayList<>();
                    if (p.restricoes != null) {
                        for (String r : p.restricoes) {
                            if (r == null) continue;
                            // Importante: no teu JSON tens "CONGELADO" e "PERIGOSO",
                            // mas o enum TipoRestricoes NÃO tem esses valores. [file:38]
                            // Então ignoramos as que não existirem:
                            try {
                                restr.add(TipoRestricoes.valueOf(r.trim().toUpperCase()));
                            } catch (IllegalArgumentException ignored) {
                                System.out.println("⚠️ Restrição ignorada (não existe no enum): " + r);
                            }
                        }
                    }

                    // validade no JSON é null -> usamos overload sem validade
                    Produto criado = produtoHandler.criarProduto(nome, categoria, unidade, restr);
                    save(criado);
                    count++;
                }

                System.out.println("✅ SUCCESS " + count + " produtos de: " + path);
                return;

            } catch (Exception e) {
                System.err.println("❌ ERRO " + path + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.err.println("❌ Nenhum dados.json válido para produtos!");
    }
}
