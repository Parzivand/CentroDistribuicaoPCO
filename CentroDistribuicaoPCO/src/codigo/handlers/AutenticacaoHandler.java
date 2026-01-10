package codigo.handlers;

import codigo.domain.Utilizador;

public class AutenticacaoHandler {

    private UtilizadorHandler utilizadores;

    public AutenticacaoHandler(UtilizadorHandler utilizadores) {
        this.utilizadores = utilizadores;
    }

    public Utilizador autenticar(String email, String password) {
        if (email == null || password == null) {
            throw new IllegalArgumentException("Informações vazias");
        }

        email = email.trim();
        password = password.trim();

         System.out.println("DEBUG: total utilizadores = " + utilizadores.listarUtilizadores().size());
         
        for (Utilizador utilizador : utilizadores.listarUtilizadores()) {
            if (utilizador.getEmail().equalsIgnoreCase(email)
                    && utilizador.getPassword().equals(password)) {

                return utilizador; // LOGIN OK
            }
        }

        return null; // LOGIN FALHOU
    }
}
