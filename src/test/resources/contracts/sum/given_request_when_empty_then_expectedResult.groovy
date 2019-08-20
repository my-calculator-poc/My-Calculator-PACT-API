package contracts.sum

import org.springframework.cloud.contract.spec.Contract

/**
 * Contract definition, written in a Groovy DSL
 */
Contract.make {

    /**
     * Request which completes with HTTP statusCode 200
     */
    ignored: true
    request {
        method 'POST'
        url('/sum')
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body''' \
{ 
    "result": 0
}
'''
    }
}