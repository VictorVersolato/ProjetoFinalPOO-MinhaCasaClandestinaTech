package br.ufersa.edu;

import br.ufersa.edu.model.Pessoa;
import br.ufersa.edu.exception.ValidacaoException;

/**
 * O perfil define o que o usuário pode fazer (apenas ADMIN pode cadastrar funcionários).
 */
public class Funcionario extends Pessoa {

    public enum Perfil { ADMIN, FUNCIONARIO } //lembrando que apenas ADMIN acessa o sistema

    private String login;
    private String senhaHash;
    private Perfil perfil;

    public Funcionario(int id, String nome, String endereco,
                       String login, String senhaHash, Perfil perfil) {
        super(id, nome, endereco);
        setLogin(login);
        this.senhaHash = senhaHash;
        setPerfil(perfil);
    }

    public String getLogin() { return login; }
    public String getSenhaHash() { return senhaHash; }
    public Perfil getPerfil() { return perfil; }

    public void setLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            throw new ValidacaoException("Login é obrigatorio.");
        }
        this.login = login.trim();
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public void setPerfil(Perfil perfil) {
        if (perfil == null) {
            throw new ValidacaoException("Perfil é obrigatorio.");
        }
        this.perfil = perfil;
    }

    public boolean isAdmin() {
        return perfil == Perfil.ADMIN;
    }

    @Override
    public String getDocumento() {
        return "login " + login + " / " + perfil;
    }
}
