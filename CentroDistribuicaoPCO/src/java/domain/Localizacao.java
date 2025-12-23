package java.domain;

public  class Localizacao {

    // Atributos

    private String tipo, suporta_resticoes;
    private int capacidade_max; // Determinamos um tamanho e depois torna-lo final
    // Algo que mostre a quantidade atual?

    // Construtor

    public Localizacao(String tipo, String suporta_resticoes, int capacidade_max) {
        this.tipo = tipo;
        this.suporta_resticoes = suporta_resticoes;
        this.capacidade_max = capacidade_max;
    }

    // Gets e Setters

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getSuporta_resticoes() { return suporta_resticoes; }
    public void setSuporta_resticoes(String suporta_resticoes) { this.suporta_resticoes = suporta_resticoes; }

    public int getCapacidade_max() { return capacidade_max; }
    public void setCapacidade_max(int capacidade_max) { this.capacidade_max = capacidade_max; }


}