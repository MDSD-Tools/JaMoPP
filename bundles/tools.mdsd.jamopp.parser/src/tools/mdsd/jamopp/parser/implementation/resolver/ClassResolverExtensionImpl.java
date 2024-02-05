package tools.mdsd.jamopp.parser.implementation.resolver;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import tools.mdsd.jamopp.model.java.classifiers.Class;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.parser.interfaces.resolver.ClassResolverExtension;

public class ClassResolverExtensionImpl implements ClassResolverExtension {

	private final String synthClass;
	private final ClassResolver classResolver;

	@Inject
	public ClassResolverExtensionImpl(@Named("synthClass") final String synthClass, final ClassResolver classResolver) {
		this.synthClass = synthClass;
		this.classResolver = classResolver;
	}

	@Override
	public void addToSyntheticClass(final Member member) {
		final Class container = classResolver.getByName(synthClass);
		container.setName(synthClass);
		if (!container.getMembers().contains(member)) {
			container.getMembers().add(member);
		}
	}

}
