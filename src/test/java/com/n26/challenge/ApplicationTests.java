package com.n26.challenge;

import static com.jayway.restassured.RestAssured.given;
import static groovy.util.GroovyTestCase.assertEquals;
import static org.hamcrest.CoreMatchers.is;

import com.jayway.restassured.RestAssured;
import com.n26.challenge.domain.PartialStatistic;
import com.n26.challenge.domain.Statistic;
import com.n26.challenge.web.controller.StatisticController;
import com.n26.challenge.web.controller.TransactionController;
import java.time.Instant;
import java.util.HashMap;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

    final Long VALID_TIME = Instant.now().minusSeconds(40).toEpochMilli();
    final Long NOT_VALID_TIME = Instant.now().minusSeconds(120).toEpochMilli();
    final double DEFAULT_AMOUNT = 12.3D;
    @Autowired
    HashMap<Long, PartialStatistic> statisticsRepository;
    @Value("${local.server.port}")
    int port;

    @Before
    public void setUp() throws Exception {
        RestAssured.port = port;
    }

    @Test
    public void testTransactionsRequest201() {
        given()
            .contentType("application/json")
            .body(constructTransactionsRequestBody(VALID_TIME, DEFAULT_AMOUNT)).post(TransactionController.ADD_TRANSACTION_PATH)
            .then()
            .assertThat()
            .statusCode(is(201));
    }

    @Test
    public void testTransactionsRequest204() {
        given()
            .contentType("application/json")
            .body(constructTransactionsRequestBody(NOT_VALID_TIME, DEFAULT_AMOUNT)).post(TransactionController.ADD_TRANSACTION_PATH)
            .then()
            .assertThat()
            .statusCode(is(204));
    }

    @Test
    public void testDefaultStatisticResponseRequest() {
        statisticsRepository.clear();

        final Statistic statistic = given()
            .contentType("application/json")
            .get(StatisticController.GET_STATISTICS_PATH)
            .then()
            .assertThat()
            .statusCode(is(200))
            .log()
            .body()
            .extract()
            .response()
            .body()
            .as(Statistic.class);
        final Statistic expectedStatistic = Statistic.buildDefaultStatisticResponse();
        assertEquals(statistic, expectedStatistic);
    }

    @Test
    public void testStatisticHappyCase() {
        statisticsRepository.clear();
        IntStream.range(1, 10).forEach(i ->
        {
            given()
                .contentType("application/json")
                .body(constructTransactionsRequestBody(VALID_TIME + i, i)).post(TransactionController.ADD_TRANSACTION_PATH);
        });
        final Statistic statistic = given()
            .contentType("application/json")
            .get(StatisticController.GET_STATISTICS_PATH)
            .then()
            .assertThat()
            .statusCode(is(200))
            .log()
            .body()
            .extract()
            .response()
            .body()
            .as(Statistic.class);
        assertEquals(statistic, createExpectedStatisticsHappyCase());
    }

    private String constructTransactionsRequestBody(long timestamp, double amount) {
        return "{ \"amount\":" + amount + ", \"timestamp\": " + timestamp + "  }";
    }

    private Statistic createExpectedStatisticsHappyCase() {
        final Statistic expectedStatistic = new Statistic();
        expectedStatistic.setCount(9);
        expectedStatistic.setAvg((45D / 9));
        expectedStatistic.setSum(45);
        expectedStatistic.setMin(1);
        expectedStatistic.setMax(9);
        return expectedStatistic;
    }
}
