package br.ufersa.edu.model.DAO;

import br.ufersa.edu.Local;
import br.ufersa.edu.Responsavel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocalDAO extends AbstractDAO<Local> {

    @Override
    public Local inserir(Local entity) {
        String sql = "INSERT INTO tb_local (nome_casa, endereco, compartimento, id_responsavel) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getNomeDaCasa());
            ps.setString(2, entity.getEndereco());
            ps.setString(3, entity.getCompartimento());
            ps.setInt(4, entity.getResponsavel().getId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir local: " + e.getMessage(), e);
        }
        return entity;
    }

    @Override
    public void alterar(Local entity) {
        String sql = "UPDATE tb_local SET nome_casa = ?, endereco = ?, compartimento = ?, id_responsavel = ? WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, entity.getNomeDaCasa());
            ps.setString(2, entity.getEndereco());
            ps.setString(3, entity.getCompartimento());
            ps.setInt(4, entity.getResponsavel().getId());
            ps.setInt(5, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar local: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletar(Local entity) {
        String sql = "DELETE FROM tb_local WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar local: " + e.getMessage(), e);
        }
    }

    @Override
    public Local buscar(String nomeCasaParam) {
        String sql = "SELECT l.*, r.nome AS nome_resp, r.endereco AS end_resp, r.telefone AS tel_resp " +
                     "FROM tb_local l INNER JOIN tb_responsavel r ON l.id_responsavel = r.id " +
                     "WHERE l.nome_casa LIKE ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + nomeCasaParam + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar local: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Local> listar() {
        String sql = "SELECT l.*, r.nome AS nome_resp, r.endereco AS end_resp, r.telefone AS tel_resp " +
                     "FROM tb_local l INNER JOIN tb_responsavel r ON l.id_responsavel = r.id ORDER BY l.nome_casa";
        List<Local> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar locais: " + e.getMessage(), e);
        }
        return lista;
    }

    private Local montar(ResultSet rs) throws SQLException {
        Responsavel resp = new Responsavel(
            rs.getInt("id_responsavel"),
            rs.getString("nome_resp"),
            rs.getString("end_resp"),
            rs.getString("tel_resp")
        );
        return new Local(
            rs.getInt("id"),
            rs.getString("nome_casa"),
            rs.getString("endereco"),
            rs.getString("compartimento"),
            resp
        );
    }
}
