package br.ufersa.edu.model.service;

import br.ufersa.edu.Funcionario;
import br.ufersa.edu.Funcionario.Perfil;
import br.ufersa.edu.exception.AutenticacaoException;
import br.ufersa.edu.model.DAO.BaseDAO;
import br.ufersa.edu.model.DAO.DAOFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Regras de login e de cadastro de usuarios.
 * Apenas o gerente (perfil ADMIN) pode cadastrar novos funcionarios.
 */
public class AutenticacaoService {
    private final BaseDAO<Funcionario> dao = DAOFactory.getFactory().criarFuncionarioDAO();

    /** Valida login e senha. Lanca AutenticacaoException se as credenciais falharem. */
    public Funcionario login(String login, String senha) throws AutenticacaoException {
        //linha para verificar a senha hash em hexadecimal
        //System.out.println("O HASH DA SENHA DIGITADA É: " + hash(senha)); 
        Funcionario f = dao.buscar(login);
        if (f == null || !f.getSenhaHash().equals(hash(senha))) {
            throw new AutenticacaoException("Usuario ou senha invalidos.");
        }
        return f;
    }

    /**
     * Cadastra um novo funcionario (pelo ADMIN).
     */
    public Funcionario cadastrarFuncionario(String nome, String endereco, String login,
                                            String senhaPlana, Perfil perfil,
                                            Funcionario solicitante) throws AutenticacaoException {
        if (solicitante == null || !solicitante.isAdmin()) {
            throw new AutenticacaoException("Apenas o gerente (ADMIN) pode cadastrar funcionarios.");
        }
        if (dao.buscar(login) != null) {
            throw new AutenticacaoException("Ja existe um usuario com o login '" + login + "'.");
        }
        Funcionario novo = new Funcionario(0, nome, endereco, login, hash(senhaPlana), perfil);
        return dao.inserir(novo);
    }

    public List<Funcionario> listarFuncionarios() {
        return dao.listar();
    }

    /** SHA-256 em hexadecimal minusculo (mesmo formato gravado no banco). */
    public static String hash(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo de hash indisponivel.", e);
        }
    }
}
