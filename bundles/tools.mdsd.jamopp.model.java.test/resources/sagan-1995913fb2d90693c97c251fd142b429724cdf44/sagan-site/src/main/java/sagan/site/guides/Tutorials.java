package sagan.site.guides;

import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sagan.site.projects.Project;
import sagan.site.renderer.GuideContent;
import sagan.site.renderer.SaganRendererClient;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Repository implementation providing data access services for tutorial guides.
 */
@Component
public class Tutorials implements GuidesRepository<Tutorial> {

	private static Logger logger = LoggerFactory.getLogger(Tutorials.class);

	public static final String CACHE_TUTORIALS = "cache.tutorials";

	public static final String CACHE_TUTORIAL = "cache.tutorial";

	private final SaganRendererClient client;

	public Tutorials(SaganRendererClient client) {
		this.client = client;
	}

	@Override
	@Cacheable(CACHE_TUTORIALS)
	public GuideHeader[] findAll() {
		return Arrays.stream(this.client.fetchTutorialGuides())
				.map(DefaultGuideHeader::new)
				.toArray(DefaultGuideHeader[]::new);
	}

	@Override
	@Cacheable(cacheNames = CACHE_TUTORIALS, key="#project.id")
	public GuideHeader[] findByProject(Project project) {
		return Arrays.stream(findAll())
				.filter(guide -> guide.getProjects().contains(project.getId()))
				.toArray(GuideHeader[]::new);
	}

	@Override
	public Optional<GuideHeader> findGuideHeaderByName(String name) {
		DefaultGuideHeader guideHeader = new DefaultGuideHeader(this.client.fetchTutorialGuide(name));
		return Optional.of(guideHeader);
	}

	@Override
	@Cacheable(CACHE_TUTORIAL)
	public Tutorial findByName(String name) {
		try {
			DefaultGuideHeader guideHeader = new DefaultGuideHeader(this.client.fetchTutorialGuide(name));
			GuideContent guideContent = this.client.fetchTutorialGuideContent(name);
			return new Tutorial(guideHeader, guideContent);
		}
		catch (Exception exc) {
			logger.error("Could not render tutorial [" + name +"]", exc);
			throw exc;
		}
	}

	@CacheEvict(CACHE_TUTORIALS)
	public void evictListFromCache() {
		logger.info("Tutorials evicted from cache");
	}

	@CacheEvict(CACHE_TUTORIAL)
	public void evictFromCache(String guide) {
		logger.info("Tutorial evicted from cache: {}", guide);
	}

}
