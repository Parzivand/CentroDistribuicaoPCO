package codigo.handlers;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;
import java.util.Random;

import codigo.domain.Encomenda;
import codigo.domain.Loja;
import codigo.domain.Produto;

public  class Encomendahandler {
        private  HashMap<String,Encomenda> encomendas = new HashMap<>();// podes alterar se te der jeito claro 

        public void CriarEncomenda(Loja loja, int prioridade){
           // cria uma encomenda solicitando a loja e a prioridade da  encomenda e  é gerado  uma  referencia  onde é criado  um valor random que ira 
           //ser  colocado no fim de cada referencia  de intervalo 0000 a 9999 
           // a encomenda gerada é combinada com a data do registo da encomenda com ENC(encomenda) e  o valor random 
           // por seguranca extra: verifica se os elementos estao corretos  
           if(loja== null|| (prioridade<1|| prioridade>5)){
                throw new IllegalAccessError("agum parametro em falta  ou a prioridade nao foi estabelecida entre 1 e 5");
            }
            
            Random random = new Random();
            StringBuilder referencia= new StringBuilder();
            LocalDate localDate = LocalDate.now();
            referencia.append("ENC-").append(localDate.toString().replace("-", "")).append("-"+random.nextInt(0000,9999));
            String referenciagerada = ""+referencia; 
            encomendas.put(referenciagerada,new Encomenda(referenciagerada, loja, prioridade));
        }
        // adiciona  linha a encomenda 
        public void  adicionarLinhaEncomenda(String referencia, Produto produto,int quantidade){
            if(!encomendas.containsKey(referencia)){// se referencia nao tiver na lista das encomendas da erro 
                throw new IllegalArgumentException("referencia invalida");
            }
            encomendas.get(referencia).adicionarLinha(produto, quantidade);
        }
        // resumo da encomenda registada 
        public String resumoencomenda(String referencia){
            return encomendas.get(referencia).toString();
        }
        // da para ver  todas as encomendas 
        public HashMap<String,Encomenda> verencomendas(){
            return new HashMap<>(encomendas);
        }
        
}
