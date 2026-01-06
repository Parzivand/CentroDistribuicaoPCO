package codigo.dto;

import java.util.List;

public class UtilizadorDTO {
    public String nome, email, password;
    public List<String> permissoes;
    public String cargo;  // ADMINISTRADOR, GESTOR_LOG, etc.
}