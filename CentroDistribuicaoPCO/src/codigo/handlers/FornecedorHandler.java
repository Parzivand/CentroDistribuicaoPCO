package codigo.handlers;

import java.util.HashMap;
import java.util.List;

import codigo.domain.Fornecedor;

public class FornecedorHandler{
    HashMap<String,Fornecedor> fornecedores;

    public void adicionarfornecedor(String nome,
        String email,String telefone){
            if(!fornecedores.containsKey(nome)&& !(nome==null||email==null||telefone==null)){
                fornecedores.put(nome.toUpperCase(),new Fornecedor(nome, email, telefone));
            }
        }
    public void removerfornecedor(String nome){
        if(fornecedores.containsKey(nome.toUpperCase())){
            fornecedores.remove(nome.toUpperCase());
        }
    }

    public HashMap<String, Fornecedor> getFornecedores(int valor) {
        return new HashMap<>(fornecedores);
    }
    
}