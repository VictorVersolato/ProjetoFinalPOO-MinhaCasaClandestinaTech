package br.ufersa.edu.view;

import br.ufersa.edu.Compra;
import br.ufersa.edu.Item;
import br.ufersa.edu.Responsavel;
import br.ufersa.edu.model.service.SistemaFacade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Registro de compra (entrada de equipamentos no estoque). */
public class PainelCompra extends JFrame {

    private final SistemaFacade facade;
    private final JComboBox<Responsavel> comboResp = new JComboBox<>();
    private final JTextField campoSerie = new JTextField(8);
    private final JTextField campoQtd = new JTextField(5);
    private final JTextField campoPreco = new JTextField(8);
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Serie", "Qtd", "Preco unit.", "Subtotal"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modelo);
    private final List<Item> itens = new ArrayList<>();

    public PainelCompra(SistemaFacade facade) {
        super("Registrar Compra");
        this.facade = facade;
        montarTela();
        carregarResponsaveis();
    }

    private void montarTela() {
        setSize(640, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel("Responsavel:"));
        topo.add(comboResp);
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

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bRegistrar = new JButton("Registrar compra");
        JButton bLimpar = new JButton("Limpar");
        acoes.add(bLimpar);
        acoes.add(bRegistrar);

        JPanel sul = new JPanel(new BorderLayout());
        sul.add(itemForm, BorderLayout.CENTER);
        sul.add(acoes, BorderLayout.SOUTH);
        add(sul, BorderLayout.SOUTH);

        bAdd.addActionListener(e -> adicionarItem());
        bRegistrar.addActionListener(e -> registrar());
        bLimpar.addActionListener(e -> limpar());
    }

    private void carregarResponsaveis() {
        comboResp.removeAllItems();
        try {
            for (Responsavel r : facade.listarResponsaveis()) comboResp.addItem(r);
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
            campoSerie.setText(""); campoQtd.setText(""); campoPreco.setText("");
        } catch (NumberFormatException ex) {
            erro("Serie, quantidade e preco devem ser numeros validos.");
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void registrar() {
        if (itens.isEmpty()) { erro("Adicione ao menos um item."); return; }
        try {
            Responsavel r = (Responsavel) comboResp.getSelectedItem();
            Compra compra = new Compra(0, LocalDate.now(), r);
            for (Item it : itens) compra.adicionarItem(it);
            facade.registrarCompra(compra);
            JOptionPane.showMessageDialog(this, "Compra registrada e estoque incrementado!");
            limpar();
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void limpar() {
        itens.clear();
        modelo.setRowCount(0);
        campoSerie.setText(""); campoQtd.setText(""); campoPreco.setText("");
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atencao", JOptionPane.WARNING_MESSAGE);
    }
}
