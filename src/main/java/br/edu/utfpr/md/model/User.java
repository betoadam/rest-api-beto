package br.edu.utfpr.md.model;

import javax.persistence.Entity;

@Entity
public class User extends Pessoa{

    public User() {
        super();
    }
    // extends pessoa

    public User(int id, String nome, String login, String senha, Endereco endereco) {
        super(id, nome, login, senha, endereco);
    }
    
    public User (Pessoa p){
        super(p.getId(), p.getNome(), p.getLogin(), p.getSenha(), p.getEndereco());
    }
}
