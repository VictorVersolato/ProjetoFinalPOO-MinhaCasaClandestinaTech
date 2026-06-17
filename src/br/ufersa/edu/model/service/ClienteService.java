package br.ufersa.edu.model.service;

import br.ufersa.edu.Cliente;
import br.ufersa.edu.model.DAO.BaseDAO;
import br.ufersa.edu.model.DAO.DAOFactory;
import java.util.List;

public class ClienteService {
    // Programa para a abstracao BaseDAO, obtida da Abstract Factory (polimorfismo).
    private final BaseDAO<Cliente> dao = DAOFactory.getFactory().criarClienteDAO();

    public void cadastrar(Cliente c) {
        dao.inserir(c);
    }

    public void editar(Cliente c) {
        if (c.getId() <= 0) {
            throw new IllegalArgumentException("Cliente sem ID válido para alteração.");
        }
        dao.alterar(c);
    }

    public void remover(Cliente c) {
        if (c.getId() <= 0) {
            throw new IllegalArgumentException("Cliente inválido para exclusão.");
        }
        dao.deletar(c);
    }

    public Cliente buscarPorCpf(String cpf) {
        return dao.buscar(cpf.replaceAll("\\D", ""));
    }

    public List<Cliente> listarTodos() {
        return dao.listar();
    }
}
