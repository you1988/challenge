package com.n26.challenge.web.controller;

import com.n26.challenge.domain.Transaction;
import com.n26.challenge.service.TransactionService;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by ychahbi on 14/07/2018.
 */
@Controller
public class TransactionController {

    public static final String ADD_TRANSACTION_PATH = "/transactions";

    @Autowired
    private TransactionService transactionService;
    @Value("${window.period}")
    private int windowPeriod;
    @RequestMapping(value = ADD_TRANSACTION_PATH,
        produces = {"application/json; charset=utf-8"},
        method = RequestMethod.POST)
    public ResponseEntity<Void> addTransaction(@RequestBody Transaction transaction) {
        final long latestAcceptedTimesTamp = Instant.now().minusSeconds(windowPeriod).toEpochMilli();
        transactionService.handleTransaction(transaction,latestAcceptedTimesTamp);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
}
