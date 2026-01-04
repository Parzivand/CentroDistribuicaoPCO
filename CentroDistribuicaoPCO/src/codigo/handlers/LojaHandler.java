package codigo.handlers;
import codigo.domain.Loja;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.random.*;
public class LojaHandler {
    private HashMap<String,Loja> lojas; // lista de lojas 
    
    // adicionar  e remover lojas  parte do UC22 pq o editar acho que se pode fazer no main 

    // verifica se a  loja ja existe como o codigo  e  gera um valor aleatorio entre 000000 e 999999 para que 
    // seja muito dificil de haver uma loja com um codigo igual
    
    public void adicionarLoja(String nome,String area_atuacao,String morada){
        Random random = new Random();
        if(area_atuacao==null||area_atuacao.matches("[a-z]{3}")){
            throw new IllegalArgumentException("a area ta errada tem de ter apenas  3  letras");
        }
        String codigogerado= area_atuacao+String.format("%06d",random.nextInt(000000,
            999999));
            if(lojas.containsKey(codigogerado)){
                throw new IllegalArgumentException("essa  loja ja existe!");
            }
        lojas.put(area_atuacao.trim().toLowerCase(),new Loja(morada, nome,codigogerado));
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
