package codigo.domain;

import java.time.LocalDateTime;

public class Movimentacao {
    private LocalDateTime data;
    private String skuProduto;
    private String origem;
    private String destino;
    private int quantidade;
    private String utilizador;
    
    public Movimentacao(LocalDateTime data, String sku, String origem, String destino, int qtd, String user) {
        this.data = data;
        this.skuProduto = sku;
        this.origem = origem;
        this.destino = destino;
        this.quantidade = qtd;
        this.utilizador = user;
    }
    
    // Getters
    public LocalDateTime getData() { return data; }
    public String getSkuProduto() { return skuProduto; }
    public String getOrigem() { return origem; }
    public String getDestino() { return destino; }
    public int getQuantidade() { return quantidade; }
    public String getUtilizador() { return utilizador; }
    
    @Override
    public String toString() {
        return String.format("%s: %s (%s→%s, qtd:%d) por %s", 
            data, skuProduto, origem, destino, quantidade, utilizador);
    }
}