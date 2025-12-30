package codigo.handlers;
import codigo.domain.Loja;
import java.util.HashMap;
import java.util.List;

public class LojaHandler {
    private HashMap<Integer,Loja> lojas; // lista de lojas 
    
    // adicionar  e remover lojas  parte do UC22 pq o editar acho que se pode fazer no main 

    
    public void adicionarLoja(String nome,int codigo,String morada){
        if(lojas.containsKey(codigo)){
            throw  new IllegalArgumentException("essa loja ja esta registada no sistema!");  
        }
        lojas.put(codigo,new Loja(morada, nome, codigo));
    }
    public void RemoverLoja(int codigo){
        if(!lojas.containsKey(codigo)){
            throw  new IllegalArgumentException("essa loja nao esta registada no sistema!");  
        }
        lojas.remove(codigo);
    }
    public HashMap verLojas_registadas(){
        return new HashMap<>(lojas);
    }

}
