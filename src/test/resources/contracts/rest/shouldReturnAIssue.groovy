import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "issues/{id} should return a single JSON issue"

    request{
        url "/issues/1"
        method GET()

    }

    response{
        //status org.springframework.http.HttpStatus.OK
        status 200
        headers {
            //header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
            contentType(applicationJsonUtf8())
        }
        /*body(
        """ { "userName" : "foo", "repository" : "spring-cloud/bar" }
      """)*/
        body([userName: 'foo', repository: 'spring-cloud/bar'])
    }
}
