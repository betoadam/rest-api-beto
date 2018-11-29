package br.edu.utfpr.md.resource;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.edu.utfpr.md.dao.DocumentDAO;
import br.edu.utfpr.md.dao.KeywordDAO;
import br.edu.utfpr.md.dao.PessoaDAO;
import br.edu.utfpr.md.model.Document;
import br.edu.utfpr.md.model.Keyword;
import br.edu.utfpr.md.model.Pessoa;
import br.edu.utfpr.md.security.Autenticado;
import br.edu.utfpr.md.security.RequestToken;
import java.util.List;
import javax.inject.Inject;

@Controller
@Path("/document")
public class DocumentResource {

    @Inject
    private RequestToken requestToken;
    @Inject
    private DocumentDAO documentDAO;
    @Inject
    private PessoaDAO pessoaDAO;
    @Inject
    private KeywordDAO keywordDAO;
    @Inject
    private Result result;

    @Autenticado
    @Post(value = {"", "/"})
    @Consumes("application/json")
    public void save(Document document) {
        try {
            documentDAO.save(document);
            result.use(Results.json()).withoutRoot().from(document).serialize();
        } catch (Exception e) {
            result.use(Results.http()).setStatusCode(400);
        }
    }

    @Autenticado
    @Put(value = {"", "/"})
    @Consumes("application/json")
    public void update(Document document) {
        Pessoa owner = documentDAO.getDocumentOwner(document);
        if (owner.getId() == requestToken.getUserID()) {
            documentDAO.update(document);
            result.use(Results.json()).withoutRoot().from(document).serialize();
        } else {
            result.use(Results.http()).setStatusCode(401);
            result.use(Results.json())
                    .from("Ação permitida somente para o criador do documento", "msg").serialize();
        }
    }

    @Autenticado
    @Delete("/{id}")
    public void delete(int id) {
        Document document = documentDAO.getById(id);
        Pessoa owner = documentDAO.getDocumentOwner(document);
        if (owner.getId() == requestToken.getUserID()) {
            if (document == null) {
                result.use(Results.status()).notFound();
            } else {
                documentDAO.delete(document);
                result.use(Results.nothing());
            }
        } else {
            result.use(Results.http()).setStatusCode(401);
            result.use(Results.json())
                    .from("Ação permitida somente para o criador do documento", "msg").serialize();
        }
    }

    @Autenticado
    @Get("/{id}")
    public void getOne(int id) {
        Document p = documentDAO.getById(id);
        result.use(Results.json()).withoutRoot().from(p).serialize();
    }

    @Autenticado
    @Get(value = {"", "/"})
    public void getAll() {
        List<Document> list = documentDAO.findAll();
        result.use(Results.json()).withoutRoot().from(list).serialize();
    }

    @Autenticado
    @Get(value = {"/person/{id}"})
    public void getDocumentsByUser(int id) {
        Pessoa p = pessoaDAO.getById(id);
        List<Document> documents = documentDAO.getDocumentsByUser(p);
        result.use(Results.json()).withoutRoot().from(documents).serialize();
    }

    @Autenticado
    @Get(value = {"/tag/{name}"})
    public void getDocumentsByKeyword(String name) {
        // obtem todos os documentos com a categoria "name"
        Keyword k = keywordDAO.getByName(name);
        List<Document> documents = documentDAO.getDocumentsByKeyword(k);
        result.use(Results.json()).withoutRoot().from(documents).serialize();
    }
}
