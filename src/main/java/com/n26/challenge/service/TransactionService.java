package com.n26.challenge.service;

import com.n26.challenge.domain.Transaction;
import org.springframework.stereotype.Service;

/**
 * Created by ychahbi on 14/07/2018.
 */

public interface TransactionService {

    void handleTransaction(final Transaction transaction, final long latestAcceptedTimestamp);
}
