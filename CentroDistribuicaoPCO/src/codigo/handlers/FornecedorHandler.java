package codigo.handlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import codigo.domain.Fornecedor;

public class FornecedorHandler{
    TreeMap<String,Fornecedor> fornecedores;
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
    
    public TreeMap<String,Fornecedor> verfornecedores(){
        return new TreeMap<>(fornecedores);
    }
}