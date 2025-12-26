package java.domain;

import java.time.LocalDate;

class StockItem {

    Produto produto;
    String lote;
    Localizacao localizacao;
    int quantidade;
    LocalDate validade;
    // StockEstado estado; // Disponivel, Quarentena, Reservado

}