package com.n26.challenge.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.n26.challenge.config.ApplicationConfiguration;
import com.n26.challenge.domain.PartialStatistic;
import com.n26.challenge.domain.Transaction;
import com.n26.challenge.domain.exceptions.OutDatedTransactionException;
import com.n26.challenge.service.impl.TransactionServiceImpl;
import java.time.Instant;
import java.util.HashMap;
import java.util.stream.IntStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by ychahbi on 15/07/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
    TransactionServiceImpl.class,
    ApplicationConfiguration.class})
public class TransactionImplTest {

    @Autowired
    TransactionService transactionService;
    @Autowired
    HashMap<Long, PartialStatistic> statisticsRepository;
    final Long VALID_TIME = Instant.now().minusSeconds(40).toEpochMilli();
    final Long LATEST_VALID_TIME = Instant.now().minusSeconds(60).toEpochMilli();
    final Long NOT_VALID_TIME = Instant.now().minusSeconds(120).toEpochMilli();
    final Double DEFAULT_AMOUNT = 18D;

    @Test(expected = OutDatedTransactionException.class)
    public void testThatTransactionOlderThan60Seconds() {
        Transaction notValidTransaction = createTransaction(NOT_VALID_TIME);
        transactionService.handleTransaction(notValidTransaction, LATEST_VALID_TIME);
    }


    @Test
    public void testThatPartialStatisticCOuntIsCorrect() {
        statisticsRepository.clear();
        final Transaction transaction = createTransaction(VALID_TIME);
        PartialStatistic expectedPartiaStatistic = createPartialStatic(1);
        transactionService.handleTransaction(transaction, LATEST_VALID_TIME);
        assertTrue(statisticsRepository.containsKey(VALID_TIME));
        assertEquals(statisticsRepository.get(VALID_TIME), expectedPartiaStatistic);

    }

    @Test
    public void testStatisticCalculationMultipleRequests() {
        statisticsRepository.clear();
        PartialStatistic expectedPartialStatistic = createPartialStatic(3);
        Transaction validTransaction = createTransaction(VALID_TIME);
        IntStream.range(0, 3).forEach(i ->
            transactionService.handleTransaction(validTransaction, LATEST_VALID_TIME));
        assertTrue(statisticsRepository.containsKey(VALID_TIME));
        assertEquals(statisticsRepository.get(VALID_TIME), expectedPartialStatistic);

    }

    @Test
    public void testThatStatisicCalculationNotIncludingOutdatedTransactions() {
        PartialStatistic expectedPartialStatistic = createPartialStatic(1);
        PartialStatistic outDatedPartialStatistic = createPartialStatic(3);
        Transaction validTransaction = createTransaction(VALID_TIME);
        statisticsRepository.clear();
        statisticsRepository.put(NOT_VALID_TIME, outDatedPartialStatistic);
        transactionService.handleTransaction(validTransaction, LATEST_VALID_TIME);
        assertTrue(statisticsRepository.containsKey(VALID_TIME));
        assertEquals(statisticsRepository.get(VALID_TIME), expectedPartialStatistic);
        assertEquals(statisticsRepository.size(), 1);
    }

    private Transaction createTransaction(final long timestamp) {
        return Transaction.builder()
            .amount(DEFAULT_AMOUNT)
            .timestamp(timestamp)
            .build();
    }

    private PartialStatistic createPartialStatic(int count) {
        return PartialStatistic.builder()
            .count(count)
            .max(DEFAULT_AMOUNT)
            .min(DEFAULT_AMOUNT)
            .sum(DEFAULT_AMOUNT * count)
            .build();
    }


}
