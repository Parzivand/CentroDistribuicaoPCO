package codigo.app.bootstrap;

import codigo.dto.LojaDTO;
import codigo.handlers.LojaHandler;
import codigo.resources.JsonService;
import java.util.List;

public class LojaLoader {

    private final JsonService json;
    private final LojaHandler handler;
    private final String ficheiro;

    public LojaLoader(JsonService json, LojaHandler handler, String ficheiro) {
        this.json = json;
        this.handler = handler;
        this.ficheiro = ficheiro;
    }

    public void carregar() throws Exception {
        List<LojaDTO> dtos = json.readListFromFile(ficheiro, LojaDTO.class);
        for (LojaDTO dto : dtos) {
            handler.adicionarLoja(dto.nome, dto.codigo, dto.morada);
        }
        System.out.println("✔ Lojas carregadas: " + dtos.size());
    }
}
