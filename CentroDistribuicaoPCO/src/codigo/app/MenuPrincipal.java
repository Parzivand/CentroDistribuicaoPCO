package codigo.app;

import codigo.domain.*;
import codigo.domain.enums.*;
import codigo.handlers.*;
import java.util.*;

/**
 * Classe principal da aplicação que implementa um menu interativo em consola
 * com autenticação por utilizador e autorização baseada em cargo.
 * Orquestra todos os handlers de negócio através de uma interface de texto.
 */
public class MenuPrincipal {

    /**
     * Handlers de negócio injetados no construtor.
     * Cada handler encapsula um sub-sistema específico da aplicação.
     */
    private final ProdutoHandler produtoHandler;
    private final UtilizadorHandler utilizadores;
    private final LojaHandler lojaHandler;
    private final FornecedorHandler fornecedorHandler;
    private final InventarioHandler inventarioHandler;
    private final RececaoHandler rececaoHandler;
    private final ExpedicaoHandler expedicaoHandler;
    private final Encomendahandler encomendaHandler;
    private final AjusteStockHandler ajusteStockHandler;

    /**
     * Construtor que recebe todos os handlers necessários.
     * Realiza injeção de dependências para desacoplar MenuPrincipal da lógica de negócio.
     *
     * @param produtoHandler     Handler para gestão de produtos
     * @param utilizadorHandler  Handler para gestão de utilizadores
     * @param lojaHandler        Handler para gestão de lojas
     * @param fornecedorHandler  Handler para gestão de fornecedores
     * @param inventarioHandler  Handler para gestão de inventário
     * @param rececaoHandler     Handler para receções de mercadoria
     * @param expedicaoHandler   Handler para expedições
     * @param encomendaHandler   Handler para encomendas
     * @param ajusteStockHandler Handler para ajustes de stock
     */
    public MenuPrincipal(
            ProdutoHandler produtoHandler,
            UtilizadorHandler utilizadorHandler,
            LojaHandler lojaHandler,
            FornecedorHandler fornecedorHandler,
            InventarioHandler inventarioHandler,
            RececaoHandler rececaoHandler,
            ExpedicaoHandler expedicaoHandler,
            Encomendahandler encomendaHandler,
            AjusteStockHandler ajusteStockHandler
    ) {
        this.utilizadores = utilizadorHandler;
        this.lojaHandler = lojaHandler;
        this.produtoHandler = produtoHandler;
        this.fornecedorHandler = fornecedorHandler;
        this.inventarioHandler = inventarioHandler;
        this.rececaoHandler = rececaoHandler;
        this.expedicaoHandler = expedicaoHandler;
        this.encomendaHandler = encomendaHandler;
        this.ajusteStockHandler = ajusteStockHandler;
    }

    /**
     * Ponto de entrada principal da aplicação.
     * Executa o ciclo de autenticação + menu até logout ou saída.
     */
    public void run() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            Utilizador u = autenticar(sc);
            if (u == null) {
                System.out.println("Login cancelado.");
                return;
            }

