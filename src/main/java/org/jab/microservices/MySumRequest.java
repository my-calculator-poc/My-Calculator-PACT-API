package org.jab.microservices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MySumRequest {

    private Integer number1;
    private Integer number2;
}
