package br.ufersa.edu;

import br.ufersa.edu.exception.EstoqueInsuficienteException;
import br.ufersa.edu.exception.ValidacaoException;

public class Equipamento {
    private String nome;
    private int numeroDeSerie;
    private double preco;
    private int qtdeEmEstoque;
    private Local local;
    private Responsavel responsavel;

    public Equipamento(int numeroDeSerie, String nome, double preco, int qtdeEmEstoque,
                       Local local, Responsavel responsavel) {
        setNumeroDeSerie(numeroDeSerie);
        setNome(nome);
        setPreco(preco);
        setQtdeEmEstoque(qtdeEmEstoque);
        setLocal(local);
        setResponsavel(responsavel);
    }

    public String getNome() { return nome; }
    public int getNumeroDeSerie() { return numeroDeSerie; }
    public double getPreco() { return preco; }
    public int getQtdeEmEstoque() { return qtdeEmEstoque; }
    public Local getLocal() { return local; }
    public Responsavel getResponsavel() { return responsavel; }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome do equipamento inválido.");
        }
        this.nome = nome;
    }

    public void setNumeroDeSerie(int numeroDeSerie) {
        if (numeroDeSerie <= 0) {
            throw new ValidacaoException("Numero de série inválido.");
        }
        this.numeroDeSerie = numeroDeSerie;
    }

    public void setPreco(double preco) {
        if (preco <= 0) {
            throw new ValidacaoException("Preço deve ser maior do que zero.");
        }
        this.preco = preco;
    }

    public void setQtdeEmEstoque(int qtdeEmEstoque) {
        if (qtdeEmEstoque < 0) {
            throw new ValidacaoException("Quantidade não pode ser negativa.");
        }
        this.qtdeEmEstoque = qtdeEmEstoque;
    }

    public void setLocal(Local local) {
        if (local == null) {
            throw new ValidacaoException("Local inválido.");
        }
        this.local = local;
    }

    public void setResponsavel(Responsavel responsavel) {
        if (responsavel == null) {
            throw new ValidacaoException("Responsável inválido.");
        }
        this.responsavel = responsavel;
    }

    public void aumentarEstoque(int qtd) {
        if (qtd <= 0) {
            throw new ValidacaoException("Quantidade a aumentar deve ser positiva.");
        }
        this.qtdeEmEstoque += qtd;
    }

    public void diminuirEstoque(int qtd) throws EstoqueInsuficienteException {
        if (qtd <= 0) {
            throw new ValidacaoException("Quantidade a diminuir deve ser positiva.");
        }
        if (this.qtdeEmEstoque < qtd) {
            throw new EstoqueInsuficienteException(
                "Estoque insuficiente para " + nome + " (série " + numeroDeSerie + ").");
        }
        this.qtdeEmEstoque -= qtd;
    }

    @Override
    public String toString() {
        return "[" + numeroDeSerie + "] " + nome + " | " + qtdeEmEstoque
             + " un. | R$ " + String.format("%.2f", preco);
    }
}
