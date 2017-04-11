package tech.linard.miniolimpiadasditec;

/**
 * Created by lucas on 03/04/17.
 */

public class User {
    private String UID;
    private String matricula;
    private String nome;
    private String email;

    public User() {
    }

    public User(String UID, String matricula, String nome, String email, int groupID) {
        this.UID = UID;
        this.matricula = matricula;
        this.nome = nome;
        this.email = email;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
