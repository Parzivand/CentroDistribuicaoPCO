package codigo.handlers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codigo.domain.Fornecedor;
import codigo.domain.LinhaRececao;
import codigo.domain.Localizacao;
import codigo.domain.Rececao;
import codigo.domain.Produto;
import codigo.domain.StockItem;
import codigo.domain.enums.Estadoencomenda;
import codigo.domain.enums.TipoRestricoes;
import codigo.domain.enums.estadoStock;
public class  RececaoHandler{
    private ArrayList<Rececao> rececoes;

    
    public void criar_Rececao(Fornecedor fornecedor){
        if(fornecedor== null){
            throw new IllegalArgumentException("erro de  colocar!");
        }
        rececoes.add(new  Rececao(fornecedor)); 
        }

    // procura a  rececao  por  id 
    public String achar_porid(String id){ 
        for(Rececao rececao: rececoes){
            if(rececao.getIdRececao()==id){
                return rececao.toString();
                
            }
         }
          return null;
    }
    //  mostra as rececoes registadas  
    public ArrayList<Rececao> getrececoes(){
        if (rececoes.isEmpty()) {
            System.out.println("nao tem rececoes nenhumas!");
        }
        return new ArrayList<>(rececoes.reversed());
    }

    // filtra a lista de rececoes por fornecedor 
    public ArrayList Filtrarfornecedores(Fornecedor fornecedor){
        if(fornecedor.equals(null)){
            throw new  IllegalArgumentException("errou ao colocar um fornecedor!");
        }
        ArrayList<Rececao> sublista=  new ArrayList<>();
        for(Rececao rececao: rececoes.reversed()){
            
            if(rececao.getFornecedor().equals(fornecedor)){
                sublista.add(rececao);
            }
        }
        return new ArrayList<>(sublista);
    }
     
    public ArrayList Filtrarperiodo(LocalDate localDate, LocalDate localDate2){
        ArrayList<Rececao> sublista=  new ArrayList<>();
        
        if(localDate==null || localDate2 ==null|| localDate.isAfter(localDate2)){
            throw new  IllegalArgumentException("faltou alguma data está em falta ou as datas estao trocadas");
        }

        for(Rececao rececao: rececoes.reversed()){
            
            if(rececao.getData().isAfter(localDate) && rececao.getData().isBefore(localDate2)){
                sublista.add(rececao);
            }
            
        }

        return new ArrayList<>(sublista);
    }


    
    // para registar  as rececoes ou seja colocar as linhas  e nao conformidades 
    // como  em cada linha vais ter um produto que pode ou nao ter  nao conformidades 
    // essa funcao  ta feita para no menu fazermos um while loop que enquanto a resposta for Sim ou Yes 
    // confinua se for Nao  ou No ela para o loop faz sentido ??? (UC07) 
    // ah se fores ver as nas rececoes e nao tiver validade é pq podes aceder a validade do produto pela 
    // validade do produto 
       public void adicionar_linhas_naoconformidades(Produto produto,int quantidade,String lote,String tipo
        , String Descricao){
            if(produto==null || quantidade<0 || lote==null){
                throw new IllegalArgumentException("falta de informacao no registo da rececao");
            
            }else if(produto.getValidade()==null && produto.getRestricoes().contains(TipoRestricoes.EXIGE_VALIDADE)){
                    rececoes.getLast().getLinhas().getLast().setEstado("NC");
                    rececoes.getLast().adicionarLinha(produto, lote, quantidade);
            }else{
                    rececoes.getLast().adicionarLinha(produto, lote, quantidade);
                    rececoes.getLast().getLinhas().getLast().setEstado("NORMAL");
            }
            rececoes.getLast().getLinhas().getLast().setnaoconformidades(tipo, Descricao);
        }

        
        // o produto de uma linha da  rececao  que está a ser registada pode nao ter nao conformidades    
        public void adicionar_linhas_naoconformidades(Produto produto,int quantidade,String lote,Localizacao localizacao){
            int espacodisponivel= localizacao.getCapacidadeMaxima()-localizacao.getQuantidade(produto);
            if(produto==null || quantidade<0 || lote==null){
                throw new IllegalArgumentException("falta de informacao no registo da rececao");
            // se a validade tiver  mal  é guardado  em  quarentena 
            }else if(produto.getValidade()==null && produto.getRestricoes().contains(TipoRestricoes.EXIGE_VALIDADE)){
                rececoes.getLast().adicionarLinha(produto, lote, quantidade);
                rececoes.getLast().getLinhas().getLast().setEstado("NC");
                localizacao.adicionarquarentena(produto, quantidade);
                // se o produto  tiver  a validade em dia ou nao tenha  validade mas a quantidade a armazenar seja maior que a disponivel na 
                //localizacao  é colocada em armazenar 
            }else if(quantidade>espacodisponivel){
                rececoes.getLast().adicionarLinha(produto, lote, quantidade);
                localizacao.adicionar(produto, espacodisponivel);
                rececoes.getLast().getLinhas().getLast().setEstado("A ARMAZENAR");
                // nao tenha  problemas com a validade nem  com a quantidade é adicionado no  stock a linha fica com estado  de disponivel 
            }else{
                rececoes.getLast().adicionarLinha(produto, lote, quantidade);
                localizacao.adicionar(produto, quantidade);
                rececoes.getLast().getLinhas().getLast().setEstado("DISPONIVEL");
            }

            }
        
        
        // nao muda 
        public String ResumoRegisto(){
            return String.format("rececao %s\n"+
            "numero total de itens: %s\n"+
            "itens com nao conformidades: %s\n"
            ,rececoes.getLast().getIdRececao(),rececoes.getLast().getLinhas().size(),rececoes.getLast().listarProdutosquarentena());
        }
        // mudar para ser no stock da localizacao 

    
    }

    