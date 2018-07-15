package com.n26.challenge.service;

/**
 * Created by ychahbi on 15/07/2018.
 */

import static org.junit.Assert.assertEquals;

import com.n26.challenge.config.ApplicationConfiguration;
import com.n26.challenge.domain.PartialStatistic;
import com.n26.challenge.domain.Statistic;
import com.n26.challenge.service.impl.StatisticServiceImpl;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
    StatisticServiceImpl.class,
    ApplicationConfiguration.class})
public class StatisticServiceImplTest {

    @Autowired
    StatisticService statisticService;
    @Autowired
    HashMap<Long, PartialStatistic> statisticsRepository;
    final Instant CURRENT_TIMESTAMP = Instant.now();
    final List<Long> VALID_TIMES = new ArrayList<Long>() {{
        add(CURRENT_TIMESTAMP.minusSeconds(40).toEpochMilli());
        add(CURRENT_TIMESTAMP.minusSeconds(30).toEpochMilli());
        add(CURRENT_TIMESTAMP.minusSeconds(20).toEpochMilli());
    }};
    final Double DEFAULT_AMOUNT = 18D;

    @Test
    public void testStatisticsCalculationIsCorrect() {
        statisticsRepository.clear();
        initTrasactionsStorage();
        final Statistic expectedStatistic = getExpectedStatisticResponse();
        assertEquals(statisticService.computeStatistics(CURRENT_TIMESTAMP.minusSeconds(60).toEpochMilli())
            ,expectedStatistic);
    }

    private void initTrasactionsStorage() {
        VALID_TIMES.stream().forEach(timestamp -> {
                statisticsRepository.put(timestamp, PartialStatistic.builder()
                    .count(1)
                    .max(DEFAULT_AMOUNT)
                    .min(DEFAULT_AMOUNT)
                    .sum(DEFAULT_AMOUNT)
                    .build());
            }
        );
    }

    private Statistic getExpectedStatisticResponse() {
        final Statistic expectedStatistic = new Statistic();
        expectedStatistic.setAvg(DEFAULT_AMOUNT);
        expectedStatistic.setCount(3);
        expectedStatistic.setMax(DEFAULT_AMOUNT);
        expectedStatistic.setMin(DEFAULT_AMOUNT);
        expectedStatistic.setSum(DEFAULT_AMOUNT * 3);
        return expectedStatistic;
    }

}
