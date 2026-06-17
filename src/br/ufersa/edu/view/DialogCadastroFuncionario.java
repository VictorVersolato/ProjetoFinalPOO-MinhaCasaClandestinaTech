package br.ufersa.edu.view;

import br.ufersa.edu.Funcionario;
import br.ufersa.edu.Funcionario.Perfil;
import br.ufersa.edu.exception.AutenticacaoException;
import br.ufersa.edu.model.service.SistemaFacade;

import javax.swing.*;
import java.awt.*;

/**
 * Cadastro de funcionarios (usuarios do sistema). So e aberto pelo gerente (ADMIN).
 */
public class DialogCadastroFuncionario extends JFrame {

    private final SistemaFacade facade;
    private final Funcionario solicitante;

    private final JTextField campoNome = new JTextField(20);
    private final JTextField campoEndereco = new JTextField(20);
    private final JTextField campoLogin = new JTextField(20);
    private final JPasswordField campoSenha = new JPasswordField(20);
    private final JComboBox<Perfil> campoPerfil = new JComboBox<>(Perfil.values());

    public DialogCadastroFuncionario(SistemaFacade facade, Funcionario solicitante) {
        super("Cadastrar Funcionario");
        this.facade = facade;
        this.solicitante = solicitante;
        montarTela();
    }

    private void montarTela() {
        setSize(420, 280);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; form.add(campoNome, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Endereco:"), gbc);
        gbc.gridx = 1; form.add(campoEndereco, gbc);
        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Login:"), gbc);
        gbc.gridx = 1; form.add(campoLogin, gbc);
        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; form.add(campoSenha, gbc);
        gbc.gridx = 0; gbc.gridy = 4; form.add(new JLabel("Perfil:"), gbc);
        gbc.gridx = 1; form.add(campoPerfil, gbc);

        JButton salvar = new JButton("Salvar");
        gbc.gridx = 1; gbc.gridy = 5; gbc.anchor = GridBagConstraints.EAST;
        form.add(salvar, gbc);

        add(form);
        salvar.addActionListener(e -> salvar());
    }

    private void salvar() {
        try {
            facade.cadastrarFuncionario(
                campoNome.getText(),
                campoEndereco.getText(),
                campoLogin.getText(),
                new String(campoSenha.getPassword()),
                (Perfil) campoPerfil.getSelectedItem(),
                solicitante
            );
            JOptionPane.showMessageDialog(this, "Funcionario cadastrado com sucesso!");
            dispose();
        } catch (AutenticacaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Acesso negado", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atencao", JOptionPane.WARNING_MESSAGE);
        }
    }
}
