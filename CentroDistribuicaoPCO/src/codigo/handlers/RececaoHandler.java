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

            }else{
                rececoes.getLast().adicionarLinha(produto, lote, quantidade);
                rececoes.getLast().getLinhas().getLast().setnaoconformidades(tipo, Descricao);
            }
        }
        // o produto de uma linha da  rececao  que está a ser registada pode nao ter nao conformidades    
        public void adicionar_linhas_naoconformidades(Produto produto,int quantidade,String lote){
            if(produto==null || quantidade<0 || lote==null){
                throw new IllegalArgumentException("falta de informacao no registo da rececao");

            }else{rececoes.getLast().adicionarLinha(produto, lote, quantidade);}
        
        }
        public String resumo_do_registo(){
            return String.format("rececao %s\n"+
            "numero total de itens: %s\n"+
            "itens com nao conformidades: %s\n"
            ,rececoes.getLast().getIdRececao(),rececoes.getLast().getLinhas().size(),rececoes.getLast().listarProdutosquarentena());
        }
        // atualiza o stock ou seja 
        public  ArrayList<StockItem> atualizarstockrececoes(){
            ArrayList<StockItem> itens_no_Stock = new ArrayList<>(); 
            for(LinhaRececao linhas : rececoes.getLast().getLinhas()){
                if(linhas.getEstado()=="NC"){
               itens_no_Stock.add(new StockItem(linhas.getProduto(),linhas.getQuantidadeRecebida(),linhas.getLote(), null));
               itens_no_Stock.getLast().setEstado("Quarentena");                
                }else{
                    itens_no_Stock.add(new StockItem(linhas.getProduto(),linhas.getQuantidadeRecebida(),linhas.getLote(), null));
               itens_no_Stock.getLast().setEstado("Disponivel");
                }

            }
            return  new ArrayList<>(itens_no_Stock);
           }}
    