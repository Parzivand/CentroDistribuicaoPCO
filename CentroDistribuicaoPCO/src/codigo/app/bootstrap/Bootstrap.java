package codigo.app.bootstrap;

import codigo.dto.*;
import codigo.repositories.*;
import codigo.resources.*;
import java.util.List;

/**
 * Classe utilitária para bootstrap/inicialização de dados de teste.
 * Lê um ficheiro JSON específico (`dados.json`) e popula repositórios em memória
 * através de DTOs que fazem mapeamento para entidades de domínio.
 * 
 * <p><strong>Nota:</strong> Esta classe é alternativa ao carregamento automático
 * dos repositórios individuais. Serve para dados de teste/development.</p>
 * 
 * <p><strong>Uso:</strong></p>
 * <pre>{@code
 * Bootstrap.initDadosCompletos();
 * UtilizadorRepository users = Bootstrap.getUtilizadorRepository();
 * }</pre>
 */
public class Bootstrap {
    
    /**
     * Serviço JSON singleton para leitura de ficheiros.
     */
    private static final JsonService json = new JsonService();
    
    /**
     * Repositórios singleton populados pelo bootstrap.
     * São reutilizáveis através dos getters estáticos.
     */
    private static final ProdutoRepository produtos = new ProdutoRepository();
    private static final UtilizadorRepository users = new UtilizadorRepository();
    private static final LojaRepository lojas = new LojaRepository();
    private static final FornecedorRepository fornecedores = new FornecedorRepository();
    
    /**
     * Caminho fixo para o ficheiro de dados JSON.
     * Deverá estar em `codigo/resources/dados.json` relativamente ao working directory.
     */
    private static final String path = "codigo/resources/dados.json";

    /**
     * Inicializa todos os repositórios lendo os campos específicos do ficheiro JSON.
     * 
     * <h3>Sequência de carregamento:</h3>
     * <ol>
     *   <li><code>"fornecedores"</code> → {@link FornecedorRepository}</li>
     *   <li><code>"produtos"</code> → {@link ProdutoRepository}</li>
     *   <li><code>"lojas"</code> → {@link LojaRepository}</li>
     *   <li><code>"utilizadores"</code> → {@link UtilizadorRepository}</li>
     * </ol>
     * 
     * <p>Cada DTO chama {@code dto.toEntity()} para conversão e {@code repo.save()}.
     * Em caso de erro, mostra mensagem no stderr mas não falha a aplicação.</p>
     */
    public static void initDadosCompletos() {
        try {
            System.out.println("user.dir=" + System.getProperty("user.dir"));
            
            /**
             * Lê arrays específicos de cada campo do JSON usando {@link JsonService#readListFieldFromFile}.
             */
            List<ProdutoDTO> produtosDTO    = json.readListFieldFromFile(path, "produtos",     ProdutoDTO.class);
            List<UtilizadorDTO> usersDTO    = json.readListFieldFromFile(path, "utilizadores", UtilizadorDTO.class);
            List<FornecedorDTO> fornDTO     = json.readListFieldFromFile(path, "fornecedores", FornecedorDTO.class);
            List<LojaDTO> lojasDTO          = json.readListFieldFromFile(path, "lojas",        LojaDTO.class);

            /**
             * Carrega cada lista no respetivo repositório.
             * Ordem: fornecedores → produtos → lojas → utilizadores
             */
            carregarFornecedores(fornDTO);
            carregarProdutos(produtosDTO);
            carregarLojas(lojasDTO);
            carregarUtilizadores(usersDTO);

        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
        }
    }

    /**
     * Getter singleton para o repositório de utilizadores populado.
     * 
     * @return Instância única de {@link UtilizadorRepository}
     */
    public static UtilizadorRepository getUtilizadorRepository() {
        return users;
    }

    /**
     * Getter singleton para o repositório de produtos populado.
     * 
     * @return Instância única de {@link ProdutoRepository}
     */
    public static ProdutoRepository getProdutoRepository() {
        return produtos;
    }

    /**
     * Getter singleton para o repositório de lojas populado.
     * 
     * @return Instância única de {@link LojaRepository}
     */
    public static LojaRepository getLojaRepository() {
        return lojas;
    }

    /**
     * Getter singleton para o repositório de fornecedores populado.
     * 
     * @return Instância única de {@link FornecedorRepository}
     */
    public static FornecedorRepository getFornecedorRepository() {
        return fornecedores;
    }
    
    // =====================================================================
    // MÉTODOS PRIVADOS DE CARREGAMENTO GENÉRICO
    // =====================================================================
    // Utiliza o padrão save() padrão dos repositórios + toEntity() dos DTOs
    
    /**
     * Carrega lista de {@link FornecedorDTO} no {@link FornecedorRepository}.
     * Cada DTO é convertido via {@code dto.toEntity()} e persistido.
     * 
     * @param dtos Lista de DTOs a carregar
     */
    private static void carregarFornecedores(List<FornecedorDTO> dtos) {
        dtos.forEach(dto -> fornecedores.save(dto.toEntity()));
        System.out.println("🏢 " + dtos.size());
    }
    
    /**
     * Carrega lista de {@link ProdutoDTO} no {@link ProdutoRepository}.
     * 
     * @param dtos Lista de DTOs a carregar
     */
    private static void carregarProdutos(List<ProdutoDTO> dtos) {
        dtos.forEach(dto -> produtos.save(dto.toEntity()));
        System.out.println("📦 " + dtos.size());
    }
    
    /**
     * Carrega lista de {@link LojaDTO} no {@link LojaRepository}.
     * 
     * @param dtos Lista de DTOs a carregar
     */
    private static void carregarLojas(List<LojaDTO> dtos) {
        dtos.forEach(dto -> lojas.save(dto.toEntity()));
        System.out.println("🏪 " + dtos.size());
    }
    
    /**
     * Carrega lista de {@link UtilizadorDTO} no {@link UtilizadorRepository}.
     * 
     * @param dtos Lista de DTOs a carregar
     */
    private static void carregarUtilizadores(List<UtilizadorDTO> dtos) {
        dtos.forEach(dto -> users.save(dto.toEntity()));
        System.out.println("👥 " + dtos.size());
    }
}
