package codigo.app.bootstrap;

import com.google.gson.Gson;
import codigo.dto.DadosIniciaisDTO;
import codigo.handlers.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    private final ProdutoHandler produtoH;
    private final FornecedorHandler fornecedorH;
    private final LojaHandler lojaH;
    private final InventarioHandler inventarioH;
    private final UtilizadorHandler utilizadorH;

    public DataLoader(ProdutoHandler produtoH, FornecedorHandler fornecedorH,
                      LojaHandler lojaH, InventarioHandler inventarioH,
                      UtilizadorHandler utilizadorH) {
        this.produtoH = produtoH;
        this.fornecedorH = fornecedorH;
        this.lojaH = lojaH;
        this.inventarioH = inventarioH;
        this.utilizadorH = utilizadorH;
    }

    public void carregar(InputStream is) {
        Gson gson = new Gson();
        DadosIniciaisDTO dados = gson.fromJson(new InputStreamReader(is), DadosIniciaisDTO.class);

        // Ordem obrigatória: dependências (fornecedores antes produtos) [file:1]
        carregarFornecedores(dados);
        carregarProdutos(dados);
        carregarLocalizacoes(dados);
        carregarLojas(dados);
        carregarStocks(dados);
        carregarUtilizadores(dados);

        System.out.println("✅ Bootstrap concluído! Produtos: " + produtoH.getProdutos(10).size());
    }

    private void carregarFornecedores(DadosIniciaisDTO dados) {
        if (dados.fornecedores == null) return;
        dados.fornecedores.forEach(f -> 
            fornecedorH.adicionarfornecedor(f.nome, f.contacto, "N/A")  // contacto → email, telefone="N/A" [file:10]
        );
    }

    private void carregarProdutos(DadosIniciaisDTO dados) {
        if (dados.produtos == null) return;
        dados.produtos.forEach(p -> {
            String categoria = p.sku.split("-")[0];  // "LAC" de "LAC-001"
            produtoH.criarProduto(p.nome, categoria, p.unidadeMedida, p.restricoes, null);  // SKU gerado auto [file:3]
        });
    }

    private void carregarLocalizacoes(DadosIniciaisDTO dados) {
        if (dados.localizacoes == null) return;
        // Adiciona ao InventarioHandler (cria método se necessário) [file:9]
        dados.localizacoes.forEach(l -> inventarioH.adicionarLocalizacao(l.id, l.tipo));  // Sugestão: novo método
    }

    private void carregarLojas(DadosIniciaisDTO dados) {
        if (dados.lojas == null) return;
        dados.lojas.forEach(l -> {
            String area = l.codigo.substring(0, 3).toLowerCase();  // "LOJ" → "loj"
            lojaH.adicionarLoja(l.nome, area, l.morada);  // Usa random, mas área do código [file:8]
        });
    }

    private void carregarStocks(DadosIniciaisDTO dados) {
        if (dados.stocks == null) return;
        ArrayList itens = new ArrayList();
        dados.stocks.forEach(s -> {
            // Cria StockItem (ajusta ao teu domain) [file:12]
            itens.add(new StockItem(s.sku, s.localizacaoId, s.quantidade, s.lote));
        });
        inventarioH.adicionar_aoStock(itens);  
    }

    private void carregarUtilizadores(DadosIniciaisDTO dados) {
        if (dados.utilizadores == null) return;
        dados.utilizadores.forEach(u -> 
            utilizadorH.dadosUtilizador(u.username, u.email, u.password, new ArrayList<>(), Role.valueOf(u.papel))  [file:11]
        );
    }
}