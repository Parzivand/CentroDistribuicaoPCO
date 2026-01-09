package codigo.handlers;

import codigo.domain.Encomenda;
import codigo.domain.Localizacao;
import codigo.domain.Loja;
import codigo.domain.Produto;
import codigo.domain.enums.Estadoencomenda;
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
        Random random = new Random();

        do {
            referencia = String.format("ENC-%s-%04d", data, random.nextInt(10000));
        } while (encomendas.containsKey(referencia));

        Encomenda e = new Encomenda(referencia, loja, prioridade);
        encomendas.put(referencia, e);
        reservasPorEncomenda.put(referencia, new ArrayList<>());

        return referencia;
    }

    // UC09: adiciona linha + tenta reservar FIFO (sem consumir stock)
    public void adicionarLinhaEncomenda(String referencia, Produto produto, int quantidade) {
        if (referencia == null || produto == null) throw new IllegalArgumentException("Dados em falta");
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade tem de ser > 0");

        Encomenda enc = encomendas.get(referencia);
        if (enc == null) throw new IllegalArgumentException("Referência inválida");

        // Registo da linha na encomenda (como já tinhas)
        enc.adicionarLinha(produto.getNome(), quantidade);

        int reservado = reservarFIFO(referencia, produto, quantidade);

        // Estado conforme reserva
        if (reservado == 0) {
            enc.setEstado(Estadoencomenda.PEDENTE); // ou PENDENTE
        } else if (reservado < quantidade) {
            enc.setEstado(Estadoencomenda.PEDENTE); // ou cria Estadoencomenda.PARCIAL se quiseres
        } else {
            enc.setEstado(Estadoencomenda.RESERVADA);
        }
    }

    // FIFO por Localizacao (ordenada por código). FEFO só dá para fazer se tiveres lotes/validade por stock item. [file:12]
    private int reservarFIFO(String referencia, Produto produto, int quantidade) {
        int falta = quantidade;
        List<Reserva> reservas = reservasPorEncomenda.get(referencia);

        for (Localizacao loc : inventario.getLocalizacoesFIFO()) {
            if (falta == 0) break;

            // Ignora localizações incompatíveis
            if (!loc.verificarCompatibilidade(produto)) continue;

            int disponivel = loc.getQuantidadeDisponivel(produto); // disponível real = stock - reservado
            if (disponivel <= 0) continue;

            int aReservar = Math.min(disponivel, falta);

            // Reserva sem consumir
            loc.reservarStock(produto, aReservar);

            // Guarda a alocação por localização (para cancelar depois)
            reservas.add(new Reserva(loc.getCodigo(), produto, aReservar));

            falta -= aReservar;
        }

        return quantidade - falta;
    }

    // Cancelar encomenda e libertar reservas que foram feitas (sem mexer no stock físico)
    public void cancelarEncomenda(String referencia) {
        Encomenda enc = encomendas.remove(referencia);
        List<Reserva> reservas = reservasPorEncomenda.remove(referencia);

        if (enc == null) throw new IllegalArgumentException("Encomenda não encontrada");
        if (reservas == null) return;

        for (Reserva r : reservas) {
            Localizacao loc = inventario.getLocalizacaoPorCodigo(r.codigoLocalizacao);
            if (loc != null) {
                loc.libertarReserva(r.produto, r.quantidade);
            }
        }

    }

    public Encomenda getEncomenda(String referencia) {
        return encomendas.get(referencia);
    }

    public Map<String, Encomenda> verEncomendas() {
        return new HashMap<>(encomendas);
    }

    

    // Resumo simples (não depende de saber como é LinhaEncomenda internamente)
    public String resumoEncomenda(String referencia) {
        Encomenda enc = encomendas.get(referencia);
        if (enc == null) return "Encomenda não encontrada";

        int reservadoTotal = reservasPorEncomenda.getOrDefault(referencia, List.of())
                .stream().mapToInt(r -> r.quantidade).sum();

        return String.format("Ref: %s | Estado: %s | Reservado (total): %d",
                referencia, enc.getEstado(), reservadoTotal);
    }

    public int totalReservadoPorProduto(Produto p) {
        int total = 0;
        
        // Percorre TODAS as encomendas
        for (List<Reserva> reservas : reservasPorEncomenda.values()) {
            
            // Percorre TODAS as reservas dessa encomenda
            for (Reserva r : reservas) {
                
                // Se é o mesmo produto, soma
                if (r.produto.equals(p)) {
                    total += r.quantidade;
                }
            }
        }
        
        return total;
    }
}
