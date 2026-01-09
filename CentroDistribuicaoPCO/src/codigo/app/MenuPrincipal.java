package codigo.app;

import codigo.handlers.UtilizadorHandler;
import codigo.handlers.ProdutoHandler;

import java.util.ArrayList;
import java.util.Scanner;

import codigo.domain.Utilizador;
import codigo.domain.enums.TipoRestricoes;
import codigo.handlers.LojaHandler;
public class MenuPrincipal {
    private ProdutoHandler produtoHandler;
    private UtilizadorHandler utilizadores;
    private LojaHandler lojaHandler;
    public MenuPrincipal(ProdutoHandler produtoHandler, UtilizadorHandler utilizadorHandler,LojaHandler LojaHandler){
        this.utilizadores= utilizadorHandler;
        this.lojaHandler= lojaHandler;
        this.produtoHandler= produtoHandler;
    }
    public void run(){
        Scanner scanner= new Scanner(System.in);
        Utilizador UtilizadorAutenticado= null; 
        boolean operacoes= true;
        while(operacoes){
            String operacao= scanner.nextLine();
            switch (operacao.trim().toLowerCase()) { 
                case "criar produto"&&UtilizadorAutenticado.getcargo().equals(UtilizadorAutenticado):
                    System.out.print("\nnome: ");
                    String nome= scanner.nextLine();
                    System.out.print("\ncategoria: ");
                    String categoria= scanner.nextLine();
                    System.out.print("\nunidade de medida: ");
                    String unidademedida= scanner.nextLine();
                    System.out.print("\n quantas restricoes tem o produto: ");
                    int numerorestricoes= scanner.nextInt();
                    ArrayList<TipoRestricoes> restricoes = new ArrayList<>(); 
                    for(int i=0;i < numerorestricoes;i++){
                        String restricao = scanner.nextLine();
                        restricoes.add(TipoRestricoes.valueOf(restricao.trim().toUpperCase()));
                    }
                    produtoHandler.criarProduto(nome,categoria,unidademedida,restricoes);
                    break;
                case "ver produtos":
                    int numerodeprodutos=10;
                    while (true) {
                        // mostra os primeiros 10(caso tenhamos 10 ou  mais que 10 produtos criados) 
                        produtoHandler.getProdutos(numerodeprodutos);
                        System.out.print("deseja continuar? ");
                        String resposta= scanner.next();
                        // se nao for nao ou no  ele  vai continuar ate mostrar mais e mais produtos ate acabar os produtos 
                        if(resposta.toLowerCase().startsWith("n")&&(resposta.equals("nao")||resposta.equals("no"))){
                            break;
                        }
                        numerodeprodutos+=10;
                        
                    }
                    break;
                case "ver por sku":
                   
                    System.out.print("digite o sku do produto esta a procurar: ");
                    String skudoproduto= scanner.next();
                    produtoHandler.procurarPorSku(skudoproduto);

                    break;
                case "log out":
                    operacoes=false;  
                    break;
                default:
                  
                    break;
                    
            }
        }
    }
}
