package codigo.dto;

import codigo.domain.Loja;

public class LojaDTO {
    // Campos EXATOS do teu dados.json: "nome" + "morada" [file:4]
    public String nome;        // "Loja Centro"
    public String morada;      // "Rua Central 123, Ponta Delgada"
    
    // Construtor vazio OBRIGATÓRIO para JsonService [file:1]
    public LojaDTO() {}
    
    /** toEntity() usa CONSTRUTOR da tua classe (morada, nome, codigo) */
    public Loja toEntity() {
        // Gera codigo simples: LOJ + nome truncado (ex: "LOJ-CENTRO")
        String codigo = gerarCodigo(nome);
        
        // Construtor EXATO: morada, nome, codigo
        return new Loja(morada, nome, codigo);
    }
    
    /** Gera codigo único baseado no nome */
    private String gerarCodigo(String nomeLoja) {
        if (nomeLoja == null) return "LOJ-000";
        
        // LOJ + primeiras 6 letras maiúsculas (ex: "LOJ-CENTRO")
        String prefixo = nomeLoja.substring(0, Math.min(6, nomeLoja.length()))
                               .replaceAll("[^A-Z0-9]", "").toUpperCase();
        return "LOJ-" + prefixo;
    }
}
