package com.n26.challenge.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by ychahbi on 14/07/2018.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistic extends PartialStatistic {

    private Double avg;

    public static Statistic buildDefaultStatistic() {
        Statistic defaultStatistic = new Statistic();
        defaultStatistic.setAvg(0D);
        defaultStatistic.setSum(0D);
        defaultStatistic.setMax(Double.MIN_VALUE);
        defaultStatistic.setMin(Double.MAX_VALUE);
        defaultStatistic.setCount(0);
        return defaultStatistic;
    }
    public static Statistic buildDefaultStatisticResponse() {
        Statistic defaultStatistic = buildDefaultStatistic();
        defaultStatistic.setMax(0D);
        defaultStatistic.setMin(0D);
        return defaultStatistic;
    }
}
