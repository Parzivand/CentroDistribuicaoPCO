package codigo.app;

import codigo.handlers.*;
import codigo.repositories.*;

public class Main {
    public static void main(String[] args) {
        // Repositórios
        UtilizadorRepository ur = new UtilizadorRepository();
        LojaRepository lr = new LojaRepository();
        FornecedorRepository fr = new FornecedorRepository();
        ProdutoRepository pr = new ProdutoRepository();
        InventarioRepository ir = new InventarioRepository();  // ✅ DECLARADO

        // Handlers com dependências corretas
        UtilizadorHandler uh = new UtilizadorHandler(ur);
        LojaHandler lh = new LojaHandler(lr);
        FornecedorHandler fh = new FornecedorHandler(fr);
        
        RececaoHandler rh = new RececaoHandler(ir);  // ✅ CORRIGIDO
        InventarioHandler ih = new InventarioHandler(null, rh);  // ✅ RececaoHandler primeiro
        Encomendahandler eh = new Encomendahandler(ih);
        ExpedicaoHandler exh = new ExpedicaoHandler(eh, ih);
        AjusteStockHandler ah = new AjusteStockHandler(rh, exh);

        ProdutoHandler ph = new ProdutoHandler(ih, pr);
        pr = new ProdutoRepository(ph);  // ✅ Carrega produtos
        ph = new ProdutoHandler(ih, pr);

        // 🔗 Liga dependências cross
        ph.setInventarioHandler(ih);
        ih.setEncomendaHandler(eh);
        ih.setRececaoHandler(rh);

        System.out.println("👥 Users: " + ur.findAll().size());
        System.out.println("📦 Produtos: " + pr.findAll().size());
        System.out.println("🏪 Lojas: " + lr.findAll().size());
        System.out.println("🏭 Fornecedores: " + fr.findAll().size());
        System.out.println("📍 Localizações: " + ir.findAll().size());  // ✅ Novo

        new MenuPrincipal(ph, uh, lh, fh, ih, rh, exh, eh, ah).run();
    }
}
