package codigo.domain;

import java.util.Objects;

/**
 * Representa uma loja física cliente do armazém.
 * 
 * <p><strong>Código único:</strong> Formato AAA000000 (3 letras área + 6 dígitos).
 * Exemplo: "LIS000123" (Lisboa #123), "SUP000456" (Supermercado #456).</p>
 * 
 * <p><strong>Uso:</strong> Destinatária de {@link Encomenda}s (UC11).</p>
 */
public class Loja {

    /**
     * Morada física da loja.
     */
    private String morada;
    
    /**
     * Nome comercial da loja.
     */
    private String nome;
    
    /**
     * Código único gerado pelo sistema.
     * Formato: 3 letras área + 6 dígitos sequenciais.
     */
    private String codigo;
    
    /**
     * Área de atuação (3 letras maiúsculas).
     * Usada na geração do código (LIS, SUP, FAR, etc.).
     */
    private String areaAtuacao;

    /**
     * Construtor básico da loja.
     * 
     * @param morada Morada física
     * @param nome   Nome comercial
     * @param codigo Código único gerado
     */
    public Loja(String morada, String nome, String codigo) {
        this.morada = morada;
        this.nome = nome;
        this.codigo = codigo;
    }

    // =====================================================================
    // GETTERS E SETTERS
    // =====================================================================

    /**
     * Morada física da loja.
     * 
     * @return Morada formatada
     */
    public String getMorada() { 
        return morada; 
    }

    /**
     * Atualiza morada.
     * 
     * @param morada Nova morada
     */
    public void setMorada(String morada) { 
        this.morada = morada; 
    }

    /**
     * Nome comercial da loja.
     * 
     * @return Nome da loja
     */
    public String getNome() { 
        return nome; 
    }

    /**
     * Atualiza nome comercial.
     * 
     * @param nome Novo nome
     */
    public void setNome(String nome) { 
        this.nome = nome; 
    }

    /**
     * Código único da loja.
     * 
     * @return Código no formato AAA000000
     */
    public String getCodigo() { 
        return codigo; 
    }

    /**
     * Atualiza código (excepcional).
     * 
     * @param codigo Novo código único
     */
    public void setCodigo(String codigo) { 
        this.codigo = codigo;
    }

    /**
     * Área de atuação da loja (3 letras).
     * 
     * @return Código de área (LIS, SUP, FAR, etc.)
     */
    public String getAreaAtuacao() {
        return areaAtuacao;
    }

    /**
     * Define área de atuação (usada na geração de código).
     * 
     * @param areaAtuacao 3 letras maiúsculas
     */
    public void setAreaAtuacao(String areaAtuacao) {
        this.areaAtuacao = areaAtuacao;
    }

    // =====================================================================
    // OVERRIDES (identificação por código)
    // =====================================================================

    /**
     * Comparação baseada no código único.
     * Duas lojas são iguais se tiverem o mesmo código.
     * 
     * @param o Objeto a comparar
     * @return true se códigos coincidem
     */
    @Override 
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Loja)) return false;
        Loja loja = (Loja) o;
        return Objects.equals(codigo, loja.codigo);
    }

    /**
     * Hashcode consistente com equals (baseado no código).
     * 
     * @return Hashcode do código
     */
    @Override 
    public int hashCode() {
        return Objects.hash(codigo);
    }

    /**
     * Representação legível para UI/debug.
     * Inclui código para identificação rápida.
     * 
     * @return String formatada da loja
     */
    @Override
    public String toString() {
        return String.format("Loja{codigo=%s, nome=%s, morada=%s, area=%s}", 
                codigo, nome, morada, areaAtuacao != null ? areaAtuacao : "n/a");
    }
}
