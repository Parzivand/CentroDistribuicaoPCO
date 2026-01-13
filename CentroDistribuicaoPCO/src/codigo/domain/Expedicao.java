package codigo.domain;

import codigo.domain.enums.EstadoEncomenda;
import codigo.domain.enums.EstadoExpedicao;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Agrupa múltiplas {@link Encomenda}s numa expedição física para seleção e expedição.
 * 
 * <p><strong>Fluxo de negócio da expedição:</strong></p>
 * <ol>
 *   <li><strong>POR_PREPARAR:</strong> Criada vazia numa localização inicial (SEL0001)</li>
 *   <li><strong>PREPARADA:</strong> Operador SEL concluiu todas as {@link Tarefa}s de picking</li>
 *   <li><strong>POR_EXPEDIR:</strong> Operador ARM moveu para doca (REC0001)</li>
 *   <li><strong>EXPEDIDA:</strong> Saída do armazém</li>
 * </ol>
 * 
 * <p>Gerida pelos handlers {@link ExpedicaoHandler} e {@link RececaoHandler}.</p>
 */
public class Expedicao {

    /**
     * Identificador único da expedição (ex: "EXP-20260113-001").
     */
    private String id;
    
    /**
     * Estado atual de processamento.
     * 
     * @see EstadoExpedicao
     */
    private EstadoExpedicao estado;
    
    /**
     * Localização física atual da expedição.
     * Ex: "SEL0001" → "REC0001".
     */
    private String localizacao;
    
    /**
     * Encomendas agrupadas nesta expedição.
     * Só aceita encomendas em estado POR_PREPARAR.
     */
    private final List<Encomenda> encomendas = new ArrayList<>();
    
    /**
     * Tarefas de picking geradas automaticamente para cada linha de encomenda.
     */
    private final List<Tarefa> tarefas = new ArrayList<>();

    /**
     * Construtor que cria expedição vazia em POR_PREPARAR.
     * 
     * @param id                  ID único da expedição
     * @param localizacaoInicial  Localização inicial (geralmente SEL0001)
     */
    public Expedicao(String id, String localizacaoInicial) {
        this.id = id;
        this.localizacao = localizacaoInicial;
        this.estado = EstadoExpedicao.POR_PREPARAR;
    }

    /**
     * Associa uma encomenda à expedição.
     * <ul>
     *   <li>Valida encomenda não-null</li>
     *   <li>Verifica estado POR_PREPARAR</li>
     *   <li>Muda encomenda para RESERVADA</li>
     *   <li>Gera tarefas de picking automáticas</li>
     * </ul>
     * 
     * @param encomenda Encomenda a associar
     * @throws IllegalArgumentException se encomenda null
     * @throws IllegalStateException se encomenda não estiver POR_PREPARAR
     */
    public void associarEncomenda(Encomenda encomenda) {
        if (encomenda == null) {
            throw new IllegalArgumentException("Encomenda não pode ser null");
        }
        if (!EstadoEncomenda.POR_PREPARAR.equals(encomenda.getEstado())) {
            throw new IllegalStateException("Só é possível associar encomendas quando o estado é POR_PREPARAR");
        }
        encomendas.add(encomenda);
        encomenda.setEstado(EstadoEncomenda.RESERVADA);
        gerarTarefasSelecao(encomenda);
    }

    /**
     * Gera automaticamente tarefas de picking para cada linha da encomenda.
     * Cada tarefa tem ID único no formato `EXP001-T1`, `EXP001-T2`, etc.
     * 
     * @param encomenda Encomenda cujas linhas gerar tarefas
     */
    public void gerarTarefasSelecao(Encomenda encomenda) {
        for (LinhaEncomenda linha : encomenda.getLinhas()) {
            String tarefaId = id + "-T" + (tarefas.size() + 1);
            Tarefa tarefa = new Tarefa(tarefaId, String.format("Selecionar %d %s (%s) de %s",
                    linha.getQuantidade(), linha.getProduto(), localizacao));
            tarefas.add(tarefa);
        }
    }

