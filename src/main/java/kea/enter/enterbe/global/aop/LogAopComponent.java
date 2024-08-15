package kea.enter.enterbe.global.aop;

import kea.enter.enterbe.global.common.exception.CustomException;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LogAopComponent {

    @AfterThrowing(pointcut = "execution(* kea.enter.enterbe.api..controller..*(..))", throwing = "ex")
    public void logError(final Exception ex) {
        String requestUrl = getCurrentRequestUrl();
        String className = ex.getClass().getSimpleName();
        String message = getLogMessage(ex);

        log.error(createLogMessage(requestUrl, className, message));
    }

    private String getCurrentRequestUrl() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest().getRequestURI();
    }

    private String getLogMessage(Exception ex) {
        if (ex instanceof CustomException customException) {
            return String.valueOf(customException.getResponseCode());
        } else {
            return ex.getMessage();
        }
    }

    private String createLogMessage(String url, String className, String message) {
        return String.format("Exception at URL: %s, ClassName: %s, Message: %s", url, className,
            message);
    }

}
