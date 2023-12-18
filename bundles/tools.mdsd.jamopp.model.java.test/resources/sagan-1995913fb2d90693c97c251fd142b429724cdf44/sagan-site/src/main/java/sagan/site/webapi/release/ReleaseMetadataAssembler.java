package sagan.site.webapi.release;

import org.modelmapper.ModelMapper;
import sagan.site.projects.Release;
import sagan.site.webapi.repository.RepositoryMetadataController;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 *
 */
@Component
class ReleaseMetadataAssembler extends RepresentationModelAssemblerSupport<Release, ReleaseMetadata> {

	private final ModelMapper modelMapper;

	public ReleaseMetadataAssembler(ModelMapper modelMapper) {
		super(ReleaseMetadataController.class, ReleaseMetadata.class);
		this.modelMapper = modelMapper;
		this.modelMapper.createTypeMap(Release.class, ReleaseMetadata.class)
				.addMapping(Release::getReleaseStatus, ReleaseMetadata::setStatus)
				.addMapping(Release::expandRefDocUrl, ReleaseMetadata::setReferenceDocUrl)
				.addMapping(Release::expandApiDocUrl, ReleaseMetadata::setApiDocUrl);
	}

	@Override
	public ReleaseMetadata toModel(Release release) {
		ReleaseMetadata releaseMetadata = this.modelMapper.map(release, ReleaseMetadata.class);
		releaseMetadata.add(linkTo(methodOn(ReleaseMetadataController.class).showRelease(release.getProject().getId(), release.getVersion().toString())).withSelfRel());
		releaseMetadata.add(linkTo(methodOn(RepositoryMetadataController.class).showRepository(release.getRepository().getId())).withRel("repository"));
		return releaseMetadata;
	}

}
