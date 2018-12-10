package br.edu.utfpr.md.security;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import com.auth0.jwt.JWTVerifyException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@RequestScoped
public class RequestToken {

    @Inject
    private HttpServletRequest request;
    
    private Result result;

    public String getToken() {
        return request.getHeader("Authorization");
    }

    public int getUserID() {
        Map<String, Object> claims = mapClaims();
        return (Integer) claims.get("user");
    }
    
    public String getUserRole(){
        Map<String, Object> claims = mapClaims();
        return (String) claims.get("role");
    }
    
    public String getUserName(){
        Map<String, Object> claims = mapClaims();
        return (String) claims.get("username");        
    }

    private Map<String, Object> mapClaims() {
        Map<String, Object> claims = null;
        try {
            return JWTUtil.decode(getToken());
        }   
            catch (
                InvalidKeyException 
                | NoSuchAlgorithmException
                | IllegalStateException 
                | SignatureException 
                | IOException
                | JWTVerifyException e
            ) 
        {
            result.use(Results.http()).setStatusCode(401);
            result.use(Results.json()).from(e.getMessage(), "msg").serialize();
        }
        return claims;
    }
}
