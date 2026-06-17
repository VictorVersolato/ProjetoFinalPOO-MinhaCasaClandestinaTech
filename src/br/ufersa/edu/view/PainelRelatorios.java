package br.ufersa.edu.view;

import br.ufersa.edu.model.service.SistemaFacade;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * Tela de relatorios: estoque, estoque critico e vendas por periodo.
 */
public class PainelRelatorios extends JFrame {

    private final SistemaFacade facade;
    private final JTextArea area = new JTextArea();
    private final JTextField campoInicio = new JTextField("2026-01-01", 10);
    private final JTextField campoFim = new JTextField(LocalDate.now().toString(), 10);

    public PainelRelatorios(SistemaFacade facade) {
        super("Relatorios");
        this.facade = facade;
        montarTela();
    }

    private void montarTela() {
        setSize(640, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton bEstoque = new JButton("Estoque");
        JButton bCritico = new JButton("Estoque critico (<=5)");
        JButton bVendas = new JButton("Vendas no periodo");
        topo.add(bEstoque);
        topo.add(bCritico);
        topo.add(new JLabel("Inicio:"));
        topo.add(campoInicio);
        topo.add(new JLabel("Fim:"));
        topo.add(campoFim);
        topo.add(bVendas);
        add(topo, BorderLayout.NORTH);

        bEstoque.addActionListener(e -> mostrar(facade.relatorioEstoque()));
        bCritico.addActionListener(e -> mostrar(facade.relatorioEstoqueCritico(5)));
        bVendas.addActionListener(e -> {
            try {
                LocalDate ini = LocalDate.parse(campoInicio.getText().trim());
                LocalDate fim = LocalDate.parse(campoFim.getText().trim());
                mostrar(facade.relatorioVendas(ini, fim));
            } catch (Exception ex) {
                mostrar("Datas invalidas. Use o formato AAAA-MM-DD.");
            }
        });
    }

    private void mostrar(String texto) {
        try {
            area.setText(texto);
            area.setCaretPosition(0);
        } catch (RuntimeException ex) {
            area.setText("Erro ao gerar relatorio: " + ex.getMessage());
        }
    }
}
