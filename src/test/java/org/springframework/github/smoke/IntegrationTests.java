package org.springframework.github.smoke;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;
import static org.awaitility.Awaitility.await;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntegrationTests.class,
		webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnableAutoConfiguration
public class IntegrationTests {

	private static final Log log = LogFactory.getLog(MethodHandles.lookup().lookupClass());

	@Value("${stubrunner.url:localhost:8083}") String stubRunnerUrl;
	@Value("${application.url:localhost:8081}") String applicationUrl;

	RestTemplate restTemplate = new RestTemplate();

	@Test
	public void shouldStoreAMessageWhenGithubDataWasReceivedViaMessaging() {
		final Integer countOfEntries = countGithubData();
		log.info("Initial count is [" + countOfEntries + "]");

		ResponseEntity<Map> response = triggerMessage();
		then(response.getStatusCode().is2xxSuccessful()).isTrue();
		log.info("Triggered additional message");

		log.info("Awaiting proper count of github data");
		await().until(() -> countGithubData() > countOfEntries);
	}

	private ResponseEntity<Map> triggerMessage() {
		return this.restTemplate.postForEntity("http://" +
				this.stubRunnerUrl + "/triggers/issue_created_v2", "", Map.class);
	}

	private Integer countGithubData() {
		Integer response = this.restTemplate
				.getForObject("http://" + this.applicationUrl + "/issues/count", Integer.class);
		log.info("Received response [" + response + "]");
		return response;
	}
}