    /**
     * Conclui preparação da expedição.
     * Só possível se <strong>todas</strong> as tarefas estiverem concluídas.
     * Muda estado para PREPARADA.
     * 
     * @throws IllegalStateException se existirem tarefas pendentes
     */
    public void concluirPreparacao() {
        boolean todasConcluidas = tarefas.stream().allMatch(Tarefa::isConcluida);
        if (!todasConcluidas) {
            throw new IllegalStateException(
                    String.format("Todas as %d tarefas devem estar concluídas (%d pendentes)", 
                            tarefas.size(), getTarefasPendentes())
            );
        }
        this.estado = EstadoExpedicao.PREPARADA;
    }

    /**
     * Move a expedição para nova localização.
     * <ul>
     *   <li>Só se estado for PREPARADA</li>
     *   <li>Nova localização não vazia</li>
     *   <li>Muda estado para POR_EXPEDIR</li>
     * </ul>
     * 
     * @param novaLocalizacao Código da nova localização (ex: "REC0001")
     * @throws IllegalStateException se não estiver PREPARADA
     * @throws IllegalArgumentException se localização inválida
     */
    public void moverPara(String novaLocalizacao) {
        if (!EstadoExpedicao.PREPARADA.equals(estado)) {
            throw new IllegalStateException(
                    "Expedição deve estar PREPARADA para mover (estado atual: " + estado + ")"
            );
        }
        if (novaLocalizacao == null || novaLocalizacao.trim().isEmpty()) {
            throw new IllegalArgumentException("Nova localização inválida");
        }
        
        this.localizacao = novaLocalizacao.trim();
        this.estado = EstadoExpedicao.POR_EXPEDIR;
    }

    // =====================================================================
    // MÉTODOS DE CONSULTA (tarefas)
    // =====================================================================

    /**
     * Cópia defensiva das tarefas de picking.
     * 
     * @return Lista não-modificável das tarefas
     */
    public List<Tarefa> getTarefas() { 
        return new ArrayList<>(tarefas); 
    }

    /**
     * Conta tarefas ainda pendentes (não concluídas).
     * 
     * @return Número de tarefas pendentes
     */
    public int getTarefasPendentes() {
        return (int) tarefas.stream().filter(t -> !t.isConcluida()).count();
    }

    /**
     * Verifica se todas as tarefas estão concluídas.
     * 
     * @return true se não houver pendentes
     */
    public boolean todasTarefasConcluidas() {
        return tarefas.stream().allMatch(Tarefa::isConcluida);
    }

    /**
     * Número total de encomendas na expedição.
     * 
     * @return Contagem de encomendas
     */
    public int getTotalEncomendas() { 
        return encomendas.size(); 
    }

    // =====================================================================
    // GETTERS/SETTERS BÁSICOS
    // =====================================================================

    /**
     * ID único da expedição.
     */
    public String getId() { 
        return id; 
    }

    /**
     * Estado atual.
     */
    public EstadoExpedicao getEstado() { 
        return estado; 
    }

    /**
     * Localização atual.
     */
    public String getLocalizacao() { 
        return localizacao; 
    }

    /**
     * Cópia defensiva das encomendas.
     */
    public List<Encomenda> getEncomendas() { 
        return new ArrayList<>(encomendas); 
    }

    /**
     * Setter de estado (workflow interno).
     */
    public void setEstado(EstadoExpedicao estado) { 
        this.estado = estado; 
    }

    /**
     * Setter de localização (excepcional).
     */
    public void setLocalizacao(String localizacao) { 
        this.localizacao = localizacao; 
    }

    // =====================================================================
    // OVERRIDES
    // =====================================================================

    /**
     * Representação para UI/debug.
     */
    @Override
    public String toString() {
        return String.format("Expedicao{id=%s, estado=%s, localizacao=%s, encomendas=%d, tarefas=%d (%d pendentes)}", 
                id, estado, localizacao, encomendas.size(), tarefas.size(), getTarefasPendentes());
    }

    /**
     * Igualdade baseada no ID único.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expedicao)) return false;
        Expedicao that = (Expedicao) o;
        return Objects.equals(id, that.id);
    }

    /**
     * Hashcode baseado no ID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
