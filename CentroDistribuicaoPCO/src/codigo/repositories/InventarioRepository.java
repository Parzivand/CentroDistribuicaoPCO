package codigo.repositories;

import codigo.domain.Localizacao;
import codigo.domain.enums.TipoLocalizacao;
import codigo.domain.enums.TipoRestricoes;
import java.util.*;

public class InventarioRepository {
    private final Map<String, Localizacao> locais = new HashMap<>();

    public InventarioRepository() {
        criarLocaisIniciais();
        System.out.println("✅ InventarioRepository pronto");
    }

    private void criarLocaisIniciais() {
        ArrayList<TipoRestricoes> restricoesArmazem = new ArrayList<>();
        restricoesArmazem.add(TipoRestricoes.EXIGE_VALIDADE);  // ✅ Para Arroz NC

        // ARM0001 - Armazém Central (ESTANTE = geral)
        locais.put("ARM0001", new Localizacao(
            "ARM0001", 
            TipoLocalizacao.ESTANTE,  // ✅ EXISTE
            10000, 
            restricoesArmazem
        ));

        // SEL0001 - Seleção (SOLO = chão/picking)
        locais.put("SEL0001", new Localizacao(
            "SEL0001", 
            TipoLocalizacao.SOLO,     // ✅ EXISTE
            2000, 
            new ArrayList<>()
        ));

        // REC0001 - Receção (DOCA = zona carga)
        locais.put("REC0001", new Localizacao(
            "REC0001", 
            TipoLocalizacao.DOCA,     // ✅ EXISTE
            500, 
            new ArrayList<>()
        ));

        System.out.println("📍 ARM0001(ESTANTE), SEL0001(SOLO), REC0001(DOCA)");
    }


    public Localizacao findByCodigo(String codigo) {
        return locais.get(codigo);
    }

    public List<Localizacao> findAll() {
        return new ArrayList<>(locais.values());
    }

    public void save(Localizacao localizacao) {
        locais.put(localizacao.getCodigo(), localizacao);
    }
}
