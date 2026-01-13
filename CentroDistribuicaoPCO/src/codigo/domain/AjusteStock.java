package codigo.domain;

/**
 * Representa um ajuste proposto ao stock de um produto numa localização específica.
 * 
 * <p><strong>Fluxo de negócio:</strong></p>
 * <ol>
 *   <li><strong>pendente:</strong> Criado pelo operador</li>
 *   <li><strong>aprovado:</strong> Validado pelo gestor (aprovacao=true)</li>
 *   <li><strong>rejeitado:</strong> Negado pelo gestor</li>
 *   <li><strong>aplicado:</strong> Stock efetivamente ajustado na localização</li>
 * </ol>
 * 
 * <p>Usado pelo {@link AjusteStockHandler} para workflow de aprovação.</p>
 */
public class AjusteStock {

    /**
     * Estados possíveis do ajuste:
     * <ul>
     *   <li>"pendente" - Aguardando aprovação</li>
     *   <li>"aprovado" - Aprovado pelo gestor</li>
     *   <li>"rejeitado" - Negado pelo gestor</li>
     *   <li>"aplicado" - Alteração efetuada no inventário</li>
     * </ul>
     */
    private String estado;
    
    /**
     * SKU do produto a ajustar.
     */
    private String produto_codigo;
    
    /**
     * Identificador único do ajuste (gerado pelo sistema).
     */
    private String id;
    
    /**
     * Localização física onde o ajuste será aplicado.
     */
    private Localizacao localizacao;
    
    /**
     * Flag de aprovação pelo gestor.
     * true = aprovado, false = pendente/rejeitado.
     */
    private boolean aprovacao;
    
    /**
     * Quantidade a ajustar (positiva=acréscimo, negativa=decréscimo).
     */
    private int quantidade;

    /**
     * Construtor padrão que inicializa um ajuste pendente.
     * 
     * @param produto_codigo SKU do produto (ex: "ALI-000001")
     * @param id             Identificador único do ajuste
     * @param localizacao    Localização onde aplicar o ajuste
     * @param quantidade     Quantidade a ajustar (+/-)
     */
    public AjusteStock(String produto_codigo, String id, Localizacao localizacao, int quantidade) {
        this.estado = "pendente";
        this.produto_codigo = produto_codigo;
        this.id = id;
        this.localizacao = localizacao;
        this.aprovacao = false;
        this.quantidade = quantidade;
    }

    // =====================================================================
    // GETTERS E SETTERS
    // =====================================================================

    /**
     * Estado atual do ajuste.
     * 
     * @return String com o estado ("pendente", "aprovado", "rejeitado", "aplicado")
     */
    public String getEstado() { 
        return estado; 
    }

    /**
     * Atualiza o estado do ajuste.
     * 
     * @param estado Novo estado válido
     */
    public void setEstado(String estado) { 
        this.estado = estado; 
    }

    /**
     * SKU do produto alvo do ajuste.
     * 
     * @return Código SKU do produto
     */
    public String getProduto_codigo() { 
        return produto_codigo; 
    }

    /**
     * Define o SKU do produto.
     * 
     * @param produto_codigo Novo SKU
     */
    public void setProduto_codigo(String produto_codigo) { 
        this.produto_codigo = produto_codigo; 
    }

    /**
     * Identificador único do ajuste.
     * 
     * @return ID único gerado pelo sistema
     */
    public String getId() { 
        return id; 
    }

    /**
     * Define o ID do ajuste.
     * 
     * @param id Novo identificador único
     */
    public void setId(String id) { 
        this.id = id; 
    }

    /**
     * Localização física do ajuste.
     * 
     * @return Localizacao onde aplicar o ajuste
     */
    public Localizacao getLocalizacao() { 
        return localizacao; 
    }

    /**
     * Define a localização do ajuste.
     * 
     * @param localizacao Nova localização
     */
    public void setLocalizacao(Localizacao localizacao) { 
        this.localizacao = localizacao; 
    }

    /**
     * Indica se o ajuste foi aprovado pelo gestor.
     * 
     * @return true se aprovado, false se pendente/rejeitado
     */
    public boolean isAprovacao() { 
        return aprovacao; 
    }

    /**
     * Define o estado de aprovação.
     * 
     * @param aprovacao true=aprovado, false=pendente/rejeitado
     */
    public void setAprovacao(boolean aprovacao) { 
        this.aprovacao = aprovacao; 
    }

    /**
     * Quantidade a ajustar no stock.
     * Positiva = acréscimo, Negativa = decréscimo.
     * 
     * @return Quantidade de ajuste
     */
    public int getQuantidade() { 
        return quantidade; 
    }

    /**
     * Define a quantidade de ajuste.
     * 
     * @param quantidade Nova quantidade (+/-)
     */
    public void setQuantidade(int quantidade) { 
        this.quantidade = quantidade; 
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("AjusteStock{id=%s, estado=%s, produto=%s, loc=%s, qtd=%+d, aprovado=%s}",
                id, estado, produto_codigo, 
                localizacao != null ? localizacao.getCodigo() : "null",
                quantidade, aprovacao);
    }
}
