package br.ufersa.edu.model.DAO;

import br.ufersa.edu.Funcionario;
import br.ufersa.edu.Funcionario.Perfil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO extends AbstractDAO<Funcionario> {

    @Override
    public Funcionario inserir(Funcionario entity) {
        String sql = "INSERT INTO tb_funcionario (nome, endereco, login, senha_hash, perfil) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getEndereco());
            ps.setString(3, entity.getLogin());
            ps.setString(4, entity.getSenhaHash());
            ps.setString(5, entity.getPerfil().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir funcionário: " + e.getMessage(), e);
        }
        return entity;
    }

    @Override
    public void alterar(Funcionario entity) {
        String sql = "UPDATE tb_funcionario SET nome = ?, endereco = ?, login = ?, senha_hash = ?, perfil = ? WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getEndereco());
            ps.setString(3, entity.getLogin());
            ps.setString(4, entity.getSenhaHash());
            ps.setString(5, entity.getPerfil().name());
            ps.setInt(6, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar funcionário: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletar(Funcionario entity) {
        String sql = "DELETE FROM tb_funcionario WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar funcionário: " + e.getMessage(), e);
        }
    }

    //Busca pelo login (usado na autenticacao)
    @Override
    public Funcionario buscar(String login) {
        String sql = "SELECT * FROM tb_funcionario WHERE login = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar funcionário: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Funcionario> listar() {
        String sql = "SELECT * FROM tb_funcionario ORDER BY nome";
        List<Funcionario> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar funcionários: " + e.getMessage(), e);
        }
        return lista;
    }

    private Funcionario montar(ResultSet rs) throws SQLException {
        return new Funcionario(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("endereco"),
            rs.getString("login"),
            rs.getString("senha_hash"),
            Perfil.valueOf(rs.getString("perfil"))
        );
    }
}
