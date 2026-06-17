package br.ufersa.edu.model.DAO;

import br.ufersa.edu.Equipamento;
import br.ufersa.edu.Local;
import br.ufersa.edu.Responsavel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipamentoDAO extends AbstractDAO<Equipamento> {

    private static final String SELECT_BASE =
        "SELECT eq.*, l.nome_casa, l.endereco AS end_local, l.compartimento, " +
        "r.nome AS nome_resp, r.endereco AS end_resp, r.telefone AS tel_resp " +
        "FROM tb_equipamento eq " +
        "INNER JOIN tb_local l ON eq.id_local = l.id " +
        "INNER JOIN tb_responsavel r ON eq.id_responsavel = r.id ";

    @Override
    public Equipamento inserir(Equipamento entity) {
        String sql = "INSERT INTO tb_equipamento (numero_serie, nome, preco, qtde_estoque, id_local, id_responsavel) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, entity.getNumeroDeSerie());
            ps.setString(2, entity.getNome());
            ps.setDouble(3, entity.getPreco());
            ps.setInt(4, entity.getQtdeEmEstoque());
            ps.setInt(5, entity.getLocal().getId());
            ps.setInt(6, entity.getResponsavel().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir equipamento: " + e.getMessage(), e);
        }
        return entity;
    }

    @Override
    public void alterar(Equipamento entity) {
        String sql = "UPDATE tb_equipamento SET nome = ?, preco = ?, qtde_estoque = ?, id_local = ?, id_responsavel = ? " +
                     "WHERE numero_serie = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, entity.getNome());
            ps.setDouble(2, entity.getPreco());
            ps.setInt(3, entity.getQtdeEmEstoque());
            ps.setInt(4, entity.getLocal().getId());
            ps.setInt(5, entity.getResponsavel().getId());
            ps.setInt(6, entity.getNumeroDeSerie());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar equipamento: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletar(Equipamento entity) {
        String sql = "DELETE FROM tb_equipamento WHERE numero_serie = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, entity.getNumeroDeSerie());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar equipamento: " + e.getMessage(), e);
        }
    }

    @Override
    public Equipamento buscar(String numeroSerieParam) {
        String sql = SELECT_BASE + "WHERE eq.numero_serie = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(numeroSerieParam));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar equipamento: " + e.getMessage(), e);
        }
        return null;
    }

    /** Busca por parte do nome (atende ao requisito de pesquisa por nome). */
    public List<Equipamento> buscarPorNome(String nomeParam) {
        String sql = SELECT_BASE + "WHERE eq.nome LIKE ?";
        List<Equipamento> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + nomeParam + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar equipamento por nome: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Equipamento> listar() {
        List<Equipamento> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_BASE + "ORDER BY eq.nome");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar equipamentos: " + e.getMessage(), e);
        }
        return lista;
    }

    private Equipamento montar(ResultSet rs) throws SQLException {
        Responsavel resp = new Responsavel(
            rs.getInt("id_responsavel"),
            rs.getString("nome_resp"),
            rs.getString("end_resp"),
            rs.getString("tel_resp")
        );
        Local local = new Local(
            rs.getInt("id_local"),
            rs.getString("nome_casa"),
            rs.getString("end_local"),
            rs.getString("compartimento"),
            resp
        );
        return new Equipamento(
            rs.getInt("numero_serie"),
            rs.getString("nome"),
            rs.getDouble("preco"),
            rs.getInt("qtde_estoque"),
            local,
            resp
        );
    }
}
