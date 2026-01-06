package codigo.app.bootstrap;

import codigo.dto.FornecedorDTO;
import codigo.handlers.FornecedorHandler;
import codigo.resources.JsonService;
import java.util.List;

public class FornecedorLoader {

    private final JsonService json;
    private final FornecedorHandler handler;
    private final String ficheiro;

    public FornecedorLoader(JsonService json, FornecedorHandler handler, String ficheiro) {
        this.json = json;
        this.handler = handler;
        this.ficheiro = ficheiro;
    }

    public void carregar() throws Exception {
        List<FornecedorDTO> dtos = json.readListFromFile(ficheiro, FornecedorDTO.class);
        for (FornecedorDTO dto : dtos) {
            handler.adicionarfornecedor(dto.nome, dto.contacto, dto.id);
        }
        System.out.println("✔ Fornecedores carregados: " + dtos.size());
    }
}
