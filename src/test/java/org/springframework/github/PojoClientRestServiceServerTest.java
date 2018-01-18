package org.springframework.github;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
@SpringBootTest(classes = AnalyticsApplication.class)
@RunWith(SpringRunner.class)
public class PojoClientRestServiceServerTest {

    private Resource customers = new ClassPathResource("customers.json");
    private Resource customerById = new ClassPathResource("customer-by-id.json");

    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private PojoClient client;

    @Autowired
    private RestTemplate restTemplate;

    @Before
    public void before() {
        this.mockRestServiceServer = MockRestServiceServer.bindTo(this.restTemplate)
                .build();
    }

    @Test
    public void customersShouldReturnAllCustomers() {

        this.mockRestServiceServer
                .expect(ExpectedCount.manyTimes(), requestTo("http://localhost:8080/customers"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(this.customers, MediaType.APPLICATION_JSON_UTF8));

        Collection<Pojo> customers = client.getPojoCollection();
        BDDAssertions.then(customers.size()).isEqualTo(2);

        this.mockRestServiceServer.verify();
    }

    @Test
    public void customerByIdShouldReturnACustomer() {

        this.mockRestServiceServer
                .expect(ExpectedCount.manyTimes(), requestTo("http://localhost:8080/customers/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(this.customerById, MediaType.APPLICATION_JSON_UTF8));

        Pojo pojo = client.getPojoById(1L);
        BDDAssertions.then(pojo.getUsername()).isEqualToIgnoringCase("first");
        BDDAssertions.then(pojo.getType()).isEqualToIgnoringCase("last");
        BDDAssertions.then(pojo.getRepository()).isEqualToIgnoringCase("email");
        BDDAssertions.then(pojo.getAction()).isEqualTo(1L);
    }
}
