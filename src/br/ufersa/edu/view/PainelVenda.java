package br.ufersa.edu.view;

import br.ufersa.edu.Cliente;
import br.ufersa.edu.Item;
import br.ufersa.edu.Venda;
import br.ufersa.edu.exception.EstoqueInsuficienteException;
import br.ufersa.edu.model.service.SistemaFacade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Emissao de nota de venda. Valida o estoque ao finalizar (trata a
 * EstoqueInsuficienteException) e permite cancelar uma venda pelo seu ID.
 */
public class PainelVenda extends JFrame {

    private final SistemaFacade facade;
    private final JComboBox<Cliente> comboCliente = new JComboBox<>();
    private final JTextField campoSerie = new JTextField(8);
    private final JTextField campoQtd = new JTextField(5);
    private final JTextField campoPreco = new JTextField(8);
    private final JTextField campoCancelId = new JTextField(6);
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Serie", "Qtd", "Preco unit.", "Subtotal"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modelo);
    private final List<Item> itens = new ArrayList<>();
    private final JLabel total = new JLabel("Total: R$ 0,00");

    public PainelVenda(SistemaFacade facade) {
        super("Nota de Venda");
        this.facade = facade;
        montarTela();
        carregarClientes();
    }

    private void montarTela() {
        setSize(660, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel("Cliente:"));
        topo.add(comboCliente);
        topo.add(new JLabel("    Cancelar venda ID:"));
        topo.add(campoCancelId);
        JButton bCancelar = new JButton("Cancelar");
        topo.add(bCancelar);
        add(topo, BorderLayout.NORTH);

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel itemForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        itemForm.add(new JLabel("Serie:"));
        itemForm.add(campoSerie);
        itemForm.add(new JLabel("Qtd:"));
        itemForm.add(campoQtd);
        itemForm.add(new JLabel("Preco unit.:"));
        itemForm.add(campoPreco);
        JButton bAdd = new JButton("Adicionar item");
        itemForm.add(bAdd);
        itemForm.add(total);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bFinalizar = new JButton("Finalizar venda");
        JButton bLimpar = new JButton("Limpar");
        acoes.add(bLimpar);
        acoes.add(bFinalizar);

        JPanel sul = new JPanel(new BorderLayout());
        sul.add(itemForm, BorderLayout.CENTER);
        sul.add(acoes, BorderLayout.SOUTH);
        add(sul, BorderLayout.SOUTH);

        bAdd.addActionListener(e -> adicionarItem());
        bFinalizar.addActionListener(e -> finalizar());
        bLimpar.addActionListener(e -> limpar());
        bCancelar.addActionListener(e -> cancelar());
    }

    private void carregarClientes() {
        comboCliente.removeAllItems();
        try {
            for (Cliente c : facade.listarClientes()) comboCliente.addItem(c);
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void adicionarItem() {
        try {
            int serie = Integer.parseInt(campoSerie.getText().trim());
            int qtd = Integer.parseInt(campoQtd.getText().trim());
            double preco = Double.parseDouble(campoPreco.getText().trim().replace(",", "."));
            for (Item it : itens) {
                if (it.getNumeroDeSerie() == serie) { erro("Esse equipamento ja foi adicionado."); return; }
            }
            Item item = new Item(serie, qtd, preco);
            itens.add(item);
            modelo.addRow(new Object[]{serie, qtd, String.format("%.2f", preco),
                    String.format("%.2f", item.calcularSubtotal())});
            atualizarTotal();
            campoSerie.setText(""); campoQtd.setText(""); campoPreco.setText("");
        } catch (NumberFormatException ex) {
            erro("Serie, quantidade e preco devem ser numeros validos.");
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void finalizar() {
        if (itens.isEmpty()) { erro("Adicione ao menos um item."); return; }
        try {
            Cliente cliente = (Cliente) comboCliente.getSelectedItem();
            Venda venda = new Venda(0, LocalDate.now(), cliente);
            for (Item it : itens) venda.adicionarItem(it);
            facade.finalizarVenda(venda);
            JOptionPane.showMessageDialog(this, "Venda finalizada! Nota emitida e estoque atualizado.");
            limpar();
        } catch (EstoqueInsuficienteException ex) {
            erro(ex.getMessage());
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void cancelar() {
        try {
            int id = Integer.parseInt(campoCancelId.getText().trim());
            facade.cancelarVenda(new Venda(id, LocalDate.now(), null));
            JOptionPane.showMessageDialog(this, "Venda " + id + " cancelada e itens devolvidos ao estoque.");
            campoCancelId.setText("");
        } catch (NumberFormatException ex) {
            erro("Informe um ID de venda valido.");
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void atualizarTotal() {
        double soma = 0;
        for (Item it : itens) soma += it.calcularSubtotal();
        total.setText("   Total: R$ " + String.format("%.2f", soma));
    }

    private void limpar() {
        itens.clear();
        modelo.setRowCount(0);
        atualizarTotal();
        campoSerie.setText(""); campoQtd.setText(""); campoPreco.setText("");
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atencao", JOptionPane.WARNING_MESSAGE);
    }
}
