package codigo.dto;

import codigo.domain.Fornecedor;

public class FornecedorDTO {
    // Campos EXATOS do teu dados.json [file:4]
    public String nome;
    public String email;     // "log@lacto.pt"
    public String telefone;  // "234123456"
    
    // Construtor vazio OBRIGATÓRIO para JsonService [file:1]
    public FornecedorDTO() {}
    
    /** toEntity() direto para teu construtor exato */
    public Fornecedor toEntity() {
        // Usa CONSTRUTOR da tua classe (nome, email, telefone)
        return new Fornecedor(nome, email, telefone);
    }
}