package br.ufersa.edu.model.DAO;

import java.sql.Connection;

/**
 * Classe ABSTRATA base dos DAOs. Centraliza o acesso a conexao (via Singleton),
 * eliminando a duplicacao e o erro do codigo original, onde alguns DAOs chamavam
 * BaseDAO.getConnection() (metodo que nao existia na interface e nao compilava).
 */
public abstract class AbstractDAO<E> implements BaseDAO<E> {

    protected Connection getConnection() {
        return SingletonConexao.getInstancia();
    }
}
