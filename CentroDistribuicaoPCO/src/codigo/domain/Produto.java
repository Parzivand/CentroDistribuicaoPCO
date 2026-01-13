package codigo.domain;

import codigo.domain.enums.TipoRestricoes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Produto registado no catálogo do armazém.
 * 
 * <p><strong>Identificador único:</strong> SKU formato CAT-0000000 (categoria + sequencial).</p>
 * <p><strong>Categoria:</strong> 3 letras maiúsculas (COM, ELE, ROU, etc.).</p>
 * 
 * <p><strong>Uso:</strong> Receções (UC07), Encomendas (UC11), Inventário (UC09).</p>
 */
public class Produto {

    // =====================================================================
    // ATRIBUTOS
    // =====================================================================

    /** Nome comercial do produto */
    private String nome;
    
    /** SKU único (CAT-0000000) */
    private String SKU;
    
    /** Unidade de medida (kg, un, litro) */
    private String unidadeMedida;
    
    /** Categoria (3 letras: COM, ELE, ROU) */
    private String categoria;
    
    /** Data de validade (pode ser null) */
    private Date validade;

    /** Restrições de armazenamento (imutável externamente) */
    private final List<TipoRestricoes> restricoes = new ArrayList<>();

    // =====================================================================
    // CONSTRUTORES
    // =====================================================================

    /**
     * Construtor completo com validade.
     * 
     * @param nome           Nome do produto
     * @param SKU            Código único (CAT-0000000)
     * @param unidadeMedida  Unidade (kg, un, litro)
     * @param restricoes     Lista de restrições (pode ser null)
     * @param validade       Data de validade (pode ser null)
     * @param categoria      Categoria (3 letras)
     */
    public Produto(String nome, String SKU, String unidadeMedida,
                   List<TipoRestricoes> restricoes, Date validade, String categoria) {
        this.nome = nome;
        this.SKU = SKU;
        this.unidadeMedida = unidadeMedida;
        if (restricoes != null) {
            this.restricoes.addAll(restricoes);
        }
        this.validade = validade;
        this.categoria = categoria;
    }

    /**
     * Construtor sem validade (chama sobrecarga).
     * 
     * @param nome           Nome do produto
     * @param SKU            Código único
     * @param unidadeMedida  Unidade de medida
     * @param restricoes     Lista de restrições
     * @param categoria      Categoria (3 letras)
     */
    public Produto(String nome, String SKU, String unidadeMedida,
                   List<TipoRestricoes> restricoes, String categoria) {
        this(nome, SKU, unidadeMedida, restricoes, null, categoria);
    }

    // =====================================================================
    // GETTERS E SETTERS
    // =====================================================================

    /** SKU único do produto */
    public String getSKU() { return SKU; }
    
    /** Atualiza SKU (excepcional) */
    public void setSKU(String sku) { this.SKU = sku; }

    /** Nome comercial */
    public String getNome() { return nome; }
    
    /** Atualiza nome */
    public void setNome(String nome) { this.nome = nome; }

    /** Unidade de medida */
    public String getUnidadeMedida() { return unidadeMedida; }
    
    /** Atualiza unidade */
    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; }

    /**
     * Lista imutável de restrições.
     * 
     * @return Cópia defensiva das restrições
     */
    public List<TipoRestricoes> getRestricoes() {
        return Collections.unmodifiableList(restricoes);
    }

    /** Data de validade */
    public Date getValidade() { return validade; }
    
    /** Atualiza validade */
    public void setValidade(Date validade) { this.validade = validade; }

    /** Categoria (3 letras) */
    public String getCategoria() { return categoria; }
    
    /** Atualiza categoria */
    public void setCategoria(String categoria) { this.categoria = categoria; }

    // =====================================================================
    // OPERAÇÕES SOBRE RESTRIÇÕES
    // =====================================================================

    /**
     * Adiciona restrição (evita duplicados).
     * 
     * @param restricao Nova restrição
     */
    public void adicionarRestricao(TipoRestricoes restricao) {
        if (restricao != null && !restricoes.contains(restricao)) {
            restricoes.add(restricao);
        }
    }

    /**
     * Remove restrição específica.
     * 
     * @param restricao Restrição a remover
     */
    public void removerRestricao(TipoRestricoes restricao) {
        restricoes.remove(restricao);
    }

    /**
     * Comparação lexicográfica por SKU (para ordenação).
     * 
     * @param o Outro produto
     * @return Resultado da comparação SKU
     */
    public int compareTo(Produto o) {
        return this.SKU.compareTo(o.SKU);
    }

    // =====================================================================
    // OVERRIDES (identificação por SKU)
    // =====================================================================

    /**
     * Igualdade baseada no SKU único.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produto)) return false;
        Produto produto = (Produto) o;
        return Objects.equals(SKU, produto.SKU);
    }

    /**
     * Hashcode baseado no SKU.
     */
    @Override
    public int hashCode() {
        return Objects.hash(SKU);
    }

    /**
     * Representação legível (inclui validade se existente).
     */
    @Override
    public String toString() {
        String base = String.format(
                "SKU: %s nome: %s unidadeMedida: %s restricoes: %s",
                SKU, nome, unidadeMedida, restricoes
        );
        if (validade == null) {
            return base;
        }
        return base + String.format(" validade: %s", validade);
    }
}
