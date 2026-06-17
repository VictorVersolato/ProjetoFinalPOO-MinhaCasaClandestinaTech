package br.ufersa.edu.model.DAO;

import br.ufersa.edu.Venda;
import br.ufersa.edu.Item;
import br.ufersa.edu.Cliente;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO extends AbstractDAO<Venda> {

    @Override
    public Venda inserir(Venda entity) {
        String sqlVenda = "INSERT INTO tb_venda (data_venda, id_cliente) VALUES (?, ?)";
        String sqlItem = "INSERT INTO tb_item_venda (id_venda, numero_serie_equip, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
        try {
            int idVenda;
            try (PreparedStatement psVenda = getConnection().prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS)) {
                psVenda.setDate(1, Date.valueOf(entity.getData()));
                psVenda.setInt(2, entity.getCliente().getId());
                psVenda.executeUpdate();
                try (ResultSet rs = psVenda.getGeneratedKeys()) {
                    idVenda = rs.next() ? rs.getInt(1) : 0;
                }
            }
            entity.setId(idVenda);
            if (idVenda > 0) {
                try (PreparedStatement psItem = getConnection().prepareStatement(sqlItem)) {
                    for (Item item : entity.getItens()) {
                        psItem.setInt(1, idVenda);
                        psItem.setInt(2, item.getNumeroDeSerie());
                        psItem.setInt(3, item.getQuantidade());
                        psItem.setDouble(4, item.getPrecoUnitario());
                        psItem.addBatch();
                    }
                    psItem.executeBatch();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir venda: " + e.getMessage(), e);
        }
        return entity;
    }

    @Override
    public void alterar(Venda entity) {
        String sql = "UPDATE tb_venda SET data_venda = ?, id_cliente = ? WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(entity.getData()));
            ps.setInt(2, entity.getCliente().getId());
            ps.setInt(3, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar venda: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletar(Venda entity) {
        try (PreparedStatement ps1 = getConnection().prepareStatement("DELETE FROM tb_item_venda WHERE id_venda = ?");
             PreparedStatement ps2 = getConnection().prepareStatement("DELETE FROM tb_venda WHERE id = ?")) {
            ps1.setInt(1, entity.getId());
            ps1.executeUpdate();
            ps2.setInt(1, entity.getId());
            ps2.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar venda: " + e.getMessage(), e);
        }
    }

    @Override
    public Venda buscar(String idParam) {
        String sqlVenda = "SELECT v.*, c.nome, c.endereco, c.cpf FROM tb_venda v " +
                          "INNER JOIN tb_cliente c ON v.id_cliente = c.id WHERE v.id = ?";
        try (PreparedStatement psVenda = getConnection().prepareStatement(sqlVenda)) {
            psVenda.setInt(1, Integer.parseInt(idParam));
            try (ResultSet rs = psVenda.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nome"),
                        rs.getString("endereco"),
                        rs.getString("cpf")
                    );
                    Venda venda = new Venda(rs.getInt("id"), rs.getDate("data_venda").toLocalDate(), cliente);
                    carregarItens(venda);
                    return venda;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar venda: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Venda> listar() {
        List<Venda> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT id FROM tb_venda ORDER BY data_venda");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Venda v = buscar(String.valueOf(rs.getInt("id")));
                if (v != null) {
                    lista.add(v);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vendas: " + e.getMessage(), e);
        }
        return lista;
    }

    //usado no relatorio por período:
    public List<Venda> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        List<Venda> lista = new ArrayList<>();
        String sql = "SELECT id FROM tb_venda WHERE data_venda BETWEEN ? AND ? ORDER BY data_venda";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fim));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Venda v = buscar(String.valueOf(rs.getInt("id")));
                    if (v != null) {
                        lista.add(v);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vendas por periodo: " + e.getMessage(), e);
        }
        return lista;
    }

    private void carregarItens(Venda venda) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM tb_item_venda WHERE id_venda = ?")) {
            ps.setInt(1, venda.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    venda.adicionarItem(new Item(
                        rs.getInt("numero_serie_equip"),
                        rs.getInt("quantidade"),
                        rs.getDouble("preco_unitario")
                    ));
                }
            }
        }
    }
}
