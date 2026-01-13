package codigo.domain;

import codigo.domain.enums.TipoLocalizacao;
import codigo.domain.enums.TipoRestricoes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Localização física no armazém com gestão completa de inventário.
 * 
 * <p><strong>Tipos suportados:</strong> {@link TipoLocalizacao} (ESTANTE, SOLO, DOCA, etc.)</p>
 * 
 * <p><strong>Inventário composto por:</strong></p>
 * <ul>
 *   <li><strong>stock:</strong> Produtos disponíveis</li>
 *   <li><strong>quarentena:</strong> Produtos NC (não conformes)</li>
 *   <li><strong>stockReservado:</strong> Produtos reservados para encomendas</li>
 * </ul>
 * 
 * <p><strong>UCs suportadas:</strong> Receções (UC07), Ajustes (UC09), Reservas (UC10)</p>
 */
public class Localizacao {

    // =====================================================================
    // ATRIBUTOS BÁSICOS
    // =====================================================================

    /**
     * Tipo físico da localização (ESTANTE, SOLO, DOCA, etc.).
     */
    private TipoLocalizacao tipo;
    
    /**
     * Capacidade máxima total em unidades.
     */
    private int capacidadeMaxima;
    
    /**
     * Restrições suportadas por este tipo de localização.
     */
    private ArrayList<TipoRestricoes> restricoesSuportadas;
    
    /**
     * Código único da localização (ex: "ARM0001", "SEL0001").
     */
    private String codigo;

    // =====================================================================
    // INVENTÁRIO (3 mapas independentes)
    // =====================================================================

    /**
     * Stock disponível para picking (não reservado).
     */
    private final Map<Produto, Integer> stock = new HashMap<>();
    
    /**
     * Produtos em quarentena (NC - não conformes).
     */
    private Map<Produto, Integer> quarentena = new HashMap<>();
    
    /**
     * Stock reservado para encomendas pendentes.
     */
    private Map<Produto, Integer> stockReservado = new HashMap<>();

