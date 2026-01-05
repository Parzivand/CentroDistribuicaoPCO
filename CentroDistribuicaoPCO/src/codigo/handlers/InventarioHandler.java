package codigo.handlers;
import java.util.ArrayList;

import codigo.domain.Localizacao;
import codigo.domain.Rececao;
import codigo.domain.StockItem;
import codigo.handlers.RececaoHandler;

public class InventarioHandler{
    // Atributos 
  ArrayList<StockItem> stock_dos_itens= new ArrayList<>();
  ArrayList<Localizacao> localizacoes= new ArrayList<>(); 
// adicona pelo menu os produtos a uma lista do stock de Itens atraves do registo das rececoes  
  
public void adicionar_aoStock(ArrayList<StockItem> item){stock_dos_itens.addAll(item);}
// adicionar  o associar as localizacoes e encomendas 

}