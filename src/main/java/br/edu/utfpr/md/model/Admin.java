package br.edu.utfpr.md.model;

import javax.persistence.Entity;

@Entity
public class Admin extends Pessoa{

    public Admin() {
        super();
    }
    
    // extends Pessoa

    public Admin(
            int id, 
            String nome, 
            String login, 
            String senha, 
            Endereco endereco
    ) 
    {
        super(
                id, 
                nome, 
                login, 
                senha, 
                endereco
        );
    }
    
    public Admin (Pessoa p){
        super(
                p.getId(), 
                p.getNome(), 
                p.getLogin(), 
                p.getSenha(), 
                p.getEndereco()
        );
    }
    
    
}