    /**
     * Construtor completo da localização.
     * 
     * @param codigo                Código único (ex: "ARM0001")
     * @param tipo                  Tipo físico (ESTANTE, SOLO, DOCA)
     * @param capacidadeMaxima      Capacidade total em unidades
     * @param restricoesSuportadas  Restrições suportadas (pode ser null → lista vazia)
     */
    public Localizacao(String codigo, TipoLocalizacao tipo, int capacidadeMaxima, 
                      ArrayList<TipoRestricoes> restricoesSuportadas) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.capacidadeMaxima = capacidadeMaxima;
        this.restricoesSuportadas = restricoesSuportadas != null ? restricoesSuportadas : new ArrayList<>();
        this.stockReservado = new HashMap<>(); 
    }

    // =====================================================================
    // CONSULTAS DE STOCK
    // =====================================================================

    /**
     * Quantidade disponível no stock principal (não quarentena).
     * 
     * @param produto Produto a consultar
     * @return Quantidade ou 0 se não existir
     */
    public int getQuantidade(Produto produto) {
        return stock.getOrDefault(produto, 0);
    }

    /**
     * Quantidade total = stock + quarentena.
     * 
     * @param produto Produto a consultar
     * @return Soma das quantidades
     */
    public int getQuantidadeTotal(Produto produto) {
        return getQuantidade(produto) + getQuantidadeQuarentena(produto);
    }

    /**
     * Quantidade em quarentena (NC).
     * 
     * @param produto Produto a consultar
     * @return Quantidade em quarentena ou 0
     */
    public int getQuantidadeQuarentena(Produto produto) {
        return quarentena.getOrDefault(produto, 0);
    }

    /**
     * Quantidade reservada para encomendas.
     * 
     * @param produto Produto a consultar
     * @return Quantidade reservada ou 0
     */
    public int getReservado(Produto produto) {
        return stockReservado.getOrDefault(produto, 0);
    }

    /**
     * Stock fisicamente disponível = stock - reservado.
     * 
     * @param produto Produto a consultar
     * @return Quantidade disponível para novo picking
     */
    public int getQuantidadeDisponivel(Produto produto) {
        return getQuantidade(produto) - getReservado(produto);
    }

    // =====================================================================
    // OPERAÇÕES DE STOCK (UC07 Receções)
    // =====================================================================

    /**
     * Adiciona ao stock principal (valida compatibilidade).
     * 
     * @param produto   Produto a adicionar
     * @param quantidade Quantidade (> 0)
     * @throws IllegalArgumentException se quantidade inválida ou incompatibilidade
     */
    public void adicionar(Produto produto, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser > 0");
        }
        
        verificarCompatibilidade(produto);
        int atual = getQuantidade(produto);
        stock.put(produto, atual + quantidade);
    }

    /**
     * Adiciona à quarentena (NC).
     * 
     * @param produto   Produto em quarentena
     * @param quantidade Quantidade (> 0)
     * @throws IllegalArgumentException se quantidade inválida ou incompatibilidade
     */
    public void adicionarQuarentena(Produto produto, int quantidade) {
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser > 0");

        verificarCompatibilidade(produto);

        int atual = getQuantidadeQuarentena(produto);
        quarentena.put(produto, atual + quantidade);
    }

    /**
     * Remove da quarentena (validação de stock).
     * 
     * @param produto   Produto a remover
     * @param quantidade Quantidade a remover
     * @throws IllegalArgumentException se quantidade inválida ou insuficiente
     */
    public void removerQuarentena(Produto produto, int quantidade) {
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser > 0");
        int atual = getQuantidadeQuarentena(produto);
        if (quantidade > atual) throw new IllegalArgumentException("Quantidade a remover maior que a existente");

        int novo = atual - quantidade;
        if (novo == 0) quarentena.remove(produto);
        else quarentena.put(produto, novo);
    }

    /**
     * Remove do stock principal (validação de stock).
     * 
     * @param produto   Produto a remover
     * @param quantidade Quantidade a remover
     * @throws IllegalArgumentException se quantidade inválida ou insuficiente
     */
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

    // =====================================================================
    // VALIDAÇÕES DE COMPATIBILIDADE (UC07)
    // =====================================================================

    /**
     * Verifica se a localização suporta todas as restrições do produto.
     * 
     * @param produto Produto a validar
     * @return true se compatível
     * @throws IllegalArgumentException se incompatível
     */
    public boolean verificarCompatibilidade(Produto produto) { 
        List<TipoRestricoes> restricoesProduto = produto.getRestricoes();
    
        for (TipoRestricoes restricao : restricoesProduto) {
            if (!suportaRestricao(restricao)) {
                throw new IllegalArgumentException(
                    String.format("Localização %s não suporta %s", codigo, restricao));
            }
        }
        return true;
    }

    /**
     * Verifica suporte a restrição específica.
     * 
     * @param restricao Restrição a verificar
     * @return true se suportada
     */
    private boolean suportaRestricao(TipoRestricoes restricao) { 
        return restricoesSuportadas.contains(restricao);
    }

    // =====================================================================
    // OPERAÇÕES DE RESERVA (UC09-10)
    // =====================================================================

    /**
     * Reserva stock para encomenda (não consome fisicamente).
     * 
     * @param produto   Produto a reservar
     * @param quantidade Quantidade a reservar
     * @throws IllegalArgumentException se stock insuficiente
     */
    public void reservarStock(Produto produto, int quantidade) {
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade inválida");
        
        int disponivel = getQuantidadeDisponivel(produto);
        if (disponivel < quantidade) {
            throw new IllegalArgumentException("Stock disponível insuficiente: " + disponivel);
        }
        
        stockReservado.put(produto, getReservado(produto) + quantidade);
    }

    /**
     * Liberta reserva (cancelamento de encomenda).
     * 
     * @param produto   Produto a libertar
     * @param quantidade Quantidade a libertar
     * @throws IllegalArgumentException se reserva insuficiente
     */
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

    /**
     * Consome reserva durante expedição (remove fisicamente).
     * 
     * @param produto   Produto a consumir
     * @param quantidade Quantidade a consumir
     * @throws IllegalArgumentException se reserva ou stock insuficiente
     */
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

    /**
     * Verifica se há reserva suficiente para picking.
     * 
     * @param produto   Produto a verificar
     * @param quantidade Quantidade necessária
     * @return true se reserva >= quantidade
     */
    public boolean temReservaSuficiente(Produto produto, int quantidade) {
        return getReservado(produto) >= quantidade;
    }

    // =====================================================================
    // GETTERS BÁSICOS (visão geral)
    // =====================================================================

    /**
     * Código único da localização.
     */
    public String getCodigo() { 
        return codigo; 
    }

    /**
     * Atualiza código (excepcional).
     */
    public void setCodigo(String codigo) { 
        this.codigo = codigo; 
    }

    /**
     * Tipo físico da localização.
     */
    public TipoLocalizacao getTipo() { 
        return tipo; 
    }

    /**
     * Atualiza tipo.
     */
    public void setTipo(TipoLocalizacao tipo) { 
        this.tipo = tipo; 
    }

    /**
     * Capacidade máxima total.
     */
    public int getCapacidadeMaxima() { 
        return capacidadeMaxima; 
    }

    /**
     * Atualiza capacidade.
     */
    public void setCapacidadeMaxima(int capacidadeMaxima) { 
        this.capacidadeMaxima = capacidadeMaxima; 
    }

    /**
     * Lista de restrições suportadas.
     */
    public List<TipoRestricoes> getRestricoesSuportadas() { 
        return restricoesSuportadas; 
    }

    /**
     * Adiciona nova restrição suportada.
     */
    public void addRestricoesSuportadas(TipoRestricoes restricao) { 
        this.restricoesSuportadas.add(restricao); 
    }

    /**
     * Remove restrição suportada.
     */
    public void removeRestricoesSuportadas(TipoRestricoes restricao) { 
        this.restricoesSuportadas.remove(restricao); 
    }

    /**
     * Vista imutável do stock reservado.
     */
    public Map<Produto, Integer> getStockReservado() {
        return Collections.unmodifiableMap(stockReservado);
    }

    /**
     * Vista imutável do stock principal.
     */
    public Map<Produto, Integer> getStock() {
        return Collections.unmodifiableMap(stock);
    }

    /**
     * Vista imutável da quarentena.
     */
    public Map<Produto, Integer> getQuarentena() {
        return Collections.unmodifiableMap(quarentena);
    }

    // =====================================================================
    // OVERRIDES
    // =====================================================================

    /**
     * Representação resumida para UI.
     */
    @Override
    public String toString() {
        return String.format("Localizacao{codigo='%s', tipo='%s', cap=%d, restricoes='%s', stock=%d itens}",
                codigo, tipo, capacidadeMaxima, restricoesSuportadas, stock.size());
    }

    /**
     * Igualdade baseada no código único.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Localizacao)) return false;
        Localizacao that = (Localizacao) o;
        return Objects.equals(codigo, that.codigo);
    }

    /**
     * Hashcode baseado no código.
     */
    @Override
    public int hashCode() {
        return Objects.hash(codigo);    
    }
}
