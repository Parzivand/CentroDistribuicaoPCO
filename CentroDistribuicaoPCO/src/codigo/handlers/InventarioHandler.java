package codigo.handlers;
import java.util.*;

import codigo.domain.Localizacao;
import codigo.domain.Produto;
import codigo.domain.enums.TipoLocalizacao;
import codigo.domain.enums.TipoRestricoes;
import codigo.domain.enums.estadoStock;

public class InventarioHandler{

    // Atributos 
  ArrayList<Localizacao> localizacoes= new ArrayList<>(); 

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
              return stockList.subList(inicio, fim);
          }
      }
      
      //Não encontrou
      throw new IllegalArgumentException("Localização não encontrada");
  }

public void moverProduto (String locOrigem, String locDestino, Produto produto, int qtd, estadoStock estado){
  Localizacao origem = null;
  Localizacao destino = null;

  // Acha loc de origem
  for (Localizacao l : localizacoes){
    if (l.getCodigo().equals(locOrigem)) origem = l;
  }
  if (origem == null) throw new IllegalArgumentException("Origem não encontrada");

  // Acha loc de destino

  for (Localizacao l : localizacoes){
    if (l.getCodigo().equals(locDestino)) destino = l;
  }
  if (destino == null) throw new IllegalArgumentException("Destino não encontrada");

  // Remove origem

  Map<Produto, Integer> mapaOrigem = (estado == estadoStock.QUARENTENA) ? origem.getQuarentena() : origem.getStock();
  int atualOrigem = mapaOrigem.getOrDefault(produto, 0);
  if (atualOrigem < qtd) throw new IllegalArgumentException("Stock insuficiente");
  mapaOrigem.put(produto, atualOrigem - qtd);
  
  // Valida destino
  if (!destino.verificarCompatibilidade(produto)) {
      throw new IllegalArgumentException("Destino não suporta restrições");
  }


  // Adiciona destino
  Map<Produto, Integer> mapaDestino = (estado == estadoStock.QUARENTENA) ? destino.getQuarentena() : destino.getStock();
  int atualDestino = mapaDestino.getOrDefault(produto, 0);
  mapaDestino.put(produto, atualDestino + qtd);
  }

 // Adicionar localização
  public void adicionarLocalizacao(Localizacao loc) {
      localizacoes.add(loc);
  }

  // Total stock por SKU 
  public int totalPorSKU(String sku) {
      int total = 0;
      for (Localizacao l : localizacoes) {          
          for (Map.Entry<Produto, Integer> item : l.getStock().entrySet()) {
              // Mesmo SKU em múltiplas locs
              if (item.getKey().getSKU().equals(sku)) {  
                  total += item.getValue();              
              }
          }
          // Mesma lógica quarentena
          for (Map.Entry<Produto, Integer> item : l.getQuarentena().entrySet()) {
              if (item.getKey().getSKU().equals(sku)) {
                  total += item.getValue();
              }
          }
      }
      return total;  // TOTAL ACUMULADO 
  }
  // cria localizacao
  public void CriarLocalizacao(TipoLocalizacao tipo,int Capacidademaxima,String codigo,ArrayList<TipoRestricoes> restricoes){
    int Contador=0;      
    for(Localizacao localizacao: localizacoes){
      if(localizacao.getTipo().equals(tipo)){
        Contador+=1;
      }
    }
    String codigogerado=tipo.toString()+Contador;
    localizacoes.add(new Localizacao(codigogerado, tipo, Capacidademaxima, restricoes));
  }
  // edita a localizacao
  public void editarlocalizacao(String alteracao,String codigo){
    Scanner scanner= new Scanner(System.in); 
    switch (alteracao.trim().toLowerCase()) {
      case "tipo":// muda o codigo tambem porque  o codigo tambem depende do tipo da localizacao
      for(Localizacao l: localizacoes){
          if(l.getCodigo().equals(codigo)){
           String tipo= scanner.next();
            l.setTipo(TipoLocalizacao.valueOf(tipo.toUpperCase()));
            l.setCodigo(gerarcodigoLocalizacao(TipoLocalizacao.valueOf(tipo.toUpperCase())));
            break;
          }
        }
        
        break;
      case "capacidade maxima":
         for(Localizacao l: localizacoes){
          if(l.getCodigo().equals(codigo)){
           int capacidadeMaximanova= scanner.nextInt();
           l.setCapacidadeMaxima(capacidadeMaximanova); 
           break;
          }
        } 
        break;
        case "adicionar restricoes":
        for(Localizacao l: localizacoes){
          if(l.getCodigo().equals(codigo)){
           String restricao= scanner.nextLine();
           l.addRestricoesSuportadas(TipoRestricoes.valueOf(restricao.toUpperCase().trim())); 
           break;
          }
        }   
        break;
        case "remover restricoes":
        for(Localizacao l: localizacoes){
          if(l.getCodigo().equals(codigo)){
           String restricao= scanner.nextLine();
           l.removeRestricoesSuportadas((TipoRestricoes.valueOf(restricao.toUpperCase().trim()))); 
           break;
          }
        }   
        break;
      default:
        break;
    }
    }
    public Localizacao verLocalizacaoporcodigo(String codigo){
      for(Localizacao l: localizacoes){
          if(l.getCodigo().equals(codigo)){
         return l; 
          }
        }
        return null;
    }
    // remove localizacao
    public void removerLocalizacao(String codigo){
      for(Localizacao l: localizacoes){
          if(l.getCodigo().equals(codigo)&& l.getStock().isEmpty() && l.getQuarentena().isEmpty()){
            localizacoes.remove(l);       
            break;
          }
        }
    }

    public String gerarcodigoLocalizacao(TipoLocalizacao tipo){
      int Contador=0;      
    for(Localizacao localizacao: localizacoes){
      if(localizacao.getTipo().equals(tipo)){
        Contador+=1;
      }
    }
    String codigogerado=tipo.toString()+Contador;
    return codigogerado;
  }
    // Getters
  public ArrayList<Localizacao> getLocalizacoes() {
      return new ArrayList<>(localizacoes);
  }
} 