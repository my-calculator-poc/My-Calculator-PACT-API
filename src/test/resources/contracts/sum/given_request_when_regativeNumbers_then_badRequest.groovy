package contracts.sum

import org.springframework.cloud.contract.spec.Contract

/**
 * Contract definition, written in a Groovy DSL
 */
Contract.make {

    /**
     * Request which completes with HTTP statusCode 200
     */
    request {
        method 'POST'
        url('/sum')
        headers {
            contentType(applicationJson())
        }
        body''' \
{ 
    "number1": 1,
    "number2": -3
}
'''
    }
    response {
        status 400
    }
}