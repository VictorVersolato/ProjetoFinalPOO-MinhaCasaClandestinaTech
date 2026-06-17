package br.ufersa.edu.model.DAO;

import br.ufersa.edu.Compra;
import br.ufersa.edu.Item;
import br.ufersa.edu.Responsavel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO extends AbstractDAO<Compra> {

    @Override
    public Compra inserir(Compra entity) {
        String sqlCompra = "INSERT INTO tb_compra (data_compra, id_responsavel) VALUES (?, ?)";
        String sqlItem = "INSERT INTO tb_item_compra (id_compra, numero_serie_equip, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
        try {
            int idCompra;
            try (PreparedStatement psCompra = getConnection().prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS)) {
                psCompra.setDate(1, Date.valueOf(entity.getData()));
                psCompra.setInt(2, entity.getResponsavel().getId());
                psCompra.executeUpdate();
                try (ResultSet rs = psCompra.getGeneratedKeys()) {
                    idCompra = rs.next() ? rs.getInt(1) : 0;
                }
            }
            entity.setId(idCompra);
            if (idCompra > 0) {
                try (PreparedStatement psItem = getConnection().prepareStatement(sqlItem)) {
                    for (Item item : entity.getItens()) {
                        psItem.setInt(1, idCompra);
                        psItem.setInt(2, item.getNumeroDeSerie());
                        psItem.setInt(3, item.getQuantidade());
                        psItem.setDouble(4, item.getPrecoUnitario());
                        psItem.addBatch();
                    }
                    psItem.executeBatch();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir compra: " + e.getMessage(), e);
        }
        return entity;
    }

    @Override
    public void alterar(Compra entity) {
        String sql = "UPDATE tb_compra SET data_compra = ?, id_responsavel = ? WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(entity.getData()));
            ps.setInt(2, entity.getResponsavel().getId());
            ps.setInt(3, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar compra: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletar(Compra entity) {
        try (PreparedStatement ps1 = getConnection().prepareStatement("DELETE FROM tb_item_compra WHERE id_compra = ?");
             PreparedStatement ps2 = getConnection().prepareStatement("DELETE FROM tb_compra WHERE id = ?")) {
            ps1.setInt(1, entity.getId());
            ps1.executeUpdate();
            ps2.setInt(1, entity.getId());
            ps2.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar compra: " + e.getMessage(), e);
        }
    }

    @Override
    public Compra buscar(String idParam) {
        String sqlCompra = "SELECT c.*, r.nome, r.endereco, r.telefone FROM tb_compra c " +
                           "INNER JOIN tb_responsavel r ON c.id_responsavel = r.id WHERE c.id = ?";
        try (PreparedStatement psCompra = getConnection().prepareStatement(sqlCompra)) {
            psCompra.setInt(1, Integer.parseInt(idParam));
            try (ResultSet rs = psCompra.executeQuery()) {
                if (rs.next()) {
                    Responsavel resp = new Responsavel(
                        rs.getInt("id_responsavel"),
                        rs.getString("nome"),
                        rs.getString("endereco"),
                        rs.getString("telefone")
                    );
                    Compra compra = new Compra(rs.getInt("id"), rs.getDate("data_compra").toLocalDate(), resp);
                    carregarItens(compra);
                    return compra;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar compra: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Compra> listar() {
        List<Compra> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT id FROM tb_compra ORDER BY data_compra");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Compra c = buscar(String.valueOf(rs.getInt("id")));
                if (c != null) {
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar compras: " + e.getMessage(), e);
        }
        return lista;
    }

    private void carregarItens(Compra compra) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM tb_item_compra WHERE id_compra = ?")) {
            ps.setInt(1, compra.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    compra.adicionarItem(new Item(
                        rs.getInt("numero_serie_equip"),
                        rs.getInt("quantidade"),
                        rs.getDouble("preco_unitario")
                    ));
                }
            }
        }
    }
}
