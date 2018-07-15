package com.n26.challenge.web.controller;

import com.n26.challenge.domain.exceptions.OutDatedTransactionException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by ychahbi on 15/07/2018.
 */
@ControllerAdvice(basePackages = {"com.n26.challenge.web.controller"})
public class TransactionExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionExceptionHandler.class);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(value = OutDatedTransactionException.class)
    public void validationExceptionHandler(HttpServletRequest req, OutDatedTransactionException e)
        throws Exception {
        LOG.error("Minor error happened req : {} exception : {}", req, e);
    }

}
