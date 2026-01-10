package codigo.domain;

import codigo.domain.enums.TipoRestricoes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Rececao {

    private Fornecedor fornecedor;
    private LocalDate data;
    private final List<LinhaRececao> linhas = new ArrayList<>();
    private String idRececao;

    public Rececao(Fornecedor fornecedor) {
        if (fornecedor == null) {
            throw new IllegalArgumentException("Fornecedor não pode ser null");
        }
        this.fornecedor = fornecedor;
        
        // gera data/ID por instância
        LocalDateTime agora = LocalDateTime.now();
        this.data = agora.toLocalDate();
        this.idRececao = "REC-" + agora.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    public void adicionarLinha(Produto produto, String lote, int quantidade) {
        if (produto == null || quantidade <= 0 || lote == null || lote.isBlank()) {
            throw new IllegalArgumentException("Produto, lote e quantidade válidos requeridos");
        }

        LinhaRececao linha = new LinhaRececao(produto, lote, quantidade);
        linhas.add(linha);

        // regras de estado
        if (produto.getValidade() == null && produto.getRestricoes().contains(TipoRestricoes.EXIGE_VALIDADE)) {
            linha.setEstado("NC");
        } else {
            linha.setEstado("DISPONIVEL");
        }
    }

    public ArrayList<LinhaRececao> listarProdutosquarentena() {
        ArrayList<LinhaRececao> subLista = new ArrayList<>();
        for (LinhaRececao linha : linhas) {
            if ("NC".equals(linha.getEstado())) {
                subLista.add(linha);
            }
        }
        return new ArrayList<>(subLista);
    }

    // Getters
    public Fornecedor getFornecedor() { return fornecedor; }
    public LocalDate getData() { return data; }
    public List<LinhaRececao> getLinhas() { return linhas; }
    public String getIdRececao() { return idRececao; }
    public int getTotalLinhas() { return linhas.size(); }
    public int getQuantidadeTotal() {
        return linhas.stream().mapToInt(LinhaRececao::getQuantidadeRecebida).sum();
    }

    public void setFornecedor(Fornecedor fornecedor) {
        if (fornecedor == null) throw new IllegalArgumentException("Fornecedor não pode ser null");
        this.fornecedor = fornecedor;
    }

    @Override
    public String toString() {
        return String.format("Rececao{id=%s, fornecedor=%s, data=%s, linhas=%d, totalQtd=%d}",
                idRececao, fornecedor.getNome(), data, linhas.size(), getQuantidadeTotal());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rececao)) return false;
        Rececao rececao = (Rececao) o;
        return Objects.equals(idRececao, rececao.idRececao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRececao);
    }
}