            boolean sessaoAtiva = true;
            while (sessaoAtiva) {
                Cargo cargo = u.getcargo();
                mostrarMenu(cargo);

                String op = sc.nextLine().trim().toLowerCase();
                switch (op) {
                    case "logout":
                    case "sair":
                        sessaoAtiva = false;
                        break;
                    default:
                        sessaoAtiva = executarOpcao(sc, u, op);
                        break;
                }
            }
        }
    }

    /**
     * Autentica um utilizador através de email e password.
     * Permite múltiplas tentativas até "sair".
     *
     * @param sc Scanner para ler input do utilizador
     * @return Utilizador autenticado ou null se cancelar
     */
    private Utilizador autenticar(Scanner sc) {
        while (true) {
            System.out.println("\n=== LOGIN ===");
            System.out.print("Email (ou 'sair'): ");
            String email = sc.nextLine().trim();
            if (email.equalsIgnoreCase("sair")) return null;

            System.out.print("Password: ");
            String pass = sc.nextLine();

            for (Utilizador u : utilizadores.listarUtilizadores()) {
                if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(pass)) {
                    System.out.println("Bem-vindo, " + u.getNome() + " (" + u.getcargo() + ")");
                    return u;
                }
            }
            System.out.println("Credenciais inválidas.");
        }
    }

    /**
     * Mostra o menu de opções específico para cada {@link Cargo}.
     * Cada cargo vê apenas as opções para as quais está autorizado.
     *
     * @param cargo Cargo do utilizador autenticado
     */
    private void mostrarMenu(Cargo cargo) {
        System.out.println("\n=== MENU (" + cargo + ") ===");

        switch (cargo) {
            case ADMINISTRADOR:
                System.out.println("- criar produto");
                System.out.println("- ver produtos");
                System.out.println("- ver por sku");
                System.out.println("- remover produto");
                System.out.println("- registar utilizador");
                System.out.println("- adicionar loja");
                System.out.println("- ver lojas");
                System.out.println("- adicionar fornecedor");
                System.out.println("- ver fornecedores");
                System.out.println("- logout");
                break;

            case GESTOR_lOG:
                System.out.println("- registar encomenda");
                System.out.println("- consultar por localizacao");
                System.out.println("- listar encomendas");
                System.out.println("- cancelar encomenda");
                System.out.println("- logout");
                break;

            case OPERDADOR_REC:
                System.out.println("- registar rececao");
                System.out.println("- consultar rececoes");
                System.out.println("- logout");
                break;

            case OPERDADOR_SEL:
                System.out.println("- preparar expedicao");
                System.out.println("- logout");
                break;

            case OPERDADOR_ARM:
                System.out.println("- mover produto");
                System.out.println("- mover expedicao");
                System.out.println("- ajusta Stock");
                System.out.println("- expede encomenda");
                System.out.println("- logout");
                break;

            default:
                System.out.println("- logout");
        }
    }

    /**
     * Roteador central que executa a opção escolhida pelo utilizador.
     * Verifica autorização baseada no cargo e delega para o método UI adequado.
     *
     * @param sc   Scanner para input
     * @param u    Utilizador autenticado
     * @param op   Opção escolhida (em minúsculas)
     * @return true se deve continuar a sessão, false para sair
     */
    private boolean executarOpcao(Scanner sc, Utilizador u, String op) {
        Cargo cargo = u.getcargo();

        // ADMINISTRADOR
        if (cargo == Cargo.ADMINISTRADOR) {
            switch (op) {
                case "criar produto":
                    criarProdutoUI(sc);
                    return true;

                case "ver produtos":
                    listarProdutosUI(sc);
                    return true;

                case "ver por sku":
                    System.out.print("SKU: ");
                    String sku = sc.nextLine().trim();
                    System.out.println(produtoHandler.procurarPorSku(sku));
                    return true;

                case "remover produto":
                    System.out.print("SKU: ");
                    String skuRem = sc.nextLine().trim();
                    produtoHandler.removerProduto(skuRem);
                    return true;

                case "registar utilizador":
                    registarUtilizadorUI(sc);
                    return true;

                case "adicionar loja":
                    adicionarLojaUI(sc);
                    return true;
                case "adicionar fornecedor":
                    adicionarFornecedorUI(sc);
                    return true;

                case "ver fornecedores":
                    fornecedorHandler.getfornecedores().forEach((k,v) -> System.out.println(k + " | " + v));
                    return true;

                case "ver lojas":
                    HashMap<String, Loja> lojas = lojaHandler.verLojas_registadas();

                    if (lojas.isEmpty()) {
                        System.out.println("Nenhuma loja registada.");
                        return true;
                    }

                    System.out.println("\n--- LOJAS ---");
                    lojas.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey())
                            .forEach(e -> {
                                Loja l = e.getValue();
                                System.out.println(e.getKey() + " | " + l.getNome() + " | " + l.getMorada());
                            });

                    return true;

                default:
                    System.out.println("Opção inválida.");
                    return true;
            }
        }

        if (cargo == Cargo.OPERDADOR_ARM) {
            switch (op) {
                case "mover produto":
                    moverProdutoUI(sc, u);
                    return true;

                case "mover expedicao":
                    moverExpedicaoUI(sc);
                    return true;

                case "ajustar stock":
                    ajustarStockUI(sc);
                    return true;

                case "expede encomenda":
                    expedirEncomendaUI(sc);
                    return true;

                default:
                    System.out.println("Opção inválida.");
                    return true;
            }
        }

        // OPERDADOR_REC
        if (cargo == Cargo.OPERDADOR_REC) {
            switch (op) {
                case "registar rececao":  
                    registarRececaoUI(sc);
                    return true;

                case "consultar rececoes":
                    List<Rececao> todas = rececaoHandler.listarRececoes();
                    if (todas.isEmpty()) {
                        System.out.println("📭 Nenhuma receção registada.");
                        return true;
                    }

                    System.out.println("\n📦 RECEÇÕES (mais recentes primeiro):");
                    for (int i = 0; i < todas.size(); i++) {
                        Rececao r = todas.get(i);
                        System.out.printf("\n%d️⃣ %s\n", (i + 1), r.toString());
                        System.out.printf("   📅 %s | 🏭 %s | 📊 %d linhas | %s%d NC\n", 
                                r.getData(), r.getFornecedor().getNome(), 
                                r.getTotalLinhas(), 
                                r.listarProdutosQuarentena().isEmpty() ? "✅ " : "⚠️ ",
                                r.listarProdutosQuarentena().size());

                        // Detalhe 1ª linha (se houver)
                        if (!r.getLinhas().isEmpty()) {
                            LinhaRececao linha = r.getLinhas().get(0);
                            System.out.printf("   📄 %s | %s x%d | %s\n", 
                                    linha.getProduto().getNome(), linha.getLote(), 
                                    linha.getQuantidadeRecebida(), linha.getEstado());
                        }
                    }
                    return true;

                default:
                    System.out.println("Opção inválida.");
                    return true;
            }
        }

        // OPERDADOR_SEL 
        if (cargo == Cargo.OPERDADOR_SEL) {
            switch (op) {
                case "preparar expedicao":
                case "prepara expedicao":
                    prepararExpedicaoUI(sc);
                    return true;

                default:
                    System.out.println("Opção inválida.");
                    return true;
            }
        }

        if (cargo == Cargo.GESTOR_lOG) {
            switch (op) {
                case "registar encomenda":
                    registarEncomendaUI(sc);
                    return true;

                case "consultar por localizacao":
                    consultarPorLocalizacaoUI(sc);
                    return true;

                case "listar encomendas":
                    listarEncomendasUI();
                    return true;

                case "cancelar encomenda":
                    cancelarEncomendaUI(sc);
                    return true;

                default:
                    System.out.println("Opção inválida.");
                    return true;
            }
        }

        System.out.println("Opção inválida.");
        return true;
    }

    /**
     * Interface de utilizador para criação de produto.
     * Recolhe dados interativamente e chama {@link ProdutoHandler#criarProduto}.
     *
     * @param sc Scanner para input do utilizador
     */
    private void criarProdutoUI(Scanner sc) {
        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Categoria (3 letras): ");
        String categoria = sc.nextLine();

        System.out.print("Unidade de medida: ");
        String unidade = sc.nextLine();

        System.out.print("Tem validade? (sim/nao): ");
        String temVal = sc.nextLine().trim().toLowerCase();

        List<TipoRestricoes> restr = lerRestricoes(sc);

        if (temVal.startsWith("s")) {
            int ano = lerInt(sc, "Ano: ");
            int mes = lerInt(sc, "Mes (1-12): ");
            int dia = lerInt(sc, "Dia: ");

            Date validade = new Date(ano - 1900, mes - 1, dia);
            produtoHandler.criarProduto(nome, categoria, unidade, restr, validade);
        } else {
            produtoHandler.criarProduto(nome, categoria, unidade, restr);
        }

        System.out.println("Produto criado.");
    }

    /**
     * Estado da paginação atual para listagem de produtos.
     * É mantido entre chamadas para permitir navegação.
     */
    private int paginaAtual = 0;

    /**
     * Interface paginada para listagem de produtos.
     * Mostra 10 produtos por página, permite navegação sequencial.
     *
     * @param sc Scanner para input do utilizador
     */
    private void listarProdutosUI(Scanner sc) {
        int itensPorPagina = 10;
        
        while (true) {
            // chama com paginação real (offset)
            ArrayList<Produto> produtos = produtoHandler.getProdutos(itensPorPagina, paginaAtual);
            
            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto para mostrar.");
                break;
            }
            
            // UM POR LINHA ✅
            System.out.println("\n--- Página " + (paginaAtual + 1) + " ---");
            for (Produto p : produtos) {
                System.out.println(p.toString());
            }
            
            System.out.print("Mais 10? (sim/nao/voltar): ");
            String r = sc.nextLine().trim().toLowerCase();
            
            if (r.startsWith("n") || r.startsWith("v")) {
                paginaAtual = 0;  // volta ao início
                break;
            }
            
            paginaAtual++;  // próxima página
        }
    }

    /**
     * Interface para registo de novo utilizador.
     * Valida o cargo através de valueOf.
     *
     * @param sc Scanner para input
     */
    private void registarUtilizadorUI(Scanner sc) {
        System.out.print("Nome: ");
        String nome = sc.nextLine().trim();
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        System.out.print("Cargo (ADMINISTRADOR/GESTOR_lOG/OPERDADOR_ARM/OPERDADOR_SEL/OPERDADOR_REC): ");
        String cargoInput = sc.nextLine().trim().toUpperCase();

        Cargo cargo;
        try {
            cargo = Cargo.valueOf(cargoInput);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Cargo inválido: " + cargoInput);
            System.out.println("Use exatamente: " + Arrays.toString(Cargo.values()));
            return;
        }

        utilizadores.dadosUtilizador(nome, email, pass, cargo);
        System.out.println("✅ Utilizador registado: " + email + " (" + cargo + ")");
    }

    /**
     * Interface para criação de nova loja.
     *
     * @param sc Scanner para input
     */
    private void adicionarLojaUI(Scanner sc) {
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Área atuação (3 letras): ");
        String area = sc.nextLine();
        System.out.print("Morada: ");
        String morada = sc.nextLine();

        lojaHandler.adicionarLoja(nome, area, morada);
        System.out.println("Loja adicionada.");
    }

    /**
     * Interface interativa para recolha de restrições de produto.
     *
     * @param sc Scanner para input
     * @return Lista de {@link TipoRestricoes} definidas pelo utilizador
     */
    private List<TipoRestricoes> lerRestricoes(Scanner sc) {
        List<TipoRestricoes> restr = new ArrayList<>();
        int n = lerInt(sc, "Quantas restrições? ");
        for (int i = 0; i < n; i++) {
            System.out.print("Restrição #" + (i + 1) + " (ex: FRIO, FRAGIL, TOXICO, EXPLOSIVO, VOLUMOSO, TEMPERATURA...): ");
            String r = sc.nextLine().trim().toUpperCase();
            restr.add(TipoRestricoes.valueOf(r));
        }
        return restr;
    }

    /**
     * Lê um inteiro do utilizador com validação de input.
     *
     * @param sc Scanner para input
     * @param prompt Mensagem a mostrar
     * @return Inteiro válido lido
     */
    private int lerInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Número inválido.");
            }
        }
    }

    /**
     * UI para movimentação de produto entre localizações.
     *
     * @param sc Scanner para input
     * @param u  Utilizador autenticado (para audit trail)
     */
    private void moverProdutoUI(Scanner sc, Utilizador u) {
        try {
            System.out.print("Localização origem: ");
            String origem = sc.nextLine().trim();

            System.out.print("Localização destino: ");
            String destino = sc.nextLine().trim();

            System.out.print("SKU: ");
            String sku = sc.nextLine().trim();

            Produto p = produtoHandler.procurarPorSku(sku);
            if (p == null) {
                System.out.println("❌ Produto não encontrado.");
                return;
            }

            int qtd = lerInt(sc, "Quantidade: ");

            System.out.print("Estado (DISPONIVEL/QUARENTENA): ");
            estadoStock estado = estadoStock.valueOf(sc.nextLine().trim().toUpperCase());

            inventarioHandler.moverProduto(origem, destino, p, qtd, estado, u.getEmail());
            System.out.println("✅ Produto movido.");
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    /**
     * UI para movimentação de expedição para nova localização.
     *
     * @param sc Scanner para input
     */
    private void moverExpedicaoUI(Scanner sc) {
        try {
            System.out.print("ID da expedição: ");
            String id = sc.nextLine().trim();

            System.out.print("Nova localização: ");
            String novaLoc = sc.nextLine().trim();

            expedicaoHandler.moverExpedicao(id, novaLoc);
            System.out.println("✅ Expedição movida.");
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    /**
     * UI para criar ajuste de stock.
     *
     * @param sc Scanner para input
     */
    private void ajustarStockUI(Scanner sc) {
        try {
            System.out.print("SKU: ");
            String sku = sc.nextLine().trim();

            System.out.print("Código da localização: ");
            String codLoc = sc.nextLine().trim();

            Localizacao loc = inventarioHandler.getLocalizacaoPorCodigo(codLoc);
            if (loc == null) {
                System.out.println("❌ Localização não encontrada.");
                return;
            }

            int qtdAjuste = lerInt(sc, "Quantidade a ajustar: ");

            ajusteStockHandler.CriarAjusteStock(sku, loc, qtdAjuste);
            System.out.println("✅ Ajuste de stock proposto.");
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    /**
     * UI para preparar expedição de encomenda.
     *
     * @param sc Scanner para input
     */
    private void expedirEncomendaUI(Scanner sc) {
        try {
            System.out.print("Referência da encomenda: ");
            String ref = sc.nextLine().trim();

            System.out.print("Localização inicial: ");
            String loc = sc.nextLine().trim();

            expedicaoHandler.prepararExpedicao(ref, loc);
            System.out.println("✅ Encomenda expedida (expedição criada).");
        } catch (Exception e) {
            System.out.println("❌ Não foi possível expedir: " + e.getMessage());
        }
    }

    /**
     * UI completa para registo de receção.
     * Cria receção → adiciona múltiplas linhas → mostra resumo.
     *
     * @param sc Scanner para input
     */
    private void registarRececaoUI(Scanner sc) {
        try {
            System.out.print("Email do fornecedor: ");
            String email = sc.nextLine().trim().toUpperCase();
            Fornecedor fornecedor = fornecedorHandler.getfornecedores().get(email);
            if (fornecedor == null) {
                System.out.println("❌ Fornecedor não encontrado.");
                return;
            }

            rececaoHandler.criarRececao(fornecedor);  

            while (true) {
                System.out.print("SKU do produto (ou 'fim'): ");
                String sku = sc.nextLine().trim();
                if (sku.equalsIgnoreCase("fim")) break;

                Produto p = produtoHandler.procurarPorSku(sku);
                if (p == null) continue;

                int qtd = lerInt(sc, "Quantidade: ");
                System.out.print("Lote: ");
                String lote = sc.nextLine().trim();
                System.out.print("Código da localização: ");
                String codLoc = sc.nextLine().trim();

                rececaoHandler.adicionarLinhaRececao(p, lote, qtd, codLoc);  // ✅ NOVO

                System.out.print("Mais linhas? (s/n): ");
                if (!sc.nextLine().trim().toLowerCase().startsWith("s")) break;
            }

            System.out.println(rececaoHandler.resumoRececaoAtual());  // ✅ NOVO
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    /**
     * UI para preparar expedição por operador de seleção.
     * Lista encomendas POR_PREPARAR e cria expedição.
     *
     * @param sc Scanner para input
     */
    private void prepararExpedicaoUI(Scanner sc) {
        try {
            System.out.println("📦 Encomendas disponíveis:");
            boolean temPronta = false;

            for (Encomenda enc : encomendaHandler.listarEncomendas()) {
                if (enc.getEstado() == EstadoEncomenda.POR_PREPARAR) {
                    System.out.println("✅ " + enc.toString());
                    temPronta = true;
                } else {
                    System.out.println("⏳ " + enc.toString() + " (estado: " + enc.getEstado() + ")");
                }
            }

            if (!temPronta) {
                System.out.println("❌ Nenhuma encomenda no estado POR_PREPARAR.");
                return;
            }

            System.out.print("Referência da encomenda: ");
            String ref = sc.nextLine().trim();

            Encomenda enc = encomendaHandler.getEncomenda(ref);
            if (enc == null) {
                System.out.println("❌ Encomenda não encontrada.");
                return;
            }

            System.out.print("Localização inicial: ");
            String loc = sc.nextLine().trim();

            Expedicao exp = expedicaoHandler.prepararExpedicao(ref, loc);
            System.out.println("✅ Expedição criada com sucesso!");
            System.out.println("Expedição: " + exp);
            System.out.println("Encomenda agora em estado EXPEDIDA.");

        } catch (Exception e) {
            System.out.println("❌ Erro ao preparar expedição: " + e.getMessage());
        }
    }

    /**
     * UI para registo de nova encomenda.
     * Cria encomenda vazia → adiciona linhas → mostra resumo.
     *
     * @param sc Scanner para input
     */
    private void registarEncomendaUI(Scanner sc) {
        try {
            System.out.print("Código da loja (ex: LIS001): ");
            String codLoja = sc.nextLine().trim().toLowerCase();

            Loja loja = lojaHandler.verLojas_registadas().get(codLoja);
            if (loja == null) {
                System.out.println("❌ Loja não encontrada.");
                return;
            }

            int prioridade = lerInt(sc, "Prioridade (1..5): ");
            String ref = encomendaHandler.criarEncomenda(loja, prioridade);
            System.out.println("✅ Encomenda criada: " + ref);

            while (true) {
                System.out.print("SKU (ou 'fim'): ");
                String sku = sc.nextLine().trim();
                if (sku.equalsIgnoreCase("fim")) break;

                Produto p = produtoHandler.procurarPorSku(sku);
                if (p == null) {
                    System.out.println("❌ Produto não encontrado.");
                    continue;
                }

                int qtd = lerInt(sc, "Quantidade: ");
                encomendaHandler.adicionarLinhaEncomenda(ref, p, qtd);

                System.out.println(encomendaHandler.resumoEncomenda(ref));
                System.out.print("Mais linhas? (s/n): ");
                if (!sc.nextLine().trim().toLowerCase().startsWith("s")) break;
            }
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }
    
    /**
     * UI paginada para consulta de stock por localização.
     *
     * @param sc Scanner para input
     */
    private void consultarPorLocalizacaoUI(Scanner sc) {
        try {
            System.out.print("Código da localização (ex: ARM0001): ");
            String codLoc = sc.nextLine().trim().toUpperCase();

            int pagina = 0;
            while (true) {
                List<Map.Entry<Produto, Integer>> itens =
                        inventarioHandler.consultarPorLocalizacao(codLoc, pagina);

                if (itens.isEmpty()) {
                    if (pagina == 0) System.out.println("Sem stock nessa localização.");
                    break;
                }

                System.out.println("\n--- " + codLoc + " | Página " + (pagina + 1) + " ---");
                for (Map.Entry<Produto, Integer> e : itens) {
                    Produto p = e.getKey();
                    System.out.println(p.getSKU() + " | " + p.getNome() + " | qtd=" + e.getValue());
                }

                System.out.print("Próxima página? (s/n): ");
                if (!sc.nextLine().trim().toLowerCase().startsWith("s")) break;
                pagina++;
            }
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    /**
     * Lista todas as encomendas registadas.
     */
    private void listarEncomendasUI() {
        System.out.println("\n📦 ENCOMENDAS:");
        for (Encomenda e : encomendaHandler.listarEncomendas()) {
            System.out.println("- " + e);
        }
    }

    /**
     * UI para cancelamento de encomenda.
     *
     * @param sc Scanner para input
     */
    private void cancelarEncomendaUI(Scanner sc) {
        try {
            System.out.print("Referência: ");
            String ref = sc.nextLine().trim();
            encomendaHandler.cancelarEncomenda(ref);
            System.out.println("✅ Encomenda cancelada e reservas libertadas.");
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    /**
     * UI para criação de novo fornecedor.
     *
     * @param sc Scanner para input
     */
    private void adicionarFornecedorUI(Scanner sc) {
        System.out.print("Nome: ");
        String nome = sc.nextLine().trim();

        System.out.print("Email: ");
        String email = sc.nextLine().trim();

        System.out.print("Telefone: ");
        String tel = sc.nextLine().trim();

        fornecedorHandler.adicionarFornecedor(nome, email, tel);
        System.out.println("✅ Fornecedor criado: " + email);
    }
}
