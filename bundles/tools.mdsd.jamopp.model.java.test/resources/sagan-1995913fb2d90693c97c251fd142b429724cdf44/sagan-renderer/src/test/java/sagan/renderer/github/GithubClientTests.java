package sagan.renderer.github;

import java.util.List;

import org.junit.jupiter.api.Test;
import sagan.renderer.RendererProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.StreamUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Tests for {@link GithubClient}
 */
@RestClientTest({GithubClient.class, RendererProperties.class})
@TestPropertySource(properties = "sagan.renderer.github.token=testtoken")
public class GithubClientTests {

	private static final MediaType GITHUB_PREVIEW = MediaType.parseMediaType("application/vnd.github.mercy-preview+json");

	private static final MediaType APPLICATION_ZIP = MediaType.parseMediaType("application/zip");

	@Autowired
	private GithubClient client;

	@Autowired
	private MockRestServiceServer server;

	@Test
	void downloadRepositoryInfo() {
		String org = "spring-guides";
		String repo = "gs-rest-service";
		String expectedUrl = String.format("/repos/%s/%s", org, repo);
		String authorization = getAuthorizationHeader();
		this.server.expect(requestTo(expectedUrl))
				.andExpect(header(HttpHeaders.AUTHORIZATION, authorization))
				.andExpect(header(HttpHeaders.ACCEPT, GITHUB_PREVIEW.toString()))
				.andRespond(withSuccess(getClassPathResource("gs-rest-service.json"), GITHUB_PREVIEW));
		Repository repository = this.client.fetchOrgRepository(org, repo);
		assertThat(repository).extracting("name").isEqualTo("gs-rest-service");
	}

	@Test
	void downloadRepositoryInfoRedirected() {
		String org = "spring-guides";
		String repo = "gs-redirected";
		String expectedUrl = String.format("/repos/%s/%s", org, repo);
		String authorization = getAuthorizationHeader();
		this.server.expect(requestTo(expectedUrl))
				.andExpect(header(HttpHeaders.AUTHORIZATION, authorization))
				.andExpect(header(HttpHeaders.ACCEPT, GITHUB_PREVIEW.toString()))
				.andRespond(withSuccess(getClassPathResource("gs-rest-service.json"), GITHUB_PREVIEW));
		assertThatThrownBy(() -> this.client.fetchOrgRepository(org, repo))
				.isInstanceOf(GithubResourceNotFoundException.class)
				.hasMessage("Could not find github repository [spring-guides/gs-redirected]")
				.getCause().hasMessage("Repository [gs-redirected] redirected to [spring-guides/gs-rest-service]");
	}

	@Test
	void downloadRepositoryAsZipBall() throws Exception {
		String org = "spring-guides";
		String repo = "gs-rest-service";
		String expectedUrl = String.format("/repos/%s/%s/zipball", org, repo);
		String authorization = getAuthorizationHeader();
		this.server.expect(requestTo(expectedUrl))
				.andExpect(header(HttpHeaders.AUTHORIZATION, authorization))
				.andExpect(header(HttpHeaders.ACCEPT, GITHUB_PREVIEW.toString()))
				.andRespond(withSuccess(getClassPathResource("gs-rest-service.zip"), APPLICATION_ZIP));
		byte[] result = this.client.downloadRepositoryAsZipball(org, repo);
		ClassPathResource resource = getClassPathResource("gs-rest-service.zip");
		assertThat(result).isEqualTo(StreamUtils.copyToByteArray(resource.getInputStream()));
	}

	@Test
	void fetchRepositoriesMultiplePages() {
		String org = "spring-guides";
		String authorization = getAuthorizationHeader();
		HttpHeaders firstPageHeaders = new HttpHeaders();
		firstPageHeaders.add("Link",
				"<https://api.github.com/organizations/4161866/repos?per_page=100&page=2>; rel=\"next\"," +
						" <https://api.github.com/organizations/4161866/repos?per_page=100&page=2>; rel=\"last\"");
		this.server.expect(requestTo("/orgs/spring-guides/repos?per_page=100"))
				.andExpect(header(HttpHeaders.AUTHORIZATION, authorization))
				.andExpect(header(HttpHeaders.ACCEPT, GITHUB_PREVIEW.toString()))
				.andRespond(withSuccess(getClassPathResource("spring-guides-repos-page1.json"),
						MediaType.APPLICATION_JSON).headers(firstPageHeaders));
		this.server.expect(requestTo("/organizations/4161866/repos?per_page=100&page=2"))
				.andExpect(header(HttpHeaders.AUTHORIZATION, authorization))
				.andExpect(header(HttpHeaders.ACCEPT, GITHUB_PREVIEW.toString()))
				.andRespond(withSuccess(getClassPathResource("spring-guides-repos-page2.json"),
						MediaType.APPLICATION_JSON));

		List<Repository> repositories = this.client.fetchOrgRepositories(org);
		assertThat(repositories).hasSize(5)
				.extracting("name")
				.containsExactlyInAnyOrder("gs-rest-service", "gs-scheduling-tasks",
						"gs-consuming-rest", "gs-relational-data-access", "deprecate-gs-device-detection");

	}

	@Test
	void fetchRateLimitInformation() {
		String authorization = getAuthorizationHeader();
		this.server.expect(requestTo("/rate_limit"))
				.andExpect(header(HttpHeaders.AUTHORIZATION, authorization))
				.andExpect(header(HttpHeaders.ACCEPT, GITHUB_PREVIEW.toString()))
				.andRespond(withSuccess(getClassPathResource("rate_limit.json"),
						MediaType.APPLICATION_JSON));

		RateLimit rateLimit = this.client.fetchRateLimitInfo();
		assertThat(rateLimit.getLimit()).isEqualTo(60);
		assertThat(rateLimit.getRemaining()).isEqualTo(60);
		assertThat(rateLimit.getUsed()).isEqualTo(0);
		assertThat(rateLimit.getReset().toString()).isEqualTo("2020-10-09T13:51:05Z");
	}

	private String getAuthorizationHeader() {
		return "Token testtoken";
	}

	private ClassPathResource getClassPathResource(String path) {
		return new ClassPathResource(path, getClass());
	}
}
