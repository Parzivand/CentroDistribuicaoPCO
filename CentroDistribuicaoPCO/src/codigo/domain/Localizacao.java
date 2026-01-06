package codigo.domain;


import java.sql.Date;
import java.sql.Time;
import java.text.ListFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import codigo.domain.enums.TipoLocalizacao;
import codigo.domain.enums.TipoRestricoes;

public class Localizacao {

    // Atributos básicos

    private TipoLocalizacao tipo; // estante, solo, frigorifico...
    private int capacidadeMaxima; 
    private ArrayList<TipoRestricoes> restricoesSuportadas; // frio, perigoso...
    private  String codigo;
    // Inventário desta localização: Produto -> quantidade
    private final Map<Produto,Integer> stock = new HashMap<>(); 
    

    // Construtor
    public Localizacao( String codigo,TipoLocalizacao tipo, int capacidadeMaxima,
         ArrayList<TipoRestricoes> restricoesSuportadas) {
        
        this.codigo=codigo;
        this.tipo = tipo;
        this.capacidadeMaxima = capacidadeMaxima;
        this.restricoesSuportadas = restricoesSuportadas;
    }

    // Métodos de inventário
    public int getQuantidade(Produto produto) {
        return stock.getOrDefault(produto, 0);
    }
   
    
    public void adicionar(Produto produto, int quantidade) {
           
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser > 0");
        }
              
        verificarCompatibilidade(produto);

        int atual = getQuantidade(produto);
        stock.put(produto, atual + quantidade);
    }

    public void remover(Produto produto, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser > 0");
        }
        int atual = getQuantidade(produto);
        if (quantidade > atual) {
            throw new IllegalArgumentException("Quantidade a remover maior que a existente");
        }
        int novo = atual - quantidade;
        if (novo == 0) {
            stock.remove(produto);
        } else {
            stock.put(produto, novo);
        }
    }

    public Map<Produto, Integer> getStock() {
        return Collections.unmodifiableMap(stock);
    }


    /**
     * Verifica se esta localização suporta todas as restrições do produto.
     */
    private void verificarCompatibilidade(Produto produto) {
        List<TipoRestricoes> restricoesProduto = produto.getRestricoes();
        
        for (TipoRestricoes restricao : restricoesProduto) {
            if (!suportaRestricao(restricao)) {
                throw new IllegalArgumentException(
                    String.format("Localização %s não suporta '%s' (produto: %s)", 
                        codigo, restricao, produto.getSKU())
                );
            }
        }
    }
   
    /**
     * Verifica se esta localização suporta uma restrição específica.
     */
    private boolean suportaRestricao(TipoRestricoes restricao) { 
         // Compara separando por vírgula ou espaço
        return restricoesSuportadas.contains(restricao);
        }





    // Getters/Setters básicos

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public TipoLocalizacao getTipo() { return tipo; }
    public void setTipo(TipoLocalizacao tipo) { this.tipo = tipo; }

    public int getCapacidadeMaxima() { return capacidadeMaxima; }
    public void setCapacidadeMaxima(int capacidadeMaxima) { this.capacidadeMaxima = capacidadeMaxima; }

    public ArrayList<TipoRestricoes> getRestricoesSuportadas() { return restricoesSuportadas; }
    // altera as restricoes que suporta
    public void addRestricoesSuportadas(TipoRestricoes restricoesSuportadas) { this.restricoesSuportadas.add(restricoesSuportadas); }
    public void removeRestricoesSuportadas(TipoRestricoes restricoesSuportadas) { this.restricoesSuportadas.remove(restricoesSuportadas); }

    @Override
    public String toString() {
        return String.format("Localizacao{codigo='%s', tipo='%s', cap=%d, restricoes='%s', stock=%d itens}",
            codigo, tipo, capacidadeMaxima, restricoesSuportadas, stock.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Localizacao)) return false;
        Localizacao that = (Localizacao) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);    
    }
}
