package codigo.app;

/**
 * Classe principal de arranque da aplicação.
 * Responsável pela criação e ligação de todos os repositórios e handlers,
 * respeitando as dependências entre componentes.
 * Executa o {@link MenuPrincipal} após inicialização completa.
 */
import codigo.handlers.*;
import codigo.repositories.*;

public class Main {
    /**
     * Método principal que inicializa toda a aplicação.
     * 
     * <h3>Sequência de inicialização:</h3>
     * <ol>
     *   <li>Criação dos repositórios em memória (carregam dados.json quando aplicável)</li>
     *   <li>Criação dos handlers com dependências mínimas</li>
     *   <li>Ligação de dependências cross-referenciadas (setters)</li>
     *   <li>Verificação de contagens iniciais</li>
     *   <li>Início da interface {@link MenuPrincipal}</li>
     * </ol>
     * 
     * @param args Argumentos de linha de comandos (não utilizados)
     */
    public static void main(String[] args) {
        // =================================================================
        // 1. REPOSITÓRIOS (camada de persistência em memória)
        // =================================================================
        /**
         * Repositórios carregam dados.json automaticamente no construtor.
         * InventarioRepository cria localizações padrão.
         */
        UtilizadorRepository ur = new UtilizadorRepository();
        LojaRepository lr = new LojaRepository();
        FornecedorRepository fr = new FornecedorRepository();
        ProdutoRepository pr = new ProdutoRepository();  // Temporário (será recriado)
        InventarioRepository ir = new InventarioRepository();  // ✅ Cria ARM0001, SEL0001, REC0001

        // =================================================================
        // 2. HANDLERS (camada de negócio) - ordem respeita dependências
        // =================================================================
        /**
         * Handlers independentes (sem dependências circulares)
         */
        UtilizadorHandler uh = new UtilizadorHandler(ur);
        LojaHandler lh = new LojaHandler(lr);
        FornecedorHandler fh = new FornecedorHandler(fr);
        
        /**
         * RececaoHandler → só precisa de InventarioRepository para validações de localização
         */
        RececaoHandler rh = new RececaoHandler(ir);  // ✅ Apenas InventarioRepository
        
        /**
         * InventarioHandler → depende de RececaoHandler (para validações) 
         *                      e mais tarde de EncomendaHandler (reservas)
         */
        InventarioHandler ih = new InventarioHandler(null, rh);  // ✅ RececaoHandler primeiro
        
        /**
         * Demais handlers em ordem de dependência
         */
        Encomendahandler eh = new Encomendahandler(ih);
        ExpedicaoHandler exh = new ExpedicaoHandler(eh, ih);
        AjusteStockHandler ah = new AjusteStockHandler(rh, exh);

        /**
         * ProdutoHandler especial: 2º passo (temporário) → 3º passo (final)
         * 1º: Criação vazia para receber dados.json
         * 2º: Recriação com handler para carregar produtos do JSON
         * 3º: Recriação final com dependências corretas
         */
        ProdutoHandler ph = new ProdutoHandler(ih, pr);
        pr = new ProdutoRepository(ph);  // ✅ Agora carrega produtos usando o handler
        ph = new ProdutoHandler(ih, pr); // ✅ Handler final com repo populado

        // =================================================================
        // 3. LIGAÇÃO DE DEPENDÊNCIAS CROSS (setters)
        // =================================================================
        /**
         * Liga handlers que só ficam prontos após criação mútua:
         * - ProdutoHandler precisa de InventarioHandler (verificação stock na remoção)
         * - InventarioHandler precisa de EncomendaHandler (gestão reservas)
         * - InventarioHandler precisa de RececaoHandler (validações NC)
         */
        ph.setInventarioHandler(ih);
        ih.setEncomendaHandler(eh);
        ih.setRececaoHandler(rh);

        // =================================================================
        // 4. VERIFICAÇÃO DE INICIALIZAÇÃO
        // =================================================================
        /**
         * Mostra contagens iniciais para confirmar carregamento de dados.json
         * e criação de localizações padrão.
         */
        System.out.println("👥 Users: " + ur.findAll().size());
        System.out.println("📦 Produtos: " + pr.findAll().size());
        System.out.println("🏪 Lojas: " + lr.findAll().size());
        System.out.println("🏭 Fornecedores: " + fr.findAll().size());
        System.out.println("📍 Localizações: " + ir.findAll().size());  // ✅ ARM0001, SEL0001, REC0001

        // =================================================================
        // 5. INÍCIO DA APLICAÇÃO
        // =================================================================
        /**
         * Inicia MenuPrincipal com todos os handlers prontos e interligados.
         * O menu implementa autenticação + autorização por cargo.
         */
        new MenuPrincipal(ph, uh, lh, fh, ih, rh, exh, eh, ah).run();
    }
}
