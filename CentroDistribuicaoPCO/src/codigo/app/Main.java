package codigo.app;
import codigo.app.bootstrap.MasterDataLoader;
import codigo.handlers.*;
import codigo.repositories.*;
import codigo.resources.JsonService;

public class Main {

    public static void main(String[] args) {
        
        try {

            // REPOSITÓRIOS
            ProdutoRepository produtoRepo = new ProdutoRepository();
            UtilizadorRepository utilizadorRepo = new UtilizadorRepository();
            FornecedorRepository fornecedorRepo = new FornecedorRepository();
            LojaRepository lojaRepo = new LojaRepository();

            // HANDLERS
            ProdutoHandler produtoHandler = new ProdutoHandler(produtoRepo);
            UtilizadorHandler utilizadorHandler = new UtilizadorHandler(utilizadorRepo);
            FornecedorHandler fornecedorHandler = new FornecedorHandler(fornecedorRepo);
            LojaHandler lojaHandler = new LojaHandler(lojaRepo);

            // JSON Service
            JsonService jsonService = new JsonService();

            // BOOTSTRAP
            MasterDataLoader loader = new MasterDataLoader(
                    jsonService,
                    produtoHandler,
                    utilizadorHandler,
                    fornecedorHandler,
                    lojaHandler
            );

            loader.loadAll(); // 🚀 carrega tudo

            System.out.println("\n🔥 Sistema pronto para uso!\n");

            // Aqui começa a tua UI / Menu principal
            new MenuPrincipal(produtoHandler, lojaHandler, utilizadorHandler).run();
            
            
        } catch (Exception e) {
            System.err.println("Erro ao iniciar sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
