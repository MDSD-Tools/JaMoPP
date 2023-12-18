package sagan.site.guides;

import java.util.Optional;

import sagan.site.projects.Project;

public interface GuidesRepository<T extends Guide> {

	GuideHeader[] findAll();

	Optional<GuideHeader> findGuideHeaderByName(String name);

	T findByName(String name);

   	GuideHeader[] findByProject(Project project);
}
