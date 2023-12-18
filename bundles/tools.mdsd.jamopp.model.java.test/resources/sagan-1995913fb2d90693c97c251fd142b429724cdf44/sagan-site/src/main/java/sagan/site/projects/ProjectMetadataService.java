package sagan.site.projects;

import java.util.List;

import sagan.site.blog.PostFormat;
import sagan.site.blog.PostContentRenderer;
import sagan.site.projects.support.SupportStatus;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProjectMetadataService {

	private static final Sort sortBySortOrderAndId = Sort.by("display.sortOrder", "id");

	private final ProjectRepository repository;

	private final ProjectGroupRepository groupRepository;

	private final ReleaseRepository releaseRepository;

	private final PostContentRenderer renderer;

	public ProjectMetadataService(ProjectRepository repository, ProjectGroupRepository groupRepository,
			ReleaseRepository releaseRepository, PostContentRenderer postContentRenderer) {
		this.repository = repository;
		this.groupRepository = groupRepository;
		this.releaseRepository = releaseRepository;
		this.renderer = postContentRenderer;
	}

	public Project fetchFullProject(String id) {
		return this.repository.fetchFullProject(id);
	}

	public List<Project> fetchActiveProjectsTree() {
		return this.repository.findDistinctByStatusAndParentProjectIsNull(SupportStatus.ACTIVE, sortBySortOrderAndId);
	}

	public List<Project> fetchAllProjects() {
		return this.repository.findAll(sortBySortOrderAndId);
	}

	public List<Project> fetchTopLevelProjectsWithGroups() {
		return this.repository.findTopLevelProjectsWithGroup();
	}

	public Project save(Project project) {
		project.computeCurrentRelease();
		project.getGenerationsInfo().computeSupportPolicyDates(project.getSupportPolicy());
		String bootConfigHtml = this.renderer.render(project.getBootConfig().getSource(), PostFormat.ASCIIDOC);
		project.getBootConfig().setHtml(bootConfigHtml);
		String overviewHtml = this.renderer.render(project.getOverview().getSource(), PostFormat.ASCIIDOC);
		project.getOverview().setHtml(overviewHtml);
		return this.repository.save(project);
	}

	public void delete(String id) {
		this.repository.deleteById(id);
	}

	public List<ProjectGroup> getAllGroups() {
		return this.groupRepository.findAll();
	}

	public ProjectGroup findGroup(String name) {
		return this.groupRepository.findByNameIgnoreCase(name);
	}

	public Release findRelease(String projectId, Version version) {
		return this.releaseRepository.findRelease(projectId, version);
	}

	public Release findCurrentRelease(String projectId) {
		return this.releaseRepository.findCurrentRelease(projectId);
	}
}
