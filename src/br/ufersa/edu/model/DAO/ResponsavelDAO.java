package br.ufersa.edu.model.DAO;

import br.ufersa.edu.Responsavel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResponsavelDAO extends AbstractDAO<Responsavel> {

    @Override
    public Responsavel inserir(Responsavel entity) {
        String sql = "INSERT INTO tb_responsavel (nome, endereco, telefone) VALUES (?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getEndereco());
            ps.setString(3, entity.getTelefone());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir responsável: " + e.getMessage(), e);
        }
        return entity;
    }

    @Override
    public void alterar(Responsavel entity) {
        String sql = "UPDATE tb_responsavel SET nome = ?, endereco = ?, telefone = ? WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getEndereco());
            ps.setString(3, entity.getTelefone());
            ps.setInt(4, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar responsável: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletar(Responsavel entity) {
        String sql = "DELETE FROM tb_responsavel WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar responsável: " + e.getMessage(), e);
        }
    }

    @Override
    public Responsavel buscar(String nomeParam) {
        String sql = "SELECT * FROM tb_responsavel WHERE nome LIKE ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + nomeParam + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar responsável: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Responsavel> listar() {
        String sql = "SELECT * FROM tb_responsavel ORDER BY nome";
        List<Responsavel> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar responsáveis: " + e.getMessage(), e);
        }
        return lista;
    }

    private Responsavel montar(ResultSet rs) throws SQLException {
        return new Responsavel(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("endereco"),
            rs.getString("telefone")
        );
    }
}
