package br.ufersa.edu.exception;

/**
 * Lançada quando o login falha (usuário inexistente ou senha incorreta)
 * ou quando um usuario sem permissão tenta uma operação restrita aoADMIN..
 */
public class AutenticacaoException extends Exception {
    public AutenticacaoException(String mensagem) {
        super(mensagem);
    }
}
