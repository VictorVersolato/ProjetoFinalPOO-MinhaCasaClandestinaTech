package br.ufersa.edu;

import br.ufersa.edu.model.Pessoa;
import br.ufersa.edu.exception.ValidacaoException;

public class Responsavel extends Pessoa {
    private String telefone;

    public Responsavel(int id, String nome, String endereco, String telefone) {
        super(id, nome, endereco);
        setTelefone(telefone);
    }

    public String getTelefone() { return telefone; }

    public void setTelefone(String telefone) {
        if (telefone == null || telefone.replaceAll("\\D", "").length() < 8) {
            throw new ValidacaoException("Telefone inválido: " + telefone);
        }
        this.telefone = telefone;
    }

    @Override
    public String getDocumento() {
        return "Telefone: " + telefone;
    }
}
