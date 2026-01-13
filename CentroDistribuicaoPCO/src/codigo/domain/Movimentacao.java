package codigo.domain;

import java.time.LocalDateTime;

/**
 * Registo de movimentação física de stock entre localizações.
 * 
 * <p><strong>Audit trail:</strong> Regista quem, quando, o quê e para onde.</p>
 * <p><strong>Uso:</strong> UC09 Movimentações (OPERDADOR_ARM).</p>
 */
public class Movimentacao {
    
    /** Timestamp da operação */
    private LocalDateTime data;
    
    /** SKU do produto movimentado */
    private String skuProduto;
    
    /** Localização origem */
    private String origem;
    
    /** Localização destino */
    private String destino;
    
    /** Quantidade movimentada */
    private int quantidade;
    
    /** Utilizador responsável */
    private String utilizador;

    /**
     * Cria registo completo de movimentação.
     * 
     * @param data      Data/hora ({@link LocalDateTime#now()})
     * @param sku       SKU do produto
     * @param origem    Localização origem
     * @param destino   Localização destino
     * @param qtd       Quantidade
     * @param user      Email do utilizador
     */
    public Movimentacao(LocalDateTime data, String sku, String origem, String destino, int qtd, String user) {
        this.data = data;
        this.skuProduto = sku;
        this.origem = origem;
        this.destino = destino;
        this.quantidade = qtd;
        this.utilizador = user;
    }
    
    // Getters
    /** @return Data da movimentação */
    public LocalDateTime getData() { return data; }
    
    /** @return SKU do produto */
    public String getSkuProduto() { return skuProduto; }
    
    /** @return Origem */
    public String getOrigem() { return origem; }
    
    /** @return Destino */
    public String getDestino() { return destino; }
    
    /** @return Quantidade */
    public int getQuantidade() { return quantidade; }
    
    /** @return Utilizador responsável */
    public String getUtilizador() { return utilizador; }
    
    /**
     * Formato legível para logs/UI.
     */
    @Override
    public String toString() {
        return String.format("%s: %s (%s→%s, qtd:%d) por %s", 
                data, skuProduto, origem, destino, quantidade, utilizador);
    }
}
