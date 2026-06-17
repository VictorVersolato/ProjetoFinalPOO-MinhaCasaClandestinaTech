package br.ufersa.edu.model.service;

import br.ufersa.edu.Local;
import br.ufersa.edu.model.DAO.BaseDAO;
import br.ufersa.edu.model.DAO.DAOFactory;
import java.util.List;

public class LocalService {
    private final BaseDAO<Local> dao = DAOFactory.getFactory().criarLocalDAO();

    public void cadastrar(Local l) {
        dao.inserir(l);
    }

    public void editar(Local l) {
        if (l.getId() <= 0) {
            throw new IllegalArgumentException("Local sem ID válido para alteração.");
        }
        dao.alterar(l);
    }

    public void remover(Local l) {
        if (l.getId() <= 0) {
            throw new IllegalArgumentException("Local inválido para exclusão.");
        }
        dao.deletar(l);
    }

    public Local buscarPorNomeDaCasa(String nome) {
        return dao.buscar(nome);
    }

    public List<Local> listarTodos() {
        return dao.listar();
    }
}
