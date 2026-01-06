package codigo.dto;

import java.util.Date;
import java.util.List;

public class ProdutoDTO {
    public String nome;
    public String unidadeMedida;
    public String categoria;
    public Date validade;                    // neste momento o JSON está com null
    public List<String> restricoes;  // FRIO, PERIGOSO, etc.
    
    // construtor vazio obrigatório para o JsonService.fromJson(...)
    public ProdutoDTO() {
    }
}