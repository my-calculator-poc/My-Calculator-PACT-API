package org.jab.microservices;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController()
@RequiredArgsConstructor
public class SumController {

    private final SumService sumService;

    @PostMapping(value = "/sum" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SumResponse> sum(@Valid @RequestBody SumRequest sumRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            LOGGER.error("Validation error: {}", bindingResult.toString());
            return buildResponse(-99, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return buildResponse(sumService.sum(sumRequest), HttpStatus.OK);
    }

    private ResponseEntity<SumResponse> buildResponse(Integer result, HttpStatus status) {
        return new ResponseEntity<>(new SumResponse(result), status);
    }

}
