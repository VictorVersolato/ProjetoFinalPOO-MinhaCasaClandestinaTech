package br.ufersa.edu;

import java.time.LocalDate;

public class Compra extends Movimentacao {
    private Responsavel responsavel;

    public Compra(int id, LocalDate data, Responsavel responsavel) {
        super(id, data);
        this.responsavel = responsavel;
    }

    public Responsavel getResponsavel() { return responsavel; }

    @Override
    public String getDescricao() {
        return "Compra #" + id + " (" + (responsavel != null ? responsavel.getNome() : "sem responsável") + ")";
    }
}
