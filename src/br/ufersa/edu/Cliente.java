package br.ufersa.edu;

import br.ufersa.edu.model.Pessoa;
import br.ufersa.edu.exception.ValidacaoException;

public class Cliente extends Pessoa {
    private String cpf;

    public Cliente(int id, String nome, String endereco, String cpf) {
        super(id, nome, endereco);
        setCpf(cpf);
    }

    public String getCpf() { return cpf; }

    public void setCpf(String cpf) {
        if (cpf == null) {
            throw new ValidacaoException("CPF é obrigatorio.");
        }
        String somenteNumeros = cpf.replaceAll("\\D", "");
        if (somenteNumeros.length() != 11) {
            throw new ValidacaoException("CPF deve conter 11 digitos: " + cpf);
        }
        this.cpf = somenteNumeros;
    }

    @Override
    public String getDocumento() {
        return "CPF " + cpf;
    }
}
