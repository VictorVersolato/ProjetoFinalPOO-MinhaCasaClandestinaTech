package br.ufersa.edu;

import br.ufersa.edu.exception.ValidacaoException;

public class Local {
    private int id;
    private String nomeDaCasa;
    private String endereco;
    private String compartimento;
    private Responsavel responsavel;

    public Local(int id, String nomeDaCasa, String endereco, String compartimento, Responsavel responsavel) {
        setId(id);
        setNomeDaCasa(nomeDaCasa);
        setEndereco(endereco);
        setCompartimento(compartimento);
        setResponsavel(responsavel);
    }

    public int getId() { return id; }
    public String getNomeDaCasa() { return nomeDaCasa; }
    public String getEndereco() { return endereco; }
    public String getCompartimento() { return compartimento; }
    public Responsavel getResponsavel() { return responsavel; }

    public void setId(int id) {
        if (id < 0) {
            throw new ValidacaoException("Id inválido.");
        }
        this.id = id;
    }

    public void setNomeDaCasa(String nomeDaCasa) {
        if (nomeDaCasa == null || nomeDaCasa.trim().isEmpty()) {
            throw new ValidacaoException("Nome da casa inválido.");
        }
        this.nomeDaCasa = nomeDaCasa;
    }

    public void setEndereco(String endereco) {
        if (endereco == null) {
            throw new ValidacaoException("Endereco inválido.");
        }
        this.endereco = endereco;
    }

    public void setCompartimento(String compartimento) {
        if (compartimento == null || compartimento.trim().isEmpty()) {
            throw new ValidacaoException("Compartimento inválido.");
        }
        this.compartimento = compartimento;
    }

    public void setResponsavel(Responsavel responsavel) {
        if (responsavel == null) {
            throw new ValidacaoException("Responsavel inválido.");
        }
        this.responsavel = responsavel;
    }

    @Override
    public String toString() {
        return nomeDaCasa + " / " + compartimento;
    }
}
