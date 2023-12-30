package tools.mdsd.jamopp.parser.jdt.implementation.helper.resolver;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ClassResolverSynthetic {

	private final String synthClass;
	private final ClassResolver classResolver;

	@Inject
	public ClassResolverSynthetic(@Named("synthClass") String synthClass, ClassResolver classResolver) {
		this.synthClass = synthClass;
		this.classResolver = classResolver;
	}

	public void addToSyntheticClass(tools.mdsd.jamopp.model.java.members.Member member) {
		tools.mdsd.jamopp.model.java.classifiers.Class container = classResolver.getByName(synthClass);
		container.setName(synthClass);
		if (!container.getMembers().contains(member)) {
			container.getMembers().add(member);
		}
	}

}
