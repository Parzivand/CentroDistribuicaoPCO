package codigo.domain;

import codigo.domain.enums.TipoRestricoes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Receção de mercadoria de um {@link Fornecedor} com múltiplas linhas.
 * 
 * <p><strong>ID automático:</strong> REC-yyyyMMddHHmmss (ex: REC-20260113143520)</p>
 * <p><strong>Estados das linhas:</strong> DISPONIVEL | NC (se falta validade obrigatória)</p>
 * 
 * <p><strong>Uso:</strong> UC06-08 Receções (OPERDADOR_REC).</p>
 */
public class Rececao {

    /** Fornecedor da receção */
    private Fornecedor fornecedor;
    
    /** Data da receção (dia atual) */
    private LocalDate data;
    
    /** Linhas da receção */
    private final List<LinhaRececao> linhas = new ArrayList<>();
    
    /** ID único gerado automaticamente */
    private String idRececao;

    /**
     * Construtor que cria receção ativa para fornecedor.
     * Gera data e ID únicos automaticamente.
     * 
     * @param fornecedor Fornecedor (não-null)
     * @throws IllegalArgumentException se fornecedor null
     */
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

    /**
     * Adiciona linha à receção ativa.
     * Aplica regras automáticas de estado (NC se falta validade obrigatória).
     * 
     * @param produto   Produto recebido
     * @param lote      Lote/series
     * @param quantidade Quantidade recebida
     * @throws IllegalArgumentException se parâmetros inválidos
     */
    public void adicionarLinha(Produto produto, String lote, int quantidade) {
        if (produto == null || quantidade <= 0 || lote == null || lote.isBlank()) {
            throw new IllegalArgumentException("Produto, lote e quantidade válidos requeridos");
        }

        LinhaRececao linha = new LinhaRececao(produto, lote, quantidade);
        linhas.add(linha);

        // regras de estado automáticas
        if (produto.getValidade() == null && 
            produto.getRestricoes().contains(TipoRestricoes.EXIGE_VALIDADE)) {
            linha.setEstado("NC");
        } else {
            linha.setEstado("DISPONIVEL");
        }
    }

    /**
     * Lista apenas linhas NC (quarentena).
     * 
     * @return Sub-lista de linhas em quarentena
     */
    public ArrayList<LinhaRececao> listarProdutosQuarentena() {
        ArrayList<LinhaRececao> subLista = new ArrayList<>();
        for (LinhaRececao linha : linhas) {
            if ("NC".equals(linha.getEstado())) {
                subLista.add(linha);
            }
        }
        return new ArrayList<>(subLista);
    }

    // =====================================================================
    // GETTERS
    // =====================================================================

    /** Fornecedor da receção */
    public Fornecedor getFornecedor() { return fornecedor; }
    
    /** Data da receção */
    public LocalDate getData() { return data; }
    
    /** Todas as linhas */
    public List<LinhaRececao> getLinhas() { return linhas; }
    
    /** ID único da receção */
    public String getIdRececao() { return idRececao; }
    
    /** Número total de linhas */
    public int getTotalLinhas() { return linhas.size(); }
    
    /**
     * Quantidade total recebida (soma todas linhas).
     * 
     * @return Total de unidades recebidas
     */
    public int getQuantidadeTotal() {
        return linhas.stream().mapToInt(LinhaRececao::getQuantidadeRecebida).sum();
    }

    // =====================================================================
    // SETTERS LIMITADOS
    // =====================================================================

    /**
     * Atualiza fornecedor (validação).
     * 
     * @param fornecedor Novo fornecedor (não-null)
     * @throws IllegalArgumentException se null
     */
    public void setFornecedor(Fornecedor fornecedor) {
        if (fornecedor == null) throw new IllegalArgumentException("Fornecedor não pode ser null");
        this.fornecedor = fornecedor;
    }

    // =====================================================================
    // OVERRIDES
    // =====================================================================

    /**
     * Resumo para UI/relatórios.
     */
    @Override
    public String toString() {
        return String.format("Rececao{id=%s, fornecedor=%s, data=%s, linhas=%d, totalQtd=%d}",
                idRececao, fornecedor.getNome(), data, linhas.size(), getQuantidadeTotal());
    }

    /**
     * Igualdade por ID único.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rececao)) return false;
        Rececao rececao = (Rececao) o;
        return Objects.equals(idRececao, rececao.idRececao);
    }

    /**
     * Hashcode por ID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idRececao);
    }
}
