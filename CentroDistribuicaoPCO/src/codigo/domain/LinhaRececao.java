package codigo.domain ;

import java.time.LocalDate;


public class LinhaRececao {

    private Produto produto;
    private String lote;
    private LocalDate validade;
    private int quantidadeRecebida;

    public LinhaRececao(Produto produto, String lote,
                        LocalDate validade, int quantidadeRecebida) {
        this.produto = produto;
        this.lote = lote;
        this.validade = validade;
        this.quantidadeRecebida = quantidadeRecebida;
    }

    // getters/setters

    public Produto getProduto() { return produto;}
    public String getLote() { return lote;}
    public LocalDate getValidade() { return validade; }
    public int getQuantidadeRecebida() { return quantidadeRecebida; }



}