package br.edu.utfpr.md.resource;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.edu.utfpr.md.dao.PessoaDAO;
import br.edu.utfpr.md.model.Admin;
import br.edu.utfpr.md.model.Pessoa;
import br.edu.utfpr.md.model.User;
import br.edu.utfpr.md.security.JWTUtil;
import javax.inject.Inject;

@Controller
//@Path("/login")
public class AuthResource {

    @Inject
    private PessoaDAO pessoaDAO;
    @Inject
    private Result result;

    @Post(value = {"/login"})
    @Consumes("application/json")
    public void login(String username, String password) {
        Pessoa p = pessoaDAO.Autenticate(username, password);
        if (p != null) {
            String pessoaType = "";
            if (p instanceof Admin) {
                pessoaType = "Admin";
            } else if (p instanceof User) {
                pessoaType = "User";
            } else {
                pessoaType = "Pessoa";
            }
            String token = JWTUtil.createToken((long) p.getId(), p.getLogin(), pessoaType);

            result.use(Results.status()).header("Content-type", "text/html");
            result.use(Results.status()).ok();
            result.use(Results.http()).body(token);
        } else {
            result.use(Results.http()).setStatusCode(404);
        }
    }
}
