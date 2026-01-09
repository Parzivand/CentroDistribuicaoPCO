package codigo.handlers;

import codigo.domain.Encomenda;
import codigo.domain.Expedicao;
import codigo.domain.Localizacao;
import codigo.domain.Tarefa;
import codigo.domain.Produto;
import codigo.domain.enums.EstadoExpedicao;
import codigo.domain.enums.EstadoEncomenda;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExpedicaoHandler {

    private final List<Expedicao> expedicoes = new ArrayList<>();
    private final Encomendahandler encomendaHandler;
    private final InventarioHandler inventarioHandler;

    public ExpedicaoHandler(Encomendahandler encomendaHandler,
                            InventarioHandler inventarioHandler) {

        if (encomendaHandler == null || inventarioHandler == null) {
            throw new IllegalArgumentException("Handlers não podem ser null");
        }

        this.encomendaHandler = encomendaHandler;
        this.inventarioHandler = inventarioHandler;
    }

    /* =========================
       UC10 – Criar Expedição
       ========================= */

    public Expedicao criarExpedicao(String referenciaEncomenda, String operador) {

        Encomenda encomenda = encomendaHandler.getEncomenda(referenciaEncomenda);
        if (encomenda == null) {
            throw new IllegalArgumentException("Encomenda não encontrada");
        }

        if (encomenda.getEstado() != EstadoEncomenda.RESERVADA) {
            throw new IllegalStateException("Encomenda não está totalmente reservada");
        }

        Expedicao expedicao = new Expedicao(
                referenciaEncomenda,
                LocalDateTime.now(),
                EstadoExpedicao.POR_PREPARAR
        );

        // Consumir reservas e criar tarefas
        for (Encomendahandler.ReservaDTO r :
                encomendaHandler.getReservasDaEncomenda(referenciaEncomenda)) {

            Localizacao loc = inventarioHandler.getLocalizacaoPorCodigo(r.codigoLocalizacao());
            if (loc == null) {
                throw new IllegalStateException("Localização não encontrada: " + r.codigoLocalizacao());
            }

            loc.consumirReserva(r.produto(), r.quantidade());

            expedicao.adicionarTarefa(
                    new Tarefa(
                            r.codigoLocalizacao(),
                            r.produto().getSKU(),
                            r.quantidade(),
                            operador
                    )
            );
        }

        encomenda.setEstado(EstadoEncomenda.EXPEDIDA);
        expedicao.setEstado(EstadoExpedicao.CONCLUIDA);

        expedicoes.add(expedicao);
        return expedicao;
    }

    /* =========================
       UC12 – Consultas
       ========================= */

    public List<Expedicao> listarExpedicoes() {
        return new ArrayList<>(expedicoes);
    }

    public Expedicao obterExpedicaoPorReferencia(String referenciaEncomenda) {
        return expedicoes.stream()
                .filter(e -> e.getReferenciaEncomenda().equals(referenciaEncomenda))
                .findFirst()
                .orElse(null);
    }
}
