package codigo.repositories;

import codigo.domain.Loja;
import codigo.dto.LojaJsonDTO;
import codigo.resources.JsonService;
import java.util.*;

public class LojaRepository {

    private final Map<String, Loja> lojas = new HashMap<>();
    private final JsonService jsonService = new JsonService();

    public LojaRepository() {
        loadFromJson();
        System.out.println("✅ Carregadas " + lojas.size() + " lojas");
    }

    public void save(Loja loja) {
        lojas.put(loja.getCodigo().trim().toLowerCase(), loja);
    }

    public Optional<Loja> findByCodigo(String codigo) {
        return Optional.ofNullable(lojas.get(codigo.trim().toLowerCase()));
    }

    public boolean existsByCodigo(String codigo) {
        return lojas.containsKey(codigo.trim().toLowerCase());
    }

    public Collection<Loja> findAll() {
        return new ArrayList<>(lojas.values());
    }

    public boolean deleteByCodigo(String codigo) {
        return lojas.remove(codigo.trim().toLowerCase()) != null;
    }

    private void loadFromJson() {
        String[] paths = {"../dados.json", "dados.json"};

        for (String path : paths) {
            if (!jsonService.fileExists(path)) continue;

            try {
                List<LojaJsonDTO> list = jsonService.readListFieldFromFile(path, "lojas", LojaJsonDTO.class);
                if (list == null || list.isEmpty()) return;

                int count = 0;
                for (int i = 0; i < list.size(); i++) {
                    LojaJsonDTO dto = list.get(i);
                    if (dto == null) continue;

                    String nome = dto.nome != null ? dto.nome.trim() : "Loja";
                    String morada = dto.morada != null ? dto.morada.trim() : "";

                    // código estável: LOJ000001, LOJ000002, ...
                    String codigo = "LOJ" + String.format("%06d", i + 1);

                    save(new Loja(morada, nome, codigo));
                    count++;
                }
                System.out.println("✅ SUCCESS " + count + " lojas de: " + path);
                return;

            } catch (Exception e) {
                System.err.println("❌ ERRO lojas em " + path + ": " + e.getMessage());
            }
        }
    }
}
