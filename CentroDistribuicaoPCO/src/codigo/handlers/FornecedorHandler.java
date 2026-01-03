package codigo.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import codigo.domain.Fornecedor;
import codigo.domain.Produto;

public class FornecedorHandler{
    Map<String,Fornecedor> fornecedores = new TreeMap<>();
    // UC03
    public void adicionarfornecedor(String nome,
        String email,String telefone){
            if(!(nome==null||email==null||telefone==null)){
                fornecedores.putIfAbsent(nome.toUpperCase(),new Fornecedor(nome, email, telefone));
                // se ja existir o nome como chave ele nao adiciona ao map
            }else{
                throw new IllegalArgumentException("falta elementos");            
            }
        }
    public void removerfornecedor(String nome){
        if(fornecedores.containsKey(nome.toUpperCase())){
            fornecedores.remove(nome.toUpperCase());
            // falta verificar se ele tem produtos associados 
        }
    }

  public ArrayList<Produto>  getProdutos(int valor) { 
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