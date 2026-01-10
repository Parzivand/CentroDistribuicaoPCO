package codigo.app;

import java.lang.classfile.instruction.SwitchCase;
import java.time.LocalDate;
import java.util.*;

import codigo.domain.Localizacao;
import codigo.domain.Utilizador;
import codigo.domain.enums.*;
import codigo.handlers.*;

public class MenuPrincipal {
    // Atributos
    private ProdutoHandler produtoHandler;
    private UtilizadorHandler utilizadores;
    private LojaHandler lojaHandler;
    private FornecedorHandler fornecedorHandler;
    private InventarioHandler inventarioHandler;
    private RececaoHandler rececaoHandler;
    private ExpedicaoHandler expediacaohandler;
    private Encomendahandler encomendahandler;
    // Construtor
    public MenuPrincipal(ProdutoHandler produtoHandler, UtilizadorHandler utilizadorHandler,LojaHandler LojaHandler,
        FornecedorHandler fornecedorHandler,InventarioHandler inventarioHandler,RececaoHandler rececaoHandler,
    ExpedicaoHandler expediacaohandler,Encomendahandler encomendahandler){
        this.utilizadores= utilizadorHandler;
        this.lojaHandler= lojaHandler;
        this.produtoHandler= produtoHandler;
        this.fornecedorHandler= fornecedorHandler;
        this.expediacaohandler= expediacaohandler;
        this.inventarioHandler=inventarioHandler;
        this.rececaoHandler=rececaoHandler;
        this.encomendahandler=encomendahandler;

    }
    // onde  comeca o menu  principal
    public void run(){
        
        Scanner scanner= new Scanner(System.in);
        Utilizador UtilizadorAutenticado= null; 
        boolean operacoes= true;
        Cargo cargo =UtilizadorAutenticado.getcargo();
        
        switch (cargo.toString()) {
            case "ADMINISTRADOR":
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
                            System.out.print("sku do produto: ");
                            String skup= scanner.next();
                            produtoHandler.removerProduto(skup);
                            break; 

                        case"criar fornecedor":
                            System.out.print("nome: ");
                            String nomefornecedor= scanner.nextLine();
                            System.out.print("\nemail: ");
                            String emailfornecedor= scanner.nextLine();
                            System.out.print("\ntelefone: ");
                            String telefonefornecedor= scanner.nextLine();
                            fornecedorHandler.adicionarFornecedor(nomefornecedor,emailfornecedor,telefonefornecedor);    
                        break;
                        case "editar fornecedor":
                            System.out.print("email: ");
                            String emailfornecedore= scanner.next();
                            
                            System.out.print("oq pretende editar: ");
                            String editar= scanner.next();
                            fornecedorHandler.editarFornecedor(emailfornecedore,editar);
                        break;
                        
                        case "remover fornecedor":
                            String emaildofornecedor = scanner.next();
                            fornecedorHandler.removerfornecedor(emaildofornecedor);
                        break;
                                    
                        case"adicionar loja":
                            
                            System.out.print("nome: ");
                            String nomeloja= scanner.nextLine();
                            
                            System.out.print("area atuacao: ");
                            String arealoja= scanner.next();
                            
                            System.out.print("morada: ");
                            String moradaloja= scanner.nextLine();
                            lojaHandler.adicionarLoja(nomeloja,arealoja, moradaloja);
                        case"editar loja":
                            
                            System.out.print("area atuacao: ");
                            String emailloja= scanner.next();
                            
                            System.out.print("morada: ");
                            String mudaratributo= scanner.nextLine();    
                            lojaHandler.editarLoja(emailloja, mudaratributo);
                        case"remover loja":
                            
                            System.out.print("email: ");
                            String codigoloja= scanner.next();    
                            lojaHandler.RemoverLoja(codigoloja);
                        break;

                        case "adicionar localizacao":
                            System.out.print("tipo: ");
                            String tipoloc= scanner.next();
                            System.out.print("tipo: ");
                            int  capacidadeMaximaloc= scanner.nextInt();
                            ArrayList<TipoRestricoes> restricoes= new ArrayList<>();
                            while (true) {
                                String restricao= scanner.next();
                                restricoes.add(TipoRestricoes.valueOf(restricao.toUpperCase()));
                                
                                System.out.print("deseja continuar? ");
                                String resposta2 = scanner.next();
                                if(resposta2.equalsIgnoreCase("nao")){
                                    break;
                                }   
                            }
                            inventarioHandler.criarLocalizacao(TipoLocalizacao.valueOf(tipoloc),capacidadeMaximaloc,restricoes);  
                            break;
                        case"editar localizacao":
                            inventarioHandler.getLocalizacoes();
                            System.out.print("codigo: ");
                            String codigoloc= scanner.next();

                            System.out.print("alteracao: ");
                            String alteracao= scanner.next();

                            System.out.print("capacidademaxima/adicionar restricao:/ remover restricao/ tipo: ");
                            String valor= scanner.next();
                            inventarioHandler.editarLocalizacao(codigoloc, alteracao, valor);
                             break;   

                        case"remover localizacao":
                        inventarioHandler.getLocalizacoes();
                            inventarioHandler.getLocalizacoes();
                            System.out.print("codigo: ");
                            String codigolocalizacao= scanner.next();
                            inventarioHandler.removerLocalizacao(codigolocalizacao);
                            break;
                        
                        case "log out":
                            operacoes=false; 
                            break; 

            case "GESTOR_LOG":
                    InventarioHandler    
                break;

            case "OPERADOR_ARM":
                
            
                break;
            case "OPERADOR_SEL":
                
                break;

            case"OPERADOR_REC":
                System.out.print("");
                String operacao1= scanner.nextLine();
                switch (operacao1) {
                    case "registar rececao":
                        fornecedorHandler.getfornecedores();
                        System.out.print("seleciona um fornecedor: ");
                        String selecao= scanner.next();
                        rececaoHandler.criar_Rececao(fornecedorHandler.getfornecedores().get(selecao));
                        while (true){
                            System.out.print("lote");
                            String lote= scanner.next();

                            System.out.print("quantidade: ");
                            int quantidaderecebida= scanner.nextInt();
                            
                            produtoHandler.getprodutos();
                            System.out.print("codigo do produto: ");
                            String codigoproduto= scanner.next();
                           fornecedorHandler.associarProdutos(produtoHandler.getprodutos().get(codigoproduto),
                           fornecedorHandler.getfornecedores().get(selecao).getEmail()); 

                            inventarioHandler.getLocalizacoes();
                            System.out.print("selecione a localizacao pelo codigo: ");
                            String codigo= scanner.next();
                            rececaoHandler.adicionar_linhas_naoconformidades(produtoHandler.getprodutos().get(codigoproduto)
                            , quantidaderecebida, lote, inventarioHandler.getLocalizacaoPorCodigo(codigo));
                            
                            System.out.print("tem mais linhas?");
                            String resposta= scanner.next();
                            if(resposta.equalsIgnoreCase("nao")){
                                break;
                            }
                        }
                        rececaoHandler.ResumoRegisto();
                        break;
                    case "consultar rececoes":
                        rececaoHandler.getrececoes();
                        System.out.print("filtrar por periodo/fornecedor?");
                        String escolha = scanner.next();
                        switch (escolha) {
                            case "periodo":
                                System.out.print("primeira data: ");
                                System.out.print("\n ano: ");
                                int ano=scanner.nextInt();
                                System.out.print("\n mes: ");
                                int mes=scanner.nextInt();
                               System.out.print("\n dia: ");
                                int dia=scanner.nextInt();
                                System.out.print("segunda data: ");
                                System.out.print("\n ano: ");
                                int ano2=scanner.nextInt();
                                System.out.print("\n mes: ");
                                int mes2=scanner.nextInt();
                               System.out.print("\n dia: ");
                                int dia2=scanner.nextInt();
                                  
                                rececaoHandler.Filtrarperiodo(LocalDate.of(ano,mes,dia)
                                ,LocalDate.of(ano2, mes2, dia2));
                                break;
                            case"fornecedor":
                            fornecedorHandler.getfornecedores();
                            System.out.print("\n selecione  o fornecedor: ");
                            String fornecedor= scanner.next();
                            rececaoHandler.Filtrarfornecedores(fornecedorHandler.getfornecedores().get(fornecedor));
                            default:
                                break;
                        }
                    default:
                        break;
                }
                break;
            
        }
            }
        }
    }
}
