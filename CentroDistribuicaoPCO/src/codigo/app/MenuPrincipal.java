package codigo.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import codigo.domain.Utilizador;
import codigo.domain.enums.Cargo;
import codigo.domain.enums.TipoRestricoes;
import codigo.handlers.*;

public class MenuPrincipal {

    private final ProdutoHandler produtoHandler;
    private final UtilizadorHandler utilizadores;
    private final LojaHandler lojaHandler;
    private final FornecedorHandler fornecedorHandler;
    private final InventarioHandler inventarioHandler;
    private final RececaoHandler rececaoHandler;
    private final ExpedicaoHandler expedicaoHandler;
    private final Encomendahandler encomendaHandler;

    public MenuPrincipal(
            ProdutoHandler produtoHandler,
            UtilizadorHandler utilizadorHandler,
            LojaHandler lojaHandler,
            FornecedorHandler fornecedorHandler,
            InventarioHandler inventarioHandler,
            RececaoHandler rececaoHandler,
            ExpedicaoHandler expedicaoHandler,
            Encomendahandler encomendaHandler
    ) {
        this.utilizadores = utilizadorHandler;
        this.lojaHandler = lojaHandler;
        this.produtoHandler = produtoHandler;
        this.fornecedorHandler = fornecedorHandler;
        this.inventarioHandler = inventarioHandler;
        this.rececaoHandler = rececaoHandler;
        this.expedicaoHandler = expedicaoHandler;
        this.encomendaHandler = encomendaHandler;
    }

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

    private Utilizador autenticar(Scanner sc) {
        while (true) {
            System.out.println("\n=== LOGIN ===");
            System.out.print("Email (ou 'sair'): ");
            String email = sc.nextLine().trim();
            if (email.equalsIgnoreCase("sair")) return null;

            System.out.print("Password: ");
            String pass = sc.nextLine();

            // O teu UtilizadorHandler tem listarUtilizadores() [file:7]
            for (Utilizador u : utilizadores.listarUtilizadores()) {
                if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(pass)) {
                    System.out.println("Bem-vindo, " + u.getNome() + " (" + u.getcargo() + ")");
                    return u;
                }
            }
            System.out.println("Credenciais inválidas.");
        }
    }

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
                System.out.println("- logout");
                break;

            case GESTOR_lOG:
                System.out.println("- registar encomenda");
                System.out.println("- consultar por localizacao");
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
                System.out.println("- (por implementar) mover produto / mover expedicao");
                System.out.println("- logout");
                break;

            default:
                System.out.println("- logout");
        }
    }

    private boolean executarOpcao(Scanner sc, Utilizador u, String op) {
        Cargo cargo = u.getcargo();

        // ADMIN
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

                case "ver lojas":
                    System.out.println(lojaHandler.verLojas_registadas());
                    return true;

                default:
                    System.out.println("Opção inválida.");
                    return true;
            }
        }

        // Exemplos mínimos para outros cargos (ajusta aos teus handlers)
        if (cargo == Cargo.OPERDADOR_REC) {
            if (op.equals("consultar rececoes")) {
                System.out.println(rececaoHandler.getrececoes());
                return true;
            }
            System.out.println("Opção inválida.");
            return true;
        }

        System.out.println("Opção inválida.");
        return true;
    }

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

            // Atenção: Date(ano,mes,dia) é esquisito (ano-1900 e mes 0-11).
            // Mantém assim por agora, mas ideal é converter de LocalDate.
            Date validade = new Date(ano - 1900, mes - 1, dia);

            produtoHandler.criarProduto(nome, categoria, unidade, restr, validade); // existe [file:10]
        } else {
            produtoHandler.criarProduto(nome, categoria, unidade, restr); // existe [file:10]
        }

        System.out.println("Produto criado.");
    }

    private void listarProdutosUI(Scanner sc) {
        int n = 10;
        while (true) {
            System.out.println(produtoHandler.getProdutos(n)); // existe [file:10]
            System.out.print("Mais 10? (sim/nao): ");
            String r = sc.nextLine().trim().toLowerCase();
            if (r.startsWith("n")) break;
            n += 10;
        }
    }

    private void registarUtilizadorUI(Scanner sc) {
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();
        System.out.print("Cargo (ADMINISTRADOR/GESTOR_LOGISTICO/OPERADOR_ARM/OPERADOR_SELECAO/OPERADOR_RECECAO): ");
        Cargo cargo = Cargo.valueOf(sc.nextLine().trim().toUpperCase());

        utilizadores.dadosUtilizador(nome, email, pass, cargo); // existe [file:7]
        System.out.println("Utilizador registado.");
    }

    private void adicionarLojaUI(Scanner sc) {
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Área atuação (3 letras): ");
        String area = sc.nextLine();
        System.out.print("Morada: ");
        String morada = sc.nextLine();

        lojaHandler.adicionarLoja(nome, area, morada); // existe [file:11]
        System.out.println("Loja adicionada.");
    }

    private List<TipoRestricoes> lerRestricoes(Scanner sc) {
        List<TipoRestricoes> restr = new ArrayList<>();
        int n = lerInt(sc, "Quantas restrições? ");
        for (int i = 0; i < n; i++) {
            System.out.print("Restrição #" + (i + 1) + " (ex: FRIO, PERIGOSO, TOXICO...): ");
            String r = sc.nextLine().trim().toUpperCase();
            restr.add(TipoRestricoes.valueOf(r));
        }
        return restr;
    }

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
}
