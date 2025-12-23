package java.domain;

public class Fornecedor {

    // Atributos

    private String nome, contacto;

    // Construtor

    public Fornecedor(String nome, String contacto) {
        this.nome = nome;
        this.contacto = contacto;
    }

    // Gets e Setters

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }

   }