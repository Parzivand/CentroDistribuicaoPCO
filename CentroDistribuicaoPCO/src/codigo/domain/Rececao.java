package codigo.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Rececao {

    private Fornecedor fornecedor;   
    private LocalDate data;
    private final List<LinhaRececao> linhas = new ArrayList<>();// lista de  linhas da rececao 
    private String idRececao; // para rastreabilidade
    

    public Rececao(Fornecedor fornecedor) {
        if (fornecedor == null) {
            throw new IllegalArgumentException("Fornecedor não pode ser null");
        }
        this.fornecedor = fornecedor;
        this.data = LocalDate.now();
        this.idRececao = "REC-" + data.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

        // por enquanto  
    public void adicionarLinha(Produto produto, String lote, int quantidade) {
        if (produto == null || quantidade <= 0) {
            throw new IllegalArgumentException("Produto e quantidade válidos requeridos");
        }
        // verifica se a linha da rececao  tem  uma linha com um produto fora do prazo
        if(produto.getValidade()== null && 
        produto.getRestricoes().contains("Requer validade".trim().toLowerCase())){
            linhas.getLast().setEstado("NC");
        }
        //  verifica se o produto da linha ta em excesso e se tiver  ele assinala a linha como "a armazenar"

       if(linhas.getLast().getnaoconformidades().contains("excesso".toLowerCase().trim())){
        linhas.getLast().setEstado("A armazenar"); 
        new StockItem(produto, quantidade, lote, null);
       }
       
       
    }
    // mostra a lista de produtos fora da validade depois de registar a encomenda como descrito no UC07
    public ArrayList<LinhaRececao> listarProdutosquarentena(){
        ArrayList<LinhaRececao> subLista = new ArrayList<>(); 
        for(LinhaRececao linha_Rececao: linhas){
            if(linha_Rececao.getEstado().equals("NC")){
                subLista.add(linha_Rececao);
            }
        }
        return new ArrayList<>(subLista);
    }
    
    
    // Getters
    public Fornecedor getFornecedor() { return fornecedor; }
    public LocalDate getData() { return data; }
    public List<LinhaRececao> getLinhas() { return new ArrayList<>(linhas); }
    public String getIdRececao() { return idRececao; }
    public int getTotalLinhas() { return linhas.size(); }
    public int getQuantidadeTotal() {
        return linhas.stream().mapToInt(LinhaRececao::getQuantidadeRecebida).sum();
    }

    // Setters (se necessário)
    public void setFornecedor(Fornecedor fornecedor) { 
        if (fornecedor == null) throw new IllegalArgumentException("Fornecedor não pode ser null");
        this.fornecedor = fornecedor; 
    }

    @Override
    public String toString() {
        return String.format("Rececao{id=%s, fornecedor=%s, data=%s, linhas=%d, totalQtd=%d}",
                idRececao, fornecedor.getNome(), data, linhas.size(), getQuantidadeTotal());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rececao)) return false;
        Rececao rececao = (Rececao) o;
        return Objects.equals(idRececao, rececao.idRececao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRececao);
    }

}
