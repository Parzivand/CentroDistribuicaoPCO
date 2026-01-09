package codigo.handlers;

import codigo.domain.*;
import codigo.domain.enums.EstadoEncomenda;

import java.time.LocalDate;
import java.util.*;

public class Encomendahandler {

    private final Map<String, Encomenda> encomendas = new HashMap<>();
    private final InventarioHandler inventario;

    // Guarda exatamente onde/quanto foi reservado por encomenda (para conseguir cancelar corretamente)
    private final Map<String, List<Reserva>> reservasPorEncomenda = new HashMap<>();

    private static class Reserva {
        final String codigoLocalizacao;
        final Produto produto;
        final int quantidade;

        Reserva(String codigoLocalizacao, Produto produto, int quantidade) {
            this.codigoLocalizacao = codigoLocalizacao;
            this.produto = produto;
            this.quantidade = quantidade;
        }
    }

    public Encomendahandler(InventarioHandler inventario) {
        if (inventario == null) throw new IllegalArgumentException("InventarioHandler não pode ser null");
        this.inventario = inventario;
    }

    // UC09: cria encomenda
    public String criarEncomenda(Loja loja, int prioridade) {
        if (loja == null || prioridade < 1 || prioridade > 5) {
            throw new IllegalArgumentException("Loja inválida ou prioridade tem de ser 1..5");
        }

        String data = LocalDate.now().toString().replace("-", "");
        String referencia;
        Random r = new Random();

        do {
            referencia = String.format("ENC-%s-%04d", data, r.nextInt(10000));
        } while (encomendas.containsKey(referencia));

        Encomenda e = new Encomenda(referencia, loja, prioridade);
        e.setEstado(EstadoEncomenda.CRIADA);

        encomendas.put(referencia, e);
        reservasPorEncomenda.put(referencia, new ArrayList<>());

        return referencia;
    }

    // UC09: adiciona linha + tenta reservar FIFO (sem consumir stock)
         public void adicionarLinhaEncomenda(String referencia, Produto produto, int quantidade) {
        if (produto == null || quantidade <= 0)
            throw new IllegalArgumentException("Dados inválidos");

        Encomenda enc = encomendas.get(referencia);
        if (enc == null)
            throw new IllegalArgumentException("Encomenda inexistente");

        if (enc.getEstado() != EstadoEncomenda.CRIADA &&
            enc.getEstado() != EstadoEncomenda.PENDENTE &&
            enc.getEstado() != EstadoEncomenda.PARCIAL)
            throw new IllegalStateException("Não é possível alterar esta encomenda");

        // Linha guarda o PRODUTO (não o nome)
        enc.adicionarLinha(produto.getNome(), quantidade);

        int reservado = reservarFIFO(referencia, produto, quantidade);

        if (reservado == 0) {
            enc.setEstado(EstadoEncomenda.PENDENTE);
        } else if (reservado < quantidade) {
            enc.setEstado(EstadoEncomenda.PARCIAL);
        } else {
            enc.setEstado(EstadoEncomenda.RESERVADA);
        }
    }

    // FIFO por Localizacao (ordenada por código). FEFO só dá para fazer se tiveres lotes/validade por stock item. [file:12]
    private int reservarFIFO(String referencia, Produto produto, int quantidade) {
        int falta = quantidade;
        List<Reserva> reservas = reservasPorEncomenda.get(referencia);

        for (Localizacao loc : inventario.getLocalizacoesFIFO()) {
            if (falta == 0) break;

            if (!loc.verificarCompatibilidade(produto)) continue;

            int disponivel = loc.getQuantidadeDisponivel(produto);
            if (disponivel <= 0) continue;

            int aReservar = Math.min(disponivel, falta);

            loc.reservarStock(produto, aReservar);
            reservas.add(new Reserva(loc.getCodigo(), produto, aReservar));

            falta -= aReservar;
        }

        return quantidade - falta;
    }

    // Cancelar encomenda e libertar reservas que foram feitas (sem mexer no stock físico)
    public void cancelarEncomenda(String referencia) {
        Encomenda enc = encomendas.get(referencia);
        if (enc == null)
            throw new IllegalArgumentException("Encomenda inexistente");

        if (enc.getEstado() == EstadoEncomenda.POR_PREPARAR ||
            enc.getEstado() == EstadoEncomenda.EXPEDIDA)
            throw new IllegalStateException("Encomenda já em processamento");

        libertarReservas(referencia);

        enc.setEstado(EstadoEncomenda.CANCELADA);
        encomendas.remove(referencia);
    }

    private void libertarReservas(String referencia) {
        List<Reserva> reservas = reservasPorEncomenda.remove(referencia);
        if (reservas == null) return;

        for (Reserva r : reservas) {
            Localizacao loc = inventario.getLocalizacaoPorCodigo(r.codigoLocalizacao);
            if (loc != null) {
                loc.libertarReserva(r.produto, r.quantidade);
            }
        }
    }


    /* =================================
       UC10 – Consumir reservas (expedição)
       ================================= */
    public void consumirReservas(String referencia) {
        Encomenda enc = encomendas.get(referencia);
        if (enc == null)
            throw new IllegalArgumentException("Encomenda inexistente");

        if (enc.getEstado() != EstadoEncomenda.RESERVADA &&
            enc.getEstado() != EstadoEncomenda.PARCIAL)
            throw new IllegalStateException("Encomenda não está pronta");

        List<Reserva> reservas = reservasPorEncomenda.remove(referencia);
        if (reservas == null) return;

        for (Reserva r : reservas) {
            Localizacao loc = inventario.getLocalizacaoPorCodigo(r.codigoLocalizacao);
            if (loc != null) {
                loc.consumirReserva(r.produto, r.quantidade);
            }
        }

        enc.setEstado(EstadoEncomenda.POR_PREPARAR);
    }


    public boolean temReservas(Produto p) {
        return reservasPorEncomenda.values().stream()
                .flatMap(List::stream)
                .anyMatch(r -> r.produto.equals(p));
    }





    /* =======================
       Consultas auxiliares
       ======================= */
    public Encomenda getEncomenda(String referencia) {
        return encomendas.get(referencia);
    }

    public Collection<Encomenda> listarEncomendas() {
        return Collections.unmodifiableCollection(encomendas.values());
    }

    public String resumoEncomenda(String referencia) {
        Encomenda enc = encomendas.get(referencia);
        if (enc == null) return "Encomenda não encontrada";

        int reservado = reservasPorEncomenda
                .getOrDefault(referencia, List.of())
                .stream().mapToInt(r -> r.quantidade).sum();

        return String.format(
                "Ref: %s | Estado: %s | Total reservado: %d",
                enc.getReferencia(),
                enc.getEstado(),
                reservado
        );
    }

    public List<?> getReservasDaEncomenda(String referencia) {
        return reservasPorEncomenda.getOrDefault(referencia, List.of());
    }
}
