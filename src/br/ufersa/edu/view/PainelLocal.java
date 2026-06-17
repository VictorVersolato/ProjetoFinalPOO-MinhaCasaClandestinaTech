package br.ufersa.edu.view;

import br.ufersa.edu.Local;
import br.ufersa.edu.Responsavel;
import br.ufersa.edu.model.service.SistemaFacade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/** CRUD de Locais. Cada local pertence a um responsavel (combo). */
public class PainelLocal extends JFrame {

    private final SistemaFacade facade;
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Casa", "Compartimento", "Endereco", "Responsavel"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modelo);
    private final JTextField campoCasa = new JTextField(20);
    private final JTextField campoEndereco = new JTextField(20);
    private final JTextField campoCompartimento = new JTextField(20);
    private final JComboBox<Responsavel> comboResp = new JComboBox<>();
    private List<Local> dados;
    private int idSelecionado = 0;

    public PainelLocal(SistemaFacade facade) {
        super("Cadastro de Locais");
        this.facade = facade;
        montarTela();
        carregarResponsaveis();
        carregarTabela();
    }

    private void montarTela() {
        setSize(760, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Dados do local"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.anchor = GridBagConstraints.WEST;
        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Nome da casa:"), g);
        g.gridx = 1; form.add(campoCasa, g);
        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Compartimento:"), g);
        g.gridx = 1; form.add(campoCompartimento, g);
        g.gridx = 0; g.gridy = 2; form.add(new JLabel("Endereco:"), g);
        g.gridx = 1; form.add(campoEndereco, g);
        g.gridx = 0; g.gridy = 3; form.add(new JLabel("Responsavel:"), g);
        g.gridx = 1; form.add(comboResp, g);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bNovo = new JButton("Novo");
        JButton bSalvar = new JButton("Salvar");
        JButton bExcluir = new JButton("Excluir");
        botoes.add(bNovo); botoes.add(bSalvar); botoes.add(bExcluir);

        JPanel sul = new JPanel(new BorderLayout());
        sul.add(form, BorderLayout.CENTER);
        sul.add(botoes, BorderLayout.SOUTH);
        add(sul, BorderLayout.SOUTH);

        tabela.getSelectionModel().addListSelectionListener(e -> preencher());
        bNovo.addActionListener(e -> limpar());
        bSalvar.addActionListener(e -> salvar());
        bExcluir.addActionListener(e -> excluir());
    }

    private void carregarResponsaveis() {
        comboResp.removeAllItems();
        try {
            for (Responsavel r : facade.listarResponsaveis()) comboResp.addItem(r);
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        try {
            dados = facade.listarLocais();
            for (Local l : dados) {
                modelo.addRow(new Object[]{l.getId(), l.getNomeDaCasa(), l.getCompartimento(),
                        l.getEndereco(), l.getResponsavel().getNome()});
            }
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void preencher() {
        int linha = tabela.getSelectedRow();
        if (linha < 0 || dados == null) return;
        Local l = dados.get(linha);
        idSelecionado = l.getId();
        campoCasa.setText(l.getNomeDaCasa());
        campoCompartimento.setText(l.getCompartimento());
        campoEndereco.setText(l.getEndereco());
        selecionarResp(l.getResponsavel().getId());
    }

    private void selecionarResp(int id) {
        for (int i = 0; i < comboResp.getItemCount(); i++) {
            if (comboResp.getItemAt(i).getId() == id) { comboResp.setSelectedIndex(i); return; }
        }
    }

    private void limpar() {
        idSelecionado = 0;
        campoCasa.setText("");
        campoCompartimento.setText("");
        campoEndereco.setText("");
        tabela.clearSelection();
    }

    private void salvar() {
        try {
            Responsavel r = (Responsavel) comboResp.getSelectedItem();
            Local l = new Local(idSelecionado, campoCasa.getText(), campoEndereco.getText(),
                    campoCompartimento.getText(), r);
            if (idSelecionado == 0) facade.cadastrarLocal(l);
            else facade.editarLocal(l);
            carregarTabela();
            limpar();
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void excluir() {
        if (idSelecionado == 0) { erro("Selecione um local."); return; }
        if (JOptionPane.showConfirmDialog(this, "Excluir o local selecionado?",
                "Confirmacao", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            Responsavel r = (Responsavel) comboResp.getSelectedItem();
            Local l = new Local(idSelecionado, campoCasa.getText(), campoEndereco.getText(),
                    campoCompartimento.getText(), r);
            facade.removerLocal(l);
            carregarTabela();
            limpar();
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atencao", JOptionPane.WARNING_MESSAGE);
    }
}
