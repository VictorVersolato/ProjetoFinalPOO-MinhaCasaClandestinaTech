package br.ufersa.edu.model;

import br.ufersa.edu.exception.ValidacaoException;

/**
 * Classe abstrata que concentra os atributos e validações das classes: 
 * Cliente, Responsavel e Funcionario.
 */
public abstract class Pessoa {
    private int id;
    private String nome;
    private String endereco;

    protected Pessoa(int id, String nome, String endereco) {
        setId(id);
        setNome(nome);
        setEndereco(endereco);
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }

    public void setId(int id) {
        if (id < 0) {
            throw new ValidacaoException("Id inválido: " + id);
        }
        this.id = id;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome é obrigatório.");
        }
        this.nome = nome.trim();
    }

    public void setEndereco(String endereco) {
        if (endereco == null) {
            throw new ValidacaoException("Endereco inválido.");
        }
        this.endereco = endereco;
    }

    public abstract String getDocumento();

    @Override
    public String toString() {
        return getNome() + " (" + getDocumento() + ")";
    }
}
