package codigo.dto;

import codigo.domain.StockItem;
import codigo.domain.Produto;
import codigo.domain.enums.estadoStock;

public class StockDTO {
    // Campos do construtor da tua StockItem
    public String produto;       // SKU ou nome? (ver abaixo)
    public int quantidade;       // 50
    public String lote;          // "LOT001"
    public String localizacao;   // "A1"
    public String estado;        // "DISPONIVEL" (string → enum)
    
    // Construtor vazio OBRIGATÓRIO para JsonService [file:1]
    public StockDTO() {}
    
    /** toEntity() precisa do Produto carregado antes! */
    public StockItem toEntity(Produto produto) {
        StockItem stock = new StockItem(produto, quantidade, lote, localizacao);
        stock.setEstado(stringToEstadoStock(estado));
        return stock;
    }
    
    /** String JSON → estadoStock enum */
    private estadoStock stringToEstadoStock(String estadoStr) {
        if (estadoStr == null || estadoStr.trim().isEmpty()) {
            return estadoStock.DISPONIVEL;  // default
        }
        return estadoStock.valueOf(estadoStr.trim().toUpperCase());
    }
}
