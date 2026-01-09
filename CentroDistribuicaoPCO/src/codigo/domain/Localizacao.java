package codigo.domain;

import java.util.ArrayList;
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
    private Map<Produto,Integer> quarentena= new HashMap<>();
    private Map<Produto, Integer> stockReservado = new HashMap<>();
    

    // Construtor
    public Localizacao(String codigo, TipoLocalizacao tipo, int capacidadeMaxima, ArrayList<TipoRestricoes> restricoesSuportadas) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.capacidadeMaxima = capacidadeMaxima;
        this.restricoesSuportadas = restricoesSuportadas != null ? restricoesSuportadas : new ArrayList<>();
        this.stockReservado = new HashMap<>(); 
    }

    // Métodos de inventário
    public int getQuantidade(Produto produto) {
        return stock.getOrDefault(produto, 0);
    }
    public int getQuantidadeTotal(Produto produto) {
        return getQuantidade(produto) + getQuantidadequarentena(produto);
    }
    
    public void adicionar(Produto produto, int quantidade) {
           
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser > 0");
        }
              
        verificarCompatibilidade(produto);

        int atual = getQuantidade(produto);
        stock.put(produto, atual + quantidade);
    }
    public void adicionarquarentena(Produto produto, int quantidade) {
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser > 0");

        if (!verificarCompatibilidade(produto)) {
            throw new IllegalArgumentException("Localização não suporta restrições do produto");
        }

        int atual = getQuantidadequarentena(produto); // <-- era getQuantidade(produto)
        quarentena.put(produto, atual + quantidade);
    }

    public void removerQuarentena(Produto produto, int quantidade) {
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser > 0");
        int atual = getQuantidadequarentena(produto);
        if (quantidade > atual) throw new IllegalArgumentException("Quantidade a remover maior que a existente");

        int novo = atual - quantidade;
        if (novo == 0) quarentena.remove(produto);
        else quarentena.put(produto, novo);
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
    public Map<Produto, Integer> getQuarentena() {
        return Collections.unmodifiableMap(quarentena);
    }
    
    /**
     * Verifica se esta localização suporta todas as restrições do produto.
     */
    public boolean verificarCompatibilidade(Produto produto) { 
        List<TipoRestricoes> restricoesProduto = produto.getRestricoes();
     
        for (TipoRestricoes restricao : restricoesProduto) {
            if (!suportaRestricao(restricao)){
                return false;
            }   
        }
        return true;
    }
   
    /**
     * Verifica se esta localização suporta uma restrição específica.
     */
    private boolean suportaRestricao(TipoRestricoes restricao) { 
        return restricoesSuportadas.contains(restricao);
        }

    
    // UC09: Reservar stock (sem consumir disponível)
    public void reservarStock(Produto produto, int quantidade) {
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade inválida");
        
        int disponivel = getQuantidade(produto) - getReservado(produto);
        if (disponivel < quantidade) {
            throw new IllegalArgumentException("Stock disponível insuficiente: " + disponivel);
        }
        
        stockReservado.put(produto, getReservado(produto) + quantidade);
    }

    // UC09: Stock disponível real (disponível - reservado)
    public int getQuantidadeDisponivel(Produto produto) {
        return getQuantidade(produto) - getReservado(produto);
    }

    // UC09: Quanto está reservado
    public int getReservado(Produto produto) {
        return stockReservado.getOrDefault(produto, 0);
    }

    // UC09: Libertar reserva (cancelar encomenda)
    public void libertarReserva(Produto produto, int quantidade) {
        int reservado = getReservado(produto);
        if (reservado < quantidade) {
            throw new IllegalArgumentException("Reserva insuficiente");
        }
        stockReservado.put(produto, reservado - quantidade);
        if (stockReservado.get(produto) == 0) {
            stockReservado.remove(produto);
        }
    }

    // UC10: Consumir reserva (preparar expedição)
    public void consumirReserva(Produto produto, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade inválida");
        }

        int reservado = getReservado(produto);
        if (reservado < quantidade) {
            throw new IllegalArgumentException("Reserva insuficiente para consumo");
        }

        int stockAtual = getQuantidade(produto);
        if (stockAtual < quantidade) {
            // Isto NUNCA devia acontecer se a reserva estiver correta
            throw new IllegalStateException("Stock físico inconsistente");
        }

        // 1. Remove do stock físico
        int novoStock = stockAtual - quantidade;
        if (novoStock == 0) {
            stock.remove(produto);
        } else {
            stock.put(produto, novoStock);
        }

        // 2. Remove da reserva
        int novoReservado = reservado - quantidade;
        if (novoReservado == 0) {
            stockReservado.remove(produto);
        } else {
            stockReservado.put(produto, novoReservado);
        }
    }


    public boolean temReservaSuficiente(Produto produto, int quantidade) {
        return getReservado(produto) >= quantidade;
    }


    // Getters/Setters básicos

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public TipoLocalizacao getTipo() { return tipo; }
    public void setTipo(TipoLocalizacao tipo) { this.tipo = tipo; }

    public int getCapacidadeMaxima() { return capacidadeMaxima; }
    public void setCapacidadeMaxima(int capacidadeMaxima) { this.capacidadeMaxima = capacidadeMaxima; }

    public List<TipoRestricoes> getRestricoesSuportadas() { return restricoesSuportadas; }
    // altera as restricoes que suporta
    public void addRestricoesSuportadas(TipoRestricoes restricoesSuportadas) { this.restricoesSuportadas.add(restricoesSuportadas); }
    public void removeRestricoesSuportadas(TipoRestricoes restricoesSuportadas) { this.restricoesSuportadas.remove(restricoesSuportadas); }

    public Map<Produto, Integer> getStockReservado() {
        return Collections.unmodifiableMap(stockReservado);
    }
    public int getQuantidadequarentena (Produto produto){
        return quarentena.getOrDefault(produto, 0);
    }


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
