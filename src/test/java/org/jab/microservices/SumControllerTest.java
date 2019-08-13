package org.jab.microservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    WireMockServer wireMockServer;

    @Before
    public void setup () {
        wireMockServer = new WireMockServer(9090);
        wireMockServer.start();
    }

    @After
    public void teardown () {
        wireMockServer.stop();
    }



    @Test
    public void given_SumWithPostAndFormData_whenMockMVC_thenResponseOKResult() throws Exception  {

        //Given
        wireMockServer.stubFor(com.github.tomakehurst.wiremock.client.WireMock.post(urlEqualTo("/sum"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{\"result\":4}")));

        //When
        Integer param1 = 1;
        Integer param2 = 3;

        final SumRequest request = new SumRequest(param1, param2);
        final ObjectMapper mapper = new ObjectMapper();

        final String jsonBodyRequest = mapper.writeValueAsString(request);
        final String expectedJsonContent = mapper.writeValueAsString(new SumResponse(param1 + param2));
        this.mockMvc.perform(post("/sum")
                .contentType(MediaType.APPLICATION_JSON)
        .content(jsonBodyRequest))
        .andDo(print())

        //Then
        .andExpect(status().isOk())
        .andExpect(content().json(expectedJsonContent));
    }

}
