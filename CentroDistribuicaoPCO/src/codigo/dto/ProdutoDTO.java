package codigo.dto;

import java.util.List;

class ProdutoDTO {
    public String sku;           
    public String nome;
    public String unidadeMedida; 
    public boolean exigeValidade;
    public List<String> restricoes;  
    public String fornecedorId;      
}