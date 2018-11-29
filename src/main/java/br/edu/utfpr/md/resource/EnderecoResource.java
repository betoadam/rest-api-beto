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
import br.edu.utfpr.md.dao.EnderecoDAO;
import br.edu.utfpr.md.model.Endereco;
import br.edu.utfpr.md.security.Autenticado;
import java.util.List;
import javax.inject.Inject;

@Controller
@Path("/endereco")
public class EnderecoResource {

    @Inject
    private EnderecoDAO enderecoDAO;
    @Inject
    private Result result;

    @Autenticado
    @Post(value = {"", "/"})
    @Consumes("application/json")
    public void save(Endereco endereco) {
        try {
            enderecoDAO.save(endereco);
            result.use(Results.json()).withoutRoot().from(endereco).serialize();
        } catch (Exception e) {
            result.use(Results.http()).setStatusCode(400);
        }
    }

    @Autenticado
    @Put(value = {"", "/"})
    @Consumes("application/json")
    public void update(Endereco endereco) {
        enderecoDAO.update(endereco);
        result.use(Results.json()).withoutRoot().from(endereco).serialize();
    }

    @Autenticado
    @Delete("/{id}")
    public void delete(int id) {
        Endereco p = enderecoDAO.getById(id);
        if (p == null) {
            result.use(Results.status()).notFound();
        } else {
            enderecoDAO.delete(p);
            result.use(Results.nothing());
        }
    }

    @Autenticado
    @Get("/{id}")
    public void getOne(int id) {
        Endereco p = enderecoDAO.getById(id);
        result.use(Results.json()).withoutRoot().from(p).serialize();
    }

    @Autenticado
    @Get(value = {"", "/"})
    public void getAll() {
        List<Endereco> list = enderecoDAO.findAll();
        result.use(Results.json()).withoutRoot().from(list).serialize();
    }
}
