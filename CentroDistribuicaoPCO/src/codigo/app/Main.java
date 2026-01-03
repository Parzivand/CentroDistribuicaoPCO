package codigo.app;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import codigo.handlers.ProdutoHandler;
import codigo.domain.Produto;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== TESTE PRODUTO HANDLER ===\n");

        ProdutoHandler handler = new ProdutoHandler();

        // Teste 1: Produtos com validade
        System.out.println("1. Criando produtos com validade...");
        try {
            Produto leite = handler.criarProduto(
                "Leite Integral",
                "LAC", 
                "litro",
                Arrays.asList("frio", "validadeObrigatoria"),
                new Date()
            );
            System.out.println("✅ " + leite + "\n");

            Produto iogurte = handler.criarProduto(
                "Iogurte Natural", 
                "LAC",
                "unidade",
                Arrays.asList("frio"),
                new Date()
            );
            System.out.println("✅ " + iogurte + "\n");

        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
        }

        // Teste 2: Produtos sem validade
        System.out.println("2. Criando produtos sem validade...");
        Produto parafuso = handler.criarProduto(
            "Parafuso M6x20",
            "FER", 
            "unidade",
            Arrays.asList("perigoso"),
            null
        );
        System.out.println("✅ " + parafuso + "\n");

        // Teste 3: Categoria inválida (vai dar erro)
        System.out.println("3. Testando categoria inválida...");
        try {
            handler.criarProduto("Produto ruim", "AB", "kg", null, null);
        } catch (IllegalArgumentException e) {
            System.out.println("✅ Erro esperado: " + e.getMessage() + "\n");
        }

        // Teste 4: Listar todos os produtos por multiplos de 10 
        System.out.println("4. Todos os produtos");
        for (Produto p : handler.getProdutos(10)) {
            System.out.println("  • " + p.getSKU() + " - " + p.getNome());
            System.out.println("    Categoria: " + p.getCategoria());
            System.out.println("    Restrições: " + p.getRestricoes());
            System.out.println();
        }

        // Teste 5: Procurar por SKU
        System.out.println("5. Procurando por SKU...");
        Produto encontrado = handler.procurarPorSku("LAC-0000001");
        if (encontrado != null) {
            System.out.println("✅ Encontrado: " + encontrado.getNome());
        } else {
            System.out.println("❌ Não encontrado");
        }
    }
}
