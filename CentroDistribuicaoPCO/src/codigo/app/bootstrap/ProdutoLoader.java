package codigo.app.bootstrap;

import codigo.domain.Produto;
import codigo.domain.enums.TipoRestricoes;
import codigo.dto.ProdutoDTO;
import codigo.handlers.ProdutoHandler;
import codigo.resources.JsonService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoLoader {

    private final JsonService jsonService;
    private final ProdutoHandler produtoHandler;
    private final String ficheiroProdutos;

    public ProdutoLoader(JsonService jsonService,
                      ProdutoHandler produtoHandler,
                      String ficheiroProdutos) {
        this.jsonService = jsonService;
        this.produtoHandler = produtoHandler;
        this.ficheiroProdutos = ficheiroProdutos;
    }

    /**
     * Carrega os produtos do ficheiro JSON e regista-os no ProdutoHandler.
     *
     * @throws IOException se não conseguir ler o ficheiro
     */
    public void carregar() throws IOException {
        // 1) Ler lista de DTOs a partir do ficheiro JSON
        List<ProdutoDTO> dtos = jsonService.readListFromFile(ficheiroProdutos, ProdutoDTO.class);

        int carregados = 0;
        int ignorados = 0;

        // 2) Para cada DTO, converter e criar produto
        for (ProdutoDTO dto : dtos) {
            try {
                
                List<TipoRestricoes> restricoesEnum = new ArrayList<>();
                if (dto.restricoes != null) {
                    for (String s : dto.restricoes) {
                        restricoesEnum.add(TipoRestricoes.valueOf(s));
                    }
                }


                if (dto.nome == null || dto.categoria == null || dto.unidadeMedida == null) {
                    System.err.printf("❌ IGNORADO (dados em falta): %s [cat: %s]%n", 
                        dto.nome, dto.categoria);
                    ignorados++;
                    continue;
                }

                // Criar produto (SKU gerado automaticamente)
                Produto p;
                if (dto.validade != null) {
                    p = produtoHandler.criarProduto(
                            dto.nome.trim(),
                            dto.categoria.trim(),
                            dto.unidadeMedida.trim(),
                            restricoesEnum,
                            dto.validade
                    );
                } else {
                    p = produtoHandler.criarProduto(
                            dto.nome.trim(),
                            dto.categoria.trim(),
                            dto.unidadeMedida.trim(),
                            restricoesEnum
                    );
                }

                carregados++;

            } catch (Exception e) {
                System.err.println("Erro ao carregar produto '" + dto.nome + "': " + e.getMessage());
                ignorados++;
            }
        }

        System.out.printf("✅ Carregados %d produtos (%d ignorados)%n" , carregados, ignorados);
    }

    /**
     * Verifica se o ficheiro existe antes de carregar.
     */
    public boolean ficheiroExiste() {
        return jsonService.fileExists(ficheiroProdutos);
    }
}
