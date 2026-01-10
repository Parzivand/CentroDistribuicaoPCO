package codigo.dto;

import codigo.domain.Produto;
import codigo.domain.enums.TipoRestricoes;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoDTO {
    // Campos EXATOS do teu dados.json [file:4]
    public String nome;
    public String unidadeMedida;
    public String categoria;
    public Date validade;  // null no JSON
    public List<String> restricoes;  // ["FRIO", "VALIDADE_OBRIGATORIA"]
    
    // Construtor vazio OBRIGATÓRIO para JsonService reflection [file:1]
    public ProdutoDTO() {}
    
    /** toEntity() ajustado à tua Produto EXATA */
    public Produto toEntity() {
        // Mapeia campos básicos com setters da tua classe
        Produto produto = new Produto(nome, null, unidadeMedida, null, categoria);
        produto.setValidade(validade);
        
        // 🔥 Mapping restricoes: List<String> JSON -> List<TipoRestricoes>
        if (restricoes != null && !restricoes.isEmpty()) {
            List<TipoRestricoes> restricoesEnum = restricoes.stream()
                .filter(r -> r != null && !r.trim().isEmpty())
                .map(this::stringToTipoRestricoes)
                .collect(Collectors.toList());
            
            // Usa método da tua classe para adicionar
            restricoesEnum.forEach(produto::adicionarRestricao);
        }
        
        // GERA SKU único (CAT-0000000 formato da tua classe)
        produto.setSKU(gerarSKU( categoria));
        
        return produto;
    }
    
    /** Converte string JSON → TipoRestricoes enum */
    private TipoRestricoes stringToTipoRestricoes(String restricaoStr) {
        if (restricaoStr == null) return null;
        
        String normalized = restricaoStr.trim().toUpperCase()
            .replace("-", "_")      // "VALIDADE-OBRIGATORIA" → "VALIDADE_OBRIGATORIA"
            .replace(" ", "_");     // espaços → _
            
        return TipoRestricoes.valueOf(normalized);
    }
    
    /** Gera SKU no formato da tua classe: CAT-0000000 */
    private String gerarSKU( String categoria) {
        String prefixo = categoria.substring(0, 3).toUpperCase();  // "ALI", "BEB", etc.
        String seq = String.format("%07d", (int)(Math.random() * 10000000));  // 7 dígitos
        return prefixo + "-" + seq;
    }
}
