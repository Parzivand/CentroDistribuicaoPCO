package java.domain;

import java.util.ArrayList;

public class Rececao {

    // Atributos

    private ArrayList<LinhaRececao> linhasRececoes;
    private Fornecedor fornecedor;
    // Talvez adicionas quantidade

    // Construtor
    public Rececao( Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
        this.linhasRececoes= new ArrayList<>(); 
    }//
    
    // Gets e Setters
    public void adicionarlinhasrececao(LinhaRececao linhaRececao){
        if(!linhasRececoes.contains(linhaRececao)){
            linhasRececoes.add(linhaRececao);
        }
    }
    public ArrayList<LinhaRececao> getlinhasrececao(){return linhasRececoes;}

    public void removerlinhasrececao(LinhaRececao linhaRececao){
        if(linhasRececoes.contains(linhaRececao)){
            linhasRececoes.remove(linhaRececao);
        }
    }
    public Fornecedor getFornecedor() { return fornecedor; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; 
    }
}