package codigo.handlers;

import codigo.domain.AjusteStock;
import codigo.domain.Localizacao;
import codigo.domain.Produto;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;


public class AjusteStockHandler {
    HashMap<String,AjusteStock> ajustesStockpropostos= new HashMap();
    private RececaoHandler rececaoHandler;    
    private ExpedicaoHandler expedicaoHandler;
    
    
    public AjusteStockHandler(RececaoHandler rececaoHandler, ExpedicaoHandler expedicaoHandler){
        this.expedicaoHandler= expedicaoHandler;
        this.rececaoHandler=rececaoHandler;
    }
    
    public void CriarAjusteStock(String skuProduto,Localizacao localizacao, int quantidadeajustada){ 
        if(skuProduto==null || localizacao==null ||quantidadeajustada<=0){
            throw new IllegalArgumentException("Informacoes inválidas, falta de  informação");
        }   
        String IDgerado = localizacao.getTipo()+skuProduto+"-"+quantidadeajustada;  
        if(ajustesStockpropostos.containsKey(IDgerado)){
                throw new IllegalArgumentException("");
            }
        ajustesStockpropostos.put(IDgerado,new AjusteStock(skuProduto, IDgerado, localizacao, quantidadeajustada));
        }
        // aprova o ajuste de stock 
        public Void aprovarAjuste(String idajuste){
            // se nao tiver na lista de ajustes nem vale a pena tentar 
            if(!ajustesStockpropostos.containsKey(idajuste)){
                throw new IllegalArgumentException("informacao invalida!!");
            }
            // variaveis para deixar mais bonito  e pequeno na coisa de verificacoes 
            Scanner scanner= new Scanner(System.in);
            String codigoproduto= ajustesStockpropostos.get(idajuste).getProduto_codigo();
            int quantidade= ajustesStockpropostos.get(idajuste).getQuantidade();
            Set<Produto> produtosStock=ajustesStockpropostos.get(idajuste).getLocalizacao().getStock().keySet();
            
            for(Produto produto:produtosStock){
                if(produto.getSKU().equals(codigoproduto)&& ajustesStockpropostos.get(idajuste).getLocalizacao().getStock().get(produto)-
                quantidade<0){ 
                   String confirmacao= scanner.nextLine();
                    if(confirmacao.toLowerCase().startsWith("s")||confirmacao.toLowerCase().startsWith("y")){
                        ajustesStockpropostos.get(idajuste).setAprovacao(true); 
                        ajustesStockpropostos.get(idajuste).getLocalizacao().getStock().put(produto,0);
                       break;
                    }
                    if(produto.getSKU().equals(codigoproduto)&&ajustesStockpropostos.get(idajuste).getLocalizacao().getStock().get(produto)-
                quantidade>0){
                        ajustesStockpropostos.get(idajuste).setAprovacao(true);
                        ajustesStockpropostos.get(idajuste).getLocalizacao().getStock().put(produto,ajustesStockpropostos.get(idajuste).getLocalizacao().getStock().get(produto)-
                quantidade);
                    //   
                   }else{
                    break;     
                
                }}}
         return null;   
        }
        
        public HashMap<String,AjusteStock> VerAjustes(){
            return new HashMap(ajustesStockpropostos);
        }
    }

