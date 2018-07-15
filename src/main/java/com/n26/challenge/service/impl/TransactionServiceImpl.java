package com.n26.challenge.service.impl;

import com.n26.challenge.domain.PartialStatistic;
import com.n26.challenge.domain.SharedLock;
import com.n26.challenge.domain.Transaction;
import com.n26.challenge.domain.exceptions.OutDatedTransactionException;
import com.n26.challenge.service.TransactionService;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ychahbi on 15/07/2018.
 */
@Component
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    HashMap<Long, PartialStatistic> statisticsRepository;
    @Autowired
    SharedLock sharedLock;


    public void handleTransaction(final Transaction transaction, final long latestAcceptedTimestamp) {
        validateUpToDateTransaction(transaction, latestAcceptedTimestamp);
        storeStatisticTransaction(transaction, latestAcceptedTimestamp);
    }

    private void validateUpToDateTransaction(final Transaction transaction, final long latestAcceptedTimestamp) {
        if (transaction.getTimestamp() < latestAcceptedTimestamp) {
            throw new OutDatedTransactionException();
        }
    }

 /* As the size of the statisticsRepository can be maximum 60*1000 so the operation of storing statistics is O(1)
       also the memory is O(1).
       The lock mechanism can be improved to a smart lock.
     */

    private void storeStatisticTransaction(final Transaction transaction, final long latestAcceptedTimestamp) {
        synchronized (sharedLock) {
            if (!statisticsRepository.containsKey(transaction.getTimestamp())) {
                insertDefaultPartialStatistic(transaction);
            } else {
                PartialStatistic partialStatistic = statisticsRepository.get(transaction.getTimestamp());
                partialStatistic.mergeStatistics(transaction);
            }
            cleanStorage(latestAcceptedTimestamp);
        }
    }

    private void insertDefaultPartialStatistic(final Transaction transaction) {
        PartialStatistic partialStatistic = PartialStatistic.builder().count(1)
            .max(transaction.getAmount())
            .min(transaction.getAmount())
            .sum(transaction.getAmount()).build();
        statisticsRepository.put(transaction.getTimestamp(), partialStatistic);
    }

    /*
    clear outdated transaction statistics
     */
    private void cleanStorage(final long latestTimestamp) {
        List<Long> outDatedStatisticKeys = statisticsRepository
            .keySet()
            .stream()
            .filter(timestamp -> timestamp < latestTimestamp)
            .collect(Collectors.<Long>toList());
        outDatedStatisticKeys.forEach(timestamp -> statisticsRepository.remove(timestamp));
    }


}
