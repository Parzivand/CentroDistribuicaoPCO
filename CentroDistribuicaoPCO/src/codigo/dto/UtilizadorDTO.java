package codigo.dto;

import codigo.domain.Utilizador;
import codigo.domain.enums.Cargo;
import java.util.List;

public class UtilizadorDTO {
    // Campos EXATOS do teu dados.json [file:4]
    public String nome;
    public String email;
    public String password;
    public List<String> permissoes;  // ["criar_produto", ...] - opcional
    public String cargo;             // "ADMINISTRADOR", "GESTOR_LOG"
    
    // Construtor vazio OBRIGATÓRIO para JsonService [file:1]
    public UtilizadorDTO() {}
    
    /** toEntity() usa CONSTRUTOR da tua classe */
    public Utilizador toEntity() {
        // Converte cargo string → Cargo enum
        Cargo cargoEnum = stringToCargo(cargo);
        
        // Construtor EXATO: nome, email, password, cargo
        return new Utilizador(nome, email, password, cargoEnum);
    }
    
    /** String JSON → Cargo enum */
    private Cargo stringToCargo(String cargoStr) {
        if (cargoStr == null) return Cargo.ADMINISTRADOR;  // default
        
        String normalized = cargoStr.trim().toUpperCase()
            .replace("_", "")      // "GESTOR_LOG" → "GESTORLOG"
            .replace("-", "");     // se houver hífen
            
        // Mapeamento direto dos teus cargos JSON
        switch (normalized) {
            case "ADMINISTRADOR": return Cargo.ADMINISTRADOR;
            case "GESTORLOG": return Cargo.GESTOR_lOG;
            case "OPERADORARM": return Cargo.OPERDADOR_ARM;  
            case "OPERADORSEL": return Cargo.OPERDADOR_SEL;
            case "OPERADORREC": return Cargo.OPERDADOR_REC;
            default: return Cargo.OPERDADOR_ARM;  
        }
    }
}
