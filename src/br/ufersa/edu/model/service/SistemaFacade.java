package br.ufersa.edu.model.service;

import br.ufersa.edu.*;
import br.ufersa.edu.Funcionario.Perfil;
import br.ufersa.edu.exception.AutenticacaoException;
import br.ufersa.edu.exception.EstoqueInsuficienteException;

import java.time.LocalDate;
import java.util.List;

public class SistemaFacade {
    private final AutenticacaoService autenticacaoService = new AutenticacaoService();
    private final ClienteService clienteService = new ClienteService();
    private final EquipamentoService equipamentoService = new EquipamentoService();
    private final VendaService vendaService = new VendaService();
    private final CompraService compraService = new CompraService();
    private final RelatorioService relatorioService = new RelatorioService();
    private final LocalService localService = new LocalService();
    private final ResponsavelService responsavelService = new ResponsavelService();

    //Autenticacao / Usuarios
    public Funcionario login(String login, String senha) throws AutenticacaoException {
        return autenticacaoService.login(login, senha);
    }

    public Funcionario cadastrarFuncionario(String nome, String endereco, String login,
                                            String senha, Perfil perfil, Funcionario solicitante)
            throws AutenticacaoException {
        return autenticacaoService.cadastrarFuncionario(nome, endereco, login, senha, perfil, solicitante);
    }

    public List<Funcionario> listarFuncionarios() {
        return autenticacaoService.listarFuncionarios();
    }

    //Cliente
    public void cadastrarCliente(Cliente c) { clienteService.cadastrar(c); }
    public void editarCliente(Cliente c) { clienteService.editar(c); }
    public void removerCliente(Cliente c) { clienteService.remover(c); }
    public Cliente buscarClientePorCpf(String cpf) { return clienteService.buscarPorCpf(cpf); }
    public List<Cliente> listarClientes() { return clienteService.listarTodos(); }

    //Equipamento
    public void cadastrarEquipamento(Equipamento eq) { equipamentoService.cadastrar(eq); }
    public void editarEquipamento(Equipamento eq) { equipamentoService.editar(eq); }
    public void removerEquipamento(Equipamento eq) { equipamentoService.remover(eq); }
    public Equipamento buscarEquipamento(int numeroSerie) { return equipamentoService.buscarPorNumeroSerie(numeroSerie); }
    public List<Equipamento> pesquisarEquipamentoPorNome(String nome) { return equipamentoService.buscarPorNome(nome); }
    public List<Equipamento> listarEquipamentos() { return equipamentoService.listarTodos(); }

    //Local
    public void cadastrarLocal(Local local) { localService.cadastrar(local); }
    public void editarLocal(Local local) { localService.editar(local); }
    public void removerLocal(Local local) { localService.remover(local); }
    public List<Local> listarLocais() { return localService.listarTodos(); }

    //Responsavel
    public void cadastrarResponsavel(Responsavel r) { responsavelService.salvar(r); }
    public void editarResponsavel(Responsavel r) { responsavelService.editar(r); }
    public void removerResponsavel(Responsavel r) { responsavelService.remover(r); }
    public List<Responsavel> listarResponsaveis() { return responsavelService.listarTodos(); }

    //Compra / Venda
    public void registrarCompra(Compra compra) { compraService.registrarCompra(compra); }
    public void finalizarVenda(Venda venda) throws EstoqueInsuficienteException { vendaService.finalizarVenda(venda); }
    public void cancelarVenda(Venda venda) { vendaService.cancelarVenda(venda); }
    public void alterarVenda(Venda venda) { vendaService.alterarVenda(venda); }
    public List<Venda> listarVendas() { return vendaService.listarTodas(); }

    //Relatorios
    public String relatorioVendas(LocalDate inicio, LocalDate fim) {
        return relatorioService.gerarRelatorioVendas(inicio, fim);
    }
    public String relatorioEstoque() {
        return relatorioService.gerarRelatorioEstoque();
    }
    public String relatorioEstoqueCritico(int limite) {
        return relatorioService.gerarRelatorioEstoqueCritico(limite);
    }
}
