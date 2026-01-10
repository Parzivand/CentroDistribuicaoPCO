package codigo.handlers;
import codigo.domain.Loja;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
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
        lojas.put(codigogerado.trim().toLowerCase(),new Loja(morada, nome,codigogerado));

    }
    public void editarLoja(String codigo,String atributo ){
        Scanner scanner = new Scanner(System.in);
        switch (atributo) {
            case "morada":
                String moradanova= scanner.nextLine();
                for (Loja loja:lojas.values()){
                    if(loja.getMorada().equals(moradanova)){
                        throw new IllegalArgumentException(String.format("a morada %s ja esta associada a outra loja ",
                        moradanova));
                    }
                }
                lojas.get(codigo.trim().toLowerCase()).setMorada(moradanova);
                break;
            
            case"nome":
                String novonome= scanner.nextLine();
                lojas.get(codigo.trim().toLowerCase()).setNome(novonome);
                break;

            case "codigo":
                String AreaAtuacao = scanner.nextLine(); 
                adicionarLoja(lojas.get(codigo).getNome(),AreaAtuacao,lojas.get(codigo).getMorada());
                lojas.remove(codigo);
                break;
            
            default:
                break;
        }
        scanner.close();
    }
    // remove as lojas da lista 
    public void RemoverLoja(String codigo){
        if(!lojas.containsKey(codigo)){
            throw  new IllegalArgumentException("essa loja nao esta registada no sistema!");  
        }
        lojas.remove(codigo);
    }
    // mostra a  loja com o respetivo codigo
    public Loja verporcodigo(String codigo){
        if (!lojas.containsKey(codigo.trim().toLowerCase())){
            throw new IllegalArgumentException("essa loja nao existe");
        }
        return lojas.get(codigo.trim().toLowerCase());
    }
    // mostra todas as lojas existentes na lista de lojas 
    public HashMap verLojas_registadas(){
        return new HashMap<>(lojas);
    }
}
