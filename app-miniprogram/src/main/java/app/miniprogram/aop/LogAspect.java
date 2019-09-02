package app.miniprogram.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author :wkh.
 * @date :2019/8/14.
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("execution(* app.miniprogram.application.*.controller.*.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint point) {
        log.debug("====> " + point.getTarget().getClass().getName() + "." + point.getSignature().getName() + " 开始");
    }

    @After("pointcut()")
    public void after(JoinPoint point) {
        log.debug("<==== " + point.getTarget().getClass().getName() + "." + point.getSignature().getName() + " 结束");
    }

}
