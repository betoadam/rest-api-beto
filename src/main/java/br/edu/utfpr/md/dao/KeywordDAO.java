package br.edu.utfpr.md.dao;

import br.edu.utfpr.md.model.Keyword;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class KeywordDAO extends GenericDAO<Integer, Keyword> {

    public KeywordDAO() {
        super();
    }

    public Keyword getByName(String nome) {
        Keyword k = null;
        try {
            k = entityManager.createQuery(("SELECT e FROM Keyword e WHERE e.nome LIKE '" + nome + "'"), Keyword.class).getSingleResult();
        } catch (Exception e) {
        }
        return k;
    }
}
