package com.n26.challenge.config;

import com.n26.challenge.domain.PartialStatistic;
import com.n26.challenge.domain.SharedLock;
import java.util.HashMap;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by ychahbi on 15/07/2018.
 */
@Configuration
public class ApplicationConfiguration {

    @Bean
    public SharedLock sharedLock() {
        return new SharedLock();
    }

    @Bean
    public HashMap<Long, PartialStatistic> statisticsRepository() {
        return new HashMap<>();
    }

}
