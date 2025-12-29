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

   
        
    }
    