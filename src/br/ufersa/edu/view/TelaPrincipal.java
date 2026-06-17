package br.ufersa.edu.view;

import br.ufersa.edu.Funcionario;
import br.ufersa.edu.model.service.SistemaFacade;

import javax.swing.*;
import java.awt.*;

/**
 * Janela principal. Os botoes disponiveis dependem do perfil do usuario logado:
 * cadastro de funcionarios só aparece para o gerente (ADMIN).
 */
public class TelaPrincipal extends JFrame {

    private final SistemaFacade facade = new SistemaFacade();
    private final Funcionario usuarioLogado;

    public TelaPrincipal(Funcionario usuarioLogado) {
        super("MinhaCasaClandestinaTech - Painel Principal");
        this.usuarioLogado = usuarioLogado;
        montarTela();
    }

    private void montarTela() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(640, 420);
        setLocationRelativeTo(null);

        JPanel topo = new JPanel(new BorderLayout());
        topo.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        JLabel saudacao = new JLabel("Usuario: " + usuarioLogado.getNome()
                + "   |   Perfil: " + usuarioLogado.getPerfil());
        saudacao.setFont(saudacao.getFont().deriveFont(Font.BOLD, 14f));
        topo.add(saudacao, BorderLayout.WEST);

        JPanel grade = new JPanel(new GridLayout(0, 2, 12, 12));
        grade.setBorder(BorderFactory.createEmptyBorder(10, 16, 16, 16));

        grade.add(criarBotao("Clientes", e -> new PainelCliente(facade).setVisible(true)));
        grade.add(criarBotao("Equipamentos / Estoque", e -> new PainelEquipamento(facade).setVisible(true)));
        grade.add(criarBotao("Locais", e -> new PainelLocal(facade).setVisible(true)));
        grade.add(criarBotao("Responsaveis", e -> new PainelResponsavel(facade).setVisible(true)));
        grade.add(criarBotao("Compras", e -> new PainelCompra(facade).setVisible(true)));
        grade.add(criarBotao("Vendas", e -> new PainelVenda(facade).setVisible(true)));
        grade.add(criarBotao("Relatorios", e -> new PainelRelatorios(facade).setVisible(true)));

        // Botao restrito ao gerente
        if (usuarioLogado.isAdmin()) {
            grade.add(criarBotao("Cadastrar Funcionario", e ->
                new DialogCadastroFuncionario(facade, usuarioLogado).setVisible(true)));
        }

        JButton sair = new JButton("Sair");
        sair.addActionListener(e -> {
            new TelaLogin().setVisible(true);
            dispose();
        });

        add(topo, BorderLayout.NORTH);
        add(grade, BorderLayout.CENTER);
        add(sair, BorderLayout.SOUTH);
    }

    private JButton criarBotao(String texto, java.awt.event.ActionListener acao) {
        JButton b = new JButton(texto);
        b.setPreferredSize(new Dimension(260, 48));
        b.addActionListener(acao);
        return b;
    }
}
