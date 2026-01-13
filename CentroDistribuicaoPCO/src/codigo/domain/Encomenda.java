package codigo.domain;

import codigo.domain.enums.EstadoEncomenda;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa uma encomenda de cliente (loja) composta por múltiplas linhas de produtos.
 * 
 * <p><strong>Fluxo de vida da encomenda:</strong></p>
 * <ol>
 *   <li><strong>POR_PREPARAR:</strong> Recém-criada (GESTOR_lOG)</li>
 *   <li><strong>PREPARADA:</strong> Operador SEL preparou expedição</li>
 *   <li><strong>POR_EXPEDIR:</strong> Operador ARM moveu para doca</li>
 *   <li><strong>EXPEDIDA:</strong> Saída do armazém</li>
 *   <li><strong>CANCELADA:</strong> Cancelada pelo gestor</li>
 * </ol>
 * 
 * <p>Gerida pelo {@link Encomendahandler} com reservas automáticas de stock.</p>
 */
public class Encomenda {

    /**
     * Referência única da encomenda (gerada pelo sistema).
     */
    private String referencia;
    
    /**
     * Loja de destino da encomenda.
     */
    private Loja loja;
    
    /**
     * Prioridade de processamento (1=baixa, 5=alta).
     */
    private int prioridade;
    
    /**
     * Estado atual da encomenda.
     * 
     * @see EstadoEncomenda
     */
    private EstadoEncomenda estado;
    
    /**
     * Linhas de itens da encomenda (imutável externamente).
     */
    private final List<LinhaEncomenda> linhas = new ArrayList<>();

    /**
     * Construtor que cria uma encomenda no estado inicial POR_PREPARAR.
     * 
     * @param referencia Referência única (ex: "ENC-20260113-001")
     * @param loja       Loja destinatária
     * @param prioridade Prioridade de 1 (baixa) a 5 (alta)
     * @throws IllegalArgumentException se prioridade inválida
     */
    public Encomenda(String referencia, Loja loja, int prioridade) {
        if (prioridade < 1 || prioridade > 5) {
            throw new IllegalArgumentException("Prioridade deve ser entre 1 (baixa) e 5 (alta)");
        }
        
        this.referencia = referencia;
        this.loja = loja;
        this.prioridade = prioridade;
        this.estado = EstadoEncomenda.POR_PREPARAR;
    }

    /**
     * Adiciona uma nova linha à encomenda.
     * 
     * @param nome       Nome/descrição do item
     * @param quantidade Quantidade pedida
     */
    public void adicionarLinha(String nome, int quantidade) {
        linhas.add(new LinhaEncomenda(nome, quantidade));
    }

    // =====================================================================
    // GETTERS (imutáveis)
    // =====================================================================

    /**
     * Referência única identificadora.
     * 
     * @return Referência da encomenda
     */
    public String getReferencia() { 
        return referencia; 
    }

    /**
     * Loja destinatária.
     * 
     * @return Loja associada
     */
    public Loja getLoja() { 
        return loja; 
    }

    /**
     * Nível de prioridade (1-5).
     * 
     * @return Prioridade numérica
     */
    public int getPrioridade() { 
        return prioridade; 
    }

    /**
     * Estado atual de processamento.
     * 
     * @return Estado da encomenda
     */
    public EstadoEncomenda getEstado() { 
        return estado; 
    }

    /**
     * Cópia defensiva das linhas da encomenda.
     * 
     * @return Lista não-modificável das linhas
     */
    public List<LinhaEncomenda> getLinhas() { 
        return new ArrayList<>(linhas); 
    }

    // =====================================================================
    // SETTERS (limitados)
    // =====================================================================

    /**
     * Atualiza o estado da encomenda (workflow).
     * 
     * @param estado Novo estado válido
     */
    public void setEstado(EstadoEncomenda estado) { 
        this.estado = estado; 
    }

    // =====================================================================
    // MÉTODOS DE NEGÓCIO (prioridade)
    // =====================================================================

    /**
     * Verifica se a encomenda tem prioridade alta (4 ou 5).
     * 
     * @return true se prioridade >= 4
     */
    public boolean isAltaPrioridade() {
        return prioridade >= 4; // 4 e 5 = alta prioridade
    }
    
    /**
     * Devolve representação textual da prioridade com estrelas visuais.
     * 
     * @return String formatada (ex: "★★★★☆ Alta")
     */
    public String getPrioridadeTexto() {
        return switch (prioridade) {
            case 1 -> "★☆☆☆☆ Baixa";
            case 2 -> "★★☆☆☆ Baixa";
            case 3 -> "★★★☆☆ Média";
            case 4 -> "★★★★☆ Alta";
            case 5 -> "★★★★★ Alta";
            default -> "Inválida";
        };
    }

    // =====================================================================
    // OVERRIDES PADRÃO
    // =====================================================================

    /**
     * Representação legível para UI/debug.
     * 
     * @return String formatada da encomenda
     */
    @Override
    public String toString() {
        return String.format("Encomenda{%s | Loja: %s | Prioridade: %s | Linhas: %d}",
                referencia, loja.getNome(), getPrioridadeTexto(), linhas.size());
    }
   
    /**
     * Comparação baseada na referência única.
     * Duas encomendas são iguais se tiverem a mesma referência.
     * 
     * @param obj Objeto a comparar
     * @return true se referências coincidem
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Encomenda)) return false;  // ✅ Corrigido: era "Expedicao"
        Encomenda encomenda = (Encomenda) obj;
        return Objects.equals(referencia, encomenda.getReferencia());
    }
    
    /**
     * Hashcode baseado na referência única.
     * 
     * @return Hashcode da referência
     */
    @Override 
    public int hashCode() {
        return Objects.hashCode(referencia);
    }
}
