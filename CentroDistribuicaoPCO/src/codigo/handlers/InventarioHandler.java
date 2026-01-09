package codigo.handlers;

import codigo.domain.Localizacao;
import codigo.domain.Movimentacao;
import codigo.domain.Produto;
import codigo.domain.enums.TipoLocalizacao;
import codigo.domain.enums.TipoRestricoes;
import codigo.domain.enums.estadoStock;
import java.util.*;


public class InventarioHandler {
    private ArrayList<Localizacao> localizacoes = new ArrayList<>();
    private List<Movimentacao> historico = new ArrayList<>();

    /**
   * int = 0, produtos 1 - 10
   * int = 1, produtos 11 - 20
   */
  
  public List<Map.Entry<Produto, Integer>> consultarPorLocalizacao(String codigoLoc, int pagina) {
      // Acha localização
      for (Localizacao l : localizacoes) {
          if (l.getCodigo().equals(codigoLoc)) {
              
              // Cria lista com stock normal
              ArrayList<Map.Entry<Produto, Integer>> stockList = new ArrayList<>();
              for (Map.Entry<Produto, Integer> item : l.getStock().entrySet()) {
                  stockList.add(item);
              }
              
              // ADICIONA quarentena
              for (Map.Entry<Produto, Integer> item : l.getQuarentena().entrySet()) {
                  stockList.add(item);
              }
              
              // Paginação simples
              int inicio = pagina * 10;
              if (inicio >= stockList.size()) {
                  return new ArrayList<>();
              }
              int fim = inicio + 10;
              if (fim > stockList.size()) {
                  fim = stockList.size();
              }
              return new ArrayList<>(stockList.subList(inicio, fim));
          }
      }
      
      //Não encontrou
      throw new IllegalArgumentException("Localização não encontrada");
  }
    // UC11: Export CSV ( usa validade como proxy)
    public String exportarCSV(String codigoLoc) {
        Localizacao l = encontrarPorCodigo(codigoLoc);
        if (l == null) throw new IllegalArgumentException("Localização não encontrada");
        
        List<Map.Entry<Produto, Integer>> todosItens = new ArrayList<>();
        todosItens.addAll(l.getStock().entrySet());
        todosItens.addAll(l.getQuarentena().entrySet());
        
        if (todosItens.isEmpty()) throw new IllegalArgumentException("Sem stock");
        
        StringBuilder csv = new StringBuilder("SKU,Nome,Quantidade,Estado,Validade,Categoria\n");
        for (Map.Entry<Produto, Integer> item : todosItens) {
            Produto p = item.getKey();
            String estado = l.getQuarentena().containsKey(p) ? "QUARENTENA" : "DISPONIVEL";
            String validadeStr = p.getValidade() != null ? p.getValidade().toString() : "N/A";
            csv.append(String.format("\"%s\",\"%s\",%d,\"%s\",\"%s\",\"%s\"\n", 
                p.getSKU(), p.getNome(), item.getValue(), estado, validadeStr, p.getCategoria()));
        }
        return csv.toString();
    }

  public void moverProduto(String locOrigem, String locDestino, Produto produto,
                          int qtd, estadoStock estado, String utilizador) {

      Localizacao origem = encontrarPorCodigo(locOrigem);
      Localizacao destino = encontrarPorCodigo(locDestino);

      if (origem == null) throw new IllegalArgumentException("Origem não encontrada");
      if (destino == null) throw new IllegalArgumentException("Destino não encontrada");
      if (qtd <= 0) throw new IllegalArgumentException("Quantidade inválida");

      if (!destino.verificarCompatibilidade(produto)) {
          throw new IllegalArgumentException("Destino não suporta restrições");
      }

      // remove na origem (SEM mexer nos maps)
      if (estado == estadoStock.QUARENTENA) origem.removerQuarentena(produto, qtd);
      else origem.remover(produto, qtd);

      // adiciona no destino (SEM mexer nos maps)
      if (estado == estadoStock.QUARENTENA) destino.adicionarquarentena(produto, qtd);
      else destino.adicionar(produto, qtd);

      historico.add(new Movimentacao(java.time.LocalDateTime.now(),
              produto.getSKU(), locOrigem, locDestino, qtd, utilizador));
  }


  // CRUD Localizações
  public void adicionarLocalizacao(Localizacao loc) { localizacoes.add(loc); }

  public void criarLocalizacao(TipoLocalizacao tipo, int capacidadeMaxima, List<TipoRestricoes> restricoes) {
      String codigo = gerarCodigoLocalizacao(tipo);
      localizacoes.add(new Localizacao(codigo, tipo, capacidadeMaxima, new ArrayList<>(restricoes)));
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

  // Utilitários
  private Localizacao encontrarPorCodigo(String codigo) {
      return localizacoes.stream().filter(l -> l.getCodigo().equals(codigo)).findFirst().orElse(null);
  }

  private String gerarCodigoLocalizacao(TipoLocalizacao tipo) {
      long contador = localizacoes.stream().filter(l -> l.getTipo() == tipo).count();
      return tipo.toString() + String.format("%04d", contador);
  }

  // Total por SKU do teu ProdutoHandler
  public int totalPorSKU(String sku) {
      int total = 0;
      for (Localizacao l : localizacoes) {
          for (Map.Entry<Produto, Integer> item : l.getStock().entrySet()) {
              if (item.getKey().getSKU().equals(sku)) total += item.getValue();
          }
          for (Map.Entry<Produto, Integer> item : l.getQuarentena().entrySet()) {
              if (item.getKey().getSKU().equals(sku)) total += item.getValue();
          }
      }
      return total;
  }

  public Localizacao getLocalizacaoPorCodigo(String codigo) {
      return encontrarPorCodigo(codigo);
  }


  public int totalReservadoGlobal(Produto p) {
  // Soma reservas por todas localizações
  return (int) localizacoes.stream()
      .mapToInt(loc -> loc.getReservado(p))
      .sum();
  }

  public List<Localizacao> getLocalizacoesFIFO() {
    ArrayList<Localizacao> copia = new ArrayList<>(localizacoes);
    copia.sort(Comparator.comparing(Localizacao::getCodigo));
    return copia;
  }



  

  // Getters
  public ArrayList<Localizacao> getLocalizacoes() { return new ArrayList<>(localizacoes); }
  public List<Movimentacao> getHistoricoUltimos(int n) {
      int inicio = Math.max(0, historico.size() - n);
      return new ArrayList<>(historico.subList(inicio, historico.size()));
  }
}
