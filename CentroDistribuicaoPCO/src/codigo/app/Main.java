package codigo.app;

import codigo.app.bootstrap.Bootstrap;
import codigo.handlers.*;

public class Main {
    public static void main(String[] args) {
        // 1) Carregar JSON (produtos/users/lojas/fornecedores)
        Bootstrap.initDadosCompletos();
        
        // 2) Criar handlers na ordem que evita o ciclo
        RececaoHandler rececaoHandler = new RececaoHandler();  // garante que o construtor inicializa rececoes
        InventarioHandler inventarioHandler = new InventarioHandler(); // novo construtor vazio

        Encomendahandler encomendaHandler = new Encomendahandler(inventarioHandler);
        inventarioHandler.setEncomendaHandler(encomendaHandler);
        inventarioHandler.setRececaoHandler(rececaoHandler);

        ExpedicaoHandler expedicaoHandler = new ExpedicaoHandler(encomendaHandler, inventarioHandler);

        // 3) Outros handlers (ajusta conforme os teus construtores reais)
        ProdutoHandler produtoHandler = new ProdutoHandler(inventarioHandler);
        UtilizadorHandler utilizadorHandler = new UtilizadorHandler();
        LojaHandler lojaHandler = new LojaHandler();
        FornecedorHandler fornecedorHandler = new FornecedorHandler();

        // 4) Menu
        MenuPrincipal menu = new MenuPrincipal(
                produtoHandler,
                utilizadorHandler,
                lojaHandler,
                fornecedorHandler,
                inventarioHandler,
                rececaoHandler,
                expedicaoHandler,
                encomendaHandler
        );
        menu.run();
    }
}
