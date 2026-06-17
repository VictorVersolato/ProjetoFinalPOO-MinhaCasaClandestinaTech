package br.ufersa.edu.model.DAO;

import br.ufersa.edu.*;

//Implementação concreta da Abstract Factory para o banco.

public class MySQLDAOFactory extends DAOFactory {

    @Override public BaseDAO<Cliente> criarClienteDAO() { return new ClienteDAO(); }
    @Override public BaseDAO<Equipamento> criarEquipamentoDAO() { return new EquipamentoDAO(); }
    @Override public BaseDAO<Local> criarLocalDAO() { return new LocalDAO(); }
    @Override public BaseDAO<Responsavel> criarResponsavelDAO() { return new ResponsavelDAO(); }
    @Override public BaseDAO<Venda> criarVendaDAO() { return new VendaDAO(); }
    @Override public BaseDAO<Compra> criarCompraDAO() { return new CompraDAO(); }
    @Override public BaseDAO<Funcionario> criarFuncionarioDAO() { return new FuncionarioDAO(); }
}
