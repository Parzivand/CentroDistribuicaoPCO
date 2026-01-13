package codigo.domain;

/**
 * Linha individual de uma {@link Encomenda}, representando um item específico.
 * 
 * <p><strong>Características:</strong></p>
 * <ul>
 *   <li>Nome imutável (definido na criação)</li>
 *   <li>Quantidade total pedida (mutável apenas via setter validado)</li>
 *   <li>Quantidade pendente (parcialmente satisfeita)</li>
 *   <li>Referência da encomenda pai (para tracking)</li>
 * </ul>
 * 
 * <p>Usada em workflows de encomendas e expedições.</p>
 */
public class LinhaEncomenda {
    
    /**
     * Nome/descrição do produto (imutável após criação).
     */
    private final String produtoNome;
    
    /**
     * Quantidade total solicitada pelo cliente.
     */
    private int quantidade;
    
    /**
     * Referência da encomenda pai (preenchida pelo handler).
     */
    private String referenciaEncomenda;
    
    /**
     * Quantidade ainda pendente de satisfação.
     * Inicialmente igual à quantidade total.
     */
    private int quantidadePendente = 0;

    /**
     * Construtor validado que cria linha com quantidade total.
     * 
     * @param nome       Nome do produto (não-null)
     * @param quantidade Quantidade pedida (> 0)
     * @throws IllegalArgumentException se validação falhar
     */
    public LinhaEncomenda(String nome, int quantidade) {
        if (nome == null || quantidade <= 0) {
            throw new IllegalArgumentException("Produto não pode ser null e quantidade deve ser > 0");
        }
        this.produtoNome = nome;
        this.quantidade = quantidade;
        this.quantidadePendente = quantidade;  // ✅ Inicializa pendente = total
    }

    // =====================================================================
    // GETTERS (camelCase consistente)
    // =====================================================================

    /**
     * Nome do produto (imutável).
     * 
     * @return Nome/descrição do produto
     */
    public String getProduto() { 
        return produtoNome; 
    }

    /**
     * Quantidade total solicitada.
     * 
     * @return Quantidade pedida originalmente
     */
    public int getQuantidade() { 
        return quantidade; 
    }

    /**
     * Referência da encomenda pai.
     * 
     * @return Referência da encomenda (pode ser null inicialmente)
     */
    public String getReferenciaEncomenda() {
        return referenciaEncomenda;
    }

    /**
     * Quantidade ainda pendente de entrega/seleção.
     * Diminui à medida que é processada.
     * 
     * @return Quantidade pendente
     */
    public int getQuantidadePendente() {
        return quantidadePendente;
    }

    // =====================================================================
    // SETTERS VALIDADOS
    // =====================================================================

    /**
     * Atualiza quantidade total solicitada.
     * 
     * @param quantidade Nova quantidade (> 0)
     * @throws IllegalArgumentException se quantidade inválida
     */
    public void setQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser > 0");
        }
        this.quantidade = quantidade;
    }

    /**
     * Atualiza quantidade pendente (diminui durante processamento).
     * 
     * @param quantidadePendente Nova quantidade pendente (>= 0)
     */
    public void setQuantidadePendente(int quantidadePendente) {
        if (quantidadePendente < 0) {
            throw new IllegalArgumentException("Quantidade pendente não pode ser negativa");
        }
        this.quantidadePendente = quantidadePendente;
    }

    /**
     * Define referência da encomenda pai (chamado pelo handler).
     * 
     * @param referenciaEncomenda Referência única da encomenda
     */
    public void setReferenciaEncomenda(String referenciaEncomenda) {
        this.referenciaEncomenda = referenciaEncomenda;
    }

    // =====================================================================
    // MÉTODOS UTILITÁRIOS
    // =====================================================================

    /**
     * Verifica se a linha está totalmente satisfeita.
     * 
     * @return true se quantidadePendente == 0
     */
    public boolean isSatisfeita() {
        return quantidadePendente == 0;
    }

    /**
     * Calcula percentagem satisfeita.
     * 
     * @return Percentagem (0.0 a 1.0)
     */
    public double getPercentagemSatisfeita() {
        return quantidade > 0 ? 1.0 - (double) quantidadePendente / quantidade : 0.0;
    }

    /**
     * Representação para UI/debug.
     * 
     * @return String formatada da linha
     */
    @Override
    public String toString() {
        return String.format("LinhaEncomenda{produto=%s, total=%d, pendente=%d, ref=%s}",
                produtoNome, quantidade, quantidadePendente, 
                referenciaEncomenda != null ? referenciaEncomenda : "sem ref");
    }
}
