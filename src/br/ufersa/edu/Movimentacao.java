package br.ufersa.edu;

import br.ufersa.edu.exception.ValidacaoException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


 //Classe abstrata que generaliza Compra e Venda 
 
public abstract class Movimentacao {
    protected int id;
    protected LocalDate data;
    protected final List<Item> itens = new ArrayList<>();

    protected Movimentacao(int id, LocalDate data) {
        setId(id);
        setData(data);
    }

    public int getId() { return id; }
    public LocalDate getData() { return data; }
    public List<Item> getItens() { return Collections.unmodifiableList(itens); }

    public void setId(int id) {
        if (id < 0) {
            throw new ValidacaoException("ID inválido.");
        }
        this.id = id;
    }

    public void setData(LocalDate data) {
        this.data = (data == null) ? LocalDate.now() : data;
    }

    public void adicionarItem(Item item) {
        if (item == null) {
            throw new ValidacaoException("Item inválido.");
        }
        for (Item existente : itens) {
            if (existente.getNumeroDeSerie() == item.getNumeroDeSerie()) {
                throw new ValidacaoException("Item já adicionado. Serie " + item.getNumeroDeSerie());
            }
        }
        itens.add(item);
    }

    public void removerItem(Item item) {
        itens.remove(item);
    }

    public double calcularTotal() {
        return itens.stream().mapToDouble(Item::calcularSubtotal).sum();
    }

    public abstract String getDescricao();
}
