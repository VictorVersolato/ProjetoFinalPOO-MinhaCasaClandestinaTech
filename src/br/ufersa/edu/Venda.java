package br.ufersa.edu;

import java.time.LocalDate;

public class Venda extends Movimentacao {
    private Cliente cliente;
    private boolean cancelada;

    public Venda(int id, LocalDate data, Cliente cliente) {
        super(id, data);
        this.cliente = cliente;
        this.cancelada = false;
    }

    public Cliente getCliente() { return cliente; }
    public boolean isCancelada() { return cancelada; }

    public void cancelar() {
        this.cancelada = true;
    }

    @Override
    public String getDescricao() {
        return "Venda #" + id + " para " + (cliente != null ? cliente.getNome() : "cliente desconhecido");
    }
}
