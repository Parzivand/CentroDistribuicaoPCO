package codigo.repositories;

import codigo.domain.Fornecedor;
import codigo.dto.FornecedorJsonDTO;
import codigo.resources.JsonService;
import java.util.*;

public class FornecedorRepository {

    private final Map<String, Fornecedor> fornecedores = new TreeMap<>(); // key: EMAIL (UPPER)
    private final JsonService jsonService = new JsonService();

    public FornecedorRepository() {
        loadFromJson();
        System.out.println("✅ Carregados " + fornecedores.size() + " fornecedores");
    }

    public void save(Fornecedor f) {
        fornecedores.put(f.getEmail().trim().toUpperCase(), f);
    }

    public Optional<Fornecedor> findByEmail(String email) {
        return Optional.ofNullable(fornecedores.get(email.trim().toUpperCase()));
    }

    public boolean existsByEmail(String email) {
        return fornecedores.containsKey(email.trim().toUpperCase());
    }

    public Collection<Fornecedor> findAll() {
        return new ArrayList<>(fornecedores.values());
    }

    public boolean deleteByEmail(String email) {
        return fornecedores.remove(email.trim().toUpperCase()) != null;
    }

    private void loadFromJson() {
        String[] paths = {"../dados.json", "dados.json"};

        for (String path : paths) {
            if (!jsonService.fileExists(path)) continue;

            try {
                List<FornecedorJsonDTO> list =
                        jsonService.readListFieldFromFile(path, "fornecedores", FornecedorJsonDTO.class);

                if (list == null || list.isEmpty()) return;

                int count = 0;
                for (FornecedorJsonDTO dto : list) {
                    if (dto == null) continue;

                    String nome = dto.nome != null ? dto.nome.trim() : "";
                    String email = dto.email != null ? dto.email.trim().toUpperCase() : "";
                    String telefone = dto.telefone != null ? dto.telefone.trim() : "";

                    if (nome.isBlank() || email.isBlank()) continue;

                    save(new Fornecedor(nome, email, telefone));
                    count++;
                }

                System.out.println("✅ SUCCESS " + count + " fornecedores de: " + path);
                return;

            } catch (Exception e) {
                System.err.println("❌ ERRO fornecedores em " + path + ": " + e.getMessage());
            }
        }
    }
}
