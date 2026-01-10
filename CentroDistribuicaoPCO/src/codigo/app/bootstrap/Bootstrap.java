package codigo.app.bootstrap;

import codigo.dto.*;
import codigo.repositories.*;
import codigo.resources.*;
import java.util.List;

public class Bootstrap {
   private static final JsonService json = new JsonService();
    private static final ProdutoRepository produtos = new ProdutoRepository();
    private static final UtilizadorRepository users = new UtilizadorRepository();
    private static final LojaRepository lojas = new LojaRepository();
    private static final FornecedorRepository fornecedores = new FornecedorRepository();
    private static final String path = "codigo/resources/dados.json";

    public static void initDadosCompletos() {
        try {
            System.out.println("user.dir=" + System.getProperty("user.dir"));
            List<ProdutoDTO> produtosDTO    = json.readListFieldFromFile(path, "produtos",     ProdutoDTO.class);
            List<UtilizadorDTO> usersDTO    = json.readListFieldFromFile(path, "utilizadores", UtilizadorDTO.class);
            List<FornecedorDTO> fornDTO     = json.readListFieldFromFile(path, "fornecedores", FornecedorDTO.class);
            List<LojaDTO> lojasDTO          = json.readListFieldFromFile(path, "lojas",        LojaDTO.class);

            carregarFornecedores(fornDTO);
            carregarProdutos(produtosDTO);
            carregarLojas(lojasDTO);
            carregarUtilizadores(usersDTO);

        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
        }
    }

    public static UtilizadorRepository getUtilizadorRepository() {
        return users;
    }

    public static ProdutoRepository getProdutoRepository() {
        return produtos;
    }

    public static LojaRepository getLojaRepository() {
        return lojas;
    }

    public static FornecedorRepository getFornecedorRepository() {
        return fornecedores;
    }
    
    // 🔥 save() genérico dos TEUS repos [file:29][file:30][file:31][file:32]
    private static void carregarFornecedores(List<FornecedorDTO> dtos) {
        dtos.forEach(dto -> fornecedores.save(dto.toEntity()));
        System.out.println("🏢 " + dtos.size());
    }
    
    private static void carregarProdutos(List<ProdutoDTO> dtos) {
        dtos.forEach(dto -> produtos.save(dto.toEntity()));
        System.out.println("📦 " + dtos.size());
    }
    
    private static void carregarLojas(List<LojaDTO> dtos) {
        dtos.forEach(dto -> lojas.save(dto.toEntity()));
        System.out.println("🏪 " + dtos.size());
    }
    
    private static void carregarUtilizadores(List<UtilizadorDTO> dtos) {
        dtos.forEach(dto -> users.save(dto.toEntity()));
        System.out.println("👥 " + dtos.size());
    }
}
