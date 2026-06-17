package br.ufersa.edu.model.DAO;

import br.ufersa.edu.*;

/**
 * ABSTRACT FACTORY (GoF): declara a criacao de uma familia de DAOs relacionados,
 * sem que o resto do sistema conheca a implementacao concreta (MySQL, arquivo, etc.).
 *
 * Por que mudou: o codigo original era uma "Simple Factory" (classe com metodos
 * estaticos retornando tipos concretos), padrao que NAO consta no livro GoF.
 * Esta versao retorna sempre a abstracao BaseDAO<E> e tem um ponto unico de troca
 * de implementacao (getFactory), o que e um padrao do livro e habilita polimorfismo.
 */
public abstract class DAOFactory {

    public abstract BaseDAO<Cliente> criarClienteDAO();
    public abstract BaseDAO<Equipamento> criarEquipamentoDAO();
    public abstract BaseDAO<Local> criarLocalDAO();
    public abstract BaseDAO<Responsavel> criarResponsavelDAO();
    public abstract BaseDAO<Venda> criarVendaDAO();
    public abstract BaseDAO<Compra> criarCompraDAO();
    public abstract BaseDAO<Funcionario> criarFuncionarioDAO();

    /** Ponto unico que define a familia de DAOs em uso. */
    public static DAOFactory getFactory() {
        return new MySQLDAOFactory();
    }
}
