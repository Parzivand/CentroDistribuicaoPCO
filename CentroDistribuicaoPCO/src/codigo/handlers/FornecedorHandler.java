package codigo.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import codigo.domain.Fornecedor;
import codigo.domain.Produto;

public class FornecedorHandler{
    Map<String,Fornecedor> fornecedores = new TreeMap<>();
    Map<Fornecedor,ArrayList<Produto>> produtosdosfornecedores; //   fornecedores  com a sua lista de produtos 
    // UC03
    public void adicionarfornecedor(String nome,
        String email,String telefone){
            if(!(nome==null||email==null||telefone==null)){
                fornecedores.putIfAbsent(nome.trim().toUpperCase(),new Fornecedor(nome.trim(), email, telefone));
                // se ja existir o nome como chave ele nao adiciona ao map
            }else{
                throw new IllegalArgumentException("falta elementos");            
            }
        }
    // verifica se o fornecedor tem produtos UC20
    public void removerfornecedor(String nome){
        Set<Fornecedor> chaves = produtosdosfornecedores.keySet();// set com todos as chaves do mapa  produtosdosfornecedores para ser usado no loop    
        if(nome==null){
        }
        if(!fornecedores.containsKey(nome)){
        throw new IllegalArgumentException(String.format("o fornecedor com o nome %s nao esta registado",nome));
        }
        for (Fornecedor fornecedor: chaves){
            
            if(fornecedor.getNome().equals(nome.trim())&& produtosdosfornecedores.get(fornecedor).isEmpty()
            && fornecedores.containsKey(nome.trim())){// verifica se o fornecedor tem as  
                
                fornecedores.remove(nome.trim());
                produtosdosfornecedores.remove(fornecedor);
                break;
            }
        }
        
    }
    

    // mostra o fornecedor que tem aquele nome 
    public Fornecedor verpornome(String nome){
        if(!fornecedores.containsKey(nome.trim().toUpperCase())){
            throw new IllegalArgumentException("Esse fornecedor nao existe");
        }
        return fornecedores.get(nome.trim().toUpperCase());
    }

  public ArrayList<Fornecedor>  getfornecedores(int valor) { 
   // criei uma lista com todos os valores do Map para que se possa  mostrar todos os valores de 10 em 10 caso
   // seja solicitado que sera  configurado  no  menu 
   
   ArrayList<Fornecedor> mostrar_fornecedores = new ArrayList<>();
   mostrar_fornecedores.addAll(fornecedores.values());
   if(valor>mostrar_fornecedores.size()-1){
    IO.println(mostrar_fornecedores);
    throw new IndexOutOfBoundsException("demasiado  grande o valor\n");
   }
        return new ArrayList(mostrar_fornecedores.subList(0, valor)); 
    }

}