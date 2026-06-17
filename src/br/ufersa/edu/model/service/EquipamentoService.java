package br.ufersa.edu.model.service;

import br.ufersa.edu.Equipamento;
import br.ufersa.edu.model.DAO.BaseDAO;
import br.ufersa.edu.model.DAO.DAOFactory;
import java.util.List;
import java.util.stream.Collectors;

public class EquipamentoService {
    private final BaseDAO<Equipamento> dao = DAOFactory.getFactory().criarEquipamentoDAO();

    public void cadastrar(Equipamento e) {
        dao.inserir(e);
    }

    public void editar(Equipamento e) {
        dao.alterar(e);
    }

    public void remover(Equipamento e) {
        dao.deletar(e);
    }

    public Equipamento buscarPorNumeroSerie(int numeroSerie) {
        return dao.buscar(String.valueOf(numeroSerie));
    }

    /** Pesquisa por parte do nome (requisito de pesquisa de equipamentos). */
    public List<Equipamento> buscarPorNome(String nome) {
        String alvo = nome.toLowerCase();
        return dao.listar().stream()
                .filter(e -> e.getNome().toLowerCase().contains(alvo))
                .collect(Collectors.toList());
    }

    public List<Equipamento> listarTodos() {
        return dao.listar();
    }

    /** Define a nova quantidade em estoque e persiste. */
    public void atualizarEstoque(int numeroSerie, int novaQtd) {
        Equipamento eq = buscarPorNumeroSerie(numeroSerie);
        if (eq != null && novaQtd >= 0) {
            eq.setQtdeEmEstoque(novaQtd);
            dao.alterar(eq);
        }
    }
}
