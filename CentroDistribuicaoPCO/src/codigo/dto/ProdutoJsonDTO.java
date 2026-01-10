package codigo.dto;

import java.util.List;

public class ProdutoJsonDTO {
    public String nome;
    public String unidadeMedida;
    public String categoria;
    public String validade;            // no teu JSON está null → vem como null
    public List<String> restricoes;    // ex: ["FRIO","EXIGE_VALIDADE"]

    public ProdutoJsonDTO() {}
}