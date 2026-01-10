package codigo.repositories;

import codigo.domain.Rececao;
import codigo.dto.RececaoJsonDTO;
import codigo.resources.JsonService;
import codigo.domain.Fornecedor;

import java.time.LocalDate;
import java.util.*;

public class RececaoRepository {

    private final List<Rececao> rececoes = new ArrayList<>();
    private final JsonService jsonService = new JsonService();

    public RececaoRepository() {
        // Receções normalmente são criadas em runtime, não carregadas do JSON
        // por isso este repo é só para persistir novas receções
        System.out.println("✅ RececaoRepository pronto");
    }

    public void save(Rececao r) {
        rececoes.add(r);
    }

    public List<Rececao> findAll() {
        return new ArrayList<>(rececoes);
    }

    public Optional<Rececao> findById(String id) {
        return rececoes.stream().filter(r -> r.getIdRececao().equals(id)).findFirst();
    }

    public List<Rececao> filtrarPorFornecedor(Fornecedor fornecedor) {
        return rececoes.stream()
                .filter(r -> r.getFornecedor().equals(fornecedor))
                .toList();
    }

    public List<Rececao> filtrarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return rececoes.stream()
                .filter(r -> r.getData().isAfter(inicio) && r.getData().isBefore(fim))
                .toList();
    }
}
