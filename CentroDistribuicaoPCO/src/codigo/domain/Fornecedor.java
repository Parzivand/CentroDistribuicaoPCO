package codigo.domain;

/**
 * Entidade que representa um fornecedor externo.
 * 
 * <p><strong>Identificação única:</strong> Nome do fornecedor (case-sensitive).
 * Email usado como chave no {@link FornecedorRepository} (case-insensitive).</p>
 * 
 * <p><strong>Uso:</strong> Associado a {@link Rececao}s e usado em relatórios UC08.</p>
 */
import java.util.Objects;

public class Fornecedor {

    /**
     * Nome comercial do fornecedor (identificador lógico único).
     */
    private String nome;
    
    /**
     * Email de contacto (chave única no repositório, normalizado para maiúsculas).
     */
    private String email;
    
    /**
     * Telefone de contacto (opcional).
     */
    private String telefone;

    /**
     * Construtor completo do fornecedor.
     * Todos os campos são atribuídos diretamente (sem validação adicional).
     * 
     * @param nome     Nome comercial único
     * @param email    Email único (normalizado para maiúsculas no repositório)
     * @param telefone Telefone de contacto (pode ser vazio)
     */
    public Fornecedor(String nome, String email, String telefone) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    // =====================================================================
    // GETTERS E SETTERS
    // =====================================================================

    /**
     * Nome comercial do fornecedor.
     * 
     * @return Nome do fornecedor
     */
    public String getNome() { 
        return nome; 
    }

    /**
     * Atualiza o nome do fornecedor.
     * 
     * @param nome Novo nome comercial
     */
    public void setNome(String nome) { 
        this.nome = nome; 
    }

    /**
     * Email de contacto.
     * No repositório é usado em maiúsculas para pesquisa case-insensitive.
     * 
     * @return Email do fornecedor
     */
    public String getEmail() { 
        return email; 
    }

    /**
     * Atualiza o email de contacto.
     * 
     * @param email Novo email
     */
    public void setEmail(String email) { 
        this.email = email; 
    }

    /**
     * Telefone de contacto.
     * 
     * @return Telefone (pode ser vazio)
     */
    public String getTelefone() { 
        return telefone; 
    }

    /**
     * Atualiza o telefone de contacto.
     * 
     * @param telefone Novo telefone
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    // =====================================================================
    // OVERRIDES (identificação por nome)
    // =====================================================================

    /**
     * Comparação baseada no nome (identificador lógico único conforme UC03).
     * Dois fornecedores são iguais se tiverem o mesmo nome.
     * 
     * @param o Objeto a comparar
     * @return true se nomes coincidem
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fornecedor)) return false;
        Fornecedor that = (Fornecedor) o;
        return Objects.equals(nome, that.nome);
    }

    /**
     * Hashcode baseado no nome (consistente com equals).
     * 
     * @return Hashcode do nome
     */
    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    /**
     * Representação JSON-like para debug e UI.
     * 
     * @return String formatada do fornecedor
     */
    @Override
    public String toString() {
        return String.format("Fornecedor{nome='%s', email='%s', telefone='%s'}",
                nome, email, telefone);
    }
}
