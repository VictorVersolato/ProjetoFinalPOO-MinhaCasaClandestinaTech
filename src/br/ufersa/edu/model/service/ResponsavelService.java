package br.ufersa.edu.model.service;

import br.ufersa.edu.Responsavel;
import br.ufersa.edu.model.DAO.BaseDAO;
import br.ufersa.edu.model.DAO.DAOFactory;
import java.util.List;

public class ResponsavelService {
    private final BaseDAO<Responsavel> dao = DAOFactory.getFactory().criarResponsavelDAO();

    public void salvar(Responsavel r) {
        dao.inserir(r);
    }

    public void editar(Responsavel r) {
        if (r.getId() <= 0) {
            throw new IllegalArgumentException("Responsável sem ID válido para alteracao.");
        }
        dao.alterar(r);
    }

    public void remover(Responsavel r) {
        if (r.getId() <= 0) {
            throw new IllegalArgumentException("Responsável inválido para exclusao.");
        }
        dao.deletar(r);
    }

    public Responsavel buscarPorNome(String nome) {
        return dao.buscar(nome);
    }

    public List<Responsavel> listarTodos() {
        return dao.listar();
    }
}
