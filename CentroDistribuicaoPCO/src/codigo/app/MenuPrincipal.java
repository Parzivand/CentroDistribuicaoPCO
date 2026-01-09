package codigo.app;

import codigo.handlers.UtilizadorHandler;
import codigo.handlers.ProdutoHandler;

import java.lang.classfile.instruction.SwitchCase;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import codigo.domain.Utilizador;
import codigo.domain.enums.Cargo;
import codigo.domain.enums.TipoRestricoes;
import codigo.handlers.FornecedorHandler;
import codigo.handlers.LojaHandler;

public class MenuPrincipal {
    // Atributos
    private ProdutoHandler produtoHandler;
    private UtilizadorHandler utilizadores;
    private LojaHandler lojaHandler;
    private FornecedorHandler fornecedorHandler;
    
    // Construtor
    public MenuPrincipal(ProdutoHandler produtoHandler, UtilizadorHandler utilizadorHandler,LojaHandler LojaHandler,
        FornecedorHandler fornecedorHandler ){
        this.utilizadores= utilizadorHandler;
        this.lojaHandler= lojaHandler;
        this.produtoHandler= produtoHandler;
        this.fornecedorHandler= fornecedorHandler;
    }
    // onde  comeca o menu  principal 
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
                            System.out.print(" tem validade? ");
                            String resposta1= scanner.next();
                            switch (resposta1) {
                                case "nao":
                                    
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
                                case "sim":
                                    System.out.print("\nnome: ");
                                    String nome1= scanner.nextLine();
        
                                    System.out.print("\ncategoria: ");
                                    String categoria1= scanner.nextLine();
                                    
                                    System.out.print("\nunidade de medida: ");
                                    String unidademedida1= scanner.nextLine();
                                    
                                    System.out.print("\n ano: ");
                                    int ano=scanner.nextInt();
                                    
                                    System.out.print("\n mes: ");
                                    int mes=scanner.nextInt();

                                    System.out.print("\n dia: ");
                                    int dia= scanner.nextInt();
                                    
                                    System.out.print("\n quantas restricoes tem o produto: ");
                                    int numerorestricoes1= scanner.nextInt();
                                    ArrayList<TipoRestricoes> restricoes1 = new ArrayList<>(); 
                                    for(int i=0;i < numerorestricoes1;i++){
                                        String restricao = scanner.nextLine();
                                        restricoes1.add(TipoRestricoes.valueOf(restricao.trim().toUpperCase()));
                                    }
                                    produtoHandler.criarProduto(nome1,categoria1,unidademedida1,restricoes1,new Date(ano, mes, 
                                       dia));
                                    break;
                                default:
                                    break;
                            }//UC05
                            
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
                            String edicao= scanner.nextLine();
                            switch (edicao.toLowerCase().trim()) {
                                case "nome":
                                    System.out.print("\n nome novo: ");
                                    String nomeproduto= scanner.nextLine();
                                    produtoHandler.procurarPorSku(skuproduto).setNome(nomeproduto);
                                    break;
                                case"restricoes": 
                                    System.out.print("adicionar remover: ");
                                    String resposta= scanner.next(); 
                                    switch (resposta) {
                                        case "adicionar":
                                            while(true){
                                                System.out.print("\nadiciona a restricao: ");
                                                String restricoesproduto= scanner.next();
                                                produtoHandler.procurarPorSku(skuproduto).adicionarRestricao(TipoRestricoes.valueOf(restricoesproduto)); 
                                                
                                                System.out.print("\ndeseja terminar: ");
                                                String terminar= scanner.next();
                                                if((terminar.toLowerCase().startsWith("s")||terminar.toLowerCase().startsWith("y"))
                                                    &&(terminar.equalsIgnoreCase("sim"))||terminar.equalsIgnoreCase("yes")
                                                ){
                                                    break;

                                                }
                                            }
                                    break;
                                        
                                        case"remover":
                                            while(true){
                                                System.out.print("\nremova  a restricao: ");
                                                String restricoesproduto= scanner.next();
                                                produtoHandler.procurarPorSku(skuproduto).removerRestricao(TipoRestricoes.valueOf(restricoesproduto)); 
                                                
                                                System.out.print("\ndeseja terminar: ");
                                                String terminar= scanner.next();
                                                if((terminar.toLowerCase().startsWith("s")||terminar.toLowerCase().startsWith("y"))
                                                    &&(terminar.equalsIgnoreCase("sim"))||terminar.equalsIgnoreCase("yes")
                                                ){
                                                    break;

                                                }
                                            }    
                                            break;
                                        default:
                                            break;
                                        }

                                    case"unidade medida":
                                        System.out.print("unidade medida nova: ");    
                                        String unidademedidanova=scanner.nextLine();
                                        produtoHandler.procurarPorSku(skuproduto).setUnidadeMedida(unidademedidanova);

                                    case"validade":
                                        int ano = scanner.nextInt();
                                        int mes = scanner.nextInt();
                                        int dia = scanner.nextInt();
                                        produtoHandler.procurarPorSku(skuproduto).setValidade(new Date(ano, mes, dia));
                                    default:
                                        break;
                                    }
                                    break;
                        case "remover produto":// UC18
                            System.out.print("\n sku do produto: ");
                            String skup= scanner.next();
                            produtoHandler.removerProduto(skup);
                            break; 

                        case"criar fornecedor":

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
}
