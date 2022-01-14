package org.pbms.pbmsserver.common.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * @author zyl 输入参数，返回值日志信息
 */
@Component
@Aspect
public class ControllerLogAspect {
    public static final Logger log = LoggerFactory.getLogger(ControllerLogAspect.class);

    private JsonMapper jsonMapper = new JsonMapper();

    /**
     * 切面点
     */

    @Pointcut("execution(* org.pbms.pbmsserver.controller..*.*(..))")
    private void pointcut() {
    }

    @Autowired
    HttpServletRequest request;

    /**
     * 前置通知，方法调用前被调用
     *
     * @param joinPoint 切面点
     */
    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames(); // 参数名
        String methodName = joinPoint.getSignature().getName();
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < args.length; i++) {
            sb.append(argNames[i]).append(':').append(args[i]).append(", ");
        }
        sb.append(']');
        if (sb.length() >= 3) {
            sb.delete(sb.length() - 3, sb.length() - 1);
        }
        log.debug("\n=====>{} : {}", methodName, sb);
    }

    @AfterReturning(value = "pointcut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) throws JsonProcessingException {
        String methodName = joinPoint.getSignature().getName();
        if (result instanceof HttpEntity) {
            result = ((HttpEntity<?>) result).getBody();
        }
        String jsonStr = this.jsonMapper.writeValueAsString(result);
        if (result instanceof Collection) {
            log.debug("\n<====={} : size:{}, {}", methodName, ((Collection<?>) result).size(), jsonStr);
        } else {
            log.debug("\n<====={} : {}", methodName, jsonStr);
        }
    }
}
