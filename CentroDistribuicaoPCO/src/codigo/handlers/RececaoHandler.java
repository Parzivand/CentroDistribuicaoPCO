package codigo.handlers;

import codigo.domain.Fornecedor;
import codigo.domain.LinhaRececao;
import codigo.domain.Localizacao;
import codigo.domain.Produto;
import codigo.domain.Rececao;
import codigo.domain.enums.TipoRestricoes;
import codigo.repositories.InventarioRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RececaoHandler {
    private final List<Rececao> rececoes = new ArrayList<>();
    private final InventarioRepository inventarioRepository;

    public RececaoHandler(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    /** UC06: Cria receção ativa */
    public void criarRececao(Fornecedor fornecedor) {
        if (fornecedor == null) {
            throw new IllegalArgumentException("Fornecedor obrigatório");
        }
        rececoes.add(new Rececao(fornecedor));
    }

    /** UC07: Adiciona linha com validação automática */
    public void adicionarLinhaRececao(Produto produto, String lote, int quantidade, String codigoLocalizacao) {
        if (produto == null || lote == null || lote.isBlank() || quantidade <= 0) {
            throw new IllegalArgumentException("Produto, lote e quantidade válidos requeridos");
        }

        Rececao ultimaRececao = rececoes.getLast();
        ultimaRececao.adicionarLinha(produto, lote, quantidade);
    
        // Valida e move para localização
        Localizacao loc = inventarioRepository.findByCodigo(codigoLocalizacao);
        if (loc == null) {
            throw new IllegalArgumentException("Localização não encontrada: " + codigoLocalizacao);
        }

        int espacoDisponivel = loc.getCapacidadeMaxima() - loc.getQuantidadeTotal(produto);
        
        if (produto.getValidade() == null && produto.getRestricoes().contains(TipoRestricoes.EXIGE_VALIDADE)) {
            ultimaRececao.getLinhas().getLast().setEstado("NC");
            loc.adicionarQuarentena(produto, quantidade);
        } else if (quantidade > espacoDisponivel) {
            ultimaRececao.getLinhas().getLast().setEstado("A_ARMAZENAR");
            loc.adicionar(produto, espacoDisponivel);
        } else {
            ultimaRececao.getLinhas().getLast().setEstado("DISPONIVEL");
            loc.adicionar(produto, quantidade);
        }
    }

    /** UC07: Lista NC da receção ativa */
    public List<LinhaRececao> listarQuarentena() {  // LinhaRececao simples (não Rececao.LinhaRececao)
        return rececoes.isEmpty() ? new ArrayList<>() : rececoes.getLast().listarProdutosQuarentena();
    }

    /** UC08: Receções por fornecedor */
    public List<Rececao> filtrarPorFornecedor(Fornecedor fornecedor) {
        if (fornecedor == null) {
            throw new IllegalArgumentException("Fornecedor obrigatório");
        }
        return rececoes.stream()
                .filter(r -> r.getFornecedor().equals(fornecedor))
                .toList();
    }

    /** UC08: Receções por período */
    public List<Rececao> filtrarPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null || inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Período inválido");
        }
        return rececoes.stream()
                .filter(r -> !r.getData().isBefore(inicio) && !r.getData().isAfter(fim))
                .toList();
    }

    /** UC08: Todas as receções (mais recentes primeiro) */
    public List<Rececao> listarRececoes() {
        return new ArrayList<>(rececoes.reversed());
    }

    /** UC06: Resumo da receção ativa */
    public String resumoRececaoAtual() {
        if (rececoes.isEmpty()) {
            return "Nenhuma receção ativa";
        }
        Rececao atual = rececoes.getLast();
        int nc = atual.listarProdutosQuarentena().size();
        return String.format(
            "Receção %s | Fornecedor: %s | Data: %s | Linhas: %d | NC: %d",
            atual.getIdRececao(), atual.getFornecedor().getNome(),
            atual.getData(), atual.getTotalLinhas(), nc
        );
    }

    /** Verifica se produto tem receções pendentes */
    public boolean produtoTemRececoes(Produto produto) {
        return rececoes.stream()
                .flatMap(r -> r.getLinhas().stream())
                .anyMatch(l -> l.getProduto().equals(produto));
    }
}
