package br.edu.utfpr.md.security;


import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.AcceptsWithAnnotations;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.view.Results;

import java.util.ResourceBundle;

@Intercepts(after = {SecurityInterceptor.class})
@RequestScoped
@AcceptsWithAnnotations(AutenticadoMestre.class)
public class InterceptadorMestre {

    private RequestToken requestToken;
    private Result result;
    private ResourceBundle bundle;

    public InterceptadorMestre() {
        this(null, null, null);
    }

    @Inject
    public InterceptadorMestre(RequestToken requestToken, Result result, ResourceBundle bundle) {
        this.requestToken = requestToken;
        this.result = result;
        this.bundle = bundle;
    }

    @AroundCall
    public void intercept(SimpleInterceptorStack stack) {

        String token = requestToken.getToken();
        String role = requestToken.getUserRole();
        if (!role.equals("Admin")) {
            result.use(Results.http()).setStatusCode(401);
            result.use(Results.json())
                    .from("Apenas Admin", "msg").serialize();
        }else{
            result.use(Results.http()).addHeader("Authorization", token);
            stack.next();
        }
    }
}
