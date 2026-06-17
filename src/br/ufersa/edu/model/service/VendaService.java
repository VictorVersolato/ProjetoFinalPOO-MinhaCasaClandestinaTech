package br.ufersa.edu.model.service;

import br.ufersa.edu.Venda;
import br.ufersa.edu.Item;
import br.ufersa.edu.Equipamento;
import br.ufersa.edu.exception.EstoqueInsuficienteException;
import br.ufersa.edu.model.DAO.BaseDAO;
import br.ufersa.edu.model.DAO.DAOFactory;
import java.util.List;

public class VendaService {
    private final BaseDAO<Venda> dao = DAOFactory.getFactory().criarVendaDAO();
    private final EquipamentoService equipamentoService = new EquipamentoService();

    public void finalizarVenda(Venda venda) throws EstoqueInsuficienteException {
        if (venda.getItens().isEmpty()) {
            throw new IllegalArgumentException("Nao se pode realizar uma venda sem itens.");
        }

        for (Item item : venda.getItens()) {
            Equipamento eq = equipamentoService.buscarPorNumeroSerie(item.getNumeroDeSerie());
            if (eq == null) {
                throw new IllegalArgumentException("Equipamento de serie " + item.getNumeroDeSerie() + " nao cadastrado.");
            }
            if (eq.getQtdeEmEstoque() < item.getQuantidade()) {
                throw new EstoqueInsuficienteException(
                    "Estoque insuficiente para a serie " + item.getNumeroDeSerie() +
                    " (disponivel: " + eq.getQtdeEmEstoque() + ", pedido: " + item.getQuantidade() + ").");
            }
        }

        dao.inserir(venda);

        for (Item item : venda.getItens()) {
            Equipamento eq = equipamentoService.buscarPorNumeroSerie(item.getNumeroDeSerie());
            equipamentoService.atualizarEstoque(eq.getNumeroDeSerie(), eq.getQtdeEmEstoque() - item.getQuantidade());
        }
    }

    //Cancela a venda devolvendo os itens ao estoque.
    public void cancelarVenda(Venda venda) {
        Venda existente = dao.buscar(String.valueOf(venda.getId()));
        if (existente == null) {
            throw new IllegalArgumentException("Venda nao localizada.");
        }
        for (Item item : existente.getItens()) {
            Equipamento eq = equipamentoService.buscarPorNumeroSerie(item.getNumeroDeSerie());
            if (eq != null) {
                equipamentoService.atualizarEstoque(eq.getNumeroDeSerie(), eq.getQtdeEmEstoque() + item.getQuantidade());
            }
        }
        dao.deletar(existente);
    }

    public void alterarVenda(Venda venda) {
        dao.alterar(venda);
    }

    public Venda buscarPorId(int id) {
        return dao.buscar(String.valueOf(id));
    }

    public List<Venda> listarTodas() {
        return dao.listar();
    }
}
