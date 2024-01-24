package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;

import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class ToAnonymousClassConverter implements Converter<AnonymousClassDeclaration, AnonymousClass> {

	private final JdtResolver utilJDTResolver;
	private final UtilLayout utilLayout;
	private final Converter<BodyDeclaration, Member> toClassMemberConverter;

	@Inject
	public ToAnonymousClassConverter(final UtilLayout utilLayout, final JdtResolver utilJDTResolver,
			@Named("ToClassMemberConverter") final Converter<BodyDeclaration, Member> toClassMemberConverter) {
		this.utilJDTResolver = utilJDTResolver;
		this.utilLayout = utilLayout;
		this.toClassMemberConverter = toClassMemberConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AnonymousClass convert(final AnonymousClassDeclaration anon) {
		final ITypeBinding binding = anon.resolveBinding();
		AnonymousClass result;
		if (binding != null) {
			result = utilJDTResolver.getAnonymousClass(binding);
		} else {
			result = utilJDTResolver.getAnonymousClass(String.valueOf(anon.hashCode()));
		}
		anon.bodyDeclarations()
				.forEach(obj -> result.getMembers().add(toClassMemberConverter.convert((BodyDeclaration) obj)));
		utilLayout.convertToMinimalLayoutInformation(result, anon);
		return result;
	}

}
