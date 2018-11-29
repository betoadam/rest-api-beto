package br.edu.utfpr.md.dao;

import br.edu.utfpr.md.model.Endereco;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class EnderecoDAO extends GenericDAO<Integer, Endereco> {

    public EnderecoDAO() {
        super();
    }
}
