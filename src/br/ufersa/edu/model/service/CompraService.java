package br.ufersa.edu.model.service;

import br.ufersa.edu.Compra;
import br.ufersa.edu.Item;
import br.ufersa.edu.Equipamento;
import br.ufersa.edu.model.DAO.BaseDAO;
import br.ufersa.edu.model.DAO.DAOFactory;
import java.util.List;

public class CompraService {
    private final BaseDAO<Compra> dao = DAOFactory.getFactory().criarCompraDAO();
    private final EquipamentoService equipamentoService = new EquipamentoService();

    public void registrarCompra(Compra compra) {
        if (compra.getItens().isEmpty()) {
            throw new IllegalArgumentException("Não se pode registrar uma compra vazia.");
        }
        dao.inserir(compra);

        for (Item item : compra.getItens()) {
            Equipamento eq = equipamentoService.buscarPorNumeroSerie(item.getNumeroDeSerie());
            if (eq != null) {
                equipamentoService.atualizarEstoque(eq.getNumeroDeSerie(), eq.getQtdeEmEstoque() + item.getQuantidade());
            } else {
                System.out.println("Aviso: Equipamento de série " + item.getNumeroDeSerie() +
                                   " precisa ser cadastrado antes de atualizar a quantidade.");
            }
        }
    }

    public Compra buscarPorId(int id) {
        return dao.buscar(String.valueOf(id));
    }

    public List<Compra> listarTodas() {
        return dao.listar();
    }
}
