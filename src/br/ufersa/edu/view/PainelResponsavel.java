package br.ufersa.edu.view;

import br.ufersa.edu.Responsavel;
import br.ufersa.edu.model.service.SistemaFacade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/** CRUD de Responsaveis (donos das casas). */
public class PainelResponsavel extends JFrame {

    private final SistemaFacade facade;
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Nome", "Endereco", "Telefone"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modelo);
    private final JTextField campoNome = new JTextField(20);
    private final JTextField campoEndereco = new JTextField(20);
    private final JTextField campoTelefone = new JTextField(20);
    private List<Responsavel> dados;
    private int idSelecionado = 0;

    public PainelResponsavel(SistemaFacade facade) {
        super("Cadastro de Responsaveis");
        this.facade = facade;
        montarTela();
        carregarTabela();
    }

    private void montarTela() {
        setSize(700, 460);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Dados do responsavel"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.anchor = GridBagConstraints.WEST;
        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Nome:"), g);
        g.gridx = 1; form.add(campoNome, g);
        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Endereco:"), g);
        g.gridx = 1; form.add(campoEndereco, g);
        g.gridx = 0; g.gridy = 2; form.add(new JLabel("Telefone:"), g);
        g.gridx = 1; form.add(campoTelefone, g);

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

    private void carregarTabela() {
        modelo.setRowCount(0);
        try {
            dados = facade.listarResponsaveis();
            for (Responsavel r : dados) {
                modelo.addRow(new Object[]{r.getId(), r.getNome(), r.getEndereco(), r.getTelefone()});
            }
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void preencher() {
        int linha = tabela.getSelectedRow();
        if (linha < 0 || dados == null) return;
        Responsavel r = dados.get(linha);
        idSelecionado = r.getId();
        campoNome.setText(r.getNome());
        campoEndereco.setText(r.getEndereco());
        campoTelefone.setText(r.getTelefone());
    }

    private void limpar() {
        idSelecionado = 0;
        campoNome.setText("");
        campoEndereco.setText("");
        campoTelefone.setText("");
        tabela.clearSelection();
    }

    private void salvar() {
        try {
            Responsavel r = new Responsavel(idSelecionado, campoNome.getText(), campoEndereco.getText(), campoTelefone.getText());
            if (idSelecionado == 0) facade.cadastrarResponsavel(r);
            else facade.editarResponsavel(r);
            carregarTabela();
            limpar();
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void excluir() {
        if (idSelecionado == 0) { erro("Selecione um responsavel."); return; }
        if (JOptionPane.showConfirmDialog(this, "Excluir o responsavel selecionado?",
                "Confirmacao", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            Responsavel r = new Responsavel(idSelecionado, campoNome.getText(), campoEndereco.getText(), campoTelefone.getText());
            facade.removerResponsavel(r);
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
