package br.edu.utfpr.md.dao;

import br.edu.utfpr.md.model.Category;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class CategoryDAO extends GenericDAO<Integer, Category> {

    public CategoryDAO() {
        super();
    }
}
