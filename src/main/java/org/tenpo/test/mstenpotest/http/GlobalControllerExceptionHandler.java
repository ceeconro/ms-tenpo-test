package org.tenpo.test.mstenpotest.http;

import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.tenpo.test.mstenpotest.exceptions.InvalidInputException;
import org.tenpo.test.mstenpotest.security.authentication.AuthenticationException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
class GlobalControllerExceptionHandler {

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public @ResponseBody
    HttpErrorDetails handleNotFoundExceptions(Exception ex, WebRequest request) {
        return createHttpErrorInfo(UNAUTHORIZED, request, ex);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({JsonMappingException.class, MethodArgumentNotValidException.class})
    public @ResponseBody
    HttpErrorDetails handleBadRequestExceptions(Exception ex, WebRequest request) {
        return createHttpErrorInfo(BAD_REQUEST, request, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler({InvalidInputException.class})
    public @ResponseBody
    HttpErrorDetails handleInvalidInputException(Exception ex, WebRequest request) {

        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    private HttpErrorDetails createHttpErrorInfo(HttpStatus httpStatus, WebRequest request, Exception ex) {
        final String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        final String message = ex.getMessage();

        log.debug("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);
        return new HttpErrorDetails(httpStatus, path, message);
    }
}