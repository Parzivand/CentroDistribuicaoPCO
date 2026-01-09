package codigo.app.bootstrap;

import codigo.domain.enums.Cargo;
import codigo.dto.UtilizadorDTO;
import codigo.handlers.UtilizadorHandler;
import codigo.resources.JsonService;
import java.util.ArrayList;
import java.util.List;

public class UtilizadorLoader {

    private final JsonService json;
    private final UtilizadorHandler handler;
    private final String ficheiro;

    public UtilizadorLoader(JsonService json, UtilizadorHandler handler, String ficheiro) {
        this.json = json;
        this.handler = handler;
        this.ficheiro = ficheiro;
    }

    public void carregar() throws Exception {
        List<UtilizadorDTO> dtos = json.readListFromFile(ficheiro, UtilizadorDTO.class);
        for (UtilizadorDTO dto : dtos) {
            handler.dadosUtilizador(
                    dto.nome,
                    dto.email,
                    dto.password,
                    Cargo.valueOf(dto.cargo)
            );
        }
        System.out.println("✔ Utilizadores carregados: " + dtos.size());
    }
}
