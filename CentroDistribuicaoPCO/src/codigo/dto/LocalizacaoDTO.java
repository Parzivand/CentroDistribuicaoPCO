package codigo.dto;

import codigo.domain.Localizacao;
import codigo.domain.enums.TipoLocalizacao;
import codigo.domain.enums.TipoRestricoes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LocalizacaoDTO {
    // Campos básicos do construtor da tua Localizacao
    public String codigo;                    // "A1"
    public String tipo;                      // "ESTANTE" (string → enum)
    public int capacidadeMaxima;             // 100
    public List<String> restricoesSuportadas; // ["FRIO", "PERIGOSO"]
    
    // Construtor vazio OBRIGATÓRIO para JsonService [file:1]
    public LocalizacaoDTO() {}
    
    /** toEntity() usa CONSTRUTOR exato da tua classe */
    public Localizacao toEntity() {
        // Converte string → TipoLocalizacao enum
        TipoLocalizacao tipoEnum = stringToTipoLocalizacao(tipo);
        
        // Converte List<String> → ArrayList<TipoRestricoes>
        ArrayList<TipoRestricoes> restricoesEnum = restricoesStringToArray(restricoesSuportadas);
        
        // Construtor EXATO da tua classe
        return new Localizacao(codigo, tipoEnum, capacidadeMaxima, restricoesEnum);
    }
    
    /** String JSON → TipoLocalizacao enum */
    private TipoLocalizacao stringToTipoLocalizacao(String tipoStr) {
        if (tipoStr == null) return TipoLocalizacao.ESTANTE;  // default
        
        return TipoLocalizacao.valueOf(tipoStr.trim().toUpperCase());
    }
    
    /** List<String> JSON → ArrayList<TipoRestricoes> para construtor */
    private ArrayList<TipoRestricoes> restricoesStringToArray(List<String> restricoesStr) {
        if (restricoesStr == null || restricoesStr.isEmpty()) {
            return new ArrayList<>();  // Construtor aceita empty
        }
        
        return restricoesStr.stream()
            .filter(r -> r != null && !r.trim().isEmpty())
            .map(r -> TipoRestricoes.valueOf(r.trim().toUpperCase().replace("-", "_")))
            .collect(Collectors.toCollection(ArrayList::new));
    }
}
