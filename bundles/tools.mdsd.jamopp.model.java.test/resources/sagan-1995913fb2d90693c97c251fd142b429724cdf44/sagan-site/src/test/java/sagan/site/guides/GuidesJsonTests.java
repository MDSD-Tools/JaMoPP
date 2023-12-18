package sagan.site.guides;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import sagan.site.renderer.GuideContent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Sagan is using Redis as a distributed cache in production.
 * To store model objects in that cache, they are (de)serialized to JSON -
 * these tests make sure that models are properly annotated to support JSON serialization.
 */
@JsonTest
public class GuidesJsonTests {

	@Autowired
	private JacksonTester<GettingStartedGuide> json;

	@Test
	public void serializeJson() throws Exception {
		Set<String> projects = new HashSet<>();
		projects.add("spring-boot");
		GuideHeader header = new DefaultGuideHeader("rest-service", "spring-guides/gs-rest-service",
				"Rest Service Title", "Description",
				"https://github.com/spring-guides/gs-rest-service",
				"git://github.com/spring-guides/gs-rest-service.git",
				"git@github.com:spring-guides/gs-rest-service.git",
				"https://github.com/spring-guides/gs-rest-service.git",
				projects);
		GuideContent content = new GuideContent("gs-rest-service", "TOC", "CONTENT");
		GettingStartedGuide guide = new GettingStartedGuide(header, content);
		assertThat(this.json.write(guide)).isEqualToJson("rest-service.json", getClass());
	}

	@Test
	public void deserializeJson() throws Exception {
		GettingStartedGuide guide = this.json.readObject("rest-service.json");
		assertThat(guide.getName()).isEqualTo("rest-service");
		assertThat(guide.getRepositoryName()).isEqualTo("spring-guides/gs-rest-service");
		assertThat(guide.getTitle()).isEqualTo("Rest Service Title");
		assertThat(guide.getDescription()).isEqualTo("Description");
		assertThat(guide.getGithubUrl()).isEqualTo("https://github.com/spring-guides/gs-rest-service");
		assertThat(guide.getGitUrl()).isEqualTo("git://github.com/spring-guides/gs-rest-service.git");
		assertThat(guide.getSshUrl()).isEqualTo("git@github.com:spring-guides/gs-rest-service.git");
		assertThat(guide.getCloneUrl()).isEqualTo("https://github.com/spring-guides/gs-rest-service.git");
		assertThat(guide.getProjects()).containsExactly("spring-boot");
		assertThat(guide.getContent()).isEqualTo("CONTENT");
		assertThat(guide.getTableOfContents()).isEqualTo("TOC");
	}

}
