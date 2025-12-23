public class Produto{

    // Atributos

    private String nome, SKU, unidadeMedida;
    private String restricoes; // Nao deveria ser uma lista?
    private boolean validade; // Deveria ser uma data ou algo do tipo?

    // Construtor

    public Produto(String nome, String SKU, String unidadeMedida, String restricoes, boolean validade) {
        this.nome = nome;
        this.SKU = SKU;
        this.unidadeMedida = unidadeMedida;
        this.restricoes = restricoes;
        this.validade = validade;
    }

    // Gets e Setters

    public String getSKU() { return SKU;} 
    public void setSKU(String SKU) { this.SKU = SKU; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getUnidadeMedida() { return unidadeMedida; }
    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; }

    public String getRestricoes() { return restricoes; }
    public void setRestricoes(String restricoes) { this.restricoes = restricoes; }  

    public boolean isValidade() { return validade; }
    public void setValidade(boolean validade) { this.validade = validade; }


}