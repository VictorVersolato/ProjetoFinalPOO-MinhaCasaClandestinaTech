package br.ufersa.edu.view;

import br.ufersa.edu.Equipamento;
import br.ufersa.edu.Local;
import br.ufersa.edu.Responsavel;
import br.ufersa.edu.model.service.SistemaFacade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * CRUD de Equipamentos e pesquisa por nome. O numero de serie e a chave do
 * equipamento; ao editar um existente ele fica bloqueado.
 */
public class PainelEquipamento extends JFrame {

    private final SistemaFacade facade;
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Serie", "Nome", "Preco", "Qtd", "Local", "Responsavel"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modelo);

    private final JTextField campoSerie = new JTextField(20);
    private final JTextField campoNome = new JTextField(20);
    private final JTextField campoPreco = new JTextField(20);
    private final JTextField campoQtd = new JTextField(20);
    private final JComboBox<Local> comboLocal = new JComboBox<>();
    private final JComboBox<Responsavel> comboResp = new JComboBox<>();
    private final JTextField campoPesquisa = new JTextField(16);

    private List<Equipamento> dados;
    private boolean editando = false;

    public PainelEquipamento(SistemaFacade facade) {
        super("Cadastro de Equipamentos");
        this.facade = facade;
        montarTela();
        carregarCombos();
        carregarTabela(null);
    }

    private void montarTela() {
        setSize(820, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton bPesquisar = new JButton("Pesquisar por nome");
        JButton bTodos = new JButton("Listar todos");
        topo.add(new JLabel("Nome:"));
        topo.add(campoPesquisa);
        topo.add(bPesquisar);
        topo.add(bTodos);
        add(topo, BorderLayout.NORTH);

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Dados do equipamento"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.anchor = GridBagConstraints.WEST;
        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Numero de serie:"), g);
        g.gridx = 1; form.add(campoSerie, g);
        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Nome:"), g);
        g.gridx = 1; form.add(campoNome, g);
        g.gridx = 0; g.gridy = 2; form.add(new JLabel("Preco:"), g);
        g.gridx = 1; form.add(campoPreco, g);
        g.gridx = 0; g.gridy = 3; form.add(new JLabel("Qtd em estoque:"), g);
        g.gridx = 1; form.add(campoQtd, g);
        g.gridx = 0; g.gridy = 4; form.add(new JLabel("Local:"), g);
        g.gridx = 1; form.add(comboLocal, g);
        g.gridx = 0; g.gridy = 5; form.add(new JLabel("Responsavel:"), g);
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
        bPesquisar.addActionListener(e -> carregarTabela(campoPesquisa.getText().trim()));
        bTodos.addActionListener(e -> { campoPesquisa.setText(""); carregarTabela(null); });
    }

    private void carregarCombos() {
        comboLocal.removeAllItems();
        comboResp.removeAllItems();
        try {
            for (Local l : facade.listarLocais()) comboLocal.addItem(l);
            for (Responsavel r : facade.listarResponsaveis()) comboResp.addItem(r);
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void carregarTabela(String filtroNome) {
        modelo.setRowCount(0);
        try {
            dados = (filtroNome == null || filtroNome.isEmpty())
                    ? facade.listarEquipamentos()
                    : facade.pesquisarEquipamentoPorNome(filtroNome);
            for (Equipamento e : dados) {
                modelo.addRow(new Object[]{e.getNumeroDeSerie(), e.getNome(),
                        String.format("%.2f", e.getPreco()), e.getQtdeEmEstoque(),
                        e.getLocal().getNomeDaCasa(), e.getResponsavel().getNome()});
            }
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void preencher() {
        int linha = tabela.getSelectedRow();
        if (linha < 0 || dados == null) return;
        Equipamento e = dados.get(linha);
        editando = true;
        campoSerie.setText(String.valueOf(e.getNumeroDeSerie()));
        campoSerie.setEditable(false);
        campoNome.setText(e.getNome());
        campoPreco.setText(String.valueOf(e.getPreco()));
        campoQtd.setText(String.valueOf(e.getQtdeEmEstoque()));
        selecionar(comboLocal, e.getLocal().getId());
        selecionar(comboResp, e.getResponsavel().getId());
    }

    private void selecionar(JComboBox<?> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            Object item = combo.getItemAt(i);
            int itemId = (item instanceof Local) ? ((Local) item).getId() : ((Responsavel) item).getId();
            if (itemId == id) { combo.setSelectedIndex(i); return; }
        }
    }

    private void limpar() {
        editando = false;
        campoSerie.setText("");
        campoSerie.setEditable(true);
        campoNome.setText("");
        campoPreco.setText("");
        campoQtd.setText("");
        tabela.clearSelection();
    }

    private void salvar() {
        try {
            int serie = Integer.parseInt(campoSerie.getText().trim());
            double preco = Double.parseDouble(campoPreco.getText().trim().replace(",", "."));
            int qtd = Integer.parseInt(campoQtd.getText().trim());
            Local local = (Local) comboLocal.getSelectedItem();
            Responsavel resp = (Responsavel) comboResp.getSelectedItem();
            Equipamento eq = new Equipamento(serie, campoNome.getText(), preco, qtd, local, resp);
            if (editando) facade.editarEquipamento(eq);
            else facade.cadastrarEquipamento(eq);
            carregarTabela(null);
            limpar();
        } catch (NumberFormatException ex) {
            erro("Serie, preco e quantidade devem ser numeros validos.");
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void excluir() {
        if (!editando) { erro("Selecione um equipamento."); return; }
        if (JOptionPane.showConfirmDialog(this, "Excluir o equipamento selecionado?",
                "Confirmacao", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            int serie = Integer.parseInt(campoSerie.getText().trim());
            Equipamento eq = facade.buscarEquipamento(serie);
            if (eq != null) facade.removerEquipamento(eq);
            carregarTabela(null);
            limpar();
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atencao", JOptionPane.WARNING_MESSAGE);
    }
}
