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
    public void adicionarFornecedor(String nome,
        String email,String telefone){
            if(!(nome==null||email==null||telefone==null)){
                fornecedores.putIfAbsent(email.trim().toUpperCase(),new Fornecedor(nome.trim(), email, telefone));
                produtosdosfornecedores.put(new Fornecedor(nome, email.trim(), telefone),null);// ao mesmo tempo que adiciona na lista de fornecedores 
                // adiciona no mapa dos produtosdosfornecedores 
                // se ja existir o nome como chave ele nao adiciona ao map
                  
            }else{
                throw new IllegalArgumentException("falta elementos");            
            }
        }
    
    public void associarProdutos(Produto produto,String email){
        if(produto==null|| email==null||!fornecedores.containsKey(email)){
            throw new IllegalArgumentException("argumentos invalidos");
        }
        Set<Fornecedor> listafornecedores= produtosdosfornecedores.keySet();
        for(Fornecedor fornecedor:listafornecedores){
            if(fornecedor.getEmail().equals(email)&& produtosdosfornecedores.get(fornecedor).contains(produto)){
                throw new IllegalArgumentException("o fornecedor ja tem  esse produto na lista");
            }
            if(fornecedor.getEmail().equals(email) && !produtosdosfornecedores.get(fornecedor).contains(produto)){
                produtosdosfornecedores.get(fornecedor).add(produto);
            }
        }    
    }

    public void desassociarproduto(Produto produto,String email ){
     if(produto==null|| email==null||!fornecedores.containsKey(email)){
            throw new IllegalArgumentException("argumentos invalidos");
        }
        Set<Fornecedor> listafornecedores= produtosdosfornecedores.keySet();

        for(Fornecedor fornecedor:listafornecedores){
            if(fornecedor.getEmail().equals(email)&& produtosdosfornecedores.get(fornecedor).contains(produto)){
                produtosdosfornecedores.get(fornecedor).remove(produto);
            }
            if(fornecedor.getEmail().equals(email)&& !produtosdosfornecedores.get(fornecedor).contains(produto)){
                throw new IllegalArgumentException("o fornecedor nao  tem  produto na  sua  lista");
            }
        }    
    }       





    // veifica se o fornecedor tem produtos UC20
    public void removerfornecedor(String email){
        Set<Fornecedor> listafornecedores= produtosdosfornecedores.keySet();// set com todos as chaves do mapa  
        // produtosdosfornecedores para ser usado no loop    
        if(!fornecedores.containsKey(email)){
        throw new IllegalArgumentException(String.format("o fornecedor com o nome %s nao esta registado",email));
        }
        for (Fornecedor fornecedor: listafornecedores){
            
            if(fornecedor.getEmail().equals(email.trim())&& produtosdosfornecedores.get(fornecedor).isEmpty()){
                // verifica se o fornecedor tem produtos na lista   
                
                fornecedores.remove(email.trim());
                produtosdosfornecedores.remove(fornecedor);
                break;
            }else{
                IO.print("nao da para tirar esse fornecedor");
            }
        }
        // 
    } 
    public void editarFornecedor(String email,String atributo){
        if(!fornecedores.containsKey(email)|| (atributo.trim().toLowerCase().equals("nome")
    ||atributo.trim().toLowerCase().equals("telefone")
    ||atributo.trim().toLowerCase().equals("email"))){
        throw new IllegalArgumentException("ou o nome nao foi preenchido ou o");
        }
        IO.println(fornecedores.get(email).toString());
            Scanner scanner= new Scanner(System.in); 
            switch (atributo.trim().toLowerCase()) {
                case "nome":
                    
                    String emailnovo= scanner.nextLine(); 
                    fornecedores.get(email).setEmail(emailnovo);
                    
                    break;
                case "email":
                    String novoemail= scanner.nextLine();
                    Set<Fornecedor> fornecedors= produtosdosfornecedores.keySet();
                    List<Produto> produtosantigos= new ArrayList<>();

                    // caso  o utilizador queira colocar o nome de outro fornecedor como novo nome 
                    for(Fornecedor fornecedor: fornecedors){
                        if(fornecedor.getNome().equals(novoemail)){
                            throw new IllegalArgumentException("esse fornecedor ja existe");
                        }
                        
                    }
                    adicionarFornecedor(fornecedores.get(email).getNome(),novoemail,fornecedores.get(email).getTelefone());
                    for(Fornecedor fornecedor: fornecedors){
                        if(fornecedor.getEmail().equals(email)){
                            produtosantigos.addAll(produtosdosfornecedores.get(fornecedor));
                            produtosdosfornecedores.get(fornecedor).clear();
                            removerfornecedor(email);
                            break;
                        }
                        
                    }
                    for(Fornecedor fornecedor: fornecedors){
                        if(fornecedor.getEmail().equals(email)){
                            produtosdosfornecedores.get(fornecedor).addAll(produtosantigos);
                            break;
                        }
                    }

                    break;
                case "telefone":
                    String telefonenovo= scanner.nextLine(); 
                    fornecedores.get(email).setTelefone(telefonenovo);
                    break;
                default:
                    break;
            }
            scanner.close();
        }
    


    // mostra o fornecedor que tem aquele nome 
    public String verpornome(String email){
        if(!fornecedores.containsKey(email.trim().toUpperCase())){
            throw new IllegalArgumentException("Esse fornecedor nao existe");
        }
        return fornecedores.get(email.trim().toUpperCase()).toString();
    }
  public HashMap<String,Fornecedor> getfornecedores(){
    return new HashMap<>(fornecedores);
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