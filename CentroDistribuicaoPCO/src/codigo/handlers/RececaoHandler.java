package codigo.handlers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codigo.domain.Fornecedor;
import codigo.domain.Localizacao;
import codigo.domain.Rececao;

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
    //  mostra as rececoes de 
    public ArrayList<Rececao> getrececoes(){
        if (rececoes.isEmpty()) {
            System.out.println("nao tem rececoes nenhumas!");
        }
        return new ArrayList<>(rececoes);
    }      
    }
    