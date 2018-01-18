package org.springframework.github;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.BDDAssertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ReflectionUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
@SpringBootTest(classes = AnalyticsApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureWireMock(port = 8080)
@Ignore
public class PojoClientTest {

    private Resource customers = new ClassPathResource("customers.json");
    private Resource customerById = new ClassPathResource("customer-by-id.json");

    @Autowired
    private PojoClient client;

    @Test
    public void issuesShouldReturnAllIssues() {

        WireMock.stubFor(WireMock.get(WireMock.urlMatching("/"))
                .willReturn(
                        WireMock.aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .withStatus(HttpStatus.OK.value())
                                .withBody(asJson(customers))));

        Collection<Pojo> customers = client.getPojoCollection();
        BDDAssertions.then(customers.size()).isEqualTo(2);
    }

    private String asJson(Resource resource) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            Stream<String> lines = bufferedReader.lines();
            return lines.collect(Collectors.joining());
        } catch (Exception e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }
        return null;
    }

    @Test
    public void customerByIdShouldReturnACustomer() {

        WireMock.stubFor(WireMock.get(WireMock.urlMatching("/1"))
                .willReturn(
                        WireMock.aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .withStatus(HttpStatus.OK.value())
                                .withBody(asJson(customerById))
                ));

        Pojo pojo = client.getPojoById(1L);
        BDDAssertions.then(pojo.getUsername()).isEqualToIgnoringCase("first");
        BDDAssertions.then(pojo.getType()).isEqualToIgnoringCase("last");
        BDDAssertions.then(pojo.getRepository()).isEqualToIgnoringCase("email");
        BDDAssertions.then(pojo.getAction()).isEqualTo(1L);
    }
}

