package com.n26.challenge.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartialStatistic {

    protected int count;
    protected double min;
    protected double max;
    protected double sum;

    public void mergeStatistics(final Transaction transaction) {
        this.setCount(this.getCount() + 1);
        this.setMin(Math.min(transaction.getAmount(), this.getMin()));
        this.setMax(Math.max(transaction.getAmount(), this.getMin()));
        this.setSum(this.getSum() + transaction.getAmount());
    }
}
