import java.domain.*;
import java.util.*;
public class ExpedicaoHandler{
    private ArrayList<Expedicao> expedicoes;
    
    public void associarEncomenda(Encomenda encomenda,Expedicao expedicao){
      if(encomenda!=null && !expedicao.getencomendas().containsKey(encomenda.getreferencia())){
        expedicao.getencomendas().put(encomenda.getreferencia(), encomenda);
      } else if (!expedicoes.contains(expedicao)|| encomenda==null) {
        IO.println("a expedicao  nao  existe"); 

      }else{
        IO.print("erro");
      }
    }
}