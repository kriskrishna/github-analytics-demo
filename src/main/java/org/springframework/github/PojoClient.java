package org.springframework.github;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
public class PojoClient {

    private final RestTemplate restTemplate;
    private final String uri;

    public PojoClient(RestTemplate restTemplate, String uri) {
        this.restTemplate = restTemplate;
        this.uri = uri;
    }

   public Collection<Pojo> getPojoCollection() {

        ParameterizedTypeReference<Collection<Pojo>> ptr =
                new ParameterizedTypeReference<Collection<Pojo>>() {
                };

        return restTemplate.exchange(this.uri + "/", HttpMethod.GET, null, ptr)
                .getBody();
    }

    public String getPojos() {

        /*ParameterizedTypeReference<Pojos> ptr =
                new ParameterizedTypeReference<Pojos>() {
                };*/

        return restTemplate.exchange(this.uri + "/", HttpMethod.GET, null, String.class)
                .getBody();
    }

    public Pojo getPojoById(Long id) {
        return restTemplate.exchange(this.uri + "/pojo/{id}", HttpMethod.GET, null, Pojo.class, id)
                .getBody();
    }
}

