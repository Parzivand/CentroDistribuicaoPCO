package codigo.domain;


import java.sql.Date;
import java.sql.Time;
import java.text.ListFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Localizacao {

    // Atributos básicos
    private String codigo;
    private String tipo; // estante, solo, frigorifico...
    private int capacidadeMaxima; 
    private String restricoesSuportadas; // frio, perigoso...
    // Inventário desta localização: Produto -> quantidade
    private final Map<Produto,Integer> stock = new HashMap<>(); 
    

    // Construtor
    public Localizacao(String codigo, String tipo, int capacidadeMaxima, String restricoesSuportadas) {
        this.codigo = codigo;   
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
        List<String> restricoesProduto = produto.getRestricoes();
        
        for (String restricao : restricoesProduto) {
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
    private boolean suportaRestricao(String restricao) {
        if (restricoesSuportadas == null || restricoesSuportadas.isEmpty()) {
            return restricao.isEmpty();
        }
        // Compara separando por vírgula ou espaço
        String[] suportadas = restricoesSuportadas.toLowerCase().split("[,\\s]+");
        return Arrays.stream(suportadas).anyMatch(r -> r.trim().equalsIgnoreCase(restricao));
    }





    // Getters/Setters básicos

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getCapacidadeMaxima() { return capacidadeMaxima; }
    public void setCapacidadeMaxima(int capacidadeMaxima) { this.capacidadeMaxima = capacidadeMaxima; }

    public String getRestricoesSuportadas() { return restricoesSuportadas; }
    public void setRestricoesSuportadas(String restricoesSuportadas) { this.restricoesSuportadas = restricoesSuportadas; }

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
