package codigo.handlers;

import codigo.domain.Loja;
import codigo.repositories.LojaRepository;
import java.util.HashMap;
import java.util.Random;

public class LojaHandler {

    private final LojaRepository repo;

    public LojaHandler(LojaRepository repo) {
        if (repo == null) throw new IllegalArgumentException("repo não pode ser null");
        this.repo = repo;
    }

    public void adicionarLoja(String nome, String area_atuacao, String morada) {
        Random random = new Random();

        if (area_atuacao == null || !area_atuacao.matches("[a-zA-Z]{3}")) {
            throw new IllegalArgumentException("Área deve ter exatamente 3 letras A-Z");
        }

        String area = area_atuacao.trim().toUpperCase();
        String codigoGerado = area + String.format("%06d", random.nextInt(1_000_000));

        while (repo.existsByCodigo(codigoGerado)) {
            codigoGerado = area + String.format("%06d", random.nextInt(1_000_000));
        }

        repo.save(new Loja(morada, nome, codigoGerado));
    }

    public void RemoverLoja(String codigo) {
        if (!repo.deleteByCodigo(codigo)) {
            throw new IllegalArgumentException("essa loja nao esta registada no sistema!");
        }
    }

    public Loja verporcodigo(String codigo) {
        return repo.findByCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("essa loja nao existe"));
    }

    public HashMap<String, Loja> verLojas_registadas() {
        HashMap<String, Loja> out = new HashMap<>();
        for (Loja l : repo.findAll()) {
            out.put(l.getCodigo().trim().toLowerCase(), l);
        }
        return out;
    }
}
