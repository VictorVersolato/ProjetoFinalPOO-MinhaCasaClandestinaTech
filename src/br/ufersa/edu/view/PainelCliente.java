package br.ufersa.edu.view;

import br.ufersa.edu.Cliente;
import br.ufersa.edu.model.service.SistemaFacade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Tela de CRUD de Clientes. Serve de MODELO para as demais telas de cadastro
 * (Equipamentos, Locais, Responsaveis): tabela + formulario + acoes via Facade.
 */
public class PainelCliente extends JFrame {

    private final SistemaFacade facade;
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Nome", "Endereco", "CPF"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modelo);

    private final JTextField campoNome = new JTextField(20);
    private final JTextField campoEndereco = new JTextField(20);
    private final JTextField campoCpf = new JTextField(20);
    private int idSelecionado = 0;

    public PainelCliente(SistemaFacade facade) {
        super("Cadastro de Clientes");
        this.facade = facade;
        montarTela();
        carregarTabela();
    }

    private void montarTela() {
        setSize(720, 460);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Dados do cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; form.add(campoNome, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Endereco:"), gbc);
        gbc.gridx = 1; form.add(campoEndereco, gbc);
        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("CPF (11 digitos):"), gbc);
        gbc.gridx = 1; form.add(campoCpf, gbc);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bNovo = new JButton("Novo");
        JButton bSalvar = new JButton("Salvar");
        JButton bExcluir = new JButton("Excluir");
        botoes.add(bNovo);
        botoes.add(bSalvar);
        botoes.add(bExcluir);

        JPanel sul = new JPanel(new BorderLayout());
        sul.add(form, BorderLayout.CENTER);
        sul.add(botoes, BorderLayout.SOUTH);
        add(sul, BorderLayout.SOUTH);

        tabela.getSelectionModel().addListSelectionListener(e -> preencherFormulario());
        bNovo.addActionListener(e -> limparFormulario());
        bSalvar.addActionListener(e -> salvar());
        bExcluir.addActionListener(e -> excluir());
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        try {
            List<Cliente> clientes = facade.listarClientes();
            for (Cliente c : clientes) {
                modelo.addRow(new Object[]{c.getId(), c.getNome(), c.getEndereco(), c.getCpf()});
            }
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void preencherFormulario() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        idSelecionado = ((Integer) modelo.getValueAt(linha, 0)).intValue();
        campoNome.setText((String) modelo.getValueAt(linha, 1));
        campoEndereco.setText((String) modelo.getValueAt(linha, 2));
        campoCpf.setText(String.valueOf(modelo.getValueAt(linha, 3)));
    }

    private void limparFormulario() {
        idSelecionado = 0;
        campoNome.setText("");
        campoEndereco.setText("");
        campoCpf.setText("");
        tabela.clearSelection();
    }

    private void salvar() {
        try {
            Cliente c = new Cliente(idSelecionado, campoNome.getText(), campoEndereco.getText(), campoCpf.getText());
            if (idSelecionado == 0) {
                facade.cadastrarCliente(c);
            } else {
                facade.editarCliente(c);
            }
            carregarTabela();
            limparFormulario();
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void excluir() {
        if (idSelecionado == 0) {
            erro("Selecione um cliente na tabela.");
            return;
        }
        int opc = JOptionPane.showConfirmDialog(this, "Excluir o cliente selecionado?",
                "Confirmacao", JOptionPane.YES_NO_OPTION);
        if (opc != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            Cliente c = new Cliente(idSelecionado, campoNome.getText(), campoEndereco.getText(), campoCpf.getText());
            facade.removerCliente(c);
            carregarTabela();
            limparFormulario();
        } catch (RuntimeException ex) {
            erro(ex.getMessage());
        }
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atencao", JOptionPane.WARNING_MESSAGE);
    }
}
