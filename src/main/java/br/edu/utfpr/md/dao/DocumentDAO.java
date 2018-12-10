package br.edu.utfpr.md.dao;

import br.edu.utfpr.md.model.Document;
import br.edu.utfpr.md.model.Keyword;
import br.edu.utfpr.md.model.Pessoa;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;

@RequestScoped
public class DocumentDAO extends GenericDAO<Integer, Document> {

    public DocumentDAO() {
        super();
    }

    public List<Document> getDocumentsByUser(Pessoa p) {
        List<Document> lista = new ArrayList<>();
        try {
            TypedQuery<Document> cat = entityManager.createQuery("SELECT d FROM Pessoa p, Document d WHERE p.documents = d AND p.id = " + p.getId(),
                    Document.class);
            lista = cat.getResultList();
        } catch (Exception e) {
        }
        return lista;
    }

    public List<Document> getDocumentsByKeyword(Keyword k) {
        List<Document> lista = new ArrayList<>();
        try {
            TypedQuery<Document> doc = entityManager.createQuery("SELECT d FROM Keyword k, Document d WHERE d.keywords = k AND k.id = " + k.getId(),
                    Document.class);
            lista = doc.getResultList();
        } catch (Exception e) {
        }
        return lista;
    }
    
    public Pessoa getDocumentOwner(Document document){
        Pessoa p = new Pessoa();
        try {
            TypedQuery<Pessoa> doc = entityManager.createQuery(""
                    + "SELECT "
                    + "     p "
                    + "FROM "
                    + "     Pessoa p, "
                    + "     Document d "
                    + "WHERE "
                    + "     p.documents = d"
                    + "     AND d.id = " + document.getId(),
                    Pessoa.class);
            p = doc.getSingleResult();
        } catch (Exception e) {
        }
        return p;
    }
}
