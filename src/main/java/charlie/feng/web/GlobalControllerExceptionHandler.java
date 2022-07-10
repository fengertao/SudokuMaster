package charlie.feng.web;

import charlie.feng.web.aa.dom.DuplicateUsernameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;

@ControllerAdvice
//Todo how to set response content type?
public class GlobalControllerExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    private String logExceptionDetail(HttpServletRequest req, Exception ex) {
        Principal principal = req.getUserPrincipal();
        String errorToken = ((principal == null) ? "NOTLOGIN" : principal.getName()) + "_" + System.currentTimeMillis();
        logger.error("Error Token : " + errorToken);
        logger.error("Request URL : " + req.getRequestURL());
        logger.error("Request Method : " + req.getMethod());
        /* Log post body is risky, may contains confidential info!!
        if ("POST".equalsIgnoreCase(req.getMethod())) {
            String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            logger.error("Request Body:" +body);
        }
         */
        logger.error("Exception catch : ", ex);
        return errorToken;
    }

    @SuppressWarnings("UnusedReturnValue")
    private String logExceptionSummary(HttpServletRequest req, Exception ex) {
        Principal principal = req.getUserPrincipal();
        String errorToken = ((principal == null) ? "NOTLOGIN" : principal.getName()) + "_" + System.currentTimeMillis();
        logger.info("Error Token : " + errorToken);
        logger.info("Request URL : " + req.getRequestURL());
        logger.info("Request Method : " + req.getMethod());
        return errorToken;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDenies(HttpServletRequest req, Exception ex) {
        logExceptionSummary(req, ex);
        // Nothing to do
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateUsernameException.class)
    public void handleDuplicateUsername(HttpServletRequest req, Exception ex) {
        logExceptionSummary(req, ex);
        // Nothing to do
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    ResponseEntity<ErrorInfo>
    defaultErrorHandler(HttpServletRequest req, Exception ex) {
        String errorToken = logExceptionDetail(req, ex);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it
        // AnnotationUtils is a Spring Framework utility class.

        ResponseStatus exceptionResponseStatus = AnnotationUtils.findAnnotation
                (ex.getClass(), ResponseStatus.class);
        if (exceptionResponseStatus != null) {
            status = exceptionResponseStatus.value();
        }
        return new ResponseEntity<>(new ErrorInfo(errorToken, ex), status);
    }

}
