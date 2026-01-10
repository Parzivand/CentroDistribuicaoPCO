package codigo.repositories;

import codigo.domain.Utilizador;
import codigo.domain.enums.Cargo;
import codigo.dto.UtilizadorJsonDTO;
import codigo.resources.JsonService;
import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class UtilizadorRepository {
    private final Map<String, Utilizador> utilizadores = new HashMap<>();
    private final JsonService jsonService = new JsonService();

    private static final Map<String, Cargo> CARGO_MAP = Map.ofEntries(
            Map.entry("ADMINISTRADOR", Cargo.ADMINISTRADOR),
            Map.entry("GESTOR_LOG", Cargo.GESTOR_lOG),      // JSON → enum
            Map.entry("GESTOR_LOGISTICO", Cargo.GESTOR_lOG),// opcional
            Map.entry("OPERDADOR_ARM", Cargo.OPERDADOR_ARM),
            Map.entry("OPERDADOR_SEL", Cargo.OPERDADOR_SEL),
            Map.entry("OPERDADOR_REC", Cargo.OPERDADOR_REC)
    );

    public UtilizadorRepository() {
        loadFromJson();
        System.out.println("✅ Carregados " + utilizadores.size() + " utilizadores");
    }

    public void save(Utilizador u) {
        utilizadores.put(u.getEmail().toLowerCase(), u);
    }

    public Optional<Utilizador> findByEmail(String email) {
        return Optional.ofNullable(utilizadores.get(email.toLowerCase()));
    }

    public Collection<Utilizador> findAll() {
        return new ArrayList<>(utilizadores.values());
    }

    private void loadFromJson() {
        String[] paths = {"../dados.json", "dados.json"};

        for (String path : paths) {
            File file = new File(path);
            System.out.println("🔍 " + path + " → " + file.getAbsolutePath() + " (exists: " + file.exists() + ")");
            if (!file.exists()) continue;

            try {
                // DEBUG preview
                String rawContent = new String(Files.readAllBytes(file.toPath()));
                System.out.println("📄 JSON preview: " +
                        rawContent.substring(0, Math.min(200, rawContent.length())) + "...");

                List<UtilizadorJsonDTO> users =
                        jsonService.readListFieldFromFile(path, "utilizadores", UtilizadorJsonDTO.class);

                System.out.println("📋 Users lidos: " + (users == null ? "null" : users.size()));
                if (users == null || users.isEmpty()) {
                    System.err.println("⚠️ Campo 'utilizadores' vazio em: " + path);
                    return;
                }

                int count = 0;
                for (UtilizadorJsonDTO uJson : users) {
                    if (uJson == null) continue;

                    String nome = uJson.nome != null ? uJson.nome : "Unknown";
                    String email = uJson.email != null ? uJson.email.trim() : "";
                    String password = uJson.password != null ? uJson.password : "";
                    String cargoStr = uJson.cargo != null ? uJson.cargo.trim().toUpperCase() : "ADMINISTRADOR";

                    if (email.isBlank() || password.isBlank()) continue;

                    Cargo cargo = CARGO_MAP.getOrDefault(cargoStr, Cargo.ADMINISTRADOR);

                    Utilizador u = new Utilizador(nome, email, password, cargo);
                    save(u);
                    count++;
                }

                System.out.println("✅ SUCCESS " + count + " users de: " + path);
                return;

            } catch (Exception e) {
                System.err.println("❌ ERRO " + path + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.err.println("❌ Nenhum dados.json válido!");
    }
}
