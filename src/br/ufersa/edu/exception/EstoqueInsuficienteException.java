package br.ufersa.edu.exception;

/**
 * Excecao lancada quando se tenta retirar do estoque uma quantidade maior do que
 * a disponivel. Forca o VendaService a decidir o que fazer para evitar estoque neg.
 */
public class EstoqueInsuficienteException extends Exception {
    public EstoqueInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
