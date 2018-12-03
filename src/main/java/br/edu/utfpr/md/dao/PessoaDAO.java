package br.edu.utfpr.md.dao;

import br.edu.utfpr.md.model.Admin;
import br.edu.utfpr.md.model.Categoria;
import br.edu.utfpr.md.model.Pessoa;
import br.edu.utfpr.md.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;

@RequestScoped
public class PessoaDAO extends GenericDAO<Integer, Pessoa> {

    public PessoaDAO() {
        super();
    }

    public Pessoa Autenticate(String username, String password) {
        Pessoa p = null;
        try {
            p = entityManager.createQuery(
                    "SELECT p FROM Pessoa p WHERE p.login = '" + username
                    + "' AND p.senha = '" + password + "'", Pessoa.class).getSingleResult();
        } catch (Exception e) {
        }
        return p;
    }

    public List<Categoria> getDistinctCategories(Pessoa p) {
        List<Categoria> lista = new ArrayList<>();
        try {
            TypedQuery<Categoria> cat = 
                    entityManager.createQuery("SELECT DISTINCT(d.categoria) FROM Pessoa p, Documento d WHERE p.documentos = d AND p.id = " + p.getId(), Categoria.class);
            lista = cat.getResultList();
        } catch (Exception e) {
        }
        return lista;
    }

    @Override
    public void save(Pessoa p) {
        Pessoa entity;
        if (p.getLogin().contains("admin")) {
            entity = new Admin(p);
        } else {
            entity = new User(p);
        }
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }
}
