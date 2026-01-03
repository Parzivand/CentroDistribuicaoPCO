package codigo.handlers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codigo.domain.Fornecedor;
import codigo.domain.Localizacao;
import codigo.domain.Rececao;
import codigo.domain.Produto;

public class  RececaoHandler{
    private ArrayList<Rececao> rececoes;
    
    
    public void criar_Rececao(Fornecedor fornecedor){
        if(fornecedor== null){
            throw new IllegalArgumentException("erro de  colocar!");
        }
        rececoes.add(new  Rececao(fornecedor)); 
        }

    // procura a  rececao  por  id 
    public String achar_porid(String id){ 
        for(Rececao rececao: rececoes){
            if(rececao.getIdRececao()==id){
                return rececao.toString();
                
            }
         }
          return null;
    }
    //  mostra as rececoes registadas  
    public ArrayList<Rececao> getrececoes(){
        if (rececoes.isEmpty()) {
            System.out.println("nao tem rececoes nenhumas!");
        }
        return new ArrayList<>(rececoes.reversed());
    }

    // filtra a lista de rececoes por fornecedor 
    public ArrayList Filtrarfornecedores(Fornecedor fornecedor){
        if(fornecedor.equals(null)){
            throw new  IllegalArgumentException("errou ao colocar um fornecedor!");
        }
        ArrayList<Rececao> sublista=  new ArrayList<>();
        for(Rececao rececao: rececoes.reversed()){
            
            if(rececao.getFornecedor().equals(fornecedor)){
                sublista.add(rececao);
            }
        }
        return new ArrayList<>(sublista);
    }
    
    // para registar  as rececoes ou seja colocar as linhas  e nao conformidades 
    // como  em cada linha vais ter um produto que pode ou nao ter  nao conformidades 
    // essa funcao  ta feita para no menu fazermos um while loop que enquanto a resposta for Sim ou Yes 
    // confinua se for Nao  ou No ela para o loop faz sentido ??? (UC07)
       public void resgistro_rececoes(Produto produto,int quantidade,String lote,String tipo
        , String Descricao){
            if(produto==null || quantidade<0 || lote==null){
                throw new IllegalArgumentException("falta de informacao no registo da rececao");

            }else{
                rececoes.getLast().adicionarLinha(produto, lote, quantidade);
                rececoes.getLast().getLinhas().getLast().setnaoconformidades(tipo, Descricao);
            }
        }    
    }
    