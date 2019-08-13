package org.jab.microservices;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SumRequestValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void given_SumRequest_when_propertiesIsNull_then_validationError() {

        //Given
        SumRequest request = new SumRequest();

        //When
        Set<ConstraintViolation<SumRequest>> constraintViolations = validator.validate(request);

        //Then
        assertThat(constraintViolations.size()).isEqualTo(2);
    }

    @Test
    public void given_SumRequest_when_validRequest_then_noError() {

        //Given
        SumRequest request = new SumRequest(1, 1);

        //When
        Set<ConstraintViolation<SumRequest>> constraintViolations = validator.validate(request);

        //Then
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

    @Test
    public void given_SumRequest_when_number1ReachLimit_then_validationError() {

        //Given
        SumRequest request = new SumRequest(1001, 1);

        //When
        Set<ConstraintViolation<SumRequest>> constraintViolations = validator.validate(request);

        //Then
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo("Number 1 between 1-1000");
    }

    @Test
    public void given_SumRequest_when_number2ReachLimit_then_validationError() {

        //Given
        SumRequest request = new SumRequest(1, 1001);

        //When
        Set<ConstraintViolation<SumRequest>> constraintViolations = validator.validate(request);

        //Then
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo("Number 2 between 1-1000");
    }

    @Test
    public void given_SumRequest_when_negativeNumber1_then_validationError() {

        //Given
        SumRequest request = new SumRequest(-3, 10);

        //When
        Set<ConstraintViolation<SumRequest>> constraintViolations = validator.validate(request);

        //Then
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo("Positive number 1");
    }

    @Test
    public void given_SumRequest_when_negativeNumber2_then_validationError() {

        //Given
        SumRequest request = new SumRequest(1, -3);

        //When
        Set<ConstraintViolation<SumRequest>> constraintViolations = validator.validate(request);

        //Then
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo("Positive number 2");
    }

    @Test
    public void given_SumRequest_when_number2IsNull_then_validationError() {

        //Given
        SumRequest request = new SumRequest(1, null);

        //When
        Set<ConstraintViolation<SumRequest>> constraintViolations = validator.validate(request);

        //Then
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo("Non empty number 2");
    }

    @Test
    public void given_SumRequest_when_number1IsNull_then_validationError() {

        //Given
        SumRequest request = new SumRequest(null, 1);

        //When
        Set<ConstraintViolation<SumRequest>> constraintViolations = validator.validate(request);

        //Then
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo("Non empty number 1");
    }

}