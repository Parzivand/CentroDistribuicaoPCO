package codigo.handlers;

import java.util.HashMap;

import codigo.domain.Utilizador;
public class AutenticacaoHandler{
// Atributos
    UtilizadorHandler utilizadores;
    
    public void Autenticar(String nome, String password){
      if(nome==null|| password==null){
         throw new IllegalArgumentException("informacoes vazias");
      }
      for(Utilizador utilizador: utilizadores.listarUtilizadores()){
         if(utilizador.getNome().equals(nome)&& utilizador.getPassword().equals(password)){
            // usar o menu  como utilizador
            break;  
         }  
         
      }
    }

    public void registar_utilizador(Utilizador utilizador){
     // utilizadores.dadosUtilizador(utilizador.getNome(),utilizador.getEmail(), utilizador.getPassword(),utilizador.getPermissoes(),
      //utilizador.getcargo());
    }
}