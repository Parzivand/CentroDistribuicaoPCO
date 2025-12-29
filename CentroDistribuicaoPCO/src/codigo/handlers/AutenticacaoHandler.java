package codigo.handlers;

import java.util.HashMap;

import codigo.domain.Utilizador;

public class AutenticacaoHandler{
// Atributos
    private HashMap<String,Utilizador> utilizadores;

    public  String Autenticar(String email,String password){
       // processo de autenticacao 
       if(utilizadores.containsKey(email)&& utilizadores.get(email).getPassword().equals(password)){
        return"autenticacao com sucesso";

       }else if(utilizadores.containsKey(email)&& utilizadores.get(email).getPassword()!= password){
        return "credenciais invalidas";
       }
       else{
        return"falha na autenticacao"; 
       }   
    }   
       // registar utilizador
    public void registarUtilizador(Utilizador utilizador){
    if(!utilizadores.values().contains(utilizador)&& utilizador != null){
        utilizadores.put(utilizador.getEmail(),utilizador);
        System.out.println("utilziador registado com sucesso");
   }
 }
 

}