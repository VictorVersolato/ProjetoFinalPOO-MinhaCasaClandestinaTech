package br.ufersa.edu.exception;

/**
 * Lancada quando um dado de entrada viola uma regra de validacao
 * (nome vazio, preco negativo, CPF com tamanho errado...)
 */
public class ValidacaoException extends RuntimeException {
    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}
