package br.ufersa.edu.model.DAO;

import java.util.List;

/**
 * Contrato comum de persistencia (interface). Atende ao requisito de uso de
 * interfaces e permite que os Services dependam da abstracao, nao da implementacao.
 */
public interface BaseDAO<E> {
    E inserir(E entity);
    void alterar(E entity);
    void deletar(E entity);
    E buscar(String param);
    List<E> listar();
}
