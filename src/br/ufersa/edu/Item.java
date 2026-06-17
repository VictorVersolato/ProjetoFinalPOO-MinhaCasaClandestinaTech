package br.ufersa.edu;

import br.ufersa.edu.exception.ValidacaoException;

public class Item {
    private int numeroDeSerie;
    private int quantidade;
    private double precoUnitario;

    public Item(int numeroDeSerie, int quantidade, double precoUnitario) {
        if (numeroDeSerie <= 0) {
            throw new ValidacaoException("Número de série do item inválido.");
        }
        if (quantidade <= 0) {
            throw new ValidacaoException("Quantidade do item deve ser positiva.");
        }
        if (precoUnitario < 0) {
            throw new ValidacaoException("Preço unitário não pode ser negativo.");
        }
        this.numeroDeSerie = numeroDeSerie;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public int getNumeroDeSerie() { return numeroDeSerie; }
    public int getQuantidade() { return quantidade; }
    public double getPrecoUnitario() { return precoUnitario; }

    public double calcularSubtotal() {
        return quantidade * precoUnitario;
    }
}
