package codigo.handlers;

import codigo.domain.Fornecedor;
import codigo.domain.Produto;
import codigo.repositories.FornecedorRepository;
import java.util.*;

public class FornecedorHandler {

    private final FornecedorRepository repo;

    // email (UPPER) -> lista de produtos associados
    private final Map<String, List<Produto>> produtosPorEmail = new HashMap<>();

    public FornecedorHandler(FornecedorRepository repo) {
        if (repo == null) throw new IllegalArgumentException("repo não pode ser null");
        this.repo = repo;

        // Inicializa o mapa de associações para os fornecedores já carregados do JSON
        for (Fornecedor f : repo.findAll()) {
            produtosPorEmail.putIfAbsent(f.getEmail().trim().toUpperCase(), new ArrayList<>());
        }
    }

    // UC03
    public void adicionarFornecedor(String nome, String email, String telefone) {
        if (nome == null || email == null || telefone == null) {
            throw new IllegalArgumentException("falta elementos");
        }

        String key = email.trim().toUpperCase();

        if (repo.existsByEmail(key)) {
            throw new IllegalArgumentException("Fornecedor já existe com esse email: " + key);
        }

        Fornecedor f = new Fornecedor(nome.trim(), key, telefone.trim());
        repo.save(f);
        produtosPorEmail.putIfAbsent(key, new ArrayList<>());
    }

    public void associarProdutos(Produto produto, String email) {
        if (produto == null || email == null) throw new IllegalArgumentException("argumentos invalidos");

        String key = email.trim().toUpperCase();
        if (!repo.existsByEmail(key)) throw new IllegalArgumentException("fornecedor inexistente: " + key);

        List<Produto> lista = produtosPorEmail.computeIfAbsent(key, k -> new ArrayList<>());
        if (lista.contains(produto)) throw new IllegalArgumentException("o fornecedor ja tem esse produto na lista");

        lista.add(produto);
    }

    public void desassociarproduto(Produto produto, String email) {
        if (produto == null || email == null) throw new IllegalArgumentException("argumentos invalidos");

        String key = email.trim().toUpperCase();
        if (!repo.existsByEmail(key)) throw new IllegalArgumentException("fornecedor inexistente: " + key);

        List<Produto> lista = produtosPorEmail.getOrDefault(key, new ArrayList<>());
        if (!lista.contains(produto)) throw new IllegalArgumentException("o fornecedor nao tem produto na sua lista");

        lista.remove(produto);
    }

    // UC20 (só remove se não tiver produtos associados)
    public void removerfornecedor(String email) {
        if (email == null) throw new IllegalArgumentException("email inválido");

        String key = email.trim().toUpperCase();
        if (!repo.existsByEmail(key)) {
            throw new IllegalArgumentException(String.format("o fornecedor com o email %s nao esta registado", key));
        }

        List<Produto> lista = produtosPorEmail.getOrDefault(key, List.of());
        if (!lista.isEmpty()) {
            throw new IllegalStateException("nao da para tirar esse fornecedor (tem produtos associados)");
        }

        repo.deleteByEmail(key);
        produtosPorEmail.remove(key);
    }

    public String verpornome(String email) {
        String key = email.trim().toUpperCase();
        return repo.findByEmail(key)
                .orElseThrow(() -> new IllegalArgumentException("Esse fornecedor nao existe"))
                .toString();
    }

    public HashMap<String, Fornecedor> getfornecedores() {
        HashMap<String, Fornecedor> out = new HashMap<>();
        for (Fornecedor f : repo.findAll()) {
            out.put(f.getEmail().trim().toUpperCase(), f);
        }
        return out;
    }

    // opcional: lista 10 a 10 (se quiseres um menu bonito como produtos)
    public ArrayList<Fornecedor> getfornecedores(int pageSize, int pageNumber) {
        ArrayList<Fornecedor> todos = new ArrayList<>(repo.findAll());
        int start = pageSize * pageNumber;
        if (start >= todos.size()) return new ArrayList<>();
        int end = Math.min(start + pageSize, todos.size());
        return new ArrayList<>(todos.subList(start, end));
    }
}
