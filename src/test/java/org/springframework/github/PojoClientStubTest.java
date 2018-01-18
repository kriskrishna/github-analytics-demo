package org.springframework.github;

import org.assertj.core.api.BDDAssertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
@SpringBootTest(classes = AnalyticsApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureStubRunner(ids = {"com.example.github:github-webhook:+:stubs:8082"}, workOffline = true)
/*@AutoConfigureStubRunner(ids = {"com.example.github:github-webhook"},
		repositoryRoot = "${REPO_WITH_JARS:https://repo.spring.io/milestone/}")*/
/**@AutoConfigureStubRunner(ids = {"com.example.github:github-webhook"},
repositoryRoot = "${REPO_WITH_JARS:https://localhost:8081/artifactory/libs-release-local/}")**/
public class PojoClientStubTest {

    @Autowired
    private PojoClient client;

    @Test
    public void pojosShouldReturnAllPojos() {
        String pojos = client.getPojos();
        //BDDAssertions.then(pojos.getData().size()).isEqualTo(2);
        //assertThatJson(pojos).field("['userName']").isEqualTo("smithapitla");
        //assertThatJson(pojos).array().hasSize(1).isEqualTo(true);

        assertThatJson(pojos).array("['data']").contains("['username']").isEqualTo("smithapitla");
    }

    @Test
    public void pojosShouldReturnPojosList() {
        Collection<Pojo> pojos = client.getPojoCollection();
        BDDAssertions.then(pojos.size()).isEqualTo(2);
    }

    @Test
    @Ignore
    public void pojoByIdShouldReturnAPojo() {
        Pojo pojo = client.getPojoById(1L);
        BDDAssertions.then(pojo.getUsername()).isEqualToIgnoringCase("first");
        BDDAssertions.then(pojo.getType()).isEqualToIgnoringCase("last");
        BDDAssertions.then(pojo.getRepository()).isEqualToIgnoringCase("email");
        BDDAssertions.then(pojo.getAction()).isEqualTo(1L);
    }
}
