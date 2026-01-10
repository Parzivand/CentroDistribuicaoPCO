package codigo.app;

import codigo.app.bootstrap.Bootstrap;
import codigo.handlers.*;
import codigo.repositories.*;

public class Main {
    public static void main(String[] args) {

        Bootstrap.initDadosCompletos();

        UtilizadorRepository userRepo = Bootstrap.getUtilizadorRepository();
        ProdutoRepository produtoRepo = Bootstrap.getProdutoRepository();
        LojaRepository lojaRepo = Bootstrap.getLojaRepository();
        FornecedorRepository fornecedorRepo = Bootstrap.getFornecedorRepository();

        RececaoHandler rececaoHandler = new RececaoHandler();
        InventarioHandler inventarioHandler = new InventarioHandler();
        Encomendahandler encomendaHandler = new Encomendahandler(inventarioHandler);
        inventarioHandler.setEncomendaHandler(encomendaHandler);
        inventarioHandler.setRececaoHandler(rececaoHandler);

        ExpedicaoHandler expedicaoHandler = new ExpedicaoHandler(encomendaHandler, inventarioHandler);

        ProdutoHandler produtoHandler = new ProdutoHandler(inventarioHandler, produtoRepo);
        UtilizadorHandler utilizadorHandler = new UtilizadorHandler(userRepo);
        LojaHandler lojaHandler = new LojaHandler(lojaRepo);
        FornecedorHandler fornecedorHandler = new FornecedorHandler(fornecedorRepo);

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

