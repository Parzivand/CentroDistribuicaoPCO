package codigo.handlers;

import codigo.domain.*;
import codigo.domain.enums.EstadoEncomenda;
import java.util.ArrayList;
import java.util.List;

public class ExpedicaoHandler {

    private final List<Expedicao> expedicoes = new ArrayList<>();
    private final Encomendahandler encomendaHandler;
    private final InventarioHandler inventarioHandler;

    public ExpedicaoHandler(Encomendahandler encomendaHandler,
                            InventarioHandler inventarioHandler) {

        if (encomendaHandler == null || inventarioHandler == null)
            throw new IllegalArgumentException("Handlers não podem ser null");

        this.encomendaHandler = encomendaHandler;
        this.inventarioHandler = inventarioHandler;
    }

    /* =========================
       UC10 – Preparar Expedição
       ========================= */

    public Expedicao prepararExpedicao(String referenciaEncomenda, String localizacaoInicial) {

        Encomenda encomenda = encomendaHandler.getEncomenda(referenciaEncomenda);
        if (encomenda == null)
            throw new IllegalArgumentException("Encomenda não encontrada");

        if (!EstadoEncomenda.POR_PREPARAR.equals(encomenda.getEstado()))
            throw new IllegalStateException("Encomenda não está pronta");

        //  CONSUME AS RESERVAS (abate stock real)
        encomendaHandler.consumirReservas(referenciaEncomenda);

        //  Cria expedição
        Expedicao expedicao = new Expedicao(referenciaEncomenda, localizacaoInicial);

        //  Cria tarefas (picking)
        expedicao.associarEncomenda(encomenda);

        //  Encomenda passa a EXPEDIDA
        encomenda.setEstado(EstadoEncomenda.EXPEDIDA);

        expedicoes.add(expedicao);
        return expedicao;
    }

    /* =========================
       UC12 – Mover Expedição
       ========================= */

    public void moverExpedicao(String idExpedicao, String novaLocalizacao) {

        Expedicao ex = obterExpedicao(idExpedicao);
        if (ex == null)
            throw new IllegalArgumentException("Expedição não encontrada");

        ex.moverPara(novaLocalizacao);
    }

    /* =========================
       Consultas
       ========================= */

    public List<Expedicao> listarExpedicoes() {
        return new ArrayList<>(expedicoes);
    }

    public Expedicao obterExpedicao(String id) {
        return expedicoes.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
