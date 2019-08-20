package org.jab.microservices;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * mvn pact:publish
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PactSumContractStubTest {

    @LocalServerPort
    int localServerPort;

    @Autowired
    TestRestTemplate client;

    @TestConfiguration
    static class TestConfig {

        @Bean
        TestRestTemplate getTestRestTemplate() {
            return new TestRestTemplate();
        }

    }

    @Rule
    public PactProviderRuleMk2 mockSumProvider =
            new PactProviderRuleMk2(
                    "sum_provider",
                    "localhost",
                    9090,
                    this);

    public String readFile(String resourceName) throws IOException {
        URL url = Resources.getResource(resourceName);
        return Resources.toString(url, Charsets.UTF_8);
    }

    @Pact(provider = "sum_provider", consumer = "MyCalculatorPACT")
    public RequestResponsePact createPact_sum(PactDslWithProvider builder) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        //Java 9+
        //Map.of("Content-Type","application/json");

        return builder
                .given("test sum POST")
                .uponReceiving("Sum POST REQUEST")
                .method("POST").path("/sum")
                .headers(headers)
                .body("{ \"number1\": 1, \"number2\": 4 }")
                //.body(readFile("request_sum_200.json"))
                .willRespondWith().status(200).headers(headers).body("5")

                /*
                .given("test sum POST without Request body")
                .uponReceiving("Sum POST REQUEST without Request body")
                .method("POST").path("/sum")
                .headers(headers)
                .willRespondWith().status(200).body("0")
                 */

                /*
                .given("test sum POST without Request reaching threshold")
                .uponReceiving("Sum POST Request reaching threshold")
                .method("POST").path("/contracts/sum")
                .headers(headers)
                .body("{ \"number1\": 1001, \"number2\": 3 }")
                .willRespondWith().status(400)

                .given("test sum POST with partial Request")
                .uponReceiving("Sum POST with partial Request")
                .method("POST").path("/contracts/sum")
                .headers(headers)
                .body("{ \"number1\": 1 }")
                .willRespondWith().status(200).body("1")

                .given("test sum POST with Request and negative params")
                .uponReceiving("Sum POST Request and negative params")
                .method("POST").path("/contracts/sum")
                .headers(headers)
                .body("{ \"number1\": 1, \"number2\": -3 }")
                .willRespondWith().status(400)
                 */

                .toPact();
    }

    @Test
    @PactVerification(fragment = "createPact_sum")
    public void given_sumProvider_when_executeCall_with_params1_and_4_then_expectedResults() {

        //Scenario 1 for provider_sum
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // when
        HttpEntity<MySumRequest> request = new HttpEntity<>(new MySumRequest(1, 4), httpHeaders);
        SumResponse sumResponse = client.postForObject(mockSumProvider.getUrl() + "/sum", request, SumResponse.class);

        // then
        then(sumResponse.getResult()).isEqualTo(5);

        /*
        //Scenario 2 for provider_sum

        // when
        HttpEntity<MySumRequest> request2 = new HttpEntity<>(null, httpHeaders);
        SumResponse sumResponse2 = client.postForObject(mockSumProvider.getUrl() + "/sum", request2, SumResponse.class);

        // then
        then(sumResponse2.getResult()).isEqualTo(0);
         */
    }

    @Ignore
    @Test
    @PactVerification(fragment = "createPact_sum")
    public void given_sumProvider_when_scenario2_then_expectedResults() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        //Scenario 2 for provider_sum

        // when
        HttpEntity<MySumRequest> request2 = new HttpEntity<>(null, httpHeaders);
        SumResponse sumResponse2 = client.postForObject(mockSumProvider.getUrl() + "/sum", request2, SumResponse.class);

        // then
        then(sumResponse2.getResult()).isEqualTo(0);

    }

    //TODO Doubt 1: How to split some asserts in multiple tests. Is it possible?
    //TODO Doubt 2: How to solve the missing tests.
    //TODO Doubt 3: Why PACT Stub server return MediaType.APPLICATION_OCTET_STREAM
    //TOOD Doubt 4: Test touch business logic. Possible security breach

    @Test
    @PactVerification
    public void given_SumControllerPath_when_scenario1_then_expectedResults() {

        //Scenario 1 for provider_sum
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // when
        HttpEntity<SumRequest> request = new HttpEntity<>(new SumRequest(1, 4), httpHeaders);
        SumResponse sumResponse = client.postForObject("http://localhost:" + localServerPort + "/sum", request, SumResponse.class);

        then(sumResponse.getResult()).isEqualTo(5);
    }
}
