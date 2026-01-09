package codigo.dto;

import codigo.domain.Produto;

public record ReservaDTO(
        String codigoLocalizacao,
        Produto produto,
        int quantidade
) {}