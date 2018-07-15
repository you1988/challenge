package com.n26.challenge.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by ychahbi on 14/07/2018.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private Long timestamp;
    private Double amount;
}
