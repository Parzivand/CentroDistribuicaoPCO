package codigo.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import codigo.domain.enums.TipoRestricoes;
import codigo.domain.enums.TipoLocalizacao;


public class Produto{

    // Atributos
   
    private String nome;
    private String SKU;             // CAT-0000000
    private String unidadeMedida;   // ex.: "kg", "unidade", "litro"
    private String categoria;       // ex.: "COM = comida", "ELE = eletronico", "ROU = roupa"
    private Date validade;

    private final List<TipoRestricoes> restricoes = new ArrayList<>();  // Lista de restrições (ex.: "frio", "perigoso", "validadeObrigatoria")

    // Construtores

    public Produto(String nome, String SKU, String unidadeMedida,
                   List<TipoRestricoes> restricoes,Date validade, String categoria) {
        this.nome = nome;
        this.SKU = SKU;
        this.unidadeMedida = unidadeMedida;
        if (restricoes != null) {
            this.restricoes.addAll(restricoes);
        }
        this.validade = validade;
        this.categoria = categoria;
    }

    public Produto(String nome, String SKU, String unidadeMedida,
                   List<TipoRestricoes> restricoes, String categoria) {
        this(nome, SKU, unidadeMedida, restricoes, null ,categoria);
        
    }

    // Getters e Setters

    public String getSKU() { return SKU; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getUnidadeMedida() { return unidadeMedida; }
    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; }

    
    /**
     * Devolve lista imutável para não alterarem as restrições por fora.
     */
    public List<TipoRestricoes> getRestricoes() {
        return Collections.unmodifiableList(restricoes);
    }

    // compara de acordo com o SkU para posteriormente ser usado na ordenacao alfabetica no Handler 
    public int compareTo(Produto o){
        return this.SKU.compareTo(o.SKU);
    } 
    
    public void adicionarRestricao(TipoRestricoes restricao) {
        if (restricao != null && !restricoes.contains(restricao)) {
            restricoes.add(restricao);
        }
    }

    public void removerRestricao(TipoRestricoes restricao) {
        restricoes.remove(restricao);
    }

    public Date getValidade() { return validade; }
    public void setValidade(Date validade) { this.validade = validade; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    // equals / hashCode (por SKU, que deve ser único)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produto)) return false;
        Produto produto = (Produto) o;
        return Objects.equals(SKU, produto.SKU);
    }

    @Override
    public int hashCode() {
        return Objects.hash(SKU);
    }

    // toString

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
