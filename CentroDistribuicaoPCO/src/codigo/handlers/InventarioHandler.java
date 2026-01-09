package codigo.handlers;

import codigo.domain.Localizacao;
import codigo.domain.Movimentacao;
import codigo.domain.Produto;
import codigo.domain.enums.TipoLocalizacao;
import codigo.domain.enums.TipoRestricoes;
import codigo.domain.enums.estadoStock;

import java.time.LocalDateTime;
import java.util.*;

public class InventarioHandler {

    private final ArrayList<Localizacao> localizacoes = new ArrayList<>();
    private final List<Movimentacao> historico = new ArrayList<>();

    /* =========================
       UC11 / UC15 – CONSULTAS
       ========================= */

    /**
     * Consulta stock por localização com paginação (10 itens por página).
     */
    public List<Map.Entry<Produto, Integer>> consultarPorLocalizacao(String codigoLoc, int pagina) {
        Localizacao l = encontrarPorCodigo(codigoLoc);
        if (l == null) throw new IllegalArgumentException("Localização não encontrada");

        List<Map.Entry<Produto, Integer>> stockList = new ArrayList<>();
        stockList.addAll(l.getStock().entrySet());
        stockList.addAll(l.getQuarentena().entrySet());

        int inicio = pagina * 10;
        if (inicio >= stockList.size()) return new ArrayList<>();

        int fim = Math.min(inicio + 10, stockList.size());
        return new ArrayList<>(stockList.subList(inicio, fim));
    }

    /**
     * UC11 – Exportação CSV do stock da localização
     */
    public String exportarCSV(String codigoLoc) {
        Localizacao l = encontrarPorCodigo(codigoLoc);
        if (l == null) throw new IllegalArgumentException("Localização não encontrada");

        List<Map.Entry<Produto, Integer>> itens = new ArrayList<>();
        itens.addAll(l.getStock().entrySet());
        itens.addAll(l.getQuarentena().entrySet());

        if (itens.isEmpty()) throw new IllegalArgumentException("Sem stock");

        StringBuilder csv = new StringBuilder(
                "SKU,Nome,Quantidade,Estado,Validade,Categoria,Reservado\n"
        );

        for (Map.Entry<Produto, Integer> item : itens) {
            Produto p = item.getKey();
            boolean emQuarentena = l.getQuarentena().containsKey(p);
            String estado = emQuarentena ? "QUARENTENA" : "DISPONIVEL";
            String validade = p.getValidade() != null ? p.getValidade().toString() : "N/A";
            int reservado = l.getReservado(p);

            csv.append(String.format(
                    "\"%s\",\"%s\",%d,\"%s\",\"%s\",\"%s\",%d\n",
                    p.getSKU(),
                    p.getNome(),
                    item.getValue(),
                    estado,
                    validade,
                    p.getCategoria(),
                    reservado
            ));
        }
        return csv.toString();
    }

    /**
     * UC15 – Consulta stock global por produto
     */
    public Map<String, Integer> consultarStockPorProduto(Produto p) {
        int total = 0;
        int reservado = 0;
        int disponivel = 0;

        for (Localizacao l : localizacoes) {
            total += l.getQuantidade(p) + l.getQuantidadequarentena(p);
            reservado += l.getReservado(p);
            disponivel += l.getQuantidadeDisponivel(p);
        }

        Map<String, Integer> res = new HashMap<>();
        res.put("TOTAL", total);
        res.put("DISPONIVEL", disponivel);
        res.put("RESERVADO", reservado);
        return res;
    }

    /* =========================
       MOVIMENTAÇÃO DE STOCK
       ========================= */

