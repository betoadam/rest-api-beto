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
import br.edu.utfpr.md.dao.PessoaDAO;
import br.edu.utfpr.md.model.Pessoa;
import br.edu.utfpr.md.security.Autenticado;
import java.util.List;
import javax.inject.Inject;

@Controller
@Path("/pessoa")
public class PessoaResource {

    @Inject
    private PessoaDAO pessoaDAO;
    @Inject
    private Result result;

    @Post(value = {"", "/"})
    @Consumes("application/json")
    public void save(Pessoa pessoa) {
        try {
            pessoaDAO.save(pessoa);
            result.use(Results.json()).withoutRoot().from(pessoa).serialize();
        } catch (Exception e) {
            result.use(Results.http()).setStatusCode(400);
        }
    }

    @Autenticado
    @Put(value = {"", "/"})
    @Consumes("application/json")
    public void update(Pessoa pessoa) {
        pessoaDAO.update(pessoa);
        result.use(Results.json()).withoutRoot().from(pessoa).serialize();
    }

    @Autenticado
    @Delete("/{id}")
    public void delete(int id) {
        Pessoa p = pessoaDAO.getById(id);
        if (p == null) {
            result.use(Results.status()).notFound();
        } else {
            pessoaDAO.delete(p);
            result.use(Results.nothing());
        }
    }

    @Autenticado
    @Get("/{id}")
    public void getOne(int id) {
        Pessoa p = pessoaDAO.getById(id);
        result.use(Results.json()).withoutRoot().from(p).serialize();
    }

    @Autenticado
    @Get(value = {"", "/"})
    public void getAll() {
        List<Pessoa> list = pessoaDAO.findAll();
        result.use(Results.json()).withoutRoot().from(list).serialize();
    }
}
