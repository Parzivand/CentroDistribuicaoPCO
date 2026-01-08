package codigo.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import codigo.domain.enums.EstadoExpedicao;
import codigo.domain.enums.Estadoencomenda;

public class Expedicao {

    private String id;
    private EstadoExpedicao estado; //POR_PREPARAR, PREPARADA, POR_EXPEDIR, EXPEDIDA

    private String localizacao;
    private final List<Encomenda> encomendas = new ArrayList<>();
    private final List<Tarefa> tarefas = new ArrayList<>();

    public Expedicao(String id, String localizacaoInicial) {
        this.id = id;
        this.localizacao = localizacaoInicial;
        this.estado = EstadoExpedicao.POR_PREPARAR;
    }

    public void associarEncomenda(Encomenda encomenda) {
        if(encomenda == null) {
            throw new IllegalArgumentException("Encomenda não pode ser null");
        }
        if(!Estadoencomenda.POR_PREPARAR.equals(encomenda.getEstado())) {
            throw new IllegalStateException("Só é possível associar encomendas quando o estado é POR_PREPARAR");
        }
        encomendas.add(encomenda);
        encomenda.setEstado(Estadoencomenda.RESERVADA);
        gerarTarefasSelecao(encomenda);
    }

    // Gera tarefas de picking para cada linha da encomenda

    public void gerarTarefasSelecao(Encomenda encomenda) {
        for(LinhaEncomenda linha : encomenda.getLinhas()) {
            String tarefaId = id + "-T" + (tarefas.size() + 1);
            Tarefa tarefa = new Tarefa(tarefaId, String.format("Selecionar %d %s (%s) de %s",
                linha.getQuantidade(), linha.getProduto(),localizacao));
            tarefas.add(tarefa);
        }
    }

    // Conclui preparação só se TODAS tarefas estiverem feitas
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

    // Move expedição só se estiver PREPARADA

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
    


    // getters/setters

    public List<Tarefa> getTarefas() { return new ArrayList<>(tarefas);}

    public int getTarefasPendentes() {
        return (int) tarefas.stream().filter(t -> !t.isConcluida()).count();
    }

    public boolean todasTarefasConcluidas() {
        return tarefas.stream().allMatch(Tarefa::isConcluida);
    }

    public int getTotalEncomendas() { return encomendas.size(); }



    public String getId() { return id; }
    public EstadoExpedicao getEstado() { return estado; }
    public String getLocalizacao() { return localizacao; }
    public List<Encomenda> getEncomendas() { return new ArrayList<>(encomendas); }

    public void setEstado(EstadoExpedicao estado) { this.estado = estado; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }


     @Override
    public String toString() {
        return String.format("Expedicao{id=%s, estado=%s, localizacao=%s, encomendas=%d, tarefas=%d (%d pendentes)}", 
            id, estado, localizacao, encomendas.size(), tarefas.size(), getTarefasPendentes());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expedicao)) return false;
        Expedicao that = (Expedicao) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
