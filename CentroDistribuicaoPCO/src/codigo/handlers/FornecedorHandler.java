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
                produtosdosfornecedores.put(new Fornecedor(nome.trim(), email, telefone),null);
                // se ja existir o nome como chave ele nao adiciona ao map
            }else{
                throw new IllegalArgumentException("falta elementos");            
            }
        }
    // veifica se o fornecedor tem produtos UC20

    public void associar_produtos(Produto produto,String fornecedorid){
        if(produto==null|| fornecedorid==null||!fornecedores.containsKey(fornecedorid)){
            throw new IllegalArgumentException("argumentos invalidos");
        }
        Set<Fornecedor> listafornecedores= produtosdosfornecedores.keySet();
        for(Fornecedor fornecedor:listafornecedores){
            if(fornecedor.getNome().equals(fornecedorid)&& produtosdosfornecedores.get(fornecedor).contains(produto)){
                throw new IllegalArgumentException("o fornecedor ja tem  esse produto na lista");
            }
            if(fornecedor.getNome().equals(fornecedorid)&& !produtosdosfornecedores.get(fornecedor).contains(produto)){
                produtosdosfornecedores.get(fornecedor).add(produto);
            }
            
        }    
    }
    public void desassociarproduto(Produto produto,String fornecedorid){
     if(produto==null|| fornecedorid==null||!fornecedores.containsKey(fornecedorid)){
            throw new IllegalArgumentException("argumentos invalidos");
        }
        Set<Fornecedor> listafornecedores= produtosdosfornecedores.keySet();

        for(Fornecedor fornecedor:listafornecedores){
            if(fornecedor.getNome().equals(fornecedorid)&& produtosdosfornecedores.get(fornecedor).contains(produto)){
                produtosdosfornecedores.get(fornecedor).remove(produto);
            }
            if(fornecedor.getNome().equals(fornecedorid)&& !produtosdosfornecedores.get(fornecedor).contains(produto)){
                throw new IllegalArgumentException("o fornecedor nao  tem  produto na  sua  lista");
            }
            
        }    
    }   
    


    public void removerfornecedor(String nome){
        Set<Fornecedor> listafornecedores= produtosdosfornecedores.keySet();// set com todos as chaves do mapa  
        // produtosdosfornecedores para ser usado no loop    
        if(nome==null){
        }
        if(!fornecedores.containsKey(nome)){
        throw new IllegalArgumentException(String.format("o fornecedor com o nome %s nao esta registado",nome));
        }
        
        for (Fornecedor fornecedor: listafornecedores){
            
            if(fornecedor.getNome().equals(nome.trim())&& produtosdosfornecedores.get(fornecedor).isEmpty()){
                // verifica se o fornecedor tem produtos na lista   
                
                fornecedores.remove(nome.trim());
                produtosdosfornecedores.remove(fornecedor);
                break;
            }else{
                IO.print("nao da para tirar esse fornecedor");
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