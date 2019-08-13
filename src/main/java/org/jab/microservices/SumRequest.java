package org.jab.microservices;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SumRequest {

    @NotNull(message = "Non empty number 1")
    @Min(value = 1, message = "Positive number 1")
    @Max(value = 1000, message = "Number 1 between 1-1000")
    private Integer number1;

    @NotNull(message = "Non empty number 2")
    @Min(value = 1, message = "Positive number 2")
    @Max(value = 1000, message = "Number 2 between 1-1000")
    private Integer number2;

}
