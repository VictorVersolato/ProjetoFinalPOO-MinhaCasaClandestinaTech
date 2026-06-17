package br.ufersa.edu.view;

import br.ufersa.edu.Funcionario;
import br.ufersa.edu.exception.AutenticacaoException;
import br.ufersa.edu.model.service.SistemaFacade;

import javax.swing.*;
import java.awt.*;

/**
 * Tela de login. So permite o uso do sistema por usuarios cadastrados.
 */
public class TelaLogin extends JFrame {

    private final SistemaFacade facade = new SistemaFacade();
    private final JTextField campoLogin = new JTextField(18);
    private final JPasswordField campoSenha = new JPasswordField(18);

    public TelaLogin() {
        super("MinhaCasaClandestinaTech - Login");
        montarTela();
    }

    private void montarTela() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(380, 220);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titulo = new JLabel("Controle de Estoque");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        painel.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("Login:"), gbc);
        gbc.gridx = 1;
        painel.add(campoLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        painel.add(campoSenha, gbc);

        JButton botaoEntrar = new JButton("Entrar");
        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        painel.add(botaoEntrar, gbc);

        add(painel);

        botaoEntrar.addActionListener(e -> autenticar());
        getRootPane().setDefaultButton(botaoEntrar);
    }

    private void autenticar() {
        String login = campoLogin.getText().trim();
        String senha = new String(campoSenha.getPassword());
        try {
            Funcionario usuario = facade.login(login, senha);
            JOptionPane.showMessageDialog(this, "Bem-vindo, " + usuario.getNome() + "!");
            new TelaPrincipal(usuario).setVisible(true);
            dispose();
        } catch (AutenticacaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Falha no login", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao acessar o banco de dados:\n" + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
