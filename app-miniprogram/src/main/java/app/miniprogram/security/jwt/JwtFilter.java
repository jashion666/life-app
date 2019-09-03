package app.miniprogram.security.jwt;

import app.miniprogram.enums.ResultCodeEnum;
import app.miniprogram.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 所有的请求都会先经过Filter,重写鉴权的方法。
 *
 * @author :wkh.
 * @date :2019/8/30.
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * 检测header里面是否包含Authorization字段，如果没有则需要验证
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        return !StringUtils.isEmpty(getAuthzHeader(request));
    }

    /**
     * 此方法调用登陆，验证逻辑
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            JwtToken token = new JwtToken(getAuthzHeader(request));
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            try {
                getSubject(request, response).login(token);
            } catch (Exception e) {
                log.error(e.getMessage());
                int codeStatus = e instanceof ExpiredCredentialsException ? ResultCodeEnum.TOKEN_EXPIRE.getCode() : ResultCodeEnum.UNAUTHORIZED.getCode();
                setErrorResponse(httpServletResponse, e, codeStatus);
                return false;
            }

        }
        return true;
    }

    private void setErrorResponse(HttpServletResponse response, Throwable ex, int codeStatus) {
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.append(JsonResult.toJson(JsonResult.failedWithCode(ex.getMessage(), codeStatus)));
        } catch (IOException e) {
            throw new RuntimeException("直接返回Response信息出现IOException异常:" + e.getMessage());
        }
    }

    /**
     * 允许跨域
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
