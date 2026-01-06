package codigo.app.bootstrap;

import codigo.dto.*;
import codigo.handlers.*;
import codigo.resources.JsonService;

import java.util.*;

public class MasterDataLoader {

    private static final String FICHEIRO = "src/codigo/resources/dadosInicias.json";

    private final JsonService json;
    private final ProdutoHandler produtoHandler;
    private final UtilizadorHandler utilizadorHandler;
    private final FornecedorHandler fornecedorHandler;
    private final LojaHandler lojaHandler;

    public MasterDataLoader(JsonService json,
                            ProdutoHandler produtoHandler,
                            UtilizadorHandler utilizadorHandler,
                            FornecedorHandler fornecedorHandler,
                            LojaHandler lojaHandler) {
        this.json = json;
        this.produtoHandler = produtoHandler;
        this.utilizadorHandler = utilizadorHandler;
        this.fornecedorHandler = fornecedorHandler;
        this.lojaHandler = lojaHandler;
    }

    public void loadAll() throws Exception {

        System.out.println("🔄 A carregar dados iniciais...");

        DadosIniciaisDTO root = json.readObjectFromFile(FICHEIRO, DadosIniciaisDTO.class);

        loadProdutos(root.produtos);
        loadUtilizadores(root.utilizadores);
        loadFornecedores(root.fornecedores);
        loadLojas(root.lojas);

        System.out.println("✅ Dados iniciais carregados com sucesso!");
    }

    // -------------------------------------------------------------

    private void loadProdutos(List<ProdutoDTO> lista) {
        if (lista == null) return;

        for (ProdutoDTO p : lista) {
            try {
                produtoHandler.criarProduto(
                        p.nome, p.categoria, p.unidadeMedida,
                        p.restricoes, p.validade
                );
            } catch (Exception e) {
                System.err.println("Produto ignorado: " + p.nome);
            }
        }
    }

    private void loadUtilizadores(List<UtilizadorDTO> lista) {
        if (lista == null) return;

        for (UtilizadorDTO u : lista) {
            try {
                utilizadorHandler.criarUtilizador(
                        u.nome, u.email, u.password, u.cargo, u.permissoes
                );
            } catch (Exception e) {
                System.err.println("Utilizador ignorado: " + u.email);
            }
        }
    }

    private void loadFornecedores(List<FornecedorDTO> lista) {
        if (lista == null) return;

        for (FornecedorDTO f : lista) {
            try {
                fornecedorHandler.criarFornecedor(
                        f.nome, f.email, f.telefone
                );
            } catch (Exception e) {
                System.err.println("Fornecedor ignorado: " + f.nome);
            }
        }
    }

    private void loadLojas(List<LojaDTO> lista) {
        if (lista == null) return;

        for (LojaDTO l : lista) {
            try {
                lojaHandler.criarLoja(l.nome, l.morada);
            } catch (Exception e) {
                System.err.println("Loja ignorada: " + l.nome);
            }
        }
    }
}
