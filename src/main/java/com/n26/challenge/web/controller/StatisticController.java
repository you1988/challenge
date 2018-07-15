package com.n26.challenge.web.controller;

import com.n26.challenge.domain.Statistic;
import com.n26.challenge.service.StatisticService;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by ychahbi on 15/07/2018.
 */
@Controller
public class StatisticController {

    public static final String GET_STATISTICS_PATH = "/statistics";
    @Value("${window.period}")
    private int windowPeriod;

    @Autowired
    private StatisticService statisticService;

    @RequestMapping(value = GET_STATISTICS_PATH,
        produces = {"application/json; charset=utf-8"},
        method = RequestMethod.GET)
    public ResponseEntity<Statistic> getStatistic() {
        final long latestAcceptedTimesTamp = Instant.now().minusSeconds(windowPeriod).toEpochMilli();
        final Statistic statistic = statisticService.computeStatistics(latestAcceptedTimesTamp);
        return new ResponseEntity<Statistic>(statistic, HttpStatus.OK);
    }

}
