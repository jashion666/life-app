package app.miniprogram.security.jwt;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author : wkh.
 */
@Data
public class JwtToken implements AuthenticationToken {

    private String token;

    JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return getCredentials();
    }

    @Override
    public Object getCredentials() {
        return token;
    }

}
