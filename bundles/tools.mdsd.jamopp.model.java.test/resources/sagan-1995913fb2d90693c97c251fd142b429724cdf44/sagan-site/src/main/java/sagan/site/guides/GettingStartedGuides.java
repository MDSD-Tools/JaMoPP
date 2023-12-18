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
 * Repository implementation providing data access services for getting started guides.
 */
@Component
public class GettingStartedGuides implements GuidesRepository<GettingStartedGuide> {

	private static Logger logger = LoggerFactory.getLogger(GettingStartedGuides.class);

	public static final String CACHE_GUIDES = "cache.guides";

	public static final String CACHE_GUIDE = "cache.guide";

	private final SaganRendererClient client;

	public GettingStartedGuides(SaganRendererClient client) {
		this.client = client;
	}

	@Override
	@Cacheable(CACHE_GUIDES)
	public GuideHeader[] findAll() {
		return Arrays.stream(this.client.fetchGettingStartedGuides())
				.map(DefaultGuideHeader::new)
				.toArray(DefaultGuideHeader[]::new);
	}

	@Override
	@Cacheable(cacheNames = CACHE_GUIDES, key="#project.id")
	public GuideHeader[] findByProject(Project project) {
		return Arrays.stream(findAll())
				.filter(guide -> guide.getProjects().contains(project.getId()))
				.toArray(GuideHeader[]::new);
	}

	@Override
	public Optional<GuideHeader> findGuideHeaderByName(String name) {
		DefaultGuideHeader guideHeader = new DefaultGuideHeader(this.client.fetchGettingStartedGuide(name));
		return Optional.of(guideHeader);
	}

	@Override
	@Cacheable(CACHE_GUIDE)
	public GettingStartedGuide findByName(String name) {
		try {
			DefaultGuideHeader guideHeader = new DefaultGuideHeader(this.client.fetchGettingStartedGuide(name));
			GuideContent guideContent = this.client.fetchGettingStartedGuideContent(name);
			return new GettingStartedGuide(guideHeader, guideContent);
		}
		catch (Exception exc) {
			logger.error("Could not render guide [" + name +"]", exc);
			throw exc;
		}
	}

	@CacheEvict(CACHE_GUIDES)
	public void evictListFromCache() {
		logger.info("Getting Started guides evicted from cache");
	}

	@CacheEvict(CACHE_GUIDE)
	public void evictFromCache(String guide) {
		logger.info("Getting Started guide evicted from cache: {}", guide);
	}

}
