package com.n26.challenge.service;

import com.n26.challenge.domain.Statistic;
import org.springframework.stereotype.Service;

/**
 * Created by ychahbi on 15/07/2018.
 */

public interface StatisticService {

    Statistic computeStatistics(Long fromTimestamp);
}
