package codigo.app;

import codigo.handlers.UtilizadorHandler;
import codigo.handlers.ProdutoHandler;

import java.lang.classfile.instruction.SwitchCase;
import java.util.ArrayList;
import java.util.Scanner;

import codigo.domain.Utilizador;
import codigo.domain.enums.Cargo;
import codigo.domain.enums.TipoRestricoes;
import codigo.handlers.FornecedorHandler;
import codigo.handlers.LojaHandler;
public class MenuPrincipal {
    private ProdutoHandler produtoHandler;
    private UtilizadorHandler utilizadores;
    private LojaHandler lojaHandler;
    private FornecedorHandler fornecedorHandler;  
    public MenuPrincipal(ProdutoHandler produtoHandler, UtilizadorHandler utilizadorHandler,LojaHandler LojaHandler,
        FornecedorHandler fornecedorHandler){
        this.utilizadores= utilizadorHandler;
        this.lojaHandler= lojaHandler;
        this.produtoHandler= produtoHandler;
        this.fornecedorHandler= fornecedorHandler;
    }
    public void run(){
        
        Scanner scanner= new Scanner(System.in);
        Utilizador UtilizadorAutenticado= null; 
        boolean operacoes= true;
        Cargo cargo =UtilizadorAutenticado.getcargo();
        
        switch (cargo) {
            case Cargo.ADMINISTRADOR:
                while(operacoes){
                    String operacao= scanner.nextLine();
                    switch (operacao.trim().toLowerCase()) {  
                        case "criar produto":
                            //UC05
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
                        //UC06    
                        // mostra os primeiros 10(caso tenhamos 10 ou  mais que 10 produtos criados) 
        
                        int numerodeprodutos=10;
        
                        while (true) {
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
                        case"registar utilizador":
                           //UC02
                            System.out.print("nome: ");
                            String  nomeutilizador =scanner.nextLine();
                           
                            System.out.print("\nemail: ");
                            String  email =scanner.nextLine();
                            
                            System.out.print("\npassword: ");
                            String  password=scanner.nextLine();
                            
                            System.out.print("\ncargo: ");
                            String  cargoutilizador=scanner.next();
                            utilizadores.dadosUtilizador(nomeutilizador,email,password,Cargo.valueOf(cargoutilizador.toUpperCase()));
                            break;

                        case "editar produto":
                            String skuproduto= scanner.next();
                            produtoHandler.
                            break;
                        case "log out":
                            operacoes=false;  
                            break; // Pode fazer commit denovo <3??? quando puder logicamente
                        
               
            default:
                break;
        }
            }
        }
    }
}
