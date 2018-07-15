package com.n26.challenge.service.impl;

import com.n26.challenge.domain.PartialStatistic;
import com.n26.challenge.domain.SharedLock;
import com.n26.challenge.domain.Statistic;
import com.n26.challenge.service.StatisticService;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ychahbi on 15/07/2018.
 */
@Component
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    HashMap<Long, PartialStatistic> statisticsRepository;
    @Autowired
    SharedLock sharedLock;

    /* As the size of the statisticsRepository can be maximum 60*1000 so the operation of computing statistics is O(1)
       also the memory is O(1).
       The lock mechanism can be improved to a smart lock.
     */
    public Statistic computeStatistics(Long fromTimestamp) {
        final Statistic statistic = Statistic.buildDefaultStatistic();
        synchronized (sharedLock) {
            statisticsRepository.entrySet().stream().filter(entry -> entry.getKey() > fromTimestamp)
                .map(entry -> entry.getValue())
                .forEach(partialStatistic -> {
                    statistic.setCount(partialStatistic.getCount() + statistic.getCount());
                    statistic.setMin(Math.min(partialStatistic.getMin(), statistic.getMin()));
                    statistic.setMax(Math.max(partialStatistic.getMax(), statistic.getMax()));
                    statistic.setSum(partialStatistic.getSum() + statistic.getSum());
                });
        }
        if (statistic.getCount() == 0) {
            return Statistic.buildDefaultStatisticResponse();
        }
        statistic.setAvg(statistic.getSum() / statistic.getCount());
        return statistic;
    }
}
