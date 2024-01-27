package tools.mdsd.jamopp.parser.implementation.resolver;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ClassResolverSynthetic {

	private final String synthClass;
	private final ClassResolver classResolver;

	@Inject
	public ClassResolverSynthetic(@Named("synthClass") final String synthClass, final ClassResolver classResolver) {
		this.synthClass = synthClass;
		this.classResolver = classResolver;
	}

	public void addToSyntheticClass(final tools.mdsd.jamopp.model.java.members.Member member) {
		final tools.mdsd.jamopp.model.java.classifiers.Class container = classResolver.getByName(synthClass);
		container.setName(synthClass);
		if (!container.getMembers().contains(member)) {
			container.getMembers().add(member);
		}
	}

}
