package br.ufersa.edu.model.service;

import br.ufersa.edu.Venda;
import br.ufersa.edu.Item;
import br.ufersa.edu.Equipamento;
import br.ufersa.edu.model.DAO.BaseDAO;
import br.ufersa.edu.model.DAO.DAOFactory;

import java.time.LocalDate;
import java.util.List;

public class RelatorioService {

    private final BaseDAO<Venda> vendaDAO = DAOFactory.getFactory().criarVendaDAO();
    private final BaseDAO<Equipamento> equipamentoDAO = DAOFactory.getFactory().criarEquipamentoDAO();

    /**
     * Relatorio de vendas por periodo COM detalhamento dos equipamentos.
     * CORRIGIDO: agora filtra de fato pela data (o codigo original nunca filtrava).
     */
    public String gerarRelatorioVendas(LocalDate inicio, LocalDate fim) {
        StringBuilder sb = new StringBuilder();
        sb.append("RELATORIO DE VENDAS (").append(inicio).append(" a ").append(fim).append(")\n");
        sb.append("------------------------------------------------------------\n");

        double totalGeral = 0;
        int contador = 0;
        for (Venda v : vendaDAO.listar()) {
            LocalDate data = v.getData();
            boolean dentroDoPeriodo = !data.isBefore(inicio) && !data.isAfter(fim);
            if (!dentroDoPeriodo) {
                continue;
            }
            contador++;
            sb.append("Venda #").append(v.getId())
              .append(" | ").append(data)
              .append(" | Cliente: ").append(v.getCliente().getNome())
              .append(" | Total: R$ ").append(String.format("%.2f", v.calcularTotal())).append("\n");

            for (Item item : v.getItens()) {
                sb.append("    - Serie ").append(item.getNumeroDeSerie())
                  .append(" x").append(item.getQuantidade())
                  .append(" @ R$ ").append(String.format("%.2f", item.getPrecoUnitario()))
                  .append(" = R$ ").append(String.format("%.2f", item.calcularSubtotal())).append("\n");
            }
            totalGeral += v.calcularTotal();
        }

        if (contador == 0) {
            sb.append("Nenhuma venda no periodo.\n");
        }
        sb.append("------------------------------------------------------------\n");
        sb.append("Vendas no periodo: ").append(contador).append("\n");
        sb.append("Faturamento total: R$ ").append(String.format("%.2f", totalGeral)).append("\n");
        return sb.toString();
    }

    /** Relatorio geral do estoque. */
    public String gerarRelatorioEstoque() {
        StringBuilder sb = new StringBuilder();
        sb.append("RELATORIO DE ESTOQUE\n");
        sb.append("------------------------------------------------------------\n");
        List<Equipamento> equipamentos = equipamentoDAO.listar();
        if (equipamentos.isEmpty()) {
            sb.append("Estoque vazio.\n");
        }
        double valorTotal = 0;
        for (Equipamento e : equipamentos) {
            sb.append(e.toString())
              .append(" | Local: ").append(e.getLocal().getNomeDaCasa())
              .append(" | Resp.: ").append(e.getResponsavel().getNome()).append("\n");
            valorTotal += e.getPreco() * e.getQtdeEmEstoque();
        }
        sb.append("------------------------------------------------------------\n");
        sb.append("Valor total imobilizado: R$ ").append(String.format("%.2f", valorTotal)).append("\n");
        return sb.toString();
    }

    /** Lista de equipamentos com estoque igual ou abaixo do limite. */
    public String gerarRelatorioEstoqueCritico(int limiteMinimo) {
        StringBuilder sb = new StringBuilder();
        sb.append("ESTOQUE CRITICO (abaixo de ").append(limiteMinimo).append(" unidades)\n");
        sb.append("------------------------------------------------------------\n");
        boolean nenhum = true;
        for (Equipamento e : equipamentoDAO.listar()) {
            if (e.getQtdeEmEstoque() <= limiteMinimo) {
                sb.append("ALERTA: ").append(e.toString()).append("\n");
                nenhum = false;
            }
        }
        if (nenhum) {
            sb.append("Tudo sob controle. Nenhum equipamento com estoque baixo.\n");
        }
        return sb.toString();
    }
}
