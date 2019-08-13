package org.jab.microservices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class SumServiceImpl implements SumService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Integer sum(SumRequest request) {

        final String mySumAddress = "http://localhost:9090/sum";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MySumRequest mySumRequest = new MySumRequest(request.getNumber1(), request.getNumber2());

        ResponseEntity<MySumResponse> result = restTemplate.postForEntity(mySumAddress, mySumRequest, MySumResponse.class);
        LOGGER.info("Result: {}", result.getBody().getResult());

        return result.getBody().getResult();
    }
}