    /**
     * Move stock entre localizações sem violar reservas.
     */
    public void moverProduto(String locOrigem, String locDestino, Produto produto,
                             int qtd, estadoStock estado, String utilizador) {

        Localizacao origem = encontrarPorCodigo(locOrigem);
        Localizacao destino = encontrarPorCodigo(locDestino);

        if (origem == null || destino == null)
            throw new IllegalArgumentException("Origem ou destino não encontrada");

        if (qtd <= 0)
            throw new IllegalArgumentException("Quantidade inválida");

        if (!destino.verificarCompatibilidade(produto))
            throw new IllegalArgumentException("Destino não suporta restrições");

        if (estado == estadoStock.DISPONIVEL) {
            int disponivel = origem.getQuantidadeDisponivel(produto);
            if (disponivel < qtd)
                throw new IllegalArgumentException("Stock disponível insuficiente (reservado)");

            origem.remover(produto, qtd);
            destino.adicionar(produto, qtd);
        } else {
            origem.removerQuarentena(produto, qtd);
            destino.adicionarquarentena(produto, qtd);
        }

        historico.add(new Movimentacao(
                LocalDateTime.now(),
                produto.getSKU(),
                locOrigem,
                locDestino,
                qtd,
                utilizador
        ));
    }

    /* =========================
       CRUD LOCALIZAÇÕES
       ========================= */

    public void adicionarLocalizacao(Localizacao loc) {
        localizacoes.add(loc);
    }

    public void criarLocalizacao(TipoLocalizacao tipo, int capacidadeMaxima,
                                 List<TipoRestricoes> restricoes) {
        String codigo = gerarCodigoLocalizacao(tipo);
        localizacoes.add(
                new Localizacao(codigo, tipo, capacidadeMaxima, new ArrayList<>(restricoes))
        );
    }

    public void editarLocalizacao(String codigo, String alteracao, Object novoValor) {
        Localizacao l = encontrarPorCodigo(codigo);
        if (l == null) throw new IllegalArgumentException("Localização não encontrada");

        switch (alteracao.toLowerCase()) {
            case "tipo" -> {
                l.setTipo((TipoLocalizacao) novoValor);
                l.setCodigo(gerarCodigoLocalizacao(l.getTipo()));
            }
            case "capacidade maxima" -> l.setCapacidadeMaxima((Integer) novoValor);
            case "adicionar restricoes" -> l.addRestricoesSuportadas((TipoRestricoes) novoValor);
            case "remover restricoes" -> l.removeRestricoesSuportadas((TipoRestricoes) novoValor);
        }
    }

    public void removerLocalizacao(String codigo) {
        Localizacao l = encontrarPorCodigo(codigo);
        if (l != null && l.getStock().isEmpty() && l.getQuarentena().isEmpty()) {
            localizacoes.remove(l);
        } else {
            throw new IllegalArgumentException("Localização não vazia");
        }
    }

    /* =========================
       UTILITÁRIOS
       ========================= */

    private Localizacao encontrarPorCodigo(String codigo) {
        return localizacoes.stream()
                .filter(l -> l.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    private String gerarCodigoLocalizacao(TipoLocalizacao tipo) {
        long contador = localizacoes.stream()
                .filter(l -> l.getTipo() == tipo)
                .count();
        return tipo + String.format("%04d", contador);
    }

    public Localizacao getLocalizacaoPorCodigo(String codigo) {
        return encontrarPorCodigo(codigo);
    }

    public List<Localizacao> getLocalizacoesFIFO() {
        ArrayList<Localizacao> copia = new ArrayList<>(localizacoes);
        copia.sort(Comparator.comparing(Localizacao::getCodigo));
        return copia;
    }

    public int totalReservadoGlobal(Produto p) {
        return localizacoes.stream()
                .mapToInt(l -> l.getReservado(p))
                .sum();
    }

    public List<Movimentacao> getHistoricoUltimos(int n) {
        int inicio = Math.max(0, historico.size() - n);
        return new ArrayList<>(historico.subList(inicio, historico.size()));
    }

    public ArrayList<Localizacao> getLocalizacoes() {
        return new ArrayList<>(localizacoes);
    }
}
