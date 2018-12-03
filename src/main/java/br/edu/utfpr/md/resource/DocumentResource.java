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
import br.edu.utfpr.md.dao.DocumentoDAO;
import br.edu.utfpr.md.dao.KeywordDAO;
import br.edu.utfpr.md.dao.PessoaDAO;
import br.edu.utfpr.md.model.Documento;
import br.edu.utfpr.md.model.Keyword;
import br.edu.utfpr.md.model.Pessoa;
import br.edu.utfpr.md.security.Autenticado;
import br.edu.utfpr.md.security.RequestToken;
import java.util.List;
import javax.inject.Inject;

@Controller
@Path("/documento")
public class DocumentResource {

    @Inject
    private RequestToken requestToken;
    @Inject
    private DocumentoDAO documentoDAO;
    @Inject
    private PessoaDAO pessoaDAO;
    @Inject
    private KeywordDAO keywordDAO;
    @Inject
    private Result result;

    @Autenticado
    @Post(value = {"", "/"})
    @Consumes("application/json")
    public void save(Documento documento) {
        try {
            documentoDAO.save(documento);
            result.use(Results.json()).withoutRoot().from(documento).serialize();
        } catch (Exception e) {
            result.use(Results.http()).setStatusCode(400);
        }
    }

    @Autenticado
    @Put(value = {"", "/"})
    @Consumes("application/json")
    public void update(Documento documento) {
        Pessoa owner = documentoDAO.getDocumentOwner(documento);
        if (owner.getId() == requestToken.getUserID()) {
            documentoDAO.update(documento);
            result.use(Results.json()).withoutRoot().from(documento).serialize();
        } else {
            result.use(Results.http()).setStatusCode(401);
            result.use(Results.json())
                    .from("Você não tem permissão ", "msg").serialize();
        }
    }

    @Autenticado
    @Delete("/{id}")
    public void delete(int id) {
        Documento documento = documentoDAO.getById(id);
        Pessoa owner = documentoDAO.getDocumentOwner(documento);
        if (owner.getId() == requestToken.getUserID()) {
            if (documento == null) {
                result.use(Results.status()).notFound();
            } else {
                documentoDAO.delete(documento);
                result.use(Results.nothing());
            }
        } else {
            result.use(Results.http()).setStatusCode(401);
            result.use(Results.json())
                    .from("Você não tem permissão ", "msg").serialize();
        }
    }

    @Autenticado
    @Get("/{id}")
    public void getOne(int id) {
        Documento p = documentoDAO.getById(id);
        result.use(Results.json()).withoutRoot().from(p).serialize();
    }

    @Autenticado
    @Get(value = {"", "/"})
    public void getAll() {
        List<Documento> list = documentoDAO.findAll();
        result.use(Results.json()).withoutRoot().from(list).serialize();
    }

    @Autenticado
    @Get(value = {"/person/{id}"})
    public void getDocumentsByUser(int id) {
        Pessoa p = pessoaDAO.getById(id);
        List<Documento> documentos = documentoDAO.getDocumentsByUser(p);
        result.use(Results.json()).withoutRoot().from(documentos).serialize();
    }

    @Autenticado
    @Get(value = {"/tag/{name}"})
    public void getDocumentsByKeyword(String name) {
        // obtem todos os documentoos com a categoria "name"
        Keyword k = keywordDAO.getByName(name);
        List<Documento> documentos = documentoDAO.getDocumentsByKeyword(k);
        result.use(Results.json()).withoutRoot().from(documentos).serialize();
    }
}
