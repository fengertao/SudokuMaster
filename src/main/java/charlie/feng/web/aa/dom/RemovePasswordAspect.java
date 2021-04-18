package charlie.feng.web.aa.dom;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RemovePasswordAspect {

    @Pointcut("execution(public * charlie.feng.web.aa.dom.UserController.*(..))")
    private void publicUserController() {
    }

    @AfterReturning(value = "publicUserController()", returning = "retVal")
    public void removePassword(Object retVal) {
        Object returnBody = retVal;
        if (returnBody instanceof ResponseEntity) {
            returnBody = ((ResponseEntity<?>) returnBody).getBody();
        }

        if (returnBody instanceof User) {
            ((User) returnBody).setPassword(null);
        }

        if (returnBody instanceof Iterable) {
            ((Iterable<?>) returnBody).forEach((Object record) -> {
                if (record instanceof User) {
                    ((User) record).setPassword(null);
                }
            });

        }

        //Todo Search user as attribute of return body. and deep search

    }
}
