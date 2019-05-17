package com.app.admin.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * @author :wkh.
 * @date :2019/5/7.
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    public RetryLimitHashedCredentialsMatcher() {
        super();
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        return false;
    }
}
