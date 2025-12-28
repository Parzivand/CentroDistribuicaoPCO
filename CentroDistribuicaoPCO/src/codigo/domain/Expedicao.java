package codigo.domain;

import java.util.ArrayList;
import java.util.List;

public class Expedicao {

    private String id;
    private String estado; // por expedir, expedida...
    private String localizacao;
    private final List<Encomenda> encomendas = new ArrayList<>();

    public Expedicao(String id, String localizacao) {
        this.id = id;
        this.localizacao = localizacao;
        this.estado = "POR_EXPEDIR";
    }

    public void associarEncomenda(Encomenda encomenda) {
        encomendas.add(encomenda);
    }

    public void moverPara(String novaLocalizacao) {
        this.localizacao = novaLocalizacao;
    }

    // getters/setters
}
