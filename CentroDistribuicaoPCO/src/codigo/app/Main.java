package codigo.app;

import codigo.app.bootstrap.DataLoader;
import codigo.handlers.*;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) {

        // Instanciar handlers
        ProdutoHandler produtoH = new ProdutoHandler();
        FornecedorHandler fornecedorH = new FornecedorHandler();
        LojaHandler lojaH = new LojaHandler();
        InventarioHandler inventarioH = new InventarioHandler();
        UtilizadorHandler utilizadorH = new UtilizadorHandler();

        // Loader
        DataLoader loader = new DataLoader(
                produtoH,
                fornecedorH,
                lojaH,
                inventarioH,
                utilizadorH
        );

        // Ler JSON do resources
        InputStream is =
            Main.class.getResourceAsStream("/dados_iniciais.json");

        if (is == null) {
            System.err.println("❌ ficheiro JSON não encontrado!");
            return;
        }

        loader.carregar(is);

        System.out.println("\nSistema iniciado ✔");
    }
}