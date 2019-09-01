package app.miniprogram.security.shiro;

import app.miniprogram.security.jwt.JwtService;
import app.miniprogram.security.jwt.JwtToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @author :wkh.
 * @date :2019/8/30.
 */
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo();
    }

    /**
     * 验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String jwtToken = (String) token.getCredentials();
        String wxOpenId = jwtService.getWxOpenIdByToken(jwtToken);
        String sessionKey = jwtService.getSessionKeyByToken(jwtToken);
        if (StringUtils.isEmpty(wxOpenId)) {
            throw new AuthenticationException("无效的用户信息");
        }
        if (StringUtils.isEmpty(sessionKey)) {
            throw new AuthenticationException("无效的回话信息");
        }
        if (!jwtService.verifyToken(jwtToken)) {
            throw new AuthenticationException("认证失败");
        }

        this.setCredentialsMatcher(credentialsMatcher());
        return new SimpleAuthenticationInfo(token, token, getName());
    }

    /**
     * 密码校验 , 这里因为是JWT形式,就无需密码校验和加密,直接让其返回为true(如果不设置的话,该值默认为false,即始终验证不通过)
     */
    private CredentialsMatcher credentialsMatcher() {
        return (token, info) -> true;
    }

}
